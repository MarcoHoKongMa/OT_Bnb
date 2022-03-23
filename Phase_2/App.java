package Phase_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 

public class App {
    public static Singleton singleton = Singleton.getInstance();
    public static void main(String[] args){

        // Check For Sufficient Arguments
        if (args.length != 3) {
            System.out.println("Usage: Phase_2.App <current_users_path> <available_tickets_path> <daily_transaction_path>");
            System.exit(1);
        }

        // Load in the current_users.txt file
        readUserFile(args[0]);
        Scanner scanner = new Scanner(System.in);
        String currentUsername = "";
        String currentAccountStatus = "";
        boolean authorize = false;
        
        while(authorize == false){
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
        System.out.print("Enter Transaction: ");
        boolean exit = false;
        String userInput;

        while(exit == false) {
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
    } 
    
    private static void readUserFile(String userPath){
        int blankIndex = 0;
        try {
            File file = new File(userPath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() != 13) {
                    System.out.println("App: Invalid Current Users File");
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
            scanner.close();
          } catch (FileNotFoundException e) {
            System.out.println("App: Current Users File Not Found");
            System.exit(1);
          }
    }
}
