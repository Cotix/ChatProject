package client.view.chatWindow.actionListeners;


import client.view.chatWindow.ChatSwitcher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Created by destion on 9-4-15.
 */
public class SelectionListener implements ListSelectionListener{

    JList<String> chatList;
    ChatSwitcher switcher;

    public SelectionListener(JList chatList, ChatSwitcher switcher){
        this.switcher = switcher;
        this.chatList = chatList;
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        switcher.switchChat(chatList.getSelectedValue());
    }
}
