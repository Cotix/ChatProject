package node;

import client.security.CryptoKeyPair;
import datatypes.Bytes;
import datatypes.SynchronizedLinkedList;
import log.Log;
import log.LogLevel;
import network.Address;
import network.connection.TCPConnection;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.MessagePacket;
import network.connection.packet.Packet;
import network.connection.packet.StringPacket;
import settings.Configuration;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static network.connection.packet.PacketUtils.*;


//This is one of the most important classes
//It implements a node
//It does a lot of things, which you can read all about in our report

public class LocalNode extends Thread {
    //ListenThread is needed cause java hardly offers non-blocking NIO
    private class ListenThread extends Thread {
        ServerSocket socket;
        ConcurrentLinkedQueue<Socket> queue;
        public ListenThread(ServerSocket s) {
            socket = s;
            queue = new ConcurrentLinkedQueue<>();
        }

        public void run() {
            while (true) {
                Socket s = null;
                try {
                    s = socket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.log("New incoming node connection!", LogLevel.INFO);
                queue.add(s);
            }
        }

        public Socket accept() {
            return queue.poll();
        }
    }

    private short clientPort;
    private short nodePort;
    private Map<Bytes, Long>  sentData;
    private SynchronizedLinkedList<Node> peers;
    private SynchronizedLinkedList<ClientHandler> clients;
    private long lastAnounce;
    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private ListenThread clientThread;
    private ListenThread nodeThread;
    private Map<Node, List<Packet>> packetBuffer;
    private Map<ClientHandler, List<Packet>> clientBuffer;
    private String localhost;
    private RoutingTable routing;
    public LocalNode(String myIP) {
        this((short) Configuration.PORT, (short) 8001, myIP);
    }

