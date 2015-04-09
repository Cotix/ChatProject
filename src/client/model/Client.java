package client.model;

/**
 * Created by destion on 9-4-15.
 */
public class Client {
    private String pKey;
    private String nick;

    public Client(String pkey, String nick) {
        this.pKey = pkey;
        this.nick = nick;
    }

    public void setpKey(String pkey) {
        this.pKey = pkey;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getpKey() {
        return this.pKey;
    }

    public String getNick(){
        return this.nick;
    }

}
