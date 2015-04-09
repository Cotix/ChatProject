package node;

import network.Address;
import network.connection.packet.DistancePacket;

import java.util.HashMap;
import java.util.Map;

public class DistanceTable {
    private Map<Address, Integer> table;

    public DistanceTable() {
        table = new HashMap<Address, Integer>();
    }

    public Map<Address, Integer> getTable() {
        return table;
    }

    public boolean equalMap(Map<Address, Integer> target) {
        if (table.keySet().size() != target.keySet().size()) return false;
        for (Address entry : table.keySet()) {
            if (!target.containsKey(entry)) return false;
            if (Math.abs(table.get(entry) - target.get(entry)) > 50) return false;
        }
        return true;
    }

    public boolean update(DistancePacket packet) {
        Map<Address, Integer> old = this.getTable();
        this.table = stringToMap(packet.getDataAsString());
        return !equalMap(old);
    }

    public static Map<Address, Integer> stringToMap(String packet) {
        Map<Address, Integer> result = new HashMap<Address, Integer>();
        for (String line : packet.split("\n")) {
            String[] parts = line.split(",");
            result.put(new Address(parts[0]), Integer.parseInt(parts[1]));
        }
        return result;
    }
}
