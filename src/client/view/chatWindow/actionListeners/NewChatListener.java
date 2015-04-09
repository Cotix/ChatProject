package client.view.chatWindow.actionListeners;

import client.view.chatWindow.TestFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by destion on 9-4-15.
 */
public class NewChatListener implements ActionListener {

    private TestFrame testFrame;

    public NewChatListener(TestFrame frame){
        this.testFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.testFrame.openChat();
    }
}
