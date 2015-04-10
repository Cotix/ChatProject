package client.controller;

import client.model.ClientsMap;
import client.model.Message;
import client.security.CryptoKeyPair;
import network.Address;
import network.connection.TCPConnection;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * NetworkController used by the client to maintain a connection to the Node.
 */
public class NetworkController implements Runnable {

    private TCPConnection connection;
    private CryptoKeyPair myKeyPair;
    private Address self;

    /**
     * Constructs a NetworkController used by the client that connects to a node.
     * @param host node host
     * @param port node port
     * @param keyPair keypair of the client
     */
    public NetworkController(String host, short port, CryptoKeyPair keyPair){
        this.connection = new TCPConnection(host, port);
        connection.connect();
        myKeyPair = keyPair;
        this.self = new Address(myKeyPair);
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
     * @param recvKeyPair keypair of the receiver
     * @throws UnsupportedEncodingException
     */
    public void sendMessage(Message message, CryptoKeyPair recvKeyPair) throws UnsupportedEncodingException {
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
        connection.handleConnection();
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

    public Address getSelf(){
        return this.self;
    }
}
