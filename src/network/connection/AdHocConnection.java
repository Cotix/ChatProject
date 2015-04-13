package network.connection;

import log.Log;
import log.LogLevel;
import network.connection.packet.CurrentTimePacket;
import network.connection.packet.Packet;
import network.connection.packet.PacketUtils;
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
    private byte[] identifier;
    private Queue<Packet> sendingQueue;
    private int ackCount;

    public AdHocConnection(short p) {
        identifier = new byte[Configuration.ADHOC_IDENTIFIER_LENGTH];
        for (int i = 0; i != Configuration.ADHOC_IDENTIFIER_LENGTH; ++i) {
            identifier[i] = (byte)(Math.random()*255);
        }
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
        try {
            multicastSocket.setSoTimeout(10);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        isConnected = true;
        packetQueue = new ConcurrentLinkedQueue<>();
        sendingQueue = new ConcurrentLinkedQueue<>();
        ackCount = 1;
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

    private byte[] addIdentifier(byte[] msg) {
        byte[] data = new byte[msg.length + Configuration.ADHOC_IDENTIFIER_LENGTH];
        System.arraycopy(msg, 0, data, 0, 4);
        System.arraycopy(identifier, 0, data, 4, Configuration.ADHOC_IDENTIFIER_LENGTH);
        System.arraycopy(msg, 4, data, 4 + Configuration.ADHOC_IDENTIFIER_LENGTH, msg.length);
        return data;

    }

    private byte[] checkIdentifier(byte[] data) {
        for (int i = 0; i != Configuration.ADHOC_IDENTIFIER_LENGTH; ++i) {
            if (data[i] != identifier[i]) {
                byte[] out = new byte[data.length - Configuration.ADHOC_IDENTIFIER_LENGTH];
                System.arraycopy(data, Configuration.ADHOC_IDENTIFIER_LENGTH, out, 0, out.length);
                return out;
            }
        }
        return null;
    }

    @Override
    public void sendPacket(Packet pckt) {
        if (ackCount <= 0) {
            sendingQueue.add(pckt);
            return;
        }
        byte[] data;
        byte[] msg = addIdentifier(pckt.getRawData());
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
                Packet toSend = sendingQueue.poll();
                if (toSend != null) {
                    sendPacket(toSend);
                }
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
                    continue;
                }
                byte[] d = checkIdentifier(data);
                if (d != null) {
                    if (d[0] == PacketUtils.PacketType.ACK.getValue()) {
                        ackCount++;
                        Log.log("Received ack!", LogLevel.INFO);
                    } else {
                        packetQueue.add(new StringPacket(d));
                    }
                }
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
        return "AdHocConnection(multicast:" + port + ") is " + (isConnected ? "" : "not ") + "connected.";
    }
}
