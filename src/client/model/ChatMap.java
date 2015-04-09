package client.model;

import client.security.CryptoKeyPair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by destion on 9-4-15.
 */
public class ChatMap {

    Map<CryptoKeyPair, Chat> chatMap;
    ArrayList<CryptoKeyPair> keys;

    public ChatMap(){
        this.chatMap = new HashMap<>();
        this.keys = new ArrayList<>();
    }

    public void addChat(Chat chat){
        chatMap.put(chat.getPublicKey(), chat);
        keys.add(chat.getPublicKey());
    }

    public Chat getChatByKey(CryptoKeyPair pKey){
        for (CryptoKeyPair key : chatMap.keySet()){
            if (key.equals(pKey)){
                return chatMap.get(key);
            }
        }
        return null;
    }



    public ArrayList<CryptoKeyPair> getKeys(){
        return this.keys;
    }
}
