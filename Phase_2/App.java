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
        
        System.out.println("Enter Username:");
        while(authorize == false){
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
        User user = new User(currentUsername, currentAccountStatus);
        user.getTransactions();

        System.out.println("Enter Transaction:");
        boolean exit = false;
        String userInput;

        while(exit == false){
            userInput = scanner.nextLine();

            if (userInput.equals("Create")){
                user.create();
            }else if(userInput.equals("Delete")){
                user.delete();
            }
            else if (userInput.equals("Logout")){
                exit = true;
            }
        }

        // note each time you run main, its will post these properties to the file
        //user.post("Oshawa",500f,4);
        //user.post("Oshawa",500f,9);
        user.search("Oshawa",500.0, 4L);
        user.rent(4,2);

    } 
    
    private static void parseJSONArray(JSONObject user){
        JSONObject userObj = (JSONObject) user.get("user");
        singleton.usernames.add((String) userObj.get("username"));
        singleton.accountStatuses.add((String) userObj.get("accountStatus"));
    }
}
