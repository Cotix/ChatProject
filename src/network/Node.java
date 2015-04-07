package network;

import network.connection.*;
import network.connection.packet.Packet;

import java.util.LinkedList;
import java.util.List;

public class Node extends Thread {
    private String ip;
    private short port;
    private Connection con;

    public Node(String IPaddress, short port) {
        ip = IPaddress;
        con = null;
        this.port = port;
    }

    public boolean isConnected() {
        if (con == null) {
            return false;
        }
        return con.isConnected();
    }

    public String getIp() {
        return ip;
    }

    public void connect() {
        connect(new TCPConnection());
    }

    public void connect(Connection c) {
        if (isConnected() == false) {
            con = c;
            c.connect(ip, port);
        }
    }

    public List<Packet> handleConnection() {
        if (!isConnected()) {
            return null;
        }
        List<Packet> list = new LinkedList<Packet>();
        con.handleConnection();
        while (true) {
            Packet p = con.readPacket();
            if (p == null) {
                return list;
            }
            list.add(p);
        }
    }

}
