package network.connection.packet;

import java.io.UnsupportedEncodingException;

/**
 * Created by cotix on 4/7/15.
 */
public class CurrentTimePacket implements Packet {
    private byte[] data;

    public CurrentTimePacket() {
        byte[] pData = new byte[8];
        long time = System.currentTimeMillis();
        for (int i = 0; i != 8; ++i) {
            pData[7-i] = (byte)(time&0xFF);
            time >>= 8;
        }
        data = new byte[pData.length + 5];
        data[0] = (byte)((((pData.length+1) & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)((((pData.length+1) & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)((((pData.length+1) & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  ((pData.length+1) & 0xFF);
        data[4] = PacketUtils.PacketType.PING.value;
        System.arraycopy(pData, 0, data, 5, pData.length);
    }

    public void setType(PacketUtils.PacketType t) {
        data[4] = t.value;
    }

    public CurrentTimePacket(byte[] rawData) {
        data = rawData;
    }

    public int getTimeDifference() {
        long time = 0;
        byte[] pData = getData();
        for (int i = 0; i != 8; ++i) {
            time = (byte)(pData[i]&0xFF);
            time <<= 8;
        }
        return (int) (System.currentTimeMillis() - time);
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
