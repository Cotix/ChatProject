package client.model;

import client.security.CryptoKeyPair;
import network.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps all chats to the corresponding client address.
 */
public class ChatMap {

    Map<Address, Chat> chatMap;
    ArrayList<Address> addresses;

    /**
     * Constructs a ChatMap.
     */
    public ChatMap() {
        this.chatMap = new HashMap<>();
        this.addresses = new ArrayList<>();
    }

    /**
     * Adds a Chat.
     * @param chat Chat model
     */
    public void addChat(Chat chat) {
        this.chatMap.put(chat.getAddress(), chat);
        this.addresses.add(chat.getAddress());
    }

    /**
     * Returns a Chat whose receiver hold given keypair.
     * @param keyPair keypair of the receiver
     * @return
     */
    public Chat getChatByKey(CryptoKeyPair keyPair){
        for (Address address : chatMap.keySet()){
            if (address.getAddress().equals(keyPair)){
                return chatMap.get(address);
            }
        }
        return null;
    }

    /**
     * Returns a list of addresses.
     * @return
     */
    public ArrayList<Address> getAdresses() {
        return this.addresses;
    }
}
