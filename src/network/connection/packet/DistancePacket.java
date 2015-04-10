package network.connection.packet;

import network.Address;

import java.io.UnsupportedEncodingException;
import java.util.Map;

//Distance packet is used for routing
//The packet contains the distances to address from the sender node
//Upon receiving this packet, the receiver can calculate his distances
//to the addresses via this node by adding his distance to sending node.
public class DistancePacket extends StringPacket {

    public DistancePacket(Map<Address, Integer> table) throws UnsupportedEncodingException {
        super(mapToString(table), PacketUtils.PacketType.DISTANCE);
    }

    public DistancePacket(byte[] data) {
        super(data, PacketUtils.PacketType.DISTANCE);
    }

    public static String mapToString(Map<Address, Integer> map) {
        String result = "";
        for (Address entry : map.keySet()) {
            result += entry.getAddress().toString() + "," + map.get(entry) + "\n";
        }
        return result;
    }
}
