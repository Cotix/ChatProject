package client.view.chatWindow;

import client.model.Chat;
import client.model.Message;

/**
 * Created by destion on 7-4-15.
 */
public class Main {

    private static ThreadPH temp;

    public static void main(String[] args) {
        Message mess = new Message("Hoi Martijn", "a1b2c3", "z26y25x24", 1000);
        Chat chat = new Chat("a1b2c3");
        chat.addMessage(mess);
        temp = new ThreadPH();
        temp.run();
    }
}