package client.controller;

import client.model.Chat;
import client.model.ChatMap;
import client.model.ClientsMap;
import client.model.Message;
import client.security.CryptoKeyPair;
import client.view.chatWindow.ClientFrame;
import log.Log;
import log.LogLevel;
import network.Address;
import node.DistanceTable;
import settings.Configuration;

/**
 * ClientController which is the central controller of the client.
 */
public class ClientController implements Runnable {

    private static final ClientController instance = new ClientController();

    public static ClientController getInstance() {
        return instance;
    }

    private boolean running;

    private CryptoKeyPair myKeyPair;
    private NetworkController networkController;
    private ChatMap chats;
    private ClientsMap clients;
    private ClientFrame view;

    /**
     * Constructs a ClientController with a new keypair, map of chats and a NetworkController.
     */
    private ClientController() {
        this.myKeyPair = new CryptoKeyPair();
        this.chats = new ChatMap();
        this.clients = new ClientsMap(new Address(myKeyPair, Configuration.NICKNAME));
    }

    public void init() {
        running = true;
        this.networkController = new NetworkController(this, Configuration.HOST, Configuration.PORT);
        new Thread(networkController).start();

        this.view = new ClientFrame();
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

    public void run() {
        while (running) {
            for (Message m : networkController.getMessage()) {
                Log.log(m.getMessage(), LogLevel.INFO);
            }
        }
    }

    public ClientsMap getClients() {
        return clients;
    }

    /**
     * Returns the ClientFrame
     * @return
     */
    public ClientFrame getView() {
        return view;
    }

    /**
     * Returns the NetworkController
     * @return
     */
    public NetworkController getNetworkController() {
        return networkController;
    }

    /**
     * Returns the keypair
     * @return
     */
    public CryptoKeyPair getKeyPair() {
        return myKeyPair;
    }

    public void updateClients(DistanceTable distanceTable) {
        Log.log("shooop", LogLevel.INFO);
        view.getClientListModel().clear();
        for(Address a : distanceTable.getTable().keySet()) {
            view.getClientListModel().addElement(Integer.toString(a.hashCode()));
            if (!clients.contains(a)) {
                clients.addClient(a);
                chats.addChat(new Chat(a));
            }
        }
        view.getClientList().setModel(view.getClientListModel());
    }
}
