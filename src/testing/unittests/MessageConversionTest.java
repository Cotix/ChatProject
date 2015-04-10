package testing.unittests;

import client.model.Message;
import client.security.CryptoKeyPair;
import network.connection.packet.Packet;

/**
 * Created by cotix on 4/10/15.
 */
public class MessageConversionTest implements UnitTest {
    @Override
    public String test() {
        String plainText = "Een test bericht";
        CryptoKeyPair keyA = new CryptoKeyPair();
        CryptoKeyPair keyB = new CryptoKeyPair();
        long currentTime = System.currentTimeMillis();
        Message msg = new Message("Een test bericht", keyA, keyB, currentTime);
        Packet p = msg.makePacket();
        Message msg2 = Message.makeMessage(p, keyB);
        if (!msg.getMessage().equals(msg2.getMessage())) {
            return "Text not the same in Messages";
        }
        if (!msg.getPublicKeyReceiver().equals(msg2.getPublicKeyReceiver())) {
            return "Message public key receiver not the same!";
        }
        if (!msg.getPublicKeySender().equals(msg2.getPublicKeySender())) {
            return "Message public key sender not the same!";
        }
        if (msg.getTimestamp() != msg2.getTimestamp()) {
            return "Timestamps in messages not the same!";
        }

        return null;
    }
}
