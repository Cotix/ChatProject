package client.view.chatWindow;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by destion on 8-4-15.
 */
public class ChatsList extends JList {

    DefaultListModel<String> chatModel;

    public ChatsList(ArrayList<String> chats, DefaultListModel<String> chatModel){
        this.chatModel = chatModel;
        for (String user : chats){
            this.chatModel.addElement(user);
        }
        this.setModel(this.chatModel);
    }


    public void removeIndex(int index){
        this.remove(index);
    }

    public void reset(){
        this.removeAll();
    }

}
