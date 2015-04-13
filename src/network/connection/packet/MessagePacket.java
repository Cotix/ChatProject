package network.connection.packet;

import client.security.CryptoKeyPair;
import network.Address;
import settings.Configuration;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A packet containing an actual message with the text entered in the chat
 */

public class MessagePacket implements Packet {

    private byte[] data;

    public MessagePacket(byte[] rawData) {
        this.data = rawData;
    }

    /**
     * Returns the recipient of the message
     * @return
     */
    public Address getRecipient() {
        byte[] data = getData();
        byte[] recipient = new byte[Configuration.ENCODED_KEY_LENGTH];
        System.arraycopy(data, 0, recipient, 0, Configuration.ENCODED_KEY_LENGTH);
        return new Address(new CryptoKeyPair(recipient));
    }

    /**
     * Returns the raw data of the packet
     * @return
     */
    @Override
    public byte[] getRawData() {
        return data;
    }

    /**
     * Returns the size of the packet
     * @return
     */
    @Override
    public int getSize() {
        return data.length;
    }

    /**
     * Returns the data of the packet
     * @return
     */
    @Override
    public byte[] getData() {
        byte[] ret = new byte[data.length - 5];
        System.arraycopy(data, 5, ret, 0, ret.length);
        return ret;
    }

    /**
     * Returns the first DataByte of the packet
     * @return
     */
    @Override
    public byte getFirstDataByte() {
        if (data == null || data.length <= 4) {
            return -128;
        }
        return data[4];
    }
}
