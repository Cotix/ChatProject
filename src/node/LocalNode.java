package node;

import log.Log;
import log.LogLevel;
import network.DistanceTable;
import network.Node;
import network.RoutingTable;
import network.connection.packet.Packet;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static network.connection.packet.PacketUtils.*;

public class LocalNode extends Thread {
    private short clientPort;
    private short nodePort;
    private List<Node> peers;
    private long lastAnounce;
    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private ServerSocket clientSocket;
    private ServerSocket nodeSocket;
    private Map<Node, List<Packet>> packetBuffer;
    private String localhost;
    private RoutingTable routing;
    public LocalNode(String myIP) {
        this((short)8000, (short)8001, myIP);
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
        packetBuffer = new HashMap<>();
        peers = new LinkedList<Node>();
        try {
            multicastSocket.setSoTimeout(1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.Log("Setting up a localnode.", LogLevel.INFO);
    }

    public void addNode(String ip, short port) {
        try {
            if (ip.equals(localhost)) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        for (Node n : peers) {
            if (n.getIp().equals(ip)) {
                if (!n.isConnected()) {
                    n.connect();
                }
                return;
            }
        }
        Log.Log("Adding node " + ip + ":" + port, LogLevel.INFO);
        Node node = new Node(ip, port);
        node.connect();
    }

    public void Announce() {
        if (System.currentTimeMillis() - lastAnounce <= 30000) {
            return;
        }
        String msg = null;
        msg = "HELLO" + localhost + ":" + nodePort;
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), multicastGroup, 6789);
        try {
            multicastSocket.send(hi);
            lastAnounce = System.currentTimeMillis();
        } catch (IOException e) {
            Log.Log("Announcing went wrong!", LogLevel.ERROR);
        }
        Log.Log("Announcement sent!", LogLevel.NONE);
    }

    public void handleConnections() {
        for (Node n : peers) {
            if (n.isConnected()) {
                List<Packet> packets = n.handleConnection();
                if (packets != null) {
                    if (!packetBuffer.containsKey(n)) {
                        packetBuffer.put(n, new LinkedList<Packet>());
                    }
                    packetBuffer.get(n).addAll(packets);
                }
            }
        }
    }

    private void sendDistanceTable(Node target) {
        target.send(routing.getMyDistanceTable());
    }

    private void sendDistanceTableToAll() {
        for (Node n : peers) {
            sendDistanceTable(n);
        }
    }

    private boolean handlePacket(Packet packet, Node n) {
        PacketType type = getPacketType(packet);
        switch(type) {
            case UNKNOWN:
                Log.Log("Got an unknown packettype!", LogLevel.INFO);
                break;
            case DISTANCE:
                Log.Log("Distance packet received!", LogLevel.NONE);
                if (routing.updateNode(n, packet)) {
                    sendDistanceTableToAll();
                }
                break;
        }

        return true;
    }

    public void forwardPackets() {
        for (Node n : peers) {
            if (packetBuffer.containsKey(n)) {
                List<Packet> packets = packetBuffer.get(n);
                List<Packet> toRemove = new LinkedList<>();
                for (Packet p : packets) {
                    handlePacket(p, n);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = new ServerSocket(clientPort);
            nodeSocket = new ServerSocket(nodePort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
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
                Log.Log("Received announcement: " + msg, LogLevel.NONE);
            }
            if (msg != null && msg.contains("HELLO")) {
                String[] announcements = msg.split("HELLO");
                for (String announcement : announcements) {
                    String[] split = announcement.split(":");
                    if (split.length == 2) {
                        try {
                            addNode(split[0], Short.parseShort(split[1]));
                        } catch (Exception e) {
                            Log.Log("Failed to add a host from announcement(" + announcement + ")", LogLevel.INFO);
                        }
                    }
                }
            }
            handleConnections();

            try {
                this.sleep(10);
            } catch (InterruptedException e) {
                Log.Log("Sleep got interrupted of localnode!", LogLevel.INFO);
            }
        }
    }
}
