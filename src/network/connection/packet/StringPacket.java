package network.connection.packet;

import java.io.UnsupportedEncodingException;

public class StringPacket implements Packet{
    private byte[] data;

    public StringPacket(byte[] pData) {
        byte[] data = new byte[pData.length + 4];
        data[0] = (byte)(((pData.length & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)(((pData.length & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)(((pData.length & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  (pData.length & 0xFF);
        System.arraycopy(pData, 0, data, 4, pData.length);
    }

    public StringPacket(String pData) throws UnsupportedEncodingException {
        this(pData.getBytes("US-ASCII"));
    }

    public byte[] getData() {
        byte[] ret = new byte[data.length - 4];
        System.arraycopy(data, 4, ret, 0, ret.length);
        return ret;
    }

    public byte[] getRawData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    public byte getFirstDataByte() {
        if (data.length <= 4) {
            return -128;
        }
        return data[4];
    }

}
