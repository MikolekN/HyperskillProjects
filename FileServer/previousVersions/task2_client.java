package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    public static void main(String[] args)  throws IOException {
        System.out.println("Client started!");
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            String msg = "Give me everything you have!";
            System.out.println("Sent: " + msg);
            output.writeUTF(msg);
            String response = input.readUTF();
            System.out.println("Received: " + response);
        }
    }
}