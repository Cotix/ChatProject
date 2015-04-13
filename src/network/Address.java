package network;
/**
 * A simple class that allows us for advanced address handling
 */
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

    /**
     * Sets the nickname of the current address.
     * @param nick The desired nickname
     */
    public void setNickName(String nick) {
        nickName = nick;
    }

    /**
     * Checks if the current instance of address is the same as the given one
     * @param o The object to be compared with
     * @return True if equal
     */
    public boolean equals(Object o) {
        return (address.equals(((Address)o).getAddress()));
    }

    /**
     * Returns the hashcode of the address, to be able to use a displayable number for the long keys
     * @return
     */
    public int hashCode() {
        return address.hashCode();
    }

    /**
     * Returns the string representation of an adress
     * @return
     */
    public String toString() {
        return String.valueOf(address.hashCode());
    }

    /**
     * Returns the nickname of the instance of address
     * @return
     */
    public String getNickName(){
        return this.nickName;
    }

    /**
     * Returns the cryptokeypair of the address
     * @return
     */
    public CryptoKeyPair getAddress(){
        return this.address;
    }
}
