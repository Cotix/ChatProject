package client.view.chatWindow;

import client.controller.ClientController;
import client.model.Chat;
import client.model.ChatMap;
import client.model.ClientsMap;
import client.model.Message;
import com.sun.security.ntlm.Client;
import log.Log;
import log.LogLevel;
import network.Address;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ClientFrame extends JFrame {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;

    private ClientController client;

    private JList<Address> clientList;
    private DefaultListModel<Address> clientListModel;

    private JList<String> chatView;
    private DefaultListModel<String> chatViewModel;

    private JButton chatButton;
    private JTextField input;
    private JPanel panel;

    private GridBagConstraints gbc;

    public ClientFrame() {
        this.client = ClientController.getInstance();

        this.gbc = new GridBagConstraints();
        this.panel = new JPanel(new GridBagLayout());

        this.clientList = new JList<>();
        this.clientListModel = new DefaultListModel<>();
        this.clientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Log.log("Toggle chats", LogLevel.INFO);
                if (clientList.getSelectedValue() != null) {
                    Log.log(String.valueOf(clientList.getSelectedValue().hashCode()), LogLevel.INFO);
                }
            }
        });

        this.chatView = new JList<>();
        this.chatViewModel = new DefaultListModel<>();

        this.chatButton = new JButton("New Chat");

        this.input = new ChatInputField();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(clientList, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(chatView, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(chatButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(input, gbc);

        this.setSize(WIDTH, HEIGHT);
        this.setTitle("WhatSwag Messenger");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(panel);

        this.setVisible(true);
    }

    public JList<Address> getClientList() {
        return clientList;
    }

    public DefaultListModel<Address> getClientListModel() {
        return clientListModel;
    }

    public JList<String> getChatView() {
        return chatView;
    }

    public DefaultListModel<String> getChatViewModel() {
        return chatViewModel;
    }

    public void showMessages(Chat chat) {
        chatViewModel.clear();
        for (Message message : chat.getMessages()) {
            chatViewModel.addElement(message.getMessage());
        }
        chatView.setModel(chatViewModel);
    }
}