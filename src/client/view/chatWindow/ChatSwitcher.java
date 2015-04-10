package client.view.chatWindow;

import client.model.*;

import javax.swing.*;
import java.security.PublicKey;

public class ChatSwitcher {

    private ChatMap chats;
    private JList<String> chatList;
    private ClientsMap clientsMap;
    private DefaultListModel chatModel;

    public ChatSwitcher(ChatMap chats, JList chatList, ClientsMap clients, DefaultListModel chatModel){
        this.chats = chats;
        this.chatList = chatList;
        this.clientsMap = clients;
        this.chatModel = chatModel;
    }

    public Chat switchChat(String nick){
        if (clientsMap.contains(nick)){
            for (PublicKey key : chats.getKeys()){
                if (key.equals(clientsMap.getpKey(nick))){
                    return chats.getChatByKey(clientsMap.getpKey(nick));
                }
            }
        }
        return null;
    }

    public void addChat(Chat chat){
        this.chats.addChat(chat);
    }

    public void showList(Chat chat){
        if (chat.getMessages().size() > 0) {
            this.chatModel.removeAllElements();
            for (Message mess : chat.getMessages()) {
                this.chatModel.addElement(String.format("%s: %s", clientsMap.getNick(mess.getPublicKeySender()), mess.getMessage()));
                this.chatList.setModel(this.chatModel);
                this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);
            }
        } else {
            System.out.println("Lege lijst");
        }
    }
}
