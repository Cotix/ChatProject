package client.view.chatWindow;

import client.controller.ClientController;
import client.model.Message;
import log.Log;
import log.LogLevel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;

public class ChatInputField extends JTextField implements KeyListener {

    private ClientController client;

    public ChatInputField() {
        super();
        this.addKeyListener(this);
        this.client = ClientController.getInstance();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                if (this.getText().length() > 0) {
                    if (client.getView().getClientList().getSelectedValue() != null) {
                        Message message = new Message(
                                this.getText(),
                                client.getKeyPair(),
                                client.getClients().getClientByName(client.getView().getClientList().getSelectedValue()).getAddress(),
                                System.currentTimeMillis()
                        );
                        client.getNetworkController().sendMessage(message);
                        this.setText("");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
