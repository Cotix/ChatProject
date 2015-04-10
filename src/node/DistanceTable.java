package node;

import network.Address;
import network.connection.packet.DistancePacket;

import java.util.HashMap;
import java.util.Map;
/**
 * Distance table is a class that maintains the distances to addresses
 * It can maintain distances over several hops, by adding the distance of the hops to the distance
 * of the final hop to the destination address.
 * DistanceTables are used to construct the RoutingTable.
 * Our distance table is broadcast to all neighbour nodes when it has a significant change
 * A significant change is defined as a change that adds or removes an entry
 * Changes in delay are not important enough for an update.
 */
public class DistanceTable {
    private Map<Address, Integer> table;

    public DistanceTable() {
        table = new HashMap<Address, Integer>();
    }

    public Map<Address, Integer> getTable() {
        return table;
    }

    /**
     * Compares this table with a map, and returns if there are any
     * significant differences. Read the comments above where it explains what
     * a significant difference is.
     */
    public boolean equalMap(Map<Address, Integer> target) {
        if (table.keySet().size() != target.keySet().size()) return false;
        for (Address entry : table.keySet()) {
            if (!target.containsKey(entry)) return false;
            if (Math.abs(table.get(entry) - target.get(entry)) > 50) return false;
        }
        return true;
    }

    /**
     * Updates the distance table. Returns whether or not it made a significant difference.
     */
    public boolean update(DistancePacket packet) {
        Map<Address, Integer> old = this.getTable();
        this.table = stringToMap(packet.getDataAsString());
        return !equalMap(old);
    }

    public static Map<Address, Integer> stringToMap(String packet) {
        Map<Address, Integer> result = new HashMap<Address, Integer>();
        for (String line : packet.split("\n")) {
            if (line.length() == 0) continue;
            String[] parts = line.split(",");
            result.put(new Address(parts[0]), Integer.parseInt(parts[1]));
        }
        return result;
    }
}
