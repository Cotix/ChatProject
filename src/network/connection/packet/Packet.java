package network.connection.packet;

public interface Packet {
    byte[] getRawData();
    int getSize();
    byte[] getData();
    byte getFirstDataByte();
}
