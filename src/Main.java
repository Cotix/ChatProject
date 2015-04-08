import log.Log;
import node.LocalNode;

/**
 * Created by cotix on 4/7/15.
 */
public class Main {
    public static void main(String[] arg) {
        Log.enableAllLevels();
        LocalNode myNode = new LocalNode("192.168.5.3");
//        LocalNode myNode = new LocalNode("130.89.230.170");
        myNode.run();

    }
}
