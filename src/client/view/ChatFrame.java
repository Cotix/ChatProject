package client.view;

import client.controller.NetworkController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by destion on 7-4-15.
 */
public class ChatFrame extends JFrame {

    private EntryBar bar;
    private JMenu menu = new JMenu("Window");
    private JMenuBar menuBar = new JMenuBar();
    private JCheckBoxMenuItem fullcreen = new JCheckBoxMenuItem("Fullscreen", false);
    private NetworkController net;

    private JList<String> chatJList = new JList<String>();
    private DefaultListModel<String> chatModel = new DefaultListModel<>();
    private JScrollPane chatScrollPane;

    private String nickName;

    public ChatFrame(NetworkController networkController, String nickName) {

        super(nickName);

        this.nickName = nickName;

        this.net = networkController;



        bar = new EntryBar(this.net,this);

        this.chatScrollPane = new JScrollPane(this.chatJList);


        this.menuBar.add(menu);
        menu.add(fullcreen);


        fullcreen.setMnemonic(KeyEvent.VK_F11);

        this.getContentPane().add(this.menuBar, BorderLayout.NORTH);
        this.getContentPane().add(this.bar, BorderLayout.SOUTH);
        this.getContentPane().add(this.chatScrollPane, BorderLayout.CENTER);


        this.setSize(400, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);


    }

    public void addMessage(String message){
        this.chatModel.addElement(String.format("%s: %s", this.nickName, message));
        this.chatJList.setModel(this.chatModel);
        this.chatJList.ensureIndexIsVisible(this.chatModel.size()-1);
    }
}
