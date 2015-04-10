package client.controller;

import client.model.Message;
import client.security.CryptoKeyPair;
import network.connection.TCPConnection;
import java.io.UnsupportedEncodingException;

public class NetworkController implements Runnable {

    private TCPConnection connection;
    private CryptoKeyPair myKeyPair;

    public NetworkController(String host, short port, CryptoKeyPair keyPair){
        this.connection = new TCPConnection(host, port);
        myKeyPair = keyPair;
    }

    @Override
    public void run() {
        connection.handleConnection();
    }

    public void send(String message, CryptoKeyPair recvKeyPair) throws UnsupportedEncodingException {
        Message mess = new Message(message, myKeyPair, recvKeyPair, 1000L);
        connection.sendPacket(mess.makePacket());
    }
}
