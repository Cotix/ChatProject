package testing;


import log.Log;
import log.LogLevel;
import node.LocalNode;
import settings.Configuration;

//A simple class that starts a few LocalNodes, used for testing

public class Simulation {

    public static void main(String[] arg) {
        Configuration.ONE_NODE_PER_IP = false;
        Log.enableAllLevels();
        Log.disableLevel(LogLevel.NONE);
        LocalNode[] nodes = new LocalNode[4];
        for (int i = 0; i != 4; ++i) {
            nodes[i] = new LocalNode((short)(2000 + i), (short)(3000+i), "127.0.0.1");
            nodes[i].start();
        }
        try {
            nodes[0].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
