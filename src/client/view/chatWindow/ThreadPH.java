package client.view.chatWindow;

import client.model.*;

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
        Chat chat2 = new Chat("test1test1");
        chat.addMessage(mess);
        chat2.addMessage(mess2);
        ClientsMap map = new ClientsMap("Destion");


        Client client1 = new Client("a1b2c3", "TestClient1");
        Client client2 = new Client("z26y25x24", "Destion");
        Client client3 = new Client("test1test1", "TestClient");

        map.addClient( client1);
        map.addClient( client2);
        map.addClient( client3);

        System.out.println(map.getMap().size());

        ChatMap chats = new ChatMap();
        chats.addChat(chat);
        chats.addChat(chat2);




        TestFrame test = new TestFrame(null, "Destion", chats, map);



        test.updateLobby(map.getNames());
    }
}
