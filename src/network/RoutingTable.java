package network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cotix on 4/2/15.
 */
public class RoutingTable {
    private Map<Address, Node> routingMap;
    private Map<Node, Set<Address>> neighbourNodes;

    public RoutingTable() {
        routingMap = new HashMap();
        neighbourNodes = new HashMap<Node, Set<Address>>();
    }



}
