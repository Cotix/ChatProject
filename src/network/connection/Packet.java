package network.connection;

import java.io.UnsupportedEncodingException;

/**
 * Created by cotix on 4/2/15.
 */
public class Packet {
    private byte[] data;

    public Packet(byte[] pData) {
        byte[] data = new byte[pData.length + 4];
        data[0] = (byte)(((pData.length & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)(((pData.length & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)(((pData.length & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  (pData.length & 0xFF);
        System.arraycopy(pData, 0, data, 4, pData.length);
    }

    public Packet(String pData) throws UnsupportedEncodingException {
        this(pData.getBytes("US-ASCII"));
    }

    public byte[] getRawData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

}
