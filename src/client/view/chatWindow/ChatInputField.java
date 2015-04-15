package client.view.chatWindow;

import client.controller.ClientController;
import client.model.Message;

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
                                client.getView().getClientList().getSelectedValue().getAddress(),
                                System.currentTimeMillis()
                        );

                        client.getChats().getChatByKey(client.getView().getClientList().getSelectedValue().getAddress()).addMessage(message);

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
