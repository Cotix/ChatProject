package client.model;

public class Message {

    private long timestamp;
    private String message;
    private String keyFrom;
    private String keyTo;

    public Message(String from, String to, String message, long timestamp){
        this.timestamp = timestamp;
        this.message = message;
        this.keyFrom = from;
        this.keyTo = to;
    }

    public String getMessage(){
        return this.message;
    }

    public String getFrom(){
        return keyFrom;
    }

    public String getTo(){
        return keyTo;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public String toString(){
        //TODO format so node understands
        return String.format("%s %s %s %s \n", timestamp, keyFrom, keyTo, message);
    }
}
