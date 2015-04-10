package client.view.chatWindow.actionListeners;

import client.controller.NetworkController;
import client.model.ChatMap;
import client.model.ClientsMap;
import network.Address;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by destion on 10-4-15.
 */
public class RefreshListener implements ActionListener {

    NetworkController net;
    ChatMap chats;
    ClientsMap clients;

    public RefreshListener(ChatMap chats, NetworkController net, ClientsMap clients){
        this.chats = chats;
        this.net = net;
        this.clients = clients;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.chats.handleNewMessages(net.getMessage());
        for(Address a : this.net.getDistanceTable().getTable().keySet()) {
            a.setNickName(Integer.toString(a.hashCode()));
            clients.addClient(a);
        }

    }
}
