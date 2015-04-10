package client.view.chatWindow;

import client.model.*;
import client.security.CryptoKeyPair;
import network.Address;

import javax.swing.*;
import java.security.PublicKey;

public class ChatSwitcher {

    private ChatMap chats;
    private JList<String> chatList;
    private ClientsMap clientsMap;
    private DefaultListModel<String> chatModel;

    public ChatSwitcher(ChatMap chats, JList<String> chatList, ClientsMap clients, DefaultListModel<String> chatModel){
        this.chats = chats;
        this.chatList = chatList;
        this.clientsMap = clients;
        this.chatModel = chatModel;
    }

    public Chat switchChat(String nick){
        if (clientsMap.contains(nick)){
            for (Address adress : chats.getAdresses()){
                if (adress.getAddress().equals(clientsMap.getClientByName(nick).getAddress())){
                    return chats.getChatByKey(clientsMap.getKeyPair(nick));
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

                System.out.println(clientsMap.getNick(mess.getSenderPair()));
                this.chatModel.addElement(String.format("%s: %s", clientsMap.getNick(mess.getSenderPair()), mess.getMessage()));
                this.chatList.setModel(this.chatModel);
                this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);
            }
        } else {
            System.out.println("Lege lijst");
        }
    }
}
