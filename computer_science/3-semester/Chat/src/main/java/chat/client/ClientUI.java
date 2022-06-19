package main.java.chat.client;


import chat.network.*;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUI extends JFrame implements ConnectionListener {
    private JTextField userName;
    private JTextField message;
    private JTextArea chat;
    private JButton send;
    private JPanel rootPanel;

   private Connection connection;

    private ClientUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(rootPanel);
        this.pack();
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), Connection.PORT);
            connection = new ConnectionImpl(socket, this);
            openConnection(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send.addActionListener(e -> {
            String msg = message.getText();
            if (msg.equals("")) return;
            message.setText(null);
            connection.send(new StringMessage(userName.getText(), msg, 1));
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI("My chat").setVisible(true));
    }

    private synchronized void printMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            chat.append(message + "\n");
            chat.setCaretPosition(chat.getDocument().getLength());
        });
    }

    @Override
    public void openConnection(Connection connection) {
        printMessage("Connection is ready");
    }

    @Override
    public void closeConnection(Connection connection) {
        printMessage("connection closed");
        connection.close();
    }

    @Override
    public void connectException(Connection connection, Exception exception) {
        printMessage("error: " + exception.getMessage());
        closeConnection(connection);
    }

    @Override
    public void receiveContent(Message msg) {
        printMessage(msg.toString());
    }
}
