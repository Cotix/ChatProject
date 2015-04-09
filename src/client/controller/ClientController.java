package client.controller;

import client.model.Chat;
import client.security.CryptoKeyPair;

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
    private Map<CryptoKeyPair, Chat> chats;

    public ClientController() {
        myKeyPair = new CryptoKeyPair();
        chats  = new HashMap<>();
        this.networkController =  new NetworkController(HOST, PORT, myKeyPair);
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void addChat(CryptoKeyPair publicKey) {
        Chat chat = new Chat(publicKey);
        chats.put(publicKey, chat);
    }

    public Map<CryptoKeyPair, Chat> getChats() {
        return chats;
    }
}