    public LocalNode(short cPort, short nPort, String IP) {
        routing = new RoutingTable();
        localhost = IP;
        try {
            multicastGroup = InetAddress.getByName("228.2.2.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            multicastSocket = new MulticastSocket(6789);
            multicastSocket.joinGroup(multicastGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastAnounce = 0;
        clientPort = cPort;
        nodePort = nPort;
        packetBuffer = new ConcurrentHashMap<>();
        clientBuffer = new ConcurrentHashMap<>();
        peers = new SynchronizedLinkedList<>();
        clients = new SynchronizedLinkedList<>();
        sentData = new ConcurrentHashMap<>();
        try {
            multicastSocket.setSoTimeout(1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.log("Setting up a localnode.", LogLevel.INFO);
    }
    //Connecting to another node
    public void connectToNode(String ip, short port) {
        if (ip.equals(localhost) && port == nodePort) {
            return;
        }
        //Check if we are already connected to this node
        for (Node n : peers) {
            if (n.getIp().equals(ip) && (n.getPort() == port || Configuration.ONE_NODE_PER_IP)) {
                if (!n.isConnected()) {
                    n.connect();
                }
                return;
            }
        }
        //Seems we are not, time to add the node
        Log.log("Adding node " + ip + ":" + port, LogLevel.INFO);
        Node node = new Node(ip, port);
        //Add him to the peers list
        peers.add(node);
        //Connect and start then connection!
        node.connect();
        (new Thread(node)).start();
        sendDistanceTable(node);
    }
    //Adds a node to the peers list. This function is used for incoming connections
    private void addNode(Node node) {
        //Is he already in the list?
        for (Node n : peers) {
            if (n.getIp().equals(node.getIp()) && (n.getPort() == node.getPort() || Configuration.ONE_NODE_PER_IP)) {
                if (!n.isConnected()) {
                    n.connect();
                }
                return;
            }
        }
        Log.log("Accepting node " + node.getIp(), LogLevel.INFO);
        peers.add(node);
        (new Thread(node)).start();
        sendDistanceTable(node);
    }
    //Adds a client to the client list
    private void addClient(ClientHandler client) {
        Log.log("Accepting client ", LogLevel.INFO);
        clients.add(client);
        (new Thread(client)).start();
    }
    //Announce ourselves, and ping the neighbours
    public void Announce() {
        //But only if we didn't do that too recently
        if (System.currentTimeMillis() - lastAnounce <= Configuration.ANNOUNCE_TIME) {
            return;
        }
        pingAllNodes();
        String msg;
        //Announcement is in the form of: HELLO127.0.0.1:8000
        msg = "HELLO" + localhost + ":" + nodePort;
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), multicastGroup, 6789);
        try {
            multicastSocket.send(hi);
            lastAnounce = System.currentTimeMillis();
            Log.log("Announcement sent!", LogLevel.NONE);
        } catch (IOException e) {
            Log.log("Announcing went wrong!", LogLevel.ERROR);
        }
    }

    public void pingAllNodes() {
        for (Node n : peers) {
            n.ping();
        }
        for (ClientHandler c : routing.getClients()) {
            c.ping();
        }
    }
    //This function handles all the node and client connections
    //It polls the packet queues and saves it to our own queue
    public void handleConnections() {
        List<Node> toRemove = new LinkedList<>();
        for (Node n : peers) {
            if (n.isConnected()) {
                List<Packet> packets = n.handleConnection();
                if (packets != null && packets.size() > 0) {
                    if (!packetBuffer.containsKey(n)) {
                        packetBuffer.put(n, new LinkedList<Packet>());
                    }
                    Log.log("Added " + packets.size() + " packets to the queue.", LogLevel.NONE);
                    packetBuffer.get(n).addAll(packets);
                }
            } else {
                Log.log("Removing node " + n.getIp() + ":" + n.getPort(), LogLevel.INFO);
                toRemove.add(n);
            }
        }
        for (Node n : toRemove) {
            peers.remove(n);
            routing.removeNode(n);
            if (routing.update()) {
                sendDistanceTableToAll();
            }
        }
        for (ClientHandler c : clients) {
            if (c.isConnected()) {
                List<Packet> packets = c.handleConnection();
                if (packets != null && packets.size() > 0) {
                    if (!clientBuffer.containsKey(c)) {
                        clientBuffer.put(c, new LinkedList<Packet>());
                    }
                    Log.log("Added " + packets.size() + " packets to the client queue.", LogLevel.NONE);
                    clientBuffer.get(c).addAll(packets);
                }
            }
        }
    }
    //Sends our distance table to a node
    private void sendDistanceTable(Node target) {
        target.send(routing.getMyDistanceTable());
    }

    private void sendDistanceTableToAll() {
        Log.log("Sending distancetable to all connected nodes", LogLevel.INFO);
        Packet p = routing.getMyDistanceTable();
        for (Node n : peers) {
            n.send(p);
        }
        for (ClientHandler c : clients) {
            c.send(p);
        }
    }
    //Handles a packet. Basically a big switch case on all packetTypes
    //Than does what ever needs to be done to the packets
    private boolean handlePacket(Packet packet, Node n) {
        PacketType type = getPacketType(packet);
        switch(type) {
            case UNKNOWN:
                Log.log("Got an unknown packettype!", LogLevel.INFO);
                break;
            case DISTANCE:
                Log.log("Distance packet received!", LogLevel.NONE);
                if (routing.updateNode(n, packet)) {
                    sendDistanceTableToAll();
                }
                break;
            case PING:
                Log.log("Received a ping packet!", LogLevel.NONE);
                CurrentTimePacket pong = new CurrentTimePacket(packet.getRawData());
                pong.setType(PacketType.PONG);
                n.send(pong);
                break;
            case PONG:
                CurrentTimePacket pongPacket = new CurrentTimePacket(packet.getRawData());
                int diff = pongPacket.getTimeDifference();
                routing.updateNodePing(n, diff);
                Log.log("Received a pong packet time diff: " + diff, LogLevel.NONE);
                break;
            case MESSAGE:
                Log.log("Received message from other node to forward.", LogLevel.NONE);
                if (sentData.containsKey(new Bytes(packet.getData()))) {
                    Log.log("Messages was already forwarded by this node, so loop detected!", LogLevel.NONE);
                } else {
                    Packet ackPacket = new StringPacket(packet.getData(), PacketType.ACK);
                    n.send(ackPacket);
                    sentData.put(new Bytes(packet.getData()), System.currentTimeMillis());
                    MessagePacket p = new MessagePacket(packet.getRawData());
                    Address dest = p.getRecipient();
                    ClientHandler client = routing.getDirectConnection(dest);
                    if (client != null) {
                        Log.log("Sending message directly to client", LogLevel.NONE);
                        client.send(p);
                    } else {
                        Node forwardNode = routing.getNode(dest);
                        if (forwardNode == n) {
                            forwardNode = routing.getAlternativeNode(dest, n);
                        }
                        if (forwardNode == null) {
                            Log.log("Can not find route to address: " + dest, LogLevel.NONE);
                            return false;
                        }
                        Log.log("Routing packet for address " + dest + " to node " + forwardNode.getIp(), LogLevel.INFO);
                        forwardNode.send(p);
                    }
                }
                break;
            case ACK:
                Log.log("Received an ACK", LogLevel.NONE);
                if (sentData.containsKey(new Bytes(packet.getData()))) {
                    sentData.remove(new Bytes(packet.getData()));
                } else {
                    Log.log("Got an ACK from node " + n.getIp() + ":" + n.getPort() + " but it is not our packet!", LogLevel.WARNING);
                }
                break;
        }
        return true;
    }
    //Handle packets for client connections
    private boolean handlePacket(Packet packet, ClientHandler c) {
        PacketType type = getPacketType(packet);
        switch (type) {
            case PING:
                CurrentTimePacket cPacket = new CurrentTimePacket(packet.getData());
                routing.clientSetPing(c, cPacket.getTimeDifference());
                break;
            case IDENTIFY:
                Log.log("Received an identify packet!", LogLevel.NONE);
                byte[] key = packet.getData();
                CryptoKeyPair keyPublic = new CryptoKeyPair(key);
                if (routing.addClient(new Address(keyPublic), c)) {
                    sendDistanceTableToAll();
                }
                Log.log("New client with public key: " + keyPublic, LogLevel.INFO);
                break;
            case MESSAGE:
                sentData.put(new Bytes(packet.getData()), System.currentTimeMillis());
                Log.log("Received a chat packet from the client!", LogLevel.NONE);
                MessagePacket p = new MessagePacket(packet.getRawData());
                Address dest = p.getRecipient();
                ClientHandler client = routing.getDirectConnection(dest);
                if (client != null) {
                    Log.log("Sending message packet directly to node", LogLevel.NONE);
                    client.send(p);
                } else {
                    Node forwardNode = routing.getNode(dest);
                    if (forwardNode == null) {
                        Log.log("Can not find route to address: " + dest, LogLevel.INFO);
                        return false;
                    }
                    Log.log("Routing packet for address " + dest + " to node " + forwardNode.getIp(), LogLevel.INFO);
                    forwardNode.send(p);
                }
                break;
        }
        return true;
    }
    //Processes packets that are in our queue, basically call handlePacket
    //If it cant be handled, lets add it back to the queue and deal with it next iteration
    public void forwardPackets() {
        for (Node n : peers) {
            if (packetBuffer.containsKey(n)) {
                List<Packet> packets = packetBuffer.get(n);
                if (packets.size() >= 100) {
                    Log.log(packets.size() + " packets in queue for node: " + n.getIp(), LogLevel.WARNING);
                }
                List<Packet> toRemove = new LinkedList<>();
                for (Packet p : packets) {
                    if (handlePacket(p, n)) {
                        toRemove.add(p);
                    }
                }
                packets.removeAll(toRemove);
            }
        }
        for (ClientHandler c : clients) {
            if (clientBuffer.containsKey(c)) {
                List<Packet> packets = clientBuffer.get(c);
                if (packets.size() >= 100) {
                    Log.log(packets.size() + " packets in queue for client", LogLevel.WARNING);
                }
                List<Packet> toRemove = new LinkedList<>();
                for (Packet p : packets) {
                    if (handlePacket(p, c)) {
                        toRemove.add(p);
                    }
                }
                packets.removeAll(toRemove);
            }
        }
    }
    //Polls incoming connections from the NodeThread
    private void acceptNodeConnections() {
        while (true) {
            Socket s = nodeThread.accept();
            if (s == null) {
                return;
            }
            Node n = new Node(new TCPConnection(s), s.getRemoteSocketAddress().toString(), (short)s.getPort());
            addNode(n);
        }
    }
    //Polls incoming connections from the ClientThread
    private void acceptClientConnections() {
        while (true) {
            Socket s = clientThread.accept();
            if (s == null) {
                return;
            }
            ClientHandler client = new ClientHandler(s);
            addClient(client);
        }
    }

    private void handlePacketTimeouts() {
        for (Bytes d : sentData.keySet()) {
            byte[] data = d.getBytes();
            if (System.currentTimeMillis() - sentData.get(d) >= Configuration.TIMEOUT) {
                Log.log("Time out for a packet timed out! time to resend..", LogLevel.INFO);
                sentData.put(new Bytes(data), System.currentTimeMillis());
                MessagePacket p = new MessagePacket(new StringPacket(data, PacketType.MESSAGE).getRawData());
                Address dest = p.getRecipient();
                ClientHandler client = routing.getDirectConnection(dest);
                if (client != null) {
                    Log.log("Resending message packet directly to node", LogLevel.NONE);
                    client.send(p);
                } else {
                    Node forwardNode = routing.getNode(dest);
                    if (forwardNode == null) {
                        Log.log("Can not find route to address: " + dest, LogLevel.INFO);
                    }
                    Log.log("Routing packet for address " + dest + " to node " + forwardNode.getIp(), LogLevel.INFO);
                    forwardNode.send(p);
                }
            }
        }
    }

    //The main loop
    @Override
    public void run() {
        //Lets first start up the serversockets
        try {
            clientThread = new ListenThread(new ServerSocket(clientPort));
            nodeThread = new ListenThread(new ServerSocket(nodePort));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientThread.start();
        nodeThread.start();
        while (true) {
            //Announce, then reads other announcements
            Announce();
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            try {
                multicastSocket.receive(recv);
            } catch (IOException e) {
            }
            String msg = null;
            try {
                msg = new String(buf, "US-ASCII");
                msg = msg.substring(0, recv.getLength());
            } catch (Exception e) {
                //
            }
            if (msg != null && msg.length() != buf.length) {
                Log.log("Received announcement: " + msg, LogLevel.NONE);
            }
            //Add a random node that announced itself.
            //A small random chance so we do not add every one right away
            if (msg != null && msg.contains("HELLO")) {
                String[] announcements = msg.split("HELLO");
                for (String announcement : announcements) {
                    String[] split = announcement.split(":");
                    if (split.length == 2) {
                        try {
                            connectToNode(split[0], Short.parseShort(split[1]));
                        } catch (Exception e) {
                            Log.log("Failed to add a host from announcement(" + announcement + ")", LogLevel.INFO);
                        }
                    }
                }
            }
            //Call all the handle functions
            handleConnections();
            forwardPackets();
            acceptNodeConnections();
            acceptClientConnections();
            handlePacketTimeouts();
            if (routing.removeOldClient()) {
                sendDistanceTableToAll();
            }
            if (routing.removeOldNode()) {
                sendDistanceTableToAll();
            }
            for (ClientHandler c : clients) {
                if (!c.getRunning()) {
                    clients.remove(c);
                }
            }
            try {
                //Sleep a little so we don't stress the computer too much
                sleep(1);
            } catch (InterruptedException e) {
                Log.log("Sleep got interrupted of localnode!", LogLevel.INFO);
            }
        }
    }
}
