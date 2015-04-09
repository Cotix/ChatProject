package client.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static final int BUFFER_SIZE = 100;

    private String publicKey;
    private List<Message> messages = new ArrayList<>();

    public Chat(String publicKey) {
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

    public String getPublicKey() {
        return publicKey;
    }
}
