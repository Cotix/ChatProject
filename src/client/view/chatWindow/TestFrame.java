package client.view.chatWindow;

import client.controller.NetworkController;
import client.view.chatWindow.actionListeners.FileClearListener;
import client.view.chatWindow.actionListeners.FocusListener;
import client.view.chatWindow.actionListeners.FullscreenActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private DefaultListModel chatModel;
    private String nickName;
    private JScrollPane chatPane;
    private JList<String> chatList;

    private JList lobbyList;
    private DefaultListModel lobbyModel;
    private JScrollPane lobbyPane;

    private final int WIDTH = 500;
    private final int HEIGHT = 400;

    public TestFrame(NetworkController net, String nick){
        this.net = net;
        this.nickName = nick;
        this.bar = new EntryBar(net , this);

        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());

        chatList = new JList<>();
        chatModel = new DefaultListModel();
        chatPane = new JScrollPane(chatList);

        lobbyList = new JList<>();
        lobbyModel = new DefaultListModel();
        lobbyPane = new JScrollPane(this.lobbyList);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.weightx = 1;


        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 340;
        panel.add(lobbyPane, gbc);

        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.ipadx = 150;
        panel.add(chatPane, gbc);

        this.bar.addFocusListener(new FocusListener(this.bar));
        this.bar.setText("Enter chat message here.");

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.ipady = 1;
        panel.add(this.bar, gbc);



        this.init(WIDTH, HEIGHT);

        this.getContentPane().add(panel);
    }

    private void init(int width, int height){
        fileClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        this.menuBar.add(file);
        file.add(fileClear);
        file.add(fileClose);

        this.menuBar.add(menu);
        menu.add(fullscreen);


        fileClear.addActionListener(new FileClearListener(this.chatModel));

        fullscreen.addActionListener(new FullscreenActionListener(this, this.fullscreen));
        this.getContentPane().add(this.menuBar, BorderLayout.NORTH);

        this.setSize(width, height);
        this.setTitle("WhatsSwag messenger");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void addMessage(String message){
        this.chatModel.addElement(String.format("%s: %s", this.nickName, message));
        this.chatList.setModel(this.chatModel);
        this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);
    }

    public void updateLobby(ArrayList<String> names){
        for (String name : names){
            this.lobbyModel.addElement(name);
        }
        this.lobbyList.setModel(lobbyModel);
        this.lobbyList.ensureIndexIsVisible(this.chatModel.size() - 1);
    }

}
