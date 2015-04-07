package client.controller;

import client.model.Message;
import network.connection.TCPConnection;
import network.connection.packet.StringPacket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;

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
        //TODO switch out placeholders (PH)
        Message mess = new Message(message, "PH");
        connection.sendPacket(new StringPacket(mess.toString()));
    }
}
