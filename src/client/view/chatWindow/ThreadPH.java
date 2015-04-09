package client.view.chatWindow;

import client.model.*;
import client.security.CryptoKeyPair;

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
        Message mess = new Message("Hoi", new CryptoKeyPair(), new CryptoKeyPair(), 1000);
        Message mess2 = new Message("Test test test", new CryptoKeyPair(), new CryptoKeyPair(), 1000);
        Chat chat = new Chat(new CryptoKeyPair());
        Chat chat2 = new Chat( new CryptoKeyPair());
        chat.addMessage(mess);
        chat2.addMessage(mess2);
        ClientsMap map = new ClientsMap("Destion",  new CryptoKeyPair());


        Client client1 = new Client(new CryptoKeyPair(), "Noah");
        Client client2 = new Client(new CryptoKeyPair(), "Destion");
        Client client3 = new Client(new CryptoKeyPair(), "Erik");

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
