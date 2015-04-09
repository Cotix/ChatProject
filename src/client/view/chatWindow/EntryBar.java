package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.ChatMap;
import client.model.Client;
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

    public EntryBar(NetworkController net, TestFrame frame, ChatMap chats, JList lobbyList, ClientsMap clientsMap){
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
                    for (Client client : clientsMap.getMap()) {
                        System.out.println(chats);
                        /*if ((clientsMap.getClientByName(lobbyList.getSelectedValue())) != null) {
                            chats.getChatByKey(clientsMap.getpKey(lobbyList.getSelectedValue())).addMessage(new Message(
                                    this.getText(),
                                    this.clientsMap.getClientByName(lobbyList.getSelectedValue()).getpKey(),
                                    this.clientsMap.getClientByName(testFrame.getName()).getpKey(),
                                    System.currentTimeMillis()
                            ));
                        }*/
                    }

                    this.setText("");

                    networkController.send(this.getText());

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
