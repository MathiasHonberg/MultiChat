package chat;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Chatserver extends JFrame {

    private JTextField userText;
    private JTextPane chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private boolean color;

    //contructor

    public Chatserver() {

        super("Maddi's Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextPane();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    //set up and run the server
    public void startRunning() {

        try {
            server = new ServerSocket(1337, 100);
            while (true) {

                try {
                    //connect and have conversation
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException e) {
                    showMessage("\n Server ended the connection! ");
                } finally {
                    closeStreams();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wait for connection, then display connection information
    private void waitForConnection() throws IOException {

        showMessage(" Waiting for someone to connect...\n");
        connection = server.accept();
        showMessage(" Now connected to " + connection.getInetAddress().getHostName());
    }

    //get stream to send and receive data
    private void setupStreams() throws IOException {

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup! \n");

    }

    //during the chat conversation
    private void whileChatting() throws IOException {

        String message = " You are now connected! ";
        sendMessage(message);
        ableToType(true);

        do {
            try {
                message = (String) input.readObject();
                if(message.contains("/col red")) {
                    color = true;
                }
                showMessage("\n" + message);

            } catch (ClassNotFoundException e) {
                showMessage("\n WTF did he sent??? ");
            }
        } while (!message.equals("CLIENT - END"));
    }

    //close streams and sockets after you are done chatting
    private void closeStreams() {

        showMessage("\n Closing connections... \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //send a message to the client
    private void sendMessage(String message) {

        try {

            output.writeObject("SERVER - " + message);
            output.flush();
            if(message.contains("/col red")) {
                color = true;
            }
            showMessage("\nSERVER - " + message);
        } catch (IOException e) {
            //chatWindow.append("\n ERROR: I cant send that message");
        }
    }

    //updates chatWindow
    private void showMessage(final String text) {
        String newText;
        if(text.contains("CLIENT - /col red") || text.contains("\nSERVER - /col red")){
            newText = text.substring(0, 9) + text.substring(18);
        }
        else{
            newText = text;
        }
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (color) {
                            appendToPane(chatWindow, newText, Color.red);
                            color = false;
                        }
                        else{
                            appendToPane(chatWindow, newText, Color.black);

                        }
                        //chatWindow.append(text);
                    }
                }
        );
    }

    //Let the user type stuff into their box
    private void ableToType(final boolean tof) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }

    private void appendToPane(JTextPane tp, String msg, Color color){

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

}
