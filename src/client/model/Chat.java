package client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martijn on 7-4-15.
 */
public class Chat {

    private String publicKey;
    private List<Message> messages = new ArrayList<>();

    public Chat(String publicKey) {
        this.publicKey = publicKey;
    }

    public void addMessage(Message msg) {
        int i = 0;
        for (Message msgs : messages) {
            if (msgs.getTimestamp() < msg.getTimestamp()) {
                messages.add(i, msg);
                break;
            }
            i++;
        }
        if (messages.size() > 100) {
            messages.remove(100);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
