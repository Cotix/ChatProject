package client.model;

/**
 * Created by destion on 7-4-15.
 */
public class Message {

    private long timestamp;
    private String message;
    private String publicKeySender;
    private String publicKeyReceiver;


    //TODO public key

    public Message(String message, String sender, String receiver){
        this.timestamp = System.currentTimeMillis() / 1000L;

        this.message = message;

        this.publicKeySender = sender;
        this.publicKeyReceiver = receiver;
    }

    public String getMessage(){
        return this.message;
    }
    public String getPublicKeySender(){
        return this.publicKeySender;
    }
    public String getPublicKeyReceiver(){
        return this.publicKeyReceiver;
    }
    public long getTimestamp(){
        return this.timestamp;
    }

    public String toString(){
        //TODO format so node understands
        return String.format("%s %s %s %s \n", timestamp, publicKeySender, publicKeyReceiver, message);
    }
}
