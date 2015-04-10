package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.ChatMap;
import client.model.ClientsMap;
import client.view.chatWindow.actionListeners.*;
import com.sun.corba.se.spi.orbutil.fsm.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestFrame extends JFrame {

    private EntryBar bar;
    //private JMenu menu = new JMenu("Window");
    private JMenu file = new JMenu("File");
    private JMenuBar menuBar = new JMenuBar();
    //private JCheckBoxMenuItem fullscreen = new JCheckBoxMenuItem("Fullscreen", false);
    private JMenuItem fileClear = new JMenuItem("Clear chat history");
    private JMenuItem fileClose = new JMenuItem("Close client");
    private JMenuItem fileOpenNewChat = new JMenuItem("Open new chat");
    private NetworkController net;
    private JScrollBar chatScroll;
    private DefaultListModel<String> chatModel;
    private String nickName;
    private JScrollPane chatPane;
    private JList<String> chatList;

    private ChatMap chats;
    private ClientsMap clients;

    private JList<String> lobbyList;
    private DefaultListModel<String> lobbyModel;
    private JScrollPane lobbyPane;

    JOptionPane newChatPane;

    private final int WIDTH = 500;
    private final int HEIGHT = 400;

    public TestFrame(NetworkController net, String nick, ChatMap chats, ClientsMap clients){
        this.clients = clients;
        this.chats = chats;
        this.net = net;
        this.nickName = nick;


        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());

        chatList = new JList<>();
        chatModel = new DefaultListModel<>();
        chatPane = new JScrollPane(chatList);

        lobbyList = new JList<>();
        lobbyModel = new DefaultListModel<>();
        lobbyPane = new JScrollPane(this.lobbyList);

        this.bar = new EntryBar(net , this, this.chats, this.lobbyList, this.clients);

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
        gbc.ipadx = 250;
        panel.add(chatPane, gbc);

        this.bar.addFocusListener(new FocusListener(this.bar));
        this.bar.setText("Enter chat message here.");

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.ipady = 1;
        panel.add(this.bar, gbc);

        JButton newChatButton = new JButton("New chat");
        newChatButton.setMnemonic(KeyEvent.VK_N);
        newChatButton.addActionListener(new NewChatListener(this));

        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        panel.add(newChatButton, gbc);

        lobbyList.addListSelectionListener(new SelectionListener(this.lobbyList, new ChatSwitcher(chats, this.chatList, this.clients, this.chatModel)));
        this.chatScroll = chatPane.getVerticalScrollBar();
        chatScroll.setAutoscrolls(true);

        this.init(WIDTH, HEIGHT);

        this.getContentPane().add(panel);
    }

    private void init(int width, int height){

        this.newChatPane = new JOptionPane(
                "The only way to close this dialog is by\n"
                        + "pressing one of the following buttons.\n"
                        + "Do you understand?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        fileClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileOpenNewChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                openChat();
            }
        });


        fileOpenNewChat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        fileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        fileClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK));
        this.menuBar.add(file);
        file.add(fileOpenNewChat);
        file.add(fileClear);
        file.add(fileClose);

        //this.menuBar.add(menu);
        //menu.add(fullscreen);


        fileClear.addActionListener(new FileClearListener(this.chatModel, this.chats, this.lobbyList, this.clients));

        //fullscreen.addActionListener(new FullscreenActionListener(this, this.fullscreen));
        this.getContentPane().add(this.menuBar, BorderLayout.NORTH);

        this.setSize(width, height);
        this.setTitle("WhatsSwag messenger");
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void addMessage(String message){
        if (message.length() > 0) {
            this.chatModel.addElement(String.format("%s: %s", this.nickName, message));
            this.chatList.setModel(this.chatModel);
            this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);
        }
    }

    public void updateLobby(ArrayList<String> names){
        for (String name : names){
            this.lobbyModel.addElement(name);
        }
        this.lobbyList.setModel(lobbyModel);
        this.lobbyList.ensureIndexIsVisible(this.chatModel.size() - 1);
    }

    public void openChat(){
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        Object[] inputs = {
                "Public Key: ", field1,
                "Username: ",
        };
        String uName = JOptionPane.showInputDialog(this.getContentPane(), inputs, "Open new chat", JOptionPane.QUESTION_MESSAGE);
        String pKey = field1.getText();

        //TODO Add code to check if the public key is available in the network
        boolean validPKey = false;
        while (!validPKey){
            if (pKey.length() <= 0){
                pKey = JOptionPane.showInputDialog(this.getContentPane(), "Public Key: ", "Please enter a valid Public Key", JOptionPane.QUESTION_MESSAGE);
                pKey = JOptionPane.showInputDialog(this.getContentPane(), "Public Key: ", "Please enter a valid Public Key", JOptionPane.QUESTION_MESSAGE);
            } else {
                validPKey = true;
            }
        }

        boolean validName = false;
        while (!validName){
            if ((!(onlyContainsLetters(uName))) || (uName.length() <=0) || (uName.contains(" "))){
                uName = JOptionPane.showInputDialog(this.getContentPane(), "Username: ", "Please enter a valid Username", JOptionPane.QUESTION_MESSAGE);
            } else {
                validName = true;
            }

        }


    }





    public boolean onlyContainsLetters(String s){
        char[] string = s.toCharArray();
        for (char c : string){
            if (!(Character.isLetter(c))){
                return false;
            }
        }
        return true;
    }
}
