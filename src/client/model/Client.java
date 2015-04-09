package client.model;

import client.security.CryptoKeyPair;

/**
 * Created by destion on 9-4-15.
 */
public class Client {
    private CryptoKeyPair pKey;
    private String nick;

    public Client(CryptoKeyPair pkey, String nick) {
        this.pKey = pkey;
        this.nick = nick;
    }

    public void setpKey(CryptoKeyPair pkey) {
        this.pKey = pkey;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public CryptoKeyPair getpKey() {
        return this.pKey;
    }

    public String getNick(){
        return this.nick;
    }

}
