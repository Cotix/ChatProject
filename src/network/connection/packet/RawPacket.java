package network.connection.packet;

import java.io.UnsupportedEncodingException;

/**
 * Created by cotix on 4/2/15.
 */
public class RawPacket implements Packet{
    private byte[] data;

    public RawPacket(byte[] pData) throws InvalidPacketException {
        byte[] data = pData;
        if (getSize() + 4 != pData.length) {
            throw new InvalidPacketException();
        }
    }
    public byte[] getRawData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    public byte[] getData() {
        byte[] ret = new byte[data.length - 4];
        System.arraycopy(data, 4, ret, 0, ret.length);
        return ret;
    }

    public byte getFirstDataByte() {
        if (data.length <= 4) {
            return -128;
        }
        return data[4];
    }

}
