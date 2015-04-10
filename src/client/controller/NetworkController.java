package client.controller;

import client.model.Message;
import client.security.CryptoKeyPair;
import network.connection.TCPConnection;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

public class NetworkController implements Runnable {

    private TCPConnection connection;
    private CryptoKeyPair myKeyPair;

    public NetworkController(String host, short port, CryptoKeyPair keyPair){
        this.connection = new TCPConnection(host, port);
        myKeyPair = keyPair;
        sendIdentify(keyPair);
    }

    public void sendIdentify(CryptoKeyPair keyPair) {
        Packet packet = new StringPacket(keyPair.getRawPublicKey(), PacketUtils.PacketType.IDENTIFY);
        connection.sendPacket(packet);
    }

    @Override
    public void run() {
        connection.handleConnection();
    }

    public void sendMessage(String message, CryptoKeyPair recvKeyPair) throws UnsupportedEncodingException {
        Message mess = new Message(message, myKeyPair, recvKeyPair, 1000L);
        connection.sendPacket(mess.makePacket());
    }
}
