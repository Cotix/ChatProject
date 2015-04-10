package client.controller;

import client.model.Chat;
import client.security.CryptoKeyPair;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by martijn on 8-4-15.
 */
public class ClientController {

    public static final String HOST = "localhost";
    public static final short PORT = 8080;
    private CryptoKeyPair myKeyPair;
    private NetworkController networkController;
    private Map<String, Chat> chats;

    public ClientController() {
        myKeyPair = new CryptoKeyPair();
        chats  = new HashMap<>();
        this.networkController =  new NetworkController(HOST, PORT, myKeyPair);
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void addChat(PublicKey publicKey) {
        Chat chat = new Chat(publicKey);
        chats.put(publicKey.toString(), chat);
    }

    public Map<String, Chat> getChats() {
        return chats;
    }
}
