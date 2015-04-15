import log.Log;
import log.LogLevel;
import network.connection.packet.CurrentTimePacket;
import node.LocalNode;
import org.omg.CORBA.Current;

public class Main {
    public static void main(String[] arg) {
        Log.enableAllLevels();

        LocalNode myNode = new LocalNode("192.168.5.1");
//        LocalNode myNode = new LocalNode("130.89.230.170");
        myNode.run();

    }
}
