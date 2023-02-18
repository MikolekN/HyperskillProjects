package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    private static final String PATH = "C:\\Users\\Mikolaj\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\";
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("Server started!");
        try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            boolean exit = false;
            while(!exit) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msg = input.readUTF();
                    ArrayList<String> arguments = new ArrayList<>(List.of(msg.split(" ")));
                    switch (arguments.get(0)){
                        case "1" -> { // GET
                            if(Files.exists(Path.of(PATH + arguments.get(1)))){
                                String content = Files.readString(Path.of(PATH + arguments.get(1)));
                                output.writeUTF("200 " + content);
                            } else {
                                output.writeUTF("404");
                            }
                        }
                        case "2" -> { // PUT
                            if(!Files.exists(Path.of(PATH + arguments.get(1)))) {
                                StringBuilder content = new StringBuilder();
                                for(int i = 2; i < arguments.size(); i++){
                                    if(i == arguments.size() - 1){
                                        content.append(arguments.get(i));
                                    } else {
                                        content.append(arguments.get(i)).append(" ");
                                    }
                                }
                                Files.createFile(Path.of(PATH + arguments.get(1)));
                                Files.writeString(Path.of(PATH + arguments.get(1)), content.toString());
                                output.writeUTF("200");
                            } else {
                                output.writeUTF("403");
                            }
                        }
                        case "3" -> { // DELETE
                            if(Files.exists(Path.of(PATH + arguments.get(1)))){
                                Files.delete(Path.of(PATH + arguments.get(1)));
                                output.writeUTF("200");
                            } else {
                                output.writeUTF("404");
                            }
                        }
                        case "exit" -> {
                            exit = true;
                        }
                    }
                }
            }
        }
    }
}