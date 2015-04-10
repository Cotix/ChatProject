package network.connection.packet;

import client.security.CryptoKeyPair;
import network.Address;
import settings.Configuration;

import java.nio.ByteBuffer;

public class MessagePacket implements Packet {

    private byte[] data;

    public MessagePacket(byte[] rawData) {
        this.data = rawData;
    }


    public Address getRecipient() {
        byte[] data = getData();
        byte[] recipient = new byte[Configuration.KEY_LENGTH/8];
        System.arraycopy(data, 5, recipient, 0, Configuration.KEY_LENGTH/8);
        return new Address(new CryptoKeyPair(recipient));
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
