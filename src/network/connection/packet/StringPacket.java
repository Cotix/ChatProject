package network.connection.packet;

import java.io.UnsupportedEncodingException;

//A very allround packet. Constructs a regular packet from input data
//Can be either a string or a byte array.

public class StringPacket implements Packet{
    private byte[] data;

    public StringPacket(byte[] pData, PacketUtils.PacketType type) {
        data = new byte[pData.length + 5];
        data[0] = (byte)(((pData.length & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)(((pData.length & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)(((pData.length & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  (pData.length & 0xFF);
        data[4] = type.getValue();
        System.arraycopy(pData, 0, data, 5, pData.length);
    }

    public StringPacket(byte[] pData) {
        data = new byte[pData.length + 4];
        data[0] = (byte)(((pData.length & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)(((pData.length & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)(((pData.length & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  (pData.length & 0xFF);
        System.arraycopy(pData, 0, data, 4, pData.length);
    }

    public StringPacket(String pData, PacketUtils.PacketType type) throws UnsupportedEncodingException {
        this(pData.getBytes("US-ASCII"), type);
    }

    public byte[] getData() {
        byte[] ret = new byte[data.length - 5];
        System.arraycopy(data, 5, ret, 0, ret.length);
        return ret;
    }

    public byte[] getRawData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    public byte getFirstDataByte() {
        if (data == null || data.length <= 4) {
            return -128;
        }
        return data[4];
    }

    public String getDataAsString() {
        try {
            return new String(getData(), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // TODO: better catching?
        }
        return "";
    }

}
