package client.controller;

import client.model.Message;
import client.security.CryptoKeyPair;
import network.connection.TCPConnection;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;

public class NetworkController implements Runnable {

    private TCPConnection connection;
    private BufferedReader in;
    private BufferedWriter out;
    private CryptoKeyPair myKeyPair;

    StringPacket message;

    public NetworkController(String host, short port, CryptoKeyPair keyPair){
        this.connection = new TCPConnection(host, port);
        myKeyPair = keyPair;
    }

    @Override
    public void run() {
        connection.handleConnection();
    }

    public void send(String message) throws UnsupportedEncodingException {
        byte[] msg = message.getBytes();
        byte[] signedMessage = myKeyPair.sign(msg);
        byte[] fullSigMsg = new byte[signedMessage.length + msg.length];
        System.arraycopy(msg, 0, fullSigMsg, 0, msg.length);
        System.arraycopy(signedMessage, 0, fullSigMsg, msg.length, signedMessage.length);
        //TODO encrypt data in message and send encrypted data
        /*
        fullSigMsg = keyPairOfReceiver.encrypt(fullSigMsg);
         */

        //TODO switch out placeholders (PH)
        Message mess = new Message(message, new CryptoKeyPair(), new CryptoKeyPair(), 1000L);
        connection.sendPacket(new StringPacket(mess.toString(), PacketUtils.PacketType.UNKNOWN));
    }
}
