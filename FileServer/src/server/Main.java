package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;
    private static final String PATH = "C:\\Users\\Mikolaj\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\";
    private static HashMap<Integer, String> files = new HashMap<>();
    private static int generateID(){
        Random rand = new Random();
        int id = rand.nextInt() % 1000;
        while (files.containsKey(id) || id <= 0){
            id = rand.nextInt() % 1000;
        }
        return id;
    }
    private static String generateName(){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        int length = 10;
        StringBuilder sb;
        do {
            sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = (int) (AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
        } while (files.containsValue(sb.toString()));
        return sb.toString();
    }
    private static void serialize() {
        try(
                FileOutputStream fos = new FileOutputStream("C:\\Users\\Mikolaj\\Desktop\\HashMap.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserialize(){
        try(
                FileInputStream fis = new FileInputStream("C:\\Users\\Mikolaj\\Desktop\\HashMap.txt");
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            files = (HashMap<Integer, String>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        files.clear();
        deserialize();
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
                            switch(arguments.get(1)){
                                case "1" -> { // BY_NAME
                                    if(files.containsValue(arguments.get(2))) {
                                        if (Files.exists(Path.of(PATH + arguments.get(2)))) {
                                            File file = new File(PATH + arguments.get(2));
                                            byte[] message = new byte[(int) file.length()];
                                            try (FileInputStream fis = new FileInputStream(file)) {
                                                fis.read(message);
                                            }
                                            output.writeUTF("200 " + message.length);
                                            output.write(message);
                                        } else {
                                            output.writeUTF("404");
                                        }
                                    } else {
                                        output.writeUTF("404");
                                    }
                                }
                                case "2" -> { // BY_ID
                                    if(files.containsKey(Integer.parseInt(arguments.get(2)))){
                                        String name = files.get(Integer.parseInt(arguments.get(2)));
                                        if(Files.exists(Path.of(PATH + name))){
                                            File file = new File(PATH + name);
                                            byte[] message = new byte[(int) file.length()];
                                            try(FileInputStream fis = new FileInputStream(file)){
                                                fis.read(message);
                                            }
                                            output.writeUTF("200 " + message.length);
                                            output.write(message);
                                        } else {
                                            output.writeUTF("404");
                                        }
                                    } else {
                                        output.writeUTF("404");
                                    }
                                }
                            }
                        }
                        case "2" -> { // PUT
                            String name = arguments.get(1);
                            if(name.equals("")){
                                name = generateName();
                            }
                            int id = generateID();
                            files.put(id, name);
                            Path path = Path.of(PATH + name);
                            if(!Files.exists(path)) {
                                Files.createFile(path);
                                int length = Integer.parseInt(arguments.get(2));
                                byte[] message = new byte[length];
                                input.readFully(message, 0, length);
                                Files.write(path, message);
                                output.writeUTF("200 " + id);
                            } else {
                                output.writeUTF("403");
                            }
                        }
                        case "3" -> { // DELETE
                            switch(arguments.get(1)) {
                                case "1" -> { // BY_NAME
                                    if(files.containsValue(arguments.get(2))) {
                                        files.entrySet().removeIf(entry -> arguments.get(2).equals(entry.getValue()));
                                        Path path = Path.of(PATH + arguments.get(2));
                                        if (Files.exists(path)) {
                                            Files.delete(path);
                                            output.writeUTF("200");
                                        } else {
                                            output.writeUTF("404");
                                        }
                                    } else {
                                        output.writeUTF("404");
                                    }
                                }
                                case "2" -> { // BY_ID
                                    if(files.containsKey(Integer.parseInt(arguments.get(2)))){
                                        Path path = Path.of(PATH + files.get(Integer.parseInt(arguments.get(2))));
                                        if(Files.exists(path)){
                                            Files.delete(path);
                                            output.writeUTF("200");
                                        } else {
                                            output.writeUTF("404");
                                        }
                                        files.remove(Integer.parseInt(arguments.get(2)));
                                    } else {
                                        output.writeUTF("404");
                                    }
                                }
                            }
                        }
                        case "exit" -> {
                            serialize();
                            exit = true;
                        }
                    }
                }
            }
        }
    }
}