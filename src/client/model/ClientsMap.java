package client.model;

import client.security.CryptoKeyPair;
import network.Address;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by destion on 9-4-15.
 */
public class ClientsMap {
    private ArrayList<Address> clients;
    private Address ownAddress;

    private boolean first = true;

    public ClientsMap(Address ownAddress){
        this.clients = new ArrayList<>();
        this.ownAddress = ownAddress;
    }

    public void addClient(Address client){
        this.clients.add(client);
    }

    public String getNick(CryptoKeyPair keyPair){
        for (Address client : clients){
            if (client.getAddress().equals(keyPair)){
                return client.getNickName();
            }
        }
        return null;
    }

    public CryptoKeyPair getKeyPair(String nick){

        if(this.first){
            this.first = false;
            return ownAddress.getAddress();
        } else {

            for (Address client : clients) {
                if (client.getNickName().equals(nick)) {
                    return client.getAddress();
                }
            }
            return null;
        }
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

    public boolean contains(Address address) {
        for (Address client : clients) {
            if (client.equals(address)) {
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

    public CryptoKeyPair getOwnKeyPair(){
        return this.ownAddress.getAddress();
    }

    public Address getOwnAddress(){
        return this.ownAddress;
    }
}
