package network;

//A simple class that allows us for advanced address handling

import client.security.CryptoKeyPair;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

public class Address {
    private CryptoKeyPair address;
    private String nickName;

    public Address(String address) {
        try {
            this.address = new CryptoKeyPair(address);
      } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
    }

    public Address(CryptoKeyPair a) {
        address = a;
    }

    public Address(CryptoKeyPair a, String nickName){
        address = a;
        this.nickName = nickName;
    }

    public boolean equals(Object o) {
        return (address.equals(((Address)o).getAddress()));
    }

    public String toString() {
        return address.toString();
    }

    public String getNickName(){
        return this.nickName;
    }

    public CryptoKeyPair getAddress(){
        return this.address;
    }
}
