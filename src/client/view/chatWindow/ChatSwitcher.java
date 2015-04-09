package client.view.chatWindow;

import client.model.Chat;
import client.model.ChatMap;
import javax.swing.*;

public class ChatSwitcher {

    private ChatMap chats;
    private JList<String> chatList;

    public ChatSwitcher(ChatMap chats, JList chatList){
        this.chats = chats;
        this.chatList = chatList;
    }

    public Chat switchChat(String nick){

        for (String key : chats.getKeys()){
            if (key.equals(nick)){
                System.out.println("Test");
                return chats.getChatByName(key);
            }
        }
        return null;
    }

    public void addChat(Chat chat){
        this.chats.addChat(chat);
    }
}
