package Phase_5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; 

public class App {
    public static Singleton singleton = Singleton.getInstance();
    public static void main(String[] args){

        boolean exit = false;

        // Check For Sufficient Arguments
        if (args.length != 4) {
            System.out.println("Usage: Phase_5.App <current_users_path> <available_tickets_path> <daily_transaction_path> <transaction_session_path>");
            System.exit(1);
        }

        // Avoid Duplicate Arguments
        ArrayList<String> argsList = new ArrayList<>();
        for (String arg : args) {
            if (argsList.contains(arg)) {
                System.out.println("App: Duplicate Arguments Found");
                System.exit(1);
            }
            argsList.add(arg);
        }

        // Load in the current_users.txt file
        exit = readUserFile(args[0], exit);
        testTickets(args[1]);
        testTransaction(args[2]);
        try {
            Scanner scanner = new Scanner(new File(args[3]));
            String currentUsername = "";
            String currentAccountStatus = "";
            boolean authorize = false;
            
            while(authorize == false && !exit){
                System.out.print("Enter Username: ");
                currentUsername = scanner.nextLine();
                if (singleton.usernames.contains(currentUsername)){
                    System.out.println("Login Successful");
                    authorize = true;
                    int index = singleton.usernames.indexOf(currentUsername);
                    currentAccountStatus = singleton.accountStatuses.get(index);

                }else{
                    System.out.println("Invalid user\n");
                }
            }

            System.out.println();
            User user = new User(currentUsername, currentAccountStatus, args[1], scanner);
            user.getTransactions();
            String userInput;
            if (!exit) {
                System.out.print("Enter Transaction: ");
            }

            while(!exit) {
                userInput = scanner.nextLine().toLowerCase();

                if (userInput.equals("create")){
                    user.create(args[0]);
                }else if(userInput.equals("delete")){
                    user.delete(args[0]);
                }
                else if (userInput.equals("logout")){
                    user.logout(args[2]);
                    exit = true;
                    break;
                }
                else if (userInput.equals("post")) {
                    user.post();
                }
                else if (userInput.equals("search")) {
                    user.search();
                }
                else if (userInput.equals("rent")) {
                    user.rent();
                }

                System.out.println();
                user.getTransactions();
                System.out.print("Enter Transaction: ");
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("App: Transaction Session File Not Found");
        }
    } 
    
    public static boolean readUserFile(String userPath, boolean exit) {
        int blankIndex = 10;
        try {
            File file = new File(userPath);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                if (line.length() != 13) {
                    System.out.println("App: Invalid Current Users File");
                    exit = true;
                } else {
                    for(int i=0; i<line.length(); i++){
                        if (line.charAt(i) == ' '){
                            blankIndex = i;
                            i = line.length();
                        }
                    }
                    singleton.usernames.add(line.substring(0, blankIndex));
                    singleton.accountStatuses.add(line.substring(11, 13));
                }
            }
            fscanner.close();
          } catch (IOException e) {
            System.out.println("App: Current Users File Not Found");
            exit = true;
        }
        return exit;
    }

    private static void testTickets(String rentalFile) {
        try {
            File file = new File(rentalFile);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                if (line.length() != 49) {
                    System.out.println("App: Invalid Available Tickets File");
                    System.exit(1);
                }
            }
            fscanner.close();
        } catch (IOException e) {
            System.out.println("App: Available Tickets File Not Found");
            System.exit(1);
        }
    }

    private static void testTransaction(String transactionFile) {
        try {
            File file = new File(transactionFile);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                if (line.length() != 53) {
                    System.out.println("App: Invalid Daily Transaction File");
                    System.exit(1);
                }
            }
            fscanner.close();
        } catch (IOException e) {
            System.out.println("App: Daily Transaction File Not Found");
            System.exit(1);
        }
    }
}
