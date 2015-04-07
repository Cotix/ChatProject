package network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoutingTable {
    private Map<Address, Node> routes;
    private Map<Node, Map<Address, Integer>> neighbourNodes;

    public RoutingTable() {
        routes = new HashMap();
        neighbourNodes = new HashMap<>();
    }



}
