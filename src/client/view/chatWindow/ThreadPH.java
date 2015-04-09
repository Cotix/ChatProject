package client.view.chatWindow;

import client.model.Chat;
import client.model.KeyNickMap;
import client.model.Message;

import java.util.ArrayList;

/**
 * Created by destion on 7-4-15.
 */
public class ThreadPH implements Runnable {

    ArrayList<String> temp;


    public ThreadPH(){
        this.temp = new ArrayList<>();

    }


    //TODO remove placeholder code and replace when node connection is enabled
    @Override
    public void run() {
        Message mess = new Message("Hoi Martijn", "a1b2c3", "z26y25x24", 1000);
        Message mess2 = new Message("Test test test", "test1test1", "z26y25x24", 1000);
        Chat chat = new Chat("a1b2c3");
        Chat chat2 = new Chat("1234qwert");
        chat.addMessage(mess);
        chat2.addMessage(mess2);
        KeyNickMap map = new KeyNickMap();
        map.addNick("a1b2c3", "Erik");
        map.addNick("z26y25x24", "Destion");
        map.addNick("test1test1", "TestClient");
        TestFrame test = new TestFrame(null, "Destion");
        test.updateLobby(map.getNames());
    }
}
