package client.controller;

import client.model.Message;
import client.security.CryptoKeyPair;
import log.Log;
import log.LogLevel;
import network.Address;
import network.connection.TCPConnection;
import network.connection.packet.DistancePacket;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;
import node.DistanceTable;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * NetworkController used by the client to maintain a connection to the Node.
 */
public class NetworkController implements Runnable {

    private TCPConnection connection;
    private CryptoKeyPair myKeyPair;
    private Address self;
    private DistanceTable distanceTable;

    /**
     * Constructs a NetworkController used by the client that connects to a node.
     * @param host node host
     * @param port node port
     * @param keyPair keypair of the client
     */
    public NetworkController(String host, short port, CryptoKeyPair keyPair, String nick){
        this.connection = new TCPConnection(host, port);
        distanceTable = new DistanceTable();
        connection.connect();
        myKeyPair = keyPair;
        this.self = new Address(myKeyPair, nick);
        sendIdentify(keyPair);
    }

    /**
     * Sends an IDENTIFY packet to the node.
     * @param keyPair keypair of the client
     */
    public void sendIdentify(CryptoKeyPair keyPair) {
        Packet packet = new StringPacket(keyPair.getRawPublicKey(), PacketUtils.PacketType.IDENTIFY);
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

    /**
     * Returns the keypair in the NetworkController
     * @return
     */
    public CryptoKeyPair getMyKeyPair() {
        return myKeyPair;
    }

    @Override
    public void run() {
        while(true) {
            connection.handleConnection();
        }
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
                distanceTable.update(new DistancePacket(p.getData()));
            } else if (PacketUtils.getPacketType(p) == PacketUtils.PacketType.MESSAGE) {
                Log.log("Client received a message!", LogLevel.INFO);
                list.add(Message.makeMessage(p, myKeyPair));
            }
        }
    }

    public Address getSelf(){
        return this.self;
    }
}
