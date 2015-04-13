package network.connection;

import log.Log;
import log.LogLevel;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.Packet;
import network.connection.packet.StringPacket;
import settings.Configuration;

import java.io.IOException;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cotix on 4/13/15.
 */
public class AdHocConnection implements Connection {
    private Queue<Packet> packetQueue;
    private int port;
    private Socket sock;
    private boolean isConnected;
    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;

    public AdHocConnection(short p) {
        port = p;
        try {
            multicastGroup = InetAddress.getByName("228.2.2.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            multicastSocket = new MulticastSocket(p);
            multicastSocket.joinGroup(multicastGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConnected = true;
        packetQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void connect() {

    }

    @Override
    public void connect(String ipAddress, short port) {

    }

    @Override
    public void disconnect() {
        isConnected = false;
    }

    @Override
    public void sendPacket(Packet pckt) {
        byte[] msg = pckt.getRawData();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, multicastGroup, port);
        try {
            multicastSocket.send(packet);
            Log.log("Announcement sent!", LogLevel.NONE);
        } catch (IOException e) {
            Log.log("Announcing went wrong!", LogLevel.ERROR);
        }
    }

    @Override
    public void handleConnection() {
        sendPacket(new CurrentTimePacket());
        if (isConnected) {
        int length;
            while (true) {
                byte[] buf = new byte[4];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                length = recv.getLength();
                if (length != 4) {
                    continue;
                }
                byte[] size = recv.getData();
                int dataSize = 0;
                dataSize += ((long)(size[0]&0xFF)) << 24;
                dataSize += ((long)(size[1]&0xFF)) << 16;
                dataSize += ((long)(size[2]&0xFF)) << 8;
                dataSize += ((long)(size[3]&0xFF));
                if (dataSize <= 0 || dataSize >= Configuration.MAX_PACKET_SIZE) {
                    continue;
                }
                byte[] data = new byte[dataSize];
                recv = new DatagramPacket(data, data.length);
                length = recv.getLength();
                if (dataSize != length) {
                    Log.log("Wrong length prefix!", LogLevel.ERROR);
                }
                packetQueue.add(new StringPacket(data));
            }
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public Packet readPacket() {
        return packetQueue.poll();
    }

    @Override
    public String getConnectionInfo() {
        return "TCPConnection(multicast:" + port + ") is " + (isConnected ? "" : "not ") + "connected.";
    }
}
