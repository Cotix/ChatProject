package client.model;

import java.util.ArrayList;

/**
 * Created by destion on 9-4-15.
 */
public class ClientsMap {
    private ArrayList<Client> clients;
    private String ownNick;
    private String ownPKey;

    public ClientsMap(String ownNick, String pKey){
        this.clients = new ArrayList<>();
        this.ownNick = ownNick;
        this.ownPKey = pKey;
    }

    public void addClient(Client client){
        this.clients.add(client);
    }

    public String getNick(String key){
        for (Client client : clients){
            if (client.getpKey().equals(key)){
                return client.getNick();
            }
        }
        return null;
    }

    public String getpKey(String nick){
        for (Client client : clients){
            if (client.getNick().equals(nick)){
                return client.getpKey();
            }
        }
        return null;
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();

        for (Client client : clients){
            if (!client.getNick().equals(ownNick)){
                names.add(client.getNick());
            }
        }
        return names;
    }

    public boolean contains(String nick){
        for (Client client : clients){
            if (client.getNick().equals(nick)){

                return true;
            }
        }
        return false;
    }

    public ArrayList<Client> getMap(){
        return this.clients;
    }

    public Client getClientByName(String name){
        for (Client client : clients){
            if (client.getNick().equals(name)){
                return client;
            }
        }
        return null;
    }

    public String getOwnPKey(){
        return this.ownPKey;
    }
}
