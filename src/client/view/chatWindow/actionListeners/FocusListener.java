package client.view.chatWindow.actionListeners;

import client.view.chatWindow.EntryBar;

import java.awt.event.*;


public class FocusListener implements java.awt.event.FocusListener {

    EntryBar entryBar;

    public FocusListener(EntryBar entry){
        this.entryBar = entry;
    }


    @Override
    public void focusGained(FocusEvent focusEvent) {
        if ((this.entryBar.getText().length() <= 0) || (this.entryBar.getText().length() == 24)){
            entryBar.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        if (this.entryBar.getText().length() <= 0){
            entryBar.setText("Enter chat message here.");
        }
    }
}
