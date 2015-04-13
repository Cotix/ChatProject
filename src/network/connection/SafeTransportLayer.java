package network.connection;

import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
import network.connection.packet.StringPacket;
import settings.Configuration;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cotix on 4/13/15.
 */
public class SafeTransportLayer {

    private class ConHandler implements Runnable {
        private Connection con;

        public ConHandler(Connection c) {
            con = c;
        }

        public void run() {
            while(true) {
                con.handleConnection();
            }
        }
    }


    private Connection con;
    private Queue<Packet> receiveQueue;
    private Map<Packet, Long> sendQueue;
    private int timeouts;
    private ConHandler handler;
    private int sequenceNumber;


    public SafeTransportLayer(Connection connection) {
        con = connection;
        receiveQueue = new ConcurrentLinkedQueue<>();
        sendQueue = new ConcurrentHashMap<>();
        timeouts = 0;
        handler = new ConHandler(con);
        new Thread(handler).start();
        sequenceNumber = 0;
    }

    public void sendPacket(Packet p) {
        byte[] data = p.getData();
        byte[] newData = ByteBuffer.allocate(4 + data.length).putInt(sequenceNumber++).put(data).array();
        Packet pckt = new StringPacket(newData);
        sendQueue.put(pckt, System.currentTimeMillis() + Configuration.TIMEOUT);
        con.sendPacket(pckt);
    }


    public void handleConnection() {
        long time = System.currentTimeMillis();
        for (Packet p : sendQueue.keySet()) {
            if (sendQueue.get(p) >= time) {
                sendQueue.put(p, System.currentTimeMillis() + Configuration.TIMEOUT);
                con.sendPacket(p);
                timeouts++;
            }
        }
        Packet read = con.readPacket();
        if (read != null) {
            Packet toRemove = null;
            byte[] data = read.getData();
            if (PacketUtils.getPacketType(read) == PacketUtils.PacketType.ACK) {
                int seq = ByteBuffer.wrap(data).getInt();
                for (Packet p : sendQueue.keySet()) {
                    if (ByteBuffer.wrap(p.getData()).getInt() == seq) {
                        toRemove = p;
                    }
                }
                sendQueue.remove(toRemove);
            }
        }
    }


}
