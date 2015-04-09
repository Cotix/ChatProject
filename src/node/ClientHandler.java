package node;

import network.connection.Connection;
import network.connection.TCPConnection;
import network.connection.packet.Packet;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Connection con;

    public ClientHandler(Socket s) {
        con = new TCPConnection(s);
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
        while (true) {
            con.handleConnection();
        }
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
}
