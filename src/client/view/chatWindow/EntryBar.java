package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.ChatMap;
import client.model.ClientsMap;
import client.model.Message;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;

/**
 * Created by destion on 7-4-15.
 */
public class EntryBar extends JTextField implements KeyListener {

    private NetworkController networkController;
    private TestFrame testFrame;
    private JList<String> lobbyList;
    private ChatMap chats;
    private ClientsMap clientsMap;

    public EntryBar(NetworkController net, TestFrame frame, ChatMap chats, JList<String> lobbyList, ClientsMap clientsMap){
        super();
        this.addKeyListener(this);
        this.networkController = net;
        this.testFrame = frame;
        this.chats = chats;
        this.lobbyList = lobbyList;
        this.clientsMap = clientsMap;
    }




    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                if (this.getText().length() > 0) {
                    this.testFrame.addMessage(this.getText());
                    if ((clientsMap.getClientByName(lobbyList.getSelectedValue())) != null) {

                        Message tempMess = new Message(
                                this.getText(),
                                this.clientsMap.getOwnAddress().getAddress(),
                                this.clientsMap.getClientByName(lobbyList.getSelectedValue()).getAddress(),
                                System.currentTimeMillis()
                        );

                        chats.getChatByKey(clientsMap.getKeyPair(lobbyList.getSelectedValue())).addMessage(tempMess);

                        this.setText("");

                        networkController.sendMessage(tempMess, clientsMap.getKeyPair(lobbyList.getSelectedValue()));
                    }
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
