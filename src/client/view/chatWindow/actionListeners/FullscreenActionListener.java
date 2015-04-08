package client.view.chatWindow.actionListeners;

import client.view.chatWindow.ChatFrame;
import client.view.chatWindow.TestFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by destion on 8-4-15.
 */
public class FullscreenActionListener implements ActionListener {

    private Toolkit tk;
    private TestFrame frame;
    JCheckBoxMenuItem cb;

    private int width;
    private int height;

    public FullscreenActionListener(TestFrame frame, JCheckBoxMenuItem cb){
        this.frame = frame;
        this.cb = cb;


        this.tk = Toolkit.getDefaultToolkit();
        this.width = (int) tk.getScreenSize().getWidth();
        this.height = (int) tk.getScreenSize().getHeight();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!this.cb.getState()){
            this.frame.setSize(400, 400);
            this.frame.setLocationRelativeTo(null);
        } else {
            this.frame.setSize(width, height);
        }
    }
}
