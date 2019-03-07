package chat;

import javax.swing.JFrame;

public class ServerTest {
    public static void main(String[] args) {
        Chatserver mathias = new Chatserver();
        mathias.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mathias.startRunning();
    }
}
