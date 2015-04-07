package network.connection.packet;

import network.Address;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DistancePacket extends StringPacket {

    public DistancePacket(Map<Address, Integer> table) throws UnsupportedEncodingException {
        super(mapToString(table));
    }

    public DistancePacket(byte[] data) {
        super(data);
    }

    public static String mapToString(Map<Address, Integer> map) {
        String result = "";
        for (Address entry : map.keySet()) {
            result += entry + "," + map.get(entry) + "\n";
        }
        return result;
    }
}
