package node;

import network.connection.Connection;
import network.connection.TCPConnection;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.Packet;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

//This class handles the connection between the node and the client
//It uses a Connection to communicate with the client.
//The LocalNode can fetch packets from the connection, which maintains a packet queue

public class ClientHandler implements Runnable {
    private boolean running;
    private Connection con;

    public ClientHandler(Socket s) {
        con = new TCPConnection(s);
        running = true;
    }

    /**
     * Returns whether the Connection exists and is connected.
     * @return true if connected
     */
    public boolean isConnected() {
        return con != null && con.isConnected();
    }

    /**
     * Handles the connection
     */
    public void run() {
        while (running) {
            con.handleConnection();
            if (!con.isConnected()) {
                stop();
            }
        }
    }

    /**
     * Closes the connection and prevents the Thread from running
     */
    public void stop() {
        this.running = false;
        con.disconnect();
    }

    /**
     * Sends a packet over the Connection.
     * @param p
     */
    public void send(Packet p) {
        con.sendPacket(p);
    }

    public boolean getRunning() {
        return running;
    }

    /**
     * Returns packets from Connection
     * @return list of packets
     */
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

    public void ping() {
        send(new CurrentTimePacket());
    }
}
