package node;

import log.Log;
import log.LogLevel;
import network.Address;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.DistancePacket;
import network.connection.packet.Packet;

import java.io.UnsupportedEncodingException;
import java.util.*;

//This class does all the routing calculations

public class RoutingTable {
    private Map<Address, Node> routes;
    private Map<Node, DistanceTable> neighbourNodes;
    private Map<Address, Integer> lowestDelays;
    private Map<Node, Integer> delayNodes;
    private Map<Address, ClientHandler> clients;
    private Map<ClientHandler, Integer> clientLatency;
    public RoutingTable() {
        routes = new HashMap<>();
        lowestDelays = null;
        neighbourNodes = new HashMap<>();
        delayNodes = new HashMap<>();
        clients = new HashMap<>();
        clientLatency = new HashMap<>();
    }

    public void updateNodePing(Node n, int latency) {
        delayNodes.put(n, latency);
    }

    public void removeNode(Node n) {
        neighbourNodes.remove(n);
    }

    public boolean addClient(Address address, ClientHandler client) {
        if (clients.containsKey(address)) {
            return false;
        }
        clients.put(address, client);
        update();
        return true;
    }

    public void clientSetPing(ClientHandler c, int latency) {
        clientLatency.put(c, latency);
    }

    public Collection<ClientHandler> getClients() {
        return clients.values();
    }

    public boolean removeOldClient() {
        boolean update = false;
        List<Address> toRemove = new LinkedList<>();
        for (Address a : clients.keySet()) {
            if (clients.get(a).getRunning() == false) {
                toRemove.add(a);
            }
        }
        for (Address a : toRemove) {
            clients.remove(a);
            Log.log("Removed client " + a, LogLevel.INFO);
            update = true;
        }
        if (update) {
            return update();
        }
        return false;
    }

    public boolean removeOldNode() {
        boolean update = false;
        List<Node> toRemove = new LinkedList<>();
        for (Node n : neighbourNodes.keySet()) {
            if (n.isConnected() == false) {
                toRemove.add(n);
            }
        }
        for (Node n : toRemove) {
            neighbourNodes.remove(n);
            Log.log("Removed node " + n.getIp() + ":" + n.getPort(), LogLevel.INFO);
            update = true;
        }
        if (update) {
            return update();
        }
        return false;
    }

    public DistancePacket getMyDistanceTable() {
        if (lowestDelays == null) {
            update();
        }
        try {
            return new DistancePacket(lowestDelays);
        } catch (UnsupportedEncodingException e) {
            Log.log("Encoding exception in getMyDistanceTable", LogLevel.ERROR);
        }
        return null;
    }
    //calculate best neighbour node to forward our packet to an address
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
    //Reads cached best node to forward our packet, calls findBestNode if there is nothing cached
    public Node getNode(Address address) {
        if (routes.containsKey(address)) {
            Node n = routes.get(address);
            if (neighbourNodes.containsKey(n) && neighbourNodes.get(n).getTable().containsKey(address)) {
                return n;
            }
        }
        return findBestNode(address);
    }
    //Calculates best node to send to, but excludes an address
    //Useful to blacklist nodes
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
    //Updates the routing table returns true if there are changes
    public boolean update() {
        Map<Address, Node> old = routes;
        lowestDelays = new HashMap<>();
        routes = new HashMap<>();
        //First add our clients, we have the fastest connection to them!
        for (Address client : clients.keySet()) {
            int latency;
            if (clientLatency.containsKey(clients.get(client))) {
                latency = clientLatency.get(clients.get(client));
            } else {
                latency = (int)(Math.random() * 50);
            }
            lowestDelays.put(client, latency);
        }
        for (Node n : neighbourNodes.keySet()) {
            if (!delayNodes.containsKey(n)) {
                delayNodes.put(n, 1000);
            }
            int latency = delayNodes.get(n);
            Map<Address, Integer> map = neighbourNodes.get(n).getTable();
            for (Address address : map.keySet()) {
                int delay = map.get(address) + latency;
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
    //Updates the table based on a change in distancetable from a neighbour node
    public boolean updateNode(Node n, Packet packet) {
        if (!neighbourNodes.containsKey(n)) {
            neighbourNodes.put(n, new DistanceTable());
        }
        if (neighbourNodes.get(n).update(new DistancePacket(packet.getData()))) {
            return update();
        }
        return false;
    }

    public ClientHandler getDirectConnection(Address a) {
        if (clients.containsKey(a)) {
            return clients.get(a);
        }
        return null;
    }


}
