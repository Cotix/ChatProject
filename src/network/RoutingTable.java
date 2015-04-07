package network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoutingTable {
    private Map<Address, Node> routingMap;
    private Map<Node, Set<Address>> neighbourNodes;

    public RoutingTable() {
        routingMap = new HashMap();
        neighbourNodes = new HashMap<Node, Set<Address>>();
    }



}
