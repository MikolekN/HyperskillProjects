import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class main {
    private static final int PORT = 34522;
    public static void main(String[] args) {
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> availableNames = new ArrayList<>(List.of("file1", "file2", "file3", "file4", "file5", "file6", "file7", "file8", "file9", "file10"));
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            ArrayList<String> commands = new ArrayList<>(List.of(scanner.nextLine().split(" ")));
            if(Objects.equals(commands.get(0).toUpperCase(), "ADD")){
                if(files.contains(commands.get(1)) || !availableNames.contains(commands.get(1))){
                    System.out.println("Cannot add the file " + commands.get(1));
                } else {
                    files.add(commands.get(1));
                    System.out.println("The file " + commands.get(1) + " added successfully");
                }
            } else if(Objects.equals(commands.get(0).toUpperCase(), "GET")) {
                if(files.contains(commands.get(1))){
                    System.out.println("The file " + commands.get(1) + " was sent");
                } else {
                    System.out.println("The file " + commands.get(1) + " not found");
                }
            } else if(Objects.equals(commands.get(0).toUpperCase(), "DELETE")) {
                if(files.contains(commands.get(1))){
                    files.remove(commands.get(1));
                    System.out.println("The file " + commands.get(1) + " was deleted");
                } else {
                    System.out.println("The file " + commands.get(1) + " not found");
                }
            } else if(Objects.equals(commands.get(0).toUpperCase(), "EXIT")) {
                break;
            }
        }
    }
}
