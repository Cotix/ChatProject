package client.model;

import network.Address;
import settings.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat model which holds all messages with a single other client
 */
public class Chat {

    private Address address;
    private ArrayList<Message> messages = new ArrayList<>();

    /**
     * Constructs a Chat for a client
     * @param address address of the receiver
     */
    public Chat(Address address) {
        this.address = address;
    }

    /**
     * Adds a message to the list, either sent or received messages.
     * @param message message to add
     */
    public void addMessage(Message message) {
        messages.add(message);

        // TODO: This did not work properly, needs fixing for correct ordering.
        /*if (messages.size() != 0) {
            for (int i = 0; i < messages.size(); i++) {
                if (message.getTimestamp() > messages.get(i).getTimestamp()) {
                    messages.add(i, message);
                    break;
                }
            }
        } else {
            messages.add(message);
        }*/
        if (messages.size() > Configuration.BUFFER_SIZE) {
            messages.remove(0);
        }
    }

    /**
     * Returns the list of Messages.
     * @return
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Returns the Address of the receiver.
     * @return
     */
    public Address getAddress(){
        return this.address;
    }

    /**
     * Removes all Messages from the Chat.
     */
    public void removeAll(){
        messages.clear();
    }
}
