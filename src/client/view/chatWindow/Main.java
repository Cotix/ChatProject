package client.view.chatWindow;


import log.Log;
import log.LogLevel;
import node.LocalNode;

public class Main {

    private static ThreadPH temp;

    public static void main(String[] args) {
        Log.enableAllLevels();
        Log.disableLevel(LogLevel.NONE);
        LocalNode node = new LocalNode("127.0.0.1");
        temp = new ThreadPH();
        temp.run();
        node.run();
    }
}