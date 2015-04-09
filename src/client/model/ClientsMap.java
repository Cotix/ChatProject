package client.model;

import java.util.ArrayList;

/**
 * Created by destion on 9-4-15.
 */
public class ClientsMap {
    private ArrayList<Client> clients;
    private String ownNick;

    public ClientsMap(String ownNick){
        this.clients = new ArrayList<>();
        this.ownNick = ownNick;
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
}
