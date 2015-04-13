package client;

import client.controller.ClientController;
import log.Log;
import node.LocalNode;

public class Main {
    public static void main(String[] args) {
        com.sun.org.apache.xml.internal.security.Init.init();
        Log.enableAllLevels();

        LocalNode node = new LocalNode("192.168.5.4");
        node.start();

        ClientController client = ClientController.getInstance();
        client.init();
        new Thread(client).start();
    }
}
