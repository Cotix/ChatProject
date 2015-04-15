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
import settings.Configuration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ClientFrame extends JFrame {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;

    private ClientController client;

    private boolean switched;

    private JScrollPane clientPane;
    private JList<Address> clientList;
    private DefaultListModel<Address> clientListModel;

    private JScrollPane chatPane;
    private JList<Message> chatView;
    private DefaultListModel<Message> chatViewModel;

    private JTextField input;
    private JPanel panel;

    private GridBagConstraints gbc;

    public ClientFrame() {
        this.switched = false;

        this.client = ClientController.getInstance();

        this.gbc = new GridBagConstraints();
        this.panel = new JPanel(new GridBagLayout());

        this.clientList = new JList<>();
        this.clientListModel = new DefaultListModel<>();
        this.clientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (clientList.getSelectedValue() != null) {
                    if (clientList.getSelectedValue().getNickName() == null){
                        /*
                        String uName = JOptionPane.showInputDialog(getContentPane(), "Please enter a new Nickname: ",
                            "Enter new Nickname", JOptionPane.QUESTION_MESSAGE);
                        boolean validName = false;
                        while (!validName){
                            if ((!(onlyContainsLetters(uName))) || (uName.length() <=0) || (uName.contains(" "))){
                                uName = JOptionPane.showInputDialog(getContentPane(), "Please enter a valid Nickname: ",
                                        "Enter valid Nickname", JOptionPane.QUESTION_MESSAGE);
                            } else {
                                validName = true;
                            }
                        }
                        clientList.getSelectedValue().setNickName(uName);
                        clientList.getSelectedValue().setHasNick();
                        Log.log(uName, LogLevel.INFO);
                        Log.log(String.valueOf("Client selected:" + clientList.getSelectedValue().hashCode()), LogLevel.NONE);
                        */
                        switched = true;
                    }
                }
            }
        });
        this.clientPane = new JScrollPane(clientList);

        this.chatView = new JList<>();
        this.chatViewModel = new DefaultListModel<>();
        this.chatPane = new JScrollPane(chatView);


        this.input = new ChatInputField();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 9;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(clientPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(chatPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        panel.add(input, gbc);

        this.setSize(WIDTH, HEIGHT);
        this.setTitle("WhatSwag Messenger" + "   |   " + Configuration.NICKNAME + " with hash: " + client.getKeyPair().hashCode());
        this.clientListModel.removeElement(client.getKeyPair());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(panel);

        chatPane.setAutoscrolls(true);

        this.setVisible(true);
    }

    public JList<Address> getClientList() {
        return clientList;
    }

    public DefaultListModel<Address> getClientListModel() {
        return clientListModel;
    }

    public JList<Message> getChatView() {
        return chatView;
    }

    public DefaultListModel<Message> getChatViewModel() {
        return chatViewModel;
    }

    public void updateMessages() {
        try {
            if (getClientList().getSelectedValue() != null) {
                final Chat chat = client.getChats().getChatByKey(getClientList().getSelectedValue().getAddress());
                if (chat != null && (chat.updated() || this.switched)) {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            chatViewModel.clear();
                            for (Message m : chat.getMessages()) {
                                chatViewModel.addElement(m);
                            }
                            chatView.setModel(chatViewModel);
                            chatView.ensureIndexIsVisible(chatViewModel.size() - 1);
                            if (switched) {
                                switched = false;
                            }
                        }
                    });
                }
            }
        } catch (Throwable e){
            System.out.println("HELP");
        }
    }

    public boolean onlyContainsLetters(String s){
        if (s != null) {
            char[] cs = s.toCharArray();
            for (char c : cs) {
                if (!(Character.isLetter(c))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
