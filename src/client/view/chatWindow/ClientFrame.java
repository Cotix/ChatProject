package client.view.chatWindow;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;

    private JList<String> clientList;
    private DefaultListModel<String> clientListModel;

    private JList<String> chatView;
    private DefaultListModel<String> chatViewModel;

    private JButton chatButton;
    private JTextField input;
    private JPanel panel;

    private GridBagConstraints gbc;

    public ClientFrame() {
        this.gbc = new GridBagConstraints();
        this.panel = new JPanel(new GridBagLayout());

        this.clientList = new JList<>();
        this.clientListModel = new DefaultListModel<>();

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

    public JList<String> getClientList() {
        return clientList;
    }

    public DefaultListModel<String> getClientListModel() {
        return clientListModel;
    }

    public JList<String> getChatView() {
        return chatView;
    }

    public DefaultListModel<String> getChatViewModel() {
        return chatViewModel;
    }

    public void updateMessages() {

    }
}
