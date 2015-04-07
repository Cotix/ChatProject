package network.connection.packet;

public class PacketUtils {
    public static enum PacketType {
        UNKNOWN((byte)-128), DISTANCE((byte) 1);
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
