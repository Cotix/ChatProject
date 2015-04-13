package network.connection.packet;

import java.nio.ByteBuffer;

/**
 * Created by cotix on 4/13/15.
 */
public class AckPacket extends StringPacket {
    public AckPacket(int seq) {
        super(ByteBuffer.allocate(4).putInt(seq).array(), PacketUtils.PacketType.ACK);
    }
}
