package network;

import network.connection.*;


/**
 * Created by cotix on 4/2/15.
 */
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

    public void handleConnection() {
        if (!isConnected()) {
            return;
        }
        con.handleConnection();
    }

}
