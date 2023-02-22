package carsharing;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.exit;

class Menu{
    enum State{
        LOGIN,
        CHOOSE,
        CREATE_COMPANY;
    }
    State state = State.LOGIN;
    Connection connection;

    public Menu(Connection connection) {
        this.connection = connection;
        System.out.println("1. Log in as a manager\n" + "0. Exit");
    }

    public void interact(String str) throws SQLException {
        switch (this.state){
            case LOGIN -> {
                switch (str){
                    case "1" -> { // Log in as a manager
                        this.state = State.CHOOSE;
                        System.out.println("1. Company list\n" + "2. Create a company\n" + "0. Back");
                    }
                    case "0" -> { // Exit
                        connection.close();
                        exit(0);
                    }
                }
            }
            case CHOOSE -> {
                switch (str){
                    case "1" -> { // Company list
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery("SELECT NAME FROM COMPANY ORDER BY ID");
                        if(rs.first()){
                            System.out.println("Company list:");
                            int i = 1;
                            do {
                                System.out.println(i + ". " + rs.getString(1));
                                i++;
                            } while (rs.next());
                        } else {
                            System.out.println("The company list is empty!");
                        }
                    }
                    case "2" -> { // Create a company
                        this.state = State.CREATE_COMPANY;
                        System.out.println("Enter the company name:");
                    }
                    case "0" -> { // Back
                        this.state = State.LOGIN;
                        System.out.println("1. Log in as a manager\n" + "0. Exit");
                    }
                }
            }
            case CREATE_COMPANY -> {
                Statement st = connection.createStatement();
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO COMPANY (NAME) VALUES (?)");
                pstmt.setString(1, str);
                pstmt.executeUpdate();
                this.state = State.CHOOSE;
                System.out.println("1. Company list\n" + "2. Create a company\n" + "0. Back");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // write your code here
        Class.forName("org.h2.Driver");
        Connection conn;
        if(args.length == 0){
            //File file = new File(".\\Car Sharing\\task\\src\\carsharing\\db\\hallo");
            conn = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/hallo");
        } else {
            //File file = new File(".\\Car Sharing\\task\\src\\carsharing\\db\\" + args[1]);
            conn = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/" + args[1]);
        }
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("DROP TABLE IF EXISTS COMPANY;" +
                "CREATE TABLE COMPANY (ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR UNIQUE NOT NULL);");
        Menu menu = new Menu(conn);
        while(true){
            menu.interact(new Scanner(System.in).nextLine());
        }
    }
}