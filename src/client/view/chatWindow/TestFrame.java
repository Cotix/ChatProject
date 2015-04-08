package client.view.chatWindow;

import client.controller.NetworkController;
import client.view.chatWindow.actionListeners.FileClearListener;
import client.view.chatWindow.actionListeners.FullscreenActionListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by destion on 8-4-15.
 */
public class TestFrame extends JFrame {

    private EntryBar bar;
    private JMenu menu = new JMenu("Window");
    private JMenu file = new JMenu("File");
    private JMenuBar menuBar = new JMenuBar();
    private JCheckBoxMenuItem fullscreen = new JCheckBoxMenuItem("Fullscreen", false);
    private JMenuItem fileClear = new JMenuItem("Clear chat");
    private JMenuItem fileClose = new JMenuItem("Close chat");
    private NetworkController net;
    private JScrollBar chatScroll;
    DefaultListModel chatModel;
    private String nickName;
    JScrollPane chatPane;
    JList<String> chatList;

    private final int WIDTH = 500;
    private final int HEIGHT = 400;

    public TestFrame(NetworkController net){
        this.net = net;

        this.bar = new EntryBar(net , this);

        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());

        chatList = new JList<>();
        chatModel = new DefaultListModel();
        chatPane = new JScrollPane(chatList);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        panel.add(new JButton("Test1"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JButton("Test2"), gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(new JButton("Test3"), gbc);


        /*
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JButton("Test4"), gbc);
        */

        chatPane.setSize(50, 300);

        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(chatPane, gbc);


        this.init(WIDTH, HEIGHT);

        this.getContentPane().add(panel);
    }

    public TestFrame(int width, int height){

        GridBagConstraints gbc = new GridBagConstraints();
        this.init(width, height);
    }

    private void init(int width, int height){
        this.menuBar.add(file);
        file.add(fileClear);
        file.add(fileClose);

        this.menuBar.add(menu);
        menu.add(fullscreen);


        fileClear.addActionListener(new FileClearListener(this.chatModel));

        this.getContentPane().add(this.bar, BorderLayout.SOUTH);

        fullscreen.addActionListener(new FullscreenActionListener(this, this.fullscreen));
        this.getContentPane().add(this.menuBar, BorderLayout.NORTH);

        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void addMessage(String message){
        this.chatModel.addElement(String.format("%s: %s", this.nickName, message));
        this.chatList.setModel(this.chatModel);
        this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);
    }

}
