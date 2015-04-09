package node;

import network.connection.Connection;
import network.connection.TCPConnection;

import java.net.Socket;

/**
 * Created by cotix on 4/9/15.
 */
public class ClientHandler extends Thread{
    private Connection con;



    public ClientHandler(Socket s) {
        con = new TCPConnection(s);
    }

    public void run() {
        while (true) {
            con.handleConnection();
        }
    }
}
