package client.view.chatWindow;

import java.util.ArrayList;

/**
 * Created by destion on 7-4-15.
 */
public class ThreadPH implements Runnable {

    ArrayList<String> temp;


    public ThreadPH(){
        this.temp = new ArrayList<>();
        temp.add("Erik");
        temp.add("Noah");
        temp.add("Martijn");

    }

    @Override
    public void run() {
        TestFrame test = new TestFrame(null, "Erik");
        test.updateLobby(temp);
    }
}
