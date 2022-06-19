package ru.itis.Tyshenko.sockets.request;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import static ru.itis.Tyshenko.sockets.Protocol.PORT;

public class Client {

    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting client...\nPlease enter a hostname");
        String hostname = scanner.nextLine().trim();
        Socket socket = new Socket(hostname, PORT);
        String request = "GET / HTTP/1.1\r\n\r\n";
        OutputStream stream = socket.getOutputStream();
        stream.write(request.getBytes());
        stream.flush();
        InputStream inputStream = socket.getInputStream();
        int ch;
        StringBuilder builder = new StringBuilder();
        while ((ch = inputStream.read()) != -1) {
            builder.append((char) ch);
        }
        System.out.println(builder.toString());
    }
}
