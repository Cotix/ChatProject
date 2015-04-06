package network.connection;

import log.Log;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import log.*;
import network.connection.packet.InvalidPacketException;
import network.connection.packet.Packet;
import network.connection.packet.RawPacket;
import network.connection.packet.StringPacket;

/**
 * Created by cotix on 4/2/15.
 */
public class TCPConnection implements Connection {
    private String ipAddress;
    private short port;
    private Socket sock;
    private boolean isConnected;
    private DataOutputStream out;
    private DataInputStream in;
    private Queue<Packet> packetQueue;

    public TCPConnection(String ipAddress, short port) {
        packetQueue = new LinkedBlockingQueue<Packet>();
        this.port = port;
        this.ipAddress = ipAddress;
        isConnected = false;
        sock = new Socket();
    }

    public TCPConnection() {
        packetQueue = new LinkedBlockingQueue<Packet>();
        isConnected = false;
        sock = new Socket();
    }

    @Override
    public void connect() {
        if (!isConnected) {
            try {
                isConnected = true;
                sock = new Socket(ipAddress, port);
                in = new DataInputStream(sock.getInputStream());
                out = new DataOutputStream(sock.getOutputStream());
            } catch (IOException e) {
                isConnected = false;
                Log.Log("TCPConnection to " + ipAddress + " on port " + port + " failed.", LogLevel.INFO);
            }
        }
    }

    @Override
    public void connect(String ipAddress, short port) {
        this.port = port;
        this.ipAddress = ipAddress;
        if (!isConnected) {
            try {
                isConnected = true;
                sock = new Socket(ipAddress, port);
                in = new DataInputStream(sock.getInputStream());
                out = new DataOutputStream(sock.getOutputStream());
            } catch (IOException e) {
                isConnected = false;
                Log.Log("TCPConnection to " + ipAddress + " on port " + port + " failed.", LogLevel.INFO);
            }
        }
    }

    @Override
    public void disconnect() {
        if (isConnected) {
            Log.Log("TCPConnection with " + ipAddress + " on port " + port + " got closed.", LogLevel.INFO);
            isConnected = false;
            try {
                sock.close();
            } catch (IOException e) {
                //Socket was already closed, nothing to worry about.
            }
        }
    }

    @Override
    public void sendPacket(Packet pckt) {
        if (!isConnected) {
            Log.Log("TCPConnection with " + ipAddress + " on port " + port + " was already closed, yet it tried to send a packet.", LogLevel.INFO);
            return;
        }
        int length = pckt.getSize();
        if (length >= Integer.MAX_VALUE) {
            Log.Log("TCPConnection failed sending a packet with size " + length + " to " + ipAddress + ":" + port, LogLevel.ERROR);
            return;
        }
        try {
            out.write(pckt.getRawData());
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public void handleConnection() {
        if (isConnected) {
            int length;
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    length = in.readInt();
                    byte[] data = new byte[length];
                    in.read(data);
                    try {
                        packetQueue.add(new RawPacket(data));
                    } catch (InvalidPacketException e) {
                        Log.Log("Received a malformed packet from " + ipAddress + "!", LogLevel.INFO);
                    }
                }
            } catch (EOFException e) {
                //Reached end of stream!
                return;
            } catch (IOException e) {
                disconnect();
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
        return "TCPConnection(" + ipAddress + ":" + port + ") is " + (isConnected ? "" : "not ") + "connected.";
    }
}
