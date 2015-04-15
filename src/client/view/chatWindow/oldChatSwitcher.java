package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.*;
import network.Address;
import javax.swing.*;

public class oldChatSwitcher {

    private ChatMap chats;
    private JList<String> chatList;
    private ClientsMap clientsMap;
    private DefaultListModel<String> chatModel;
    private NetworkController netControl;

    public oldChatSwitcher(ChatMap chats, JList<String> chatList, ClientsMap clients, DefaultListModel<String> chatModel, NetworkController netControl){
        this.chats = chats;
        this.chatList = chatList;
        this.clientsMap = clients;
        this.chatModel = chatModel;
        this.netControl = netControl;
    }

    public Chat switchChat(String nick){
        if (clientsMap.contains(nick)){
            for (Address address : chats.getAddresses()){
                if (address.getAddress().equals(clientsMap.getClientByName(nick).getAddress())){
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
        if (chat != null && chat.getMessages().size() > 0) {
            this.chatModel.removeAllElements();
            Message previous = null;
            for (Message mess : chat.getMessages()) {
                if (previous == null || mess.getSenderPair() != previous.getSenderPair()) {
                    this.chatModel.addElement(String.format("<html><font color=green size=+1>%s</font></html>", clientsMap.getNick(mess.getSenderPair())));
                }
                this.chatModel.addElement(String.format("<html><span> %s</html>", mess.getMessage()));
                this.chatList.setModel(this.chatModel);
                this.chatList.ensureIndexIsVisible(this.chatModel.size() - 1);

                previous = mess;
            }
        } else {
            //System.out.println("Empty list");
        }
    }
}
