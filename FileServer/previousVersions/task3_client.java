package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    public static void main(String[] args) throws IOException, InterruptedException {
        sleep(100);
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.next();
            switch (command){
                case "1" -> { // GET
                    System.out.println("Enter filename:");
                    String name = scanner.next();
                    output.writeUTF(command + " " + name);
                    System.out.println("The request was sent.");
                    String msg = input.readUTF();
                    if(msg.startsWith("200")){
                        System.out.println("The content of the file is: " + msg.substring(4));
                    } else if (msg.startsWith("404")){
                        System.out.println("The response says that the file was not found!");
                    }
                }
                case "2" -> { // PUT
                    System.out.println("Enter filename:");
                    String name = scanner.next();
                    System.out.println("Enter file content:");
                    String content = scanner.next();
                    output.writeUTF(command + " " + name + " " + content);
                    System.out.println("The request was sent.");
                    String msg = input.readUTF();
                    if(msg.startsWith("200")){
                        System.out.println("The response says that file was created!");
                    } else if (msg.startsWith("403")){
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                }
                case "3" -> { // DELETE
                    System.out.println("Enter filename:");
                    String name = scanner.next();
                    output.writeUTF(command + " " + name);
                    System.out.println("The request was sent.");
                    String msg = input.readUTF();
                    if(msg.startsWith("200")){
                        System.out.println("The response says that the file was successfully deleted!");
                    } else if (msg.startsWith("404")){
                        System.out.println("The response says that the file was not found!");
                    }
                }
                case "exit" -> output.writeUTF(command);
            }
        }
    }
}