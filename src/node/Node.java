package node;

import log.Log;
import log.LogLevel;
import network.connection.*;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.Packet;

import java.util.LinkedList;
import java.util.List;
//This class maintains the connection between our node and neighbour nodes.
//Basicly it has a Connection, that it receives packets from in a thread
//The LocalNode can poll packets from the queue
//Also holds some information about the node, ip, port, etc
public class Node implements Runnable {
    private String ip;
    private short port;
    private Connection con;

    public Node(String IPaddress, short port) {
        ip = IPaddress;
        con = null;
        this.port = port;
    }

    public Node(Connection c, String ip, short port) {
        con = c;
        this.ip = ip;
        this.port = port;
    }

    public boolean isConnected() {
        return con != null && con.isConnected();
    }

    public String getIp() {
        return ip;
    }

    public short getPort() {return port;}

    public void connect() {
        connect(new TCPConnection());
    }

    public void connect(Connection c) {
        if (!isConnected()) {
            con = c;
            c.connect(ip, port);
        }
    }

    public void send(Packet p) {
        if (isConnected()) {
            con.sendPacket(p);
        } else {
            Log.log("Tried to send while not connected!", LogLevel.ERROR);
        }
    }

    public void ping() {
        Log.log("Pinging node " + getIp() + ":" + getPort(), LogLevel.INFO);
        send(new CurrentTimePacket());
    }

    //We return a list of all packets for performance reasons
    //Returning one packet at a time is a bottleneck
    public List<Packet> handleConnection() {
        if (!isConnected()) {
            return null;
        }
        List<Packet> list = new LinkedList<>();
        while (true) {
            Packet p = con.readPacket();
            if (p == null) {
                return list;
            }
            list.add(p);
        }
    }

    @Override
    public void run() {
        con.handleConnection();
    }
}
