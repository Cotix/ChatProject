package client.controller;

import client.model.Message;
import client.security.CryptoKeyPair;
import network.connection.TCPConnection;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;

public class NetworkController implements Runnable {

    private TCPConnection connection;
    private CryptoKeyPair myKeyPair;

    public NetworkController(String host, short port, CryptoKeyPair keyPair){
        this.connection = new TCPConnection(host, port);
        connection.connect();
        myKeyPair = keyPair;
        sendIdentify(keyPair);
    }

    public void sendIdentify(CryptoKeyPair keyPair) {
        Packet packet = new StringPacket(keyPair.getRawPublicKey(), PacketUtils.PacketType.IDENTIFY);
        connection.sendPacket(packet);
    }

    public CryptoKeyPair getMyKeyPair() {
        return myKeyPair;
    }

    @Override
    public void run() {
        connection.handleConnection();
    }

    public void sendMessage(Message message, CryptoKeyPair recvKeyPair) throws UnsupportedEncodingException {
        connection.sendPacket(message.makePacket());
    }

    public List<Message> getMessage() {
        if (!connection.isConnected()) {
            return null;
        }
        List<Message> list = new LinkedList<>();
        while (true) {
            Packet p = connection.readPacket();
            if (p == null) {
                return list;
            }

            list.add(Message.makeMessage(p, myKeyPair));
        }
    }
}
