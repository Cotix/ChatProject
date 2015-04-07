package network.connection.packet;

public interface Packet {
    public byte[] getRawData();
    public int getSize();
    public byte[] getData();
    public byte getFirstDataByte();
}
