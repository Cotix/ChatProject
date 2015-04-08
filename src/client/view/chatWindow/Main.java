package client.view.chatWindow;

/**
 * Created by destion on 7-4-15.
 */
public class Main {

    private static ThreadPH temp;

    public static void main(String[] args) {
        temp = new ThreadPH();
        temp.run();
    }
}