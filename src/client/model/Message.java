package client.model;

import client.security.CryptoKeyPair;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;
import settings.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Message that holds a receiver, origin, message and timestamp.
 */
public class Message {

    private long timestamp;
    private String message;
    private CryptoKeyPair keySender;
    private CryptoKeyPair keyReceiver;

    /**
     * Constructs a Message.
     *
     * @param message   message text
     * @param sender    origin
     * @param receiver  receiver
     * @param timestamp timestamp of button press
     */
    public Message(String message, CryptoKeyPair sender, CryptoKeyPair receiver, long timestamp) {
        this.timestamp = timestamp;
        this.message = message;
        this.keySender = sender;
        this.keyReceiver = receiver;
    }
    
    /**
     * Constructs a Message from a Packet.
     *
     * @param p packet
     */
    public static Message makeMessage(Packet p, CryptoKeyPair myKey) {
        byte[] data = p.getData();        byte[] recv = new byte[myKey.getRawPublicKey().length];
        System.arraycopy(data, 0, recv, 0, recv.length);
        byte[] encryptedBlob = new byte[data.length - recv.length];
        System.arraycopy(data, recv.length, encryptedBlob, 0, encryptedBlob.length);
        encryptedBlob = myKey.decrypt(encryptedBlob);
        if (encryptedBlob == null) {
            return null;
        }
        byte[] sign = new byte[Configuration.SIGN_LENGTH];
        byte[] text = new byte[encryptedBlob.length - sign.length];
        System.arraycopy(encryptedBlob, 0, sign, 0, sign.length);
        System.arraycopy(encryptedBlob, sign.length, text, 0, text.length);
        byte[] timeStamp = new byte[8];
        byte[] msg = new byte[text.length - 8 - recv.length];
        byte[] sendKey = new byte[recv.length];
        System.arraycopy(text, 0, timeStamp, 0, 8);
        System.arraycopy(text, 8, msg, 0, msg.length);
        System.arraycopy(text, 8 + msg.length, sendKey, 0, sendKey.length);

        String message = "Error";
        try {
            message = new String(msg, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CryptoKeyPair send = new CryptoKeyPair(sendKey);
        long time = 0;
        time += ((long)(timeStamp[0]&0xFF)) << 56;
        time += ((long)(timeStamp[1]&0xFF)) << 48;
        time += ((long)(timeStamp[2]&0xFF)) << 40;
        time += ((long)(timeStamp[3]&0xFF)) << 32;
        time += ((long)(timeStamp[4]&0xFF)) << 24;
        time += ((long)(timeStamp[5]&0xFF)) << 16;
        time += ((long)(timeStamp[6]&0xFF)) << 8;
        time += ((long)(timeStamp[7]&0xFF));

        return new Message(message, send, myKey, time);
    }

    /**
     * Returns the message text.
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the public key of the sender.
     *
     * @return
     */
    public String getPublicKeySender() {
        return this.keySender.toString();
    }

    /**
     * Returns the public key of the receiver.
     *
     * @return
     */
    public String getPublicKeyReceiver() {
        return this.keyReceiver.toString();
    }

    /**
     * Returns the CryptoKeyPair of the sender.
     *
     * @return
     */
    public CryptoKeyPair getSenderPair() {
        return this.keySender;
    }

    /**
     * Returns the CryptoKeyPair of the receiver.
     *
     * @return
     */
    public CryptoKeyPair getReceiverPair() {
        return this.keyReceiver;
    }

    /**
     * Returns the timestamp.
     *
     * @return
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    
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
        byte[] encryptedBlob = new byte[msg.length + sign.length + Configuration.KEY_LENGTH];
        System.arraycopy(sign, 0, encryptedBlob, 0, sign.length);
        System.arraycopy(msg, 0, encryptedBlob, sign.length, msg.length);
        byte[] keySenderBytes = keySender.getRawPublicKey();
        System.arraycopy(keySenderBytes, 0, encryptedBlob, msg.length + sign.length, keySenderBytes.length);
        encryptedBlob = keyReceiver.encrypt(encryptedBlob);
        byte[] recvKey = keyReceiver.getRawPublicKey();
        byte[] data = new byte[recvKey.length + encryptedBlob.length];
        // [Destination key] [Encrypted Message]
        System.arraycopy(recvKey, 0, data, 0, recvKey.length);
        System.arraycopy(encryptedBlob, 0, data, recvKey.length, encryptedBlob.length);
        return new StringPacket(data, PacketUtils.PacketType.MESSAGE);
    }

    /**
     * Returns a String representation of the Message.
     *
     * @return
     */
    public String toString() {
        return String.format("%s %s %s %s \n", timestamp, getPublicKeySender(), getPublicKeyReceiver(), message);
    }
}
