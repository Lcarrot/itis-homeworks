package main.java.chat.network;

import java.io.*;
import java.net.Socket;

public class ConnectionImpl implements Connection, Runnable {

    private final ConnectionListener listener;
    private OutputStream out;
    private InputStream in;
    Thread thread;

    public ConnectionImpl(Socket socket, ConnectionListener listener) {
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listener = listener;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void send(Message msg) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(msg);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        thread.interrupt();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                if (in.available() != 0) {
                    ObjectInputStream inputStream = new ObjectInputStream(in);
                    listener.receiveContent((Message) inputStream.readObject());
                } else {
                    Thread.sleep(200);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
