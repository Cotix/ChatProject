package client.controller;

import client.model.Chat;
import client.security.CryptoKeyPair;
import network.Address;
import settings.Configuration;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by martijn on 8-4-15.
 */
public class ClientController {

    private CryptoKeyPair myKeyPair;
    private NetworkController networkController;
    private Map<String, Chat> chats;

    public ClientController() {
        myKeyPair = new CryptoKeyPair();
        chats  = new HashMap<>();
        this.networkController =  new NetworkController(Configuration.HOST, Configuration.PORT, myKeyPair);
    }

    public ClientController(String host, short port) {
        this.myKeyPair = new CryptoKeyPair();
        this.networkController = new NetworkController(host, port, this.myKeyPair);
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void addChat(Address address) {
        Chat chat = new Chat(address);
        chats.put(address.getNickName(), chat);
    }

    public Map<String, Chat> getChats() {
        return chats;
    }
}
