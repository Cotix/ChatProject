package client;

import client.controller.NetworkController;
import client.model.Message;
import client.security.CryptoKeyPair;
import log.Log;
import log.LogLevel;
import network.Address;
import node.DistanceTable;
import node.LocalNode;
import settings.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by cotix on 4/13/15.
 */
public class SimpleClient {
    public static void main(String[] arg) {
        com.sun.org.apache.xml.internal.security.Init.init();
        Log.enableAllLevels();
        //Log.disableLevel(LogLevel.NONE);
        LocalNode node = new LocalNode("192.168.5.4");
        node.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NetworkController ncon = new NetworkController(Configuration.HOST, Configuration.PORT,
                new CryptoKeyPair(), "Noah");
        new Thread(ncon).start();
        Log.log("My key: " + ncon.getMyKeyPair().hashCode(), LogLevel.INFO);
        while (true) {
            List<Message> list = ncon.getMessage();
            if (list != null) {
                for (Message m : list) {
                    Log.log("Received a message from " + m.getReceiverPair().hashCode() + ": "
                    + m.getMessage(), LogLevel.INFO);
                }
            }
            DistanceTable table = ncon.getDistanceTable();
            if (table != null) {
                Log.log("All clients connected: ", LogLevel.NONE);
                for (Address a : table.getTable().keySet()) {
                    Message m = new Message("Hai", ncon.getMyKeyPair(), a.getAddress(), System.currentTimeMillis());
                    try {
                        ncon.sendMessage(m);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.log("" + a.hashCode(), LogLevel.NONE);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
