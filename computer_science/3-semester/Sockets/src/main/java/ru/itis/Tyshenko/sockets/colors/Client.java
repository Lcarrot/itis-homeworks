package ru.itis.Tyshenko.sockets.colors;

import java.awt.*;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

import static ru.itis.Tyshenko.sockets.Protocol.PORT;

public class Client {

    private static final String[] COLORS = {"red", "green", "blue"};

    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting client...");
        Socket s = new Socket(InetAddress.getLocalHost(), PORT);
        OutputStream out = s.getOutputStream();
        ByteBuffer buf = ByteBuffer.allocate(12);
        int[] buffer = new int[COLORS.length];
        while(true) {
            getColorFromConsole(scanner, buffer);
            if (!addRightColorValuesInBuffer(buffer, buf)) continue;
            System.out.println(">> " + new Color(buffer[0], buffer[1], buffer[2]));
            System.out.println(Arrays.toString(buf.array()));
            out.write(buf.array());
            out.flush();
        }
    }

    public static void getColorFromConsole(Scanner scanner, int[] colors) {
        for (int i = 0; i < 3; i++) {
            System.out.println("Print value for next color: " + COLORS[i]);
            colors[i] = Integer.parseInt(scanner.next());
        }
    }

    public static boolean addRightColorValuesInBuffer(int[] buffer, ByteBuffer buf) {
        for (int v : buffer) {
            if (v < 0 || v > 255) {
                System.out.println("value must me in range (0,255)");
                buf.rewind();
                return false;
            }
            buf.putInt(v);
        }
        return true;
    }
}