package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");
        try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            try (Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                String msg = input.readUTF();
                System.out.println("Received: " + msg);
                String response = "All files were sent!";
                output.writeUTF(response);
                System.out.println("Sent: " + response);
            }
        }
    }
}