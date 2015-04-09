package client.view.chatWindow;


public class Main {

    private static ThreadPH temp;

    public static void main(String[] args) {
        temp = new ThreadPH();
        temp.run();
    }
}