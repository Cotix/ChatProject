package client.view.chatWindow;

import client.controller.NetworkController;
import client.model.*;
import client.security.CryptoKeyPair;
import network.Address;
import settings.Configuration;

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
        CryptoKeyPair keyPair1 = new CryptoKeyPair();
        CryptoKeyPair keyPair2 = new CryptoKeyPair();

        CryptoKeyPair keyPair3 = new CryptoKeyPair();

        Message mess = new Message("Hoi", keyPair1, new CryptoKeyPair(), 1428654264136L * 3);
        Message mess2 = new Message("Test test test", keyPair2, new CryptoKeyPair(), 1428654264136L * 3);
        Chat chat = new Chat(new Address(keyPair1, "Erik"));
        Chat chat2 = new Chat(new Address(keyPair2, "Noah"));
        chat.addMessage(mess);
        chat2.addMessage(mess2);
        ChatMap chats = new ChatMap();
        chats.addChat(chat);
        chats.addChat(chat2);
        ClientsMap map = new ClientsMap(new Address(keyPair3, "Destion"));

        Address client1 = new Address(keyPair1, "Erik");
        Address client2 = new Address(keyPair2, "Noah");
        Address self = new Address(keyPair3, "Destion");

        map.addClient(client1);
        map.addClient(client2);
        map.addClient(self);


        System.out.println(map.getMap().size());


        NetworkController net = new NetworkController(Configuration.HOST, Configuration.PORT, keyPair3, "Destion");



        TestFrame test = new TestFrame(net, "Destion", chats, map);



        test.updateLobby(map.getNames());
    }
}
