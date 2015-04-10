package network.connection;

import network.connection.packet.Packet;

public interface Connection {
    void connect();
    void connect(String ipAddress, short port);
    void disconnect();
    void sendPacket(Packet pckt);
    void handleConnection();
    boolean isConnected();
    Packet readPacket();
    String getConnectionInfo();
}
