package client.model;

/**
 * Created by destion on 7-4-15.
 */
public class Message {

    private long timestamp;
    private String message;
    private String publicKey;


    //TODO public key

    public Message(String message, String publicKey){
        this.timestamp = System.currentTimeMillis() / 1000L;

        this.message = message;

        this.publicKey = publicKey;
    }

    public String getMessage(){
        return this.message;
    }
    public String getPublicKey(){
        return this.publicKey;
    }
    public long getTimestamp(){
        return this.timestamp;
    }

    public String toString(){
        //TODO format so node understands
        return String.format("%s %s %s \n", timestamp, publicKey, message);
    }
}
