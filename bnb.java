import java.io.*;
import java.util.Scanner;

class bnb {
    String username;
    static Boolean loggedIn = false;

    public static void login() {
        // Reads the current users names
        try {
            File currentUsers = new File("current_users.txt");
            BufferedReader usersReader = new BufferedReader(new FileReader(currentUsers));
            String line = usersReader.readLine();
            String[] usernames = line.split(", ");
            for (String user : usernames) {
                System.out.println(user);
            }
            usersReader.close();
            loggedIn = true;
        } catch (IOException e) {
            System.out.println("FAILED to read current_users.txt or File Not found");
        }
    }

    public static void main(String[] args) {
        String option;
        Scanner getInput = new Scanner(System.in);

        while (true) {
            if (!loggedIn) {
                System.out.println("Please choose the corresponding number from the following options:");
                System.out.println("1 - Login");
                System.out.println("2 - Exit");
                System.out.print("Your Option: ");
                do {
                    option = getInput.next();
                } while (!(option.equals("1") || option.equals("2")));
                switch (option) {
                    case "1":
                        System.out.println("Hello");
                        login();
                        if (!loggedIn) {
                            System.out.println("Invalid Username");
                        }
                    case "2":
                        break;
                }
            }
            else {
                System.out.println("You are now logged in");
                break;
            }
        }
    }
}