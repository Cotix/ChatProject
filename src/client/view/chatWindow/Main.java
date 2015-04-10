package client.view.chatWindow;


import log.Log;
import log.LogLevel;
import node.LocalNode;

public class Main {

    private static ThreadPH temp;

    public static void main(String[] args) {
        com.sun.org.apache.xml.internal.security.Init.init();
        Log.enableAllLevels();
        Log.disableLevel(LogLevel.NONE);
        LocalNode node = new LocalNode("127.0.0.1");
        node.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        temp = new ThreadPH();
        temp.run();
    }
}