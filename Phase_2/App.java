package Phase_2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.ArrayList;
import java.util.Scanner; 

public class App {
    public static ArrayList<String> usernames = new ArrayList<>();
    public static ArrayList<String> accountStatuses = new ArrayList<>();

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
            if (usernames.contains(currentUsername)){
                System.out.println("Login Successful");
                authorize = true;
                int index = usernames.indexOf(currentUsername);
                currentAccountStatus = accountStatuses.get(index);
                scanner.close();

            }else{
                System.out.println("Invalid user\n");
            }
        }

        System.out.println();
        User user = new User(currentUsername, currentAccountStatus);
        user.getTransactions();


    } 
    
    private static void parseJSONArray(JSONObject user){
        JSONObject userObj = (JSONObject) user.get("user");
        usernames.add((String) userObj.get("username"));
        accountStatuses.add((String) userObj.get("accountStatus"));
    }
}
