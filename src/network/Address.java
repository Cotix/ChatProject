package network;
/**
 * A simple class that allows us for advanced address handling
 */
import client.controller.ClientController;
import client.security.CryptoKeyPair;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import log.Log;
import log.LogLevel;

public class Address {
    private CryptoKeyPair address;
    private String nickName;

    boolean hasNick;

    public Address(String address) {
        hasNick = false;
        try {
            this.address = new CryptoKeyPair(address);
        } catch (Base64DecodingException e) {
            Log.log("Error while decoding address " + address + ": " + e.getMessage(), LogLevel.ERROR);
        }
    }

    public Address(CryptoKeyPair a) {
        hasNick = false;
        address = a;
    }

    public Address(CryptoKeyPair a, String nickName){
        hasNick = false;
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
     * Returns the string representation of an adress, the nick if available, otherwise the hashcode
     * @return
     */
    public String toString() {
        if(!hasNick) {
            String result = this.getNickName();
            int unread;
            if ((unread = ClientController.getInstance().getChats().getChatByKey(address).getUnread()) > 0) result += unread + " new";
            return result;
        } else {
            return this.getNickName();
        }
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

    /**
     * Sets the address to know it has a nickname
     */
    public void setHasNick(){
        this.hasNick = true;
    }
}
