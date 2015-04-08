package client.view.chatWindow.actionListeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by destion on 8-4-15.
 */
public class FileClearListener implements ActionListener {

    private DefaultListModel chatModel;

    public FileClearListener(DefaultListModel chatModel){
        this.chatModel = chatModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.chatModel.removeAllElements();
    }
}
