package client.model;

import client.security.CryptoKeyPair;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static final int BUFFER_SIZE = 100;

    private CryptoKeyPair publicKey;
    private ArrayList<Message> messages = new ArrayList<>();

    public Chat(CryptoKeyPair publicKey) {
        this.publicKey = publicKey;
    }

    public void addMessage(Message message) {

        this.messages.add(message);

        /*
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
        if (messages.size() > BUFFER_SIZE) {
            messages.remove(BUFFER_SIZE);
        }*/
    }

    public List<Message> getMessages() {
        return messages;
    }

    public CryptoKeyPair getPublicKey() {
        return publicKey;
    }

    public void removeAll(){
        System.out.println("Chat test");
        messages.removeAll(messages);
    }
}
