package client.model;

import network.Address;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by destion on 9-4-15.
 */
public class ClientsMap {
    private ArrayList<Address> clients;
    private Address ownAddress;

    public ClientsMap(Address ownAddress){
        this.clients = new ArrayList<>();
        this.ownAddress = ownAddress;
    }

    public void addClient(Address client){
        this.clients.add(client);
    }

    public String getNick(String key){
        for (Address client : clients){
            if (client.getAddress().getPublicKey().equals(key)){
                return client.getNickName();
            }
        }
        return null;
    }

    public PublicKey getpKey(String nick){
        for (Address client : clients){
            if (client.getNickName().equals(nick)){
                return client.getAddress().getPublicKey();
            }
        }
        return null;
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();

        for (Address client : clients){
            if (!client.getNickName().equals(ownAddress.getNickName())){
                names.add(client.getNickName());
            }
        }
        return names;
    }

    public boolean contains(String nick){
        for (Address client : clients){
            if (client.getNickName().equals(nick)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Address> getMap(){
        return this.clients;
    }

    public Address getClientByName(String name){
        for (Address client : clients){
            if (client.getNickName().equals(name)){
                return client;
            }
        }
        return null;
    }

    public PublicKey getOwnPKey(){
        return this.ownAddress.getAddress().getPublicKey();
    }

    public Address getOwnAddress(){
        return this.ownAddress;
    }
}
