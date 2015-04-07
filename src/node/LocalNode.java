package node;

import log.Log;
import log.LogLevel;
import network.Node;
import network.connection.packet.Packet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class LocalNode extends Thread {
    private short clientPort;
    private short nodePort;
    private List<Node> peers;
    private long lastAnounce;
    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private ServerSocket clientSocket;
    private ServerSocket nodeSocket;
    private List<Packet> packetBuffer;
    public LocalNode() {
        this((short)8000, (short)8001);
    }

    public LocalNode(short cPort, short nPort) {
        try {
            multicastGroup = InetAddress.getByName("228.5.6.7");
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
        packetBuffer = new LinkedList<Packet>();
        peers = new LinkedList<Node>();
        try {
            multicastSocket.getChannel().configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNode(String ip, short port) {
        for (Node n : peers) {
            if (n.getIp().equals(ip)) {
                if (!n.isConnected()) {
                    n.connect();
                    return;
                }
            }
        }
        Node node = new Node(ip, port);
        node.connect();
    }

    public void Announce() {
        if (System.currentTimeMillis() - lastAnounce <= 60000) {
            return;
        }
        String msg = "HELLO" + nodeSocket.getInetAddress() + ":" + nodeSocket.getLocalPort();
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), multicastGroup, 6789);
        try {
            multicastSocket.send(hi);
            lastAnounce = System.currentTimeMillis();
        } catch (IOException e) {
            Log.Log("Announcing went wrong!", LogLevel.ERROR);
        }
    }

    public void handleConnections() {
        for (Node n : peers) {
            if (n.isConnected()) {
                List<Packet> packets = n.handleConnection();
                if (packets != null) {
                    packetBuffer.addAll(packets);
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
                Log.Log("LocalNode failed to read multicast!", LogLevel.ERROR);
            }
            String msg = null;
            try {
                msg = new String(buf, "US-ASCII");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (msg != null && msg.contains("HELLO")) {
                String[] announcements = msg.split("HELLO");
                for (String announcement : announcements) {
                    String[] split = announcement.split(":");
                    if (split.length == 2) {
                        try {
                            addNode(split[0], Short.parseShort(split[1]));
                        } catch (Exception E) {
                            //Not a good format!
                        }
                    }
                }
            }
            handleConnections();

        }
    }
}
