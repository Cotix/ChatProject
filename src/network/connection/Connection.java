package network.connection;

import network.connection.packet.Packet;

public interface Connection {
    public void connect();
    public void connect(String ipAddress, short port);
    public void disconnect();
    public void sendPacket(Packet pckt);
    public void handleConnection();
    public boolean isConnected();
    public Packet readPacket();
    public String getConnectionInfo();
}
