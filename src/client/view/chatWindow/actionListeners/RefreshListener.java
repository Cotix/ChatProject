package client.view.chatWindow.actionListeners;

import client.controller.NetworkController;
import client.model.ChatMap;
import client.model.ClientsMap;
import client.view.chatWindow.TestFrame;
import network.Address;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by destion on 10-4-15.
 */
public class RefreshListener implements ActionListener {

    private NetworkController net;
    private ChatMap chats;
    private ClientsMap clients;
    private TestFrame frame;

    public RefreshListener(ChatMap chats, NetworkController net, ClientsMap clients, TestFrame frame){
        this.chats = chats;
        this.net = net;
        this.clients = clients;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ArrayList<String> names = new ArrayList<>();
        this.chats.handleNewMessages(net.getMessage());
        for(Address a : this.net.getDistanceTable().getTable().keySet()) {
            a.setNickName(Integer.toString(a.hashCode()));
            clients.addClient(a);
            names.add(a.getNickName());

        }
        frame.updateLobby(names);
    }
}
