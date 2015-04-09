package client.view.chatWindow.actionListeners;

import client.model.ChatMap;
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
    private JList<CryptoKeyPair> lobbyList;

    public FileClearListener(DefaultListModel chatModel, ChatMap chats, JList lobbyList){
        this.chatModel = chatModel;
        this.chats = chats;
        this.lobbyList = lobbyList;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Listener test");

        //TODO waaor mwerkt niet
        this.chats.getChatByKey(lobbyList.getSelectedValue()).removeAll();
        this.chatModel.removeAllElements();
    }
}
