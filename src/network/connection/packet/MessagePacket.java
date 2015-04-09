package network.connection.packet;

import java.nio.ByteBuffer;

public class MessagePacket implements Packet {
    public static final int KEY_SIZE = 256;

    private byte[] data;

    public MessagePacket(byte[] rawData) {
        this.data = rawData;
    }

    public MessagePacket(byte[] message, byte[] timestamp, byte[] recipient) {
        byte[] data = new byte[message.length + timestamp.length + recipient.length];
        System.arraycopy(recipient, 0, data, 0, recipient.length);
        System.arraycopy(timestamp, 0, data, recipient.length, timestamp.length);
        System.arraycopy(message, 0, data, recipient.length + timestamp.length, message.length);

        byte[] header = ByteBuffer.allocate(5)
                .putInt(data.length)
                .put(PacketUtils.PacketType.MESSAGE.value).array();
        this.data = new byte[header.length + data.length];
        System.arraycopy(header, 0, this.data, 0, header.length);
        System.arraycopy(data, 0, this.data, header.length, data.length);
    }

    public byte[] getRecipient() {
        byte[] data = getData();
        byte[] recipient = new byte[KEY_SIZE];
        System.arraycopy(data, 0, recipient, 0, KEY_SIZE);
        return recipient;
    }

    @Override
    public byte[] getRawData() {
        return data;
    }

    @Override
    public int getSize() {
        return data.length;
    }

    @Override
    public byte[] getData() {
        byte[] ret = new byte[data.length - 5];
        System.arraycopy(data, 5, ret, 0, ret.length);
        return ret;
    }

    @Override
    public byte getFirstDataByte() {
        if (data == null || data.length <= 4) {
            return -128;
        }
        return data[4];
    }
}
