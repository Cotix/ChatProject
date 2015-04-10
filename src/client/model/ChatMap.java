package client.model;

import client.security.CryptoKeyPair;
import network.Address;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by destion on 9-4-15.
 */
public class ChatMap {

    Map<Address, Chat> chatMap;
    ArrayList<Address> addresses;

    public ChatMap(){
        this.chatMap = new HashMap<>();
        this.addresses = new ArrayList<>();
    }

    public void addChat(Chat chat){
        this.chatMap.put(chat.getAddress(), chat);
        this.addresses.add(chat.getAddress());
    }

    public Chat getChatByKey(CryptoKeyPair keyPair){
        for (Address address : chatMap.keySet()){
            if (address.getAddress().equals(keyPair)){
                return chatMap.get(address);
            }
        }
        return null;
    }



    public ArrayList<Address> getAdresses(){
        return this.addresses;
    }
}
