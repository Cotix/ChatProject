package network.connection.packet;
/**
 * All packet types are listed here
 * The static getPacketType function can be used to read the packet type of a packet
 */
public class PacketUtils {
    public enum PacketType {
        UNKNOWN((byte)-128), DISTANCE((byte) 1), PING((byte) 2), PONG((byte) 3), ACK((byte)4),
        IDENTIFY((byte) 64), MESSAGE((byte) 65);
        byte value;
        PacketType(byte v) {
            value = v;
        }
        public byte getValue() {
            return value;
        }
    }

    public static PacketType getPacketType(Packet p) {
        byte id = p.getFirstDataByte();
        for (PacketType type : PacketType.values()) {
            if (id == type.getValue()) {
                return type;
            }
        }
        return PacketType.UNKNOWN;
    }
}
