package client.view.chatWindow.actionListeners;

import client.model.ChatMap;
import client.model.ClientsMap;
import client.security.CryptoKeyPair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by destion on 8-4-15.
 */
public class FileClearListener implements ActionListener {

    private DefaultListModel chatModel;
    private ChatMap chats;
    private JList<String> lobbyList;
    private ClientsMap clients;

    public FileClearListener(DefaultListModel chatModel, ChatMap chats, JList<String> lobbyList, ClientsMap clients){
        this.chatModel = chatModel;
        this.chats = chats;
        this.lobbyList = lobbyList;
        this.clients = clients;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Listener test");


       // this.chats.getChatByKey(clients.getpKey(lobbyList.getSelectedValue())).removeAll();
        this.chatModel.removeAllElements();
    }
}
