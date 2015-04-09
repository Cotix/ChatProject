package client.controller;

import client.model.Message;
import client.security.MessageSignature;
import client.security.RSAEncryption;
import network.connection.TCPConnection;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.PrivateKey;

public class NetworkController implements Runnable {

    TCPConnection connection;
    BufferedReader in;
    BufferedWriter out;

    StringPacket message;

    public NetworkController(String host, short port){
        this.connection = new TCPConnection(host, port);
    }

    @Override
    public void run() {
        connection.handleConnection();
    }

    public void send(String message) throws UnsupportedEncodingException {

        Key placeHolder = null;
        PrivateKey placeHolderu = null;
        MessageSignature signature = new MessageSignature();
        signature.generateKeys();

        byte[] msg = message.getBytes();
        byte[] signedMessage = signature.sign(msg, placeHolderu);
        byte[] fullSigMsg = new byte[signedMessage.length + msg.length];
        System.arraycopy(msg, 0, fullSigMsg, 0, msg.length);
        System.arraycopy(signedMessage, 0, fullSigMsg, msg.length, signedMessage.length);
        //TODO encrypt data in message and send encrypted data
        RSAEncryption encryption = new RSAEncryption();
        RSAEncryption.generateKeys();
        encryption.rsaEncrypt(fullSigMsg, placeHolder);

        //TODO switch out placeholders (PH)
        Message mess = new Message(message, "PH", "PH", 1000L);
        connection.sendPacket(new StringPacket(mess.toString(), PacketUtils.PacketType.UNKNOWN));
    }
}
