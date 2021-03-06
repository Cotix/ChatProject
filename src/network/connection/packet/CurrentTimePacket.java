package network.connection.packet;

/**
 * A packet used for pinging other nodes.
 * This packet also includes the time at which the packet was constructed.
 * This enables us to read the timedifference between sending and receiving without keeping
 * track of any information about who we are pinging.
 */

public class CurrentTimePacket implements Packet {
    private byte[] data;

    public CurrentTimePacket() {
        byte[] pData = new byte[8];
        long v = System.currentTimeMillis();
        //Convert the long to a byte array
        pData[0] = (byte)(v >>> 56);
        pData[1] = (byte)(v >>> 48);
        pData[2] = (byte)(v >>> 40);
        pData[3] = (byte)(v >>> 32);
        pData[4] = (byte)(v >>> 24);
        pData[5] = (byte)(v >>> 16);
        pData[6] = (byte)(v >>>  8);
        pData[7] = (byte)(v >>>  0);
        //Put the timestamp in a packet, which means it had to be prefixed with a length and a type
        data = new byte[pData.length + 5];
        data[0] = (byte)((((pData.length+1) & 0xFF000000) >> 24) & 0xFF);
        data[1] = (byte)((((pData.length+1) & 0xFF0000) >> 16) & 0xFF);
        data[2] = (byte)((((pData.length+1) & 0xFF00) >> 8) & 0xFF);
        data[3] = (byte)  ((pData.length+1) & 0xFF);
        data[4] = PacketUtils.PacketType.PING.value;
        System.arraycopy(pData, 0, data, 5, pData.length);
    }

    /**
     * Sets the type of the packet you are going to send
     * @param t the type from the PacketUtils class.
     */
    public void setType(PacketUtils.PacketType t) {
        data[4] = t.value;
    }

    public CurrentTimePacket(byte[] rawData) {
        data = rawData;
    }

    public int getTimeDifference() {
        long time = 0;
        byte[] pData = getData();
        time += ((long)(pData[0]&0xFF)) << 56;
        time += ((long)(pData[1]&0xFF)) << 48;
        time += ((long)(pData[2]&0xFF)) << 40;
        time += ((long)(pData[3]&0xFF)) << 32;
        time += ((long)(pData[4]&0xFF)) << 24;
        time += ((long)(pData[5]&0xFF)) << 16;
        time += ((long)(pData[6]&0xFF)) << 8;
        time += ((long)(pData[7]&0xFF));

        return (int) (System.currentTimeMillis() - time);
    }

    /**
     * Returns the data which is inside the packet
     * @return
     */
    public byte[] getData() {
        byte[] ret = new byte[data.length - 5];
        System.arraycopy(data, 5, ret, 0, ret.length);
        return ret;
    }


    /**
     * Returns the raw data inside the packet
     * @return
     */
    public byte[] getRawData() {
        return data;
    }

    /**
     * Returns the size of the packet
     * @return
     */
    public int getSize() {
        return data.length;
    }

    /**
     * Return only the first DataByte of the packet
     * @return
     */
    public byte getFirstDataByte() {
        if (data.length <= 4) {
            return -128;
        }
        return data[4];
    }
}
