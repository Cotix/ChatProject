package network.connection;

/**
 * Created by cotix on 4/2/15.
 */
public interface Connection {
    public void connect();
    public void disconnect();
    public void sendPacket(Packet pckt);
    public void handleConnection();
    public boolean isConnected();
    public Packet readPacket();
    public String getConnectionInfo();
}
