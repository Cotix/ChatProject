package client.controller;

import client.model.Chat;
import client.model.ChatMap;
import client.security.CryptoKeyPair;
import client.view.chatWindow.ThreadPH;
import network.Address;
import settings.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClientController which is the central controller of the client.
 */
public class ClientController {

    private CryptoKeyPair myKeyPair;
    private NetworkController networkController;
    private ChatMap chats;
    private ThreadPH view;

    /**
     * Constructs a ClientController with a new keypair, map of chats and a NetworkController.
     */
    public ClientController() {
        myKeyPair = new CryptoKeyPair();
        this.chats = new ChatMap();
        this.networkController =  new NetworkController(Configuration.HOST, Configuration.PORT, myKeyPair, "Destion");
        view = new ThreadPH();
        view.run();
    }

    /**
     * Constructs a ClientController with parameters for the NetworkController.
     * @param host node host
     * @param port node port
     */
    public ClientController(String host, short port) {
        this.myKeyPair = new CryptoKeyPair();
        this.networkController = new NetworkController(host, port, this.myKeyPair, "Destion");
    }

    /**
     * Returns the NetworkController
     * @return
     */
    public NetworkController getNetworkController() {
        return networkController;
    }

    /**
     * TODO: Obsolete?
     * Create as a Chat object and adds it to the list.
     * @param address address of the receiver
     */
    public void addChat(Address address) {
        Chat chat = new Chat(address);
        chats.addChat(chat);
    }

    /**
     * Returns all chats
     * @return
     */

}
