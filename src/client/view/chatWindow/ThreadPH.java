package client.view.chatWindow;

import client.model.*;
import client.security.CryptoKeyPair;
import network.Address;

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
        Chat chat = new Chat(new Address(new CryptoKeyPair(), "Erik"));
        Chat chat2 = new Chat(new Address(new CryptoKeyPair(), "Noah"));
        chat.addMessage(mess);
        chat2.addMessage(mess2);
        ChatMap chats = new ChatMap();
        chats.addChat(chat);
        chats.addChat(chat2);
        ClientsMap map = new ClientsMap(new Address(new CryptoKeyPair(), "Destion"));

        Address client1 = new Address(new CryptoKeyPair(), "Erik");
        Address client2 = new Address(new CryptoKeyPair(), "Noah");

        map.addClient(client1);
        map.addClient(client2);


        System.out.println(map.getMap().size());






        TestFrame test = new TestFrame(null, "Destion", chats, map);



        test.updateLobby(map.getNames());
    }
}
