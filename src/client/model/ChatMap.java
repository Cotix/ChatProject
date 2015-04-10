package client.model;


import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by destion on 9-4-15.
 */
public class ChatMap {

    Map<PublicKey, Chat> chatMap;
    ArrayList<PublicKey> keys;

    public ChatMap(){
        this.chatMap = new HashMap<>();
        this.keys = new ArrayList<>();
    }

    public void addChat(Chat chat){
        chatMap.put(chat.getPublicKey(), chat);
        keys.add(chat.getPublicKey());
    }

    public Chat getChatByKey(PublicKey pKey){
        for (PublicKey key : chatMap.keySet()){
            if (key.equals(pKey)){
                return chatMap.get(key);
            }
        }
        return null;
    }



    public ArrayList<PublicKey> getKeys(){
        return this.keys;
    }
}
