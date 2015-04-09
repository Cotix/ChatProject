package client.view.chatWindow.actionListeners;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Created by destion on 9-4-15.
 */
public class SelectionListener implements ListSelectionListener{

    JList<String> chatList;

    public SelectionListener(JList chatList){
        this.chatList = chatList;
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        System.out.println(chatList.getSelectedValue());
    }
}
