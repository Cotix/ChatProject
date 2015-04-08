package node;

import network.Address;
import network.connection.packet.DistancePacket;
import network.connection.packet.Packet;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RoutingTable {
    private Map<Address, Node> routes;
    private Map<Node, DistanceTable> neighbourNodes;
    private Map<Address, Integer> lowestDelays;

    public RoutingTable() {
        routes = new HashMap();
        lowestDelays = null;
        neighbourNodes = new HashMap<>();
    }

    public DistancePacket getMyDistanceTable() {
        if (lowestDelays == null) {
            update();
        }
        try {
            return new DistancePacket(lowestDelays);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Node findBestNode(Address address) {
        int best = Integer.MAX_VALUE;
        Node bestNode = null;
        for (Node n : neighbourNodes.keySet()) {
            if (!neighbourNodes.get(n).getTable().containsKey(address)) {
                continue;
            }
            int latency = neighbourNodes.get(n).getTable().get(address);
            if (latency < best) {
                best = latency;
                bestNode = n;
            }
        }
        return bestNode;
    }

    public Node getNode(Address address) {
        if (routes.containsKey(address)) {
            Node n = routes.get(address);
            if (neighbourNodes.containsKey(n) && neighbourNodes.get(n).getTable().containsKey(address)) {
                return n;
            }
        }
        return findBestNode(address);
    }

    public Node getAlternativeNode(Address address, Node exclude) {
        int best = Integer.MAX_VALUE;
        Node bestNode = null;
        for (Node n : neighbourNodes.keySet()) {
            if (!neighbourNodes.get(n).getTable().containsKey(address) || n == exclude) {
                continue;
            }
            int latency = neighbourNodes.get(n).getTable().get(address);
            if (latency < best) {
                best = latency;
                bestNode = n;
            }
        }
        return bestNode;
    }

    public boolean update() {
        Map<Address, Node> old = routes;
        lowestDelays = new HashMap<>();
        routes = new HashMap<>();
        for (Node n : neighbourNodes.keySet()) {
            Map<Address, Integer> map = neighbourNodes.get(n).getTable();
            for (Address address : map.keySet()) {
                int delay = map.get(address);
                if (!lowestDelays.containsKey(address)) {
                    lowestDelays.put(address, delay);
                    routes.put(address, n);
                }
                if (lowestDelays.get(address) > delay) {
                    lowestDelays.put(address, delay);
                    routes.put(address, n);
                }
            }
        }
        if (old.equals(routes)) {
            return false;
        }
        return true;
    }

    public boolean updateNode(Node n, Packet packet) {
        if (!neighbourNodes.containsKey(n)) {
            neighbourNodes.put(n, new DistanceTable());
        }
        if (neighbourNodes.get(n).update(new DistancePacket(packet.getData()))) {
            return update();
        }
        return false;
    }


}
