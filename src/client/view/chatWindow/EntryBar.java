package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.ChatMap;

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

    public EntryBar(NetworkController net, TestFrame frame, ChatMap chats){
        super();
        this.addKeyListener(this);
        this.networkController = net;
        this.testFrame = frame;
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
