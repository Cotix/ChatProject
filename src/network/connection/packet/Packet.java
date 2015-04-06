package network.connection.packet;

/**
 * Created by cotix on 4/6/15.
 */
public interface Packet {
    public byte[] getRawData();
    public int getSize();
    public byte[] getData();
}
