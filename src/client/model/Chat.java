package client.model;

import settings.Configuration;

import client.security.CryptoKeyPair;
import network.Address;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    private Address address;
    private ArrayList<Message> messages = new ArrayList<>();

    public Chat(Address address) {
        this.address = address;
    }

    public void addMessage(Message message) {
        if (messages.size() != 0) {
            for (int i = 0; i < messages.size(); i++) {
                if (message.getTimestamp() > messages.get(i).getTimestamp()) {
                    messages.add(i, message);
                    break;
                }
            }
        } else {
            messages.add(message);
        }
        if (messages.size() > Configuration.BUFFER_SIZE) {
            messages.remove(Configuration.BUFFER_SIZE);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Address getAddress(){
        return this.address;
    }

    public void removeAll(){
        System.out.println("Chat test");
        messages.removeAll(messages);
    }
}
