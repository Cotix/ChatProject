package client.controller;

import client.model.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martijn on 8-4-15.
 */
public class ClientController {

    private List<Chat> chats;

    public ClientController() {
        chats  = new ArrayList<>();
    }

    public void createChat(String publicKey) {
        Chat chat = new Chat(publicKey);
        chats.add(chat);
    }

    public List<Chat> getChats() {
        return chats;
    }

}
