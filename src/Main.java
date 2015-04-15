import datatypes.Bytes;
import log.Log;
import log.LogLevel;
import network.connection.packet.CurrentTimePacket;
import node.LocalNode;
import org.omg.CORBA.Current;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] arg) {
        ConcurrentHashMap<Bytes, Integer> map = new ConcurrentHashMap<>();
        byte[] data = new byte[2];
        data[0] = 4;
        data[1] = 8;
        map.put(new Bytes(data), 1);
        byte[] ddata = new byte[2];
        ddata[0] = 4;
        ddata[1] = 8;
        if (map.containsKey(new Bytes(ddata))) {
            System.out.print("JA!!");
        }
    }
}
