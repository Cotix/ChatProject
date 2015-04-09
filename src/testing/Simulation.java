package testing;


import log.Log;
import log.LogLevel;
import node.LocalNode;
import settings.Configuration;

/**
 * Created by cotix on 4/8/15.
 */
public class Simulation {

    public static void main(String[] arg) {
        Configuration.ONENODEPERIP = false;
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
