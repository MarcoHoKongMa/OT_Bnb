package Phase_3;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner; 

public class App {
    public static Singleton singleton = Singleton.getInstance();
    public static void main(String[] args){

        // Empty Output File
        try {
            FileWriter errWriter = new FileWriter("Phase_3/output.txt");
            errWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get File Inputs
        try {
            File inFile = new File("Phase_3/Create_Transaction/Test_Case1/input.txt");
            Scanner fScanner = new Scanner(inFile);
            while (fScanner.hasNextLine()) {
                String line = fScanner.nextLine();
                singleton.inputs.add(line);
            }
            fScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check For Sufficient Arguments
        if (args.length != 3) {
            System.out.println("Usage: Phase_2.App <current_users_path> <available_tickets_path> <daily_transaction_path>");
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
        readUserFile(args[0]);
        testTickets(args[1]);
        testTransaction(args[2]);
        Scanner scanner = new Scanner(System.in);
        String currentUsername = "";
        String currentAccountStatus = "";
        boolean authorize = false;
        
        while(authorize == false){
            System.out.print("Enter Username: ");
            currentUsername = singleton.inputs.remove();
            if (singleton.usernames.contains(currentUsername)){
                singleton.printError("Login Successful");
                authorize = true;
                int index = singleton.usernames.indexOf(currentUsername);
                currentAccountStatus = singleton.accountStatuses.get(index);

            }else{
                singleton.printError("Invalid user\n");
                System.exit(1);
            }
        }

        System.out.println();
        User user = new User(currentUsername, currentAccountStatus, args[1], scanner);
        boolean exit = false;
        String userInput;

        while(exit == false) {
            userInput = singleton.inputs.remove().toLowerCase();

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
            else{
                singleton.printError("Invalid Transaction");
                System.exit(1);
            }

            System.out.println();
        }

        scanner.close();
    } 
    
    private static void readUserFile(String userPath) {
        int blankIndex = 0;
        try {
            File file = new File(userPath);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                if (line.length() != 13) {
                    System.out.println("App: Invalid Current Users File");
                    System.exit(1);
                }
                for(int i=0; i<line.length(); i++){
                    if (line.charAt(i) == ' '){
                        blankIndex = i;
                        i = line.length();
                    }
                }
                singleton.usernames.add(line.substring(0, blankIndex));
                singleton.accountStatuses.add(line.substring(11, 13));
            }
            fscanner.close();
          } catch (IOException e) {
            System.out.println("App: Current Users File Not Found");
            System.exit(1);
          }
    }

    private static void testTickets(String rentalFile) {
        try {
            File file = new File(rentalFile);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                System.out.println(line.length());
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
