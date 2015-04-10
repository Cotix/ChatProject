package client.model;

import client.security.CryptoKeyPair;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by destion on 7-4-15.
 */
public class Message {

    private long timestamp;
    private String message;
    private CryptoKeyPair keySender;
    private CryptoKeyPair keyReceiver;

    public Message(String message, CryptoKeyPair sender, CryptoKeyPair receiver, long timestamp){
        this.timestamp = timestamp;
        this.message = message;
        this.keySender = sender;
        this.keyReceiver = receiver;
    }

    public String getMessage(){
        return this.message;
    }
    public String getPublicKeySender(){
        return this.keySender.toString();
    }
    public String getPublicKeyReceiver(){
        return this.keyReceiver.toString();
    }
    public long getTimestamp(){
        return this.timestamp;
    }

    /**
     * Signs and encrypts the message and puts it in a stringPacket.
     * @return a stringPacket with the message signed and encrypted.
     */
    public Packet makePacket() {
        byte[] msg;
        try {
            msg = ByteBuffer.allocate(8 + message.length())
                    .putLong(timestamp)
                    .put(message.getBytes("US-ASCII"))
                    .array();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        byte[] sign = keySender.sign(msg);
        byte[] encryptedBlob = new byte[msg.length + sign.length];
        System.arraycopy(sign, 0, encryptedBlob, 0, sign.length);
        System.arraycopy(msg, 0, encryptedBlob, sign.length, msg.length);
        encryptedBlob = keyReceiver.encrypt(encryptedBlob);
        byte[] recvKey = keyReceiver.getRawPublicKey();
        byte[] data = new byte[recvKey.length + encryptedBlob.length];
        // [Destination key] [Encrypted Message]
        System.arraycopy(recvKey, 0, data, 0, recvKey.length);
        System.arraycopy(encryptedBlob, 0, data, recvKey.length, encryptedBlob.length);
        return new StringPacket(data, PacketUtils.PacketType.MESSAGE);
    }

    /**
     * returns a String representation of the Message class.
     */
    public String toString(){
        return String.format("%s %s %s %s \n", timestamp, getPublicKeySender(), getPublicKeyReceiver(), message);
    }
}
