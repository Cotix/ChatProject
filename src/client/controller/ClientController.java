package client.controller;

import client.model.Chat;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by martijn on 8-4-15.
 */
public class ClientController {

    public static final String HOST = "localhost";
    public static final short PORT = 8080;

    private NetworkController networkController;
    private Map<String, Chat> chats;

    public ClientController() {
        chats  = new HashMap<>();
        this.networkController =  new NetworkController(HOST, PORT);
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void addChat(String publicKey) {
        Chat chat = new Chat(publicKey);
        chats.put(publicKey, chat);
    }

    public Map<String, Chat> getChats() {
        return chats;
    }
}
