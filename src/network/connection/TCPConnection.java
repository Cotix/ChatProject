package network.connection;

import log.Log;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import log.*;
import network.connection.packet.Packet;
import network.connection.packet.StringPacket;
import settings.Configuration;
/**
 * A wrapper around the Socket class that allows us for very easy NIO
 * It maintains a packet queue, which other threads or classes can read
 * Also allows for easy sending of binary data using our Packet interface
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
        packetQueue = new ConcurrentLinkedQueue<>();
        this.port = port;
        this.ipAddress = ipAddress;
        isConnected = false;
        sock = new Socket();
    }

    public TCPConnection() {
        packetQueue = new ConcurrentLinkedQueue<>();
        isConnected = false;
        sock = new Socket();
    }

    public TCPConnection(Socket s) {
        sock = s;
        packetQueue = new ConcurrentLinkedQueue<>();
        this.port = (short) s.getPort();
        this.ipAddress = s.getLocalAddress().toString();

        try {
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            isConnected = true;
            Log.log("TCPConnection to " + ipAddress + " on port " + port + " connected!", LogLevel.INFO);
        } catch (IOException e) {
            isConnected = false;
            Log.log("TCPConnection to " + ipAddress + " on port " + port + " failed.", LogLevel.INFO);
        }

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
                Log.log("TCPConnection to " + ipAddress + " on port " + port + " failed.", LogLevel.INFO);
            }
        }
        Log.log("TCPConnection to " + ipAddress + " on port " + port + " connected!", LogLevel.INFO);
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
                Log.log("TCPConnection to " + ipAddress + " on port " + port + " failed.", LogLevel.INFO);
            }
        }
    }

    @Override
    public void disconnect() {
        if (isConnected) {
            Log.log("TCPConnection with " + ipAddress + " on port " + port + " got closed.", LogLevel.INFO);
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
            Log.log("TCPConnection with " + ipAddress + " on port " + port + " was already closed, yet it tried to send a packet.", LogLevel.INFO);
            return;
        }
        int length = pckt.getSize();
        if (length >= Integer.MAX_VALUE) {
            Log.log("TCPConnection failed sending a packet with size " + length + " to " + ipAddress + ":" + port, LogLevel.ERROR);
            return;
        }
        try {
            out.write(pckt.getRawData());
            out.flush();
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
                    if (length <= 0 || length >= Configuration.MAX_PACKET_SIZE) {
                        continue;
                    }
                    byte[] data = new byte[length];
                    int recvLen = in.read(data);
                    if (recvLen != length) {
                        Log.log("Wrong length prefix!", LogLevel.ERROR);
                    }
                    packetQueue.add(new StringPacket(data));
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
