package client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by destion on 9-4-15.
 */
public class KeyNickMap {
    private Map<String, String> identityMap;

    public KeyNickMap(){
        this.identityMap = new HashMap<>();
    }

    public void addNick(String key, String nick){
        this.identityMap.put(key, nick);
    }

    public String getNick(String key){
        return this.identityMap.get(key);
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();

        for (String nick : identityMap.values()){
            names.add(nick);
        }

        return names;
    }
}
