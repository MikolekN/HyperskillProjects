package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    private static final String PATH = "C:\\Users\\Mikolaj\\Desktop\\File Server\\File Server\\task\\src\\client\\data\\";
    public static void main(String[] args) throws IOException, InterruptedException {
        sleep(100);
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
            Scanner scanner = new Scanner(System.in);
            String action = scanner.next();
            switch (action){
                case "1" -> { // GET
                    System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id):");
                    String by = scanner.next();
                    switch(by){
                        case "1" -> { // BY_NAME
                            System.out.println("Enter name of the file:");
                            String name = scanner.next();
                            output.writeUTF(action + " " + by + " " + name);
                        }
                        case "2" -> { // BY_ID
                            System.out.println("Enter id:");
                            String id = scanner.next();
                            output.writeUTF(action + " " + by + " " + id);
                        }
                    }
                    System.out.println("The request was sent.");
                    String msg = input.readUTF();
                    if(msg.startsWith("200")){
                        int length = Integer.parseInt(msg.substring(4));
                        byte[] message = new byte[length];
                        input.readFully(message, 0, length);
                        System.out.println("The file was downloaded! Specify a name for it:");
                        String newName = scanner.next();
                        try(FileOutputStream fos = new FileOutputStream(PATH + newName)){
                            fos.write(message);
                        }
                        System.out.println("File saved on the hard drive!");
                    } else if (msg.startsWith("404")){
                        System.out.println("The response says that this file is not found!");
                    }
                }
                case "2" -> { // PUT
                    System.out.println("Enter name of the file:");
                    String name = scanner.next();
                    System.out.println("Enter name of the file to be saved on server:");
                    scanner.nextLine();
                    String newName = scanner.nextLine();
                    if(Files.exists(Path.of(PATH + name))){
                        File file = new File(PATH + name);
                        byte[] message = new byte[(int) file.length()];
                        try(FileInputStream fis = new FileInputStream(file)){
                            fis.read(message);
                        }
                        output.writeUTF(action + " " + newName + " " + message.length);
                        output.write(message);
                        System.out.println("The request was sent.");
                        String msg = input.readUTF();
                        if(msg.startsWith("200")){
                            System.out.println("Response says that file is saved! ID = " + msg.substring(4));
                        } else if (msg.startsWith("403")){
                            System.out.println("The response says that creating the file was forbidden!");
                        }
                    }
                }
                case "3" -> { // DELETE
                    System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id):");
                    String by = scanner.next();
                    switch(by){
                        case "1" -> { // BY_NAME
                            System.out.println("Enter name of the file:");
                            String name = scanner.next();
                            output.writeUTF(action + " " + by + " " + name);
                        }
                        case "2" -> { // BY_ID
                            System.out.println("Enter id:");
                            String id = scanner.next();
                            output.writeUTF(action + " " + by + " " + id);
                        }
                    }
                    System.out.println("The request was sent.");
                    String msg = input.readUTF();
                    if(msg.startsWith("200")){
                        System.out.println("The response says that the file was successfully deleted!");
                    } else if (msg.startsWith("404")){
                        System.out.println("The response says that the file was not found!");
                    }
                }
                case "exit" -> {
                    output.writeUTF(action);
                    System.out.println("The request was sent.");
                }
            }
        }
    }
}