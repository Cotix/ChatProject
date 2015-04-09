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

    public boolean equals(Object o) {
        return o instanceof Address && toString().equals(o.toString());
    }

    public String toString() {
        return address.toString();
    }
}
