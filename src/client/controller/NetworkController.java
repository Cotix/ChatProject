package client.controller;

import client.model.Message;
import log.Log;
import log.LogLevel;
import network.Address;
import network.connection.TCPConnection;
import network.connection.packet.*;
import node.DistanceTable;
import settings.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * NetworkController used by the client to maintain a connection to the Node.
 */
public class NetworkController implements Runnable {

    private ClientController client;

    private TCPConnection connection;
    private Address address;
    private DistanceTable distanceTable;

    /**
     * Constructs a NetworkController used by the client that connects to a node.
     * @param host node host
     * @param port node port
     */
    public NetworkController(ClientController client, String host, short port) {
        this.client = client;
        this.connection = new TCPConnection(host, port);
        this.distanceTable = new DistanceTable();
        this.address = new Address(client.getKeyPair(), Configuration.NICKNAME);


        connection.connect();
        sendIdentify();
    }

    /**
     * Sends an IDENTIFY packet to the node.
     */
    public void sendIdentify() {
        Packet packet = new StringPacket(client.getKeyPair().getRawPublicKey(), PacketUtils.PacketType.IDENTIFY);
        connection.sendPacket(packet);
    }

    /**
     * Sends a MESSAGE packet to the node.
     * @param message Message to send
     * @throws UnsupportedEncodingException
     */
    public void sendMessage(Message message) throws UnsupportedEncodingException {
        connection.sendPacket(message.makePacket());
    }

    @Override
    public void run() {
        while(true) {
            connection.handleConnection();
        }
    }

    public Address getAddress() {
        return address;
    }

    public DistanceTable getDistanceTable() {
        return distanceTable;
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
            if (PacketUtils.getPacketType(p) == PacketUtils.PacketType.DISTANCE) {
                if (distanceTable.update(new DistancePacket(p.getData()))){
                    client.updateClients(distanceTable);
                }
            } else if (PacketUtils.getPacketType(p) == PacketUtils.PacketType.PING) {
                CurrentTimePacket pong = new CurrentTimePacket(p.getRawData());
                pong.setType(PacketUtils.PacketType.PONG);
                connection.sendPacket(pong);
            } else if (PacketUtils.getPacketType(p) == PacketUtils.PacketType.MESSAGE) {
                Log.log("Client received a message!", LogLevel.NONE);
                list.add(Message.makeMessage(p, client.getKeyPair()));
            }
        }
    }
}
