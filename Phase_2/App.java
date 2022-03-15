package Phase_2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.Scanner; 

public class App {
    public static Singleton singleton = Singleton.getInstance();
    public static void main(String[] args){

        // Load in the current_users.json file
        try(FileReader fileReader = new FileReader("Phase_2/Files/current_users.json")){
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            // Transfer all the data in the object into a JSONArray
            JSONArray usersList = (JSONArray) obj;

            // Iterate over the usersList
            usersList.forEach(record -> parseJSONArray((JSONObject)record));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        User user = new User(currentUsername, currentAccountStatus, scanner);
        user.getTransactions();
        System.out.print("Enter Transaction: ");
        boolean exit = false;
        String userInput;

        while(exit == false) {
            userInput = scanner.nextLine().toLowerCase();

            if (userInput.equals("create")){
                user.create();
            }else if(userInput.equals("delete")){
                user.delete(currentUsername);
            }
            else if (userInput.equals("logout")){
                user.logout();
                exit = true;
                break;
            }
            else if (userInput.equals("post")) {
                user.post(currentUsername);
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

        // note each time you run main, its will post these properties to the file
        // user.post("Oshawa",500f,4);
        // user.post("Oshawa",500f,9);
        // user.search("Oshawa",500.0, 4L);
        // user.rent(2,2);
        scanner.close();
    } 
    
    private static void parseJSONArray(JSONObject user){
        JSONObject userObj = (JSONObject) user.get("user");
        singleton.usernames.add((String) userObj.get("username"));
        singleton.accountStatuses.add((String) userObj.get("accountStatus"));
    }
}
