package Phase_2;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import javax.management.Query;
import javax.swing.text.StyledEditorKit.BoldAction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class User {
    private String userName;
    private String accountStatus;
    private ArrayList<Long> newRentals = new ArrayList<>();
    private Queue<String> daily_transaction = new LinkedList<>();
    public static Singleton singleton = Singleton.getInstance();
    private Scanner scanner;

    public User(String userName, String accountStatus, Scanner scanner) {
        this.userName = userName;
        this.accountStatus = accountStatus;
        this.scanner = scanner;
    }

    public void getTransactions() {
        if(this.accountStatus.equals("AA")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Create");
            System.out.println("Delete");
            System.out.println("Post");
            System.out.println("Search");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("FS")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Post");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("RS")){
            System.out.println("Logout");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("PS")){
            System.out.println("Logout");
            System.out.println("Post\n");
        }
    }

    public void logout() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Phase_2/Files/daily_transaction.txt", true));
            while (!(daily_transaction.isEmpty())) {
                if (new File("Phase_2/Files/daily_transaction.txt").length() != 0){
                    bufferedWriter.newLine();
                    bufferedWriter.write(daily_transaction.remove());
                    bufferedWriter.flush();
                }else{
                    bufferedWriter.write(daily_transaction.remove());
                    bufferedWriter.flush();
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Unable To Save Daily Transactions");
        }
    }

    public void create() {
        boolean validUsername = false;
        boolean validAccountStatus = false;
        String userInput1="";
        String userInput2="";
        String account;
        String dailyTransactionString = "";

        try(FileReader fileReader = new FileReader("Phase_2/Files/current_users.txt")){
            // Provide user name and check to see if it exists
            while(validUsername == false){
                System.out.print("\nEnter New Username: ");
                userInput1 = scanner.nextLine();
    
                if (singleton.usernames.contains(userInput1)){
                    System.out.println(userInput1 + "already exists.\n");
                }
                else if (userInput1.length() > 15) {
                    System.out.println("Username Can Only Have 15 Characters At Most");
                }
                else if (userInput1.isEmpty() || userInput1.isBlank()) {
                    System.out.println("No Username Provided");
                }
                else{
                    singleton.usernames.add(userInput1);
                    validUsername = true;
                }
            }

            while(validAccountStatus == false){
                System.out.print("\nEnter Account Status: ");
                    userInput2 = scanner.nextLine().toUpperCase();
                    
                    if (userInput2.toLowerCase().equals("ADMIN") || userInput2.toUpperCase().equals("AA")){
                        singleton.accountStatuses.add("AA");
                        validAccountStatus = true;
                    }
                    else if (userInput2.toLowerCase().equals("FULL-STANDARD") || userInput2.toUpperCase().equals("FS")){
                        singleton.accountStatuses.add("FS");
                        validAccountStatus = true;
                    }
                    else if (userInput2.toLowerCase().equals("RENT-STANDARD") || userInput2.toUpperCase().equals("RS")){
                        singleton.accountStatuses.add("RS");
                        validAccountStatus = true;
                    }
                    else if (userInput2.toLowerCase().equals("POST-STANDARD") || userInput2.toUpperCase().equals("PS")){
                        singleton.accountStatuses.add("PS");
                        validAccountStatus = true;
                    }
            }

            account = userInput1;
            while(account.length() < 13){
                account+=" ";
            }
            account+=userInput2;
            
            // Write to the current_users.txt file
            BufferedWriter writer = new BufferedWriter(new FileWriter("Phase_2/Files/current_users.txt", true));
            if (new File("Phase_2/Files/current_users.txt").length() != 0){
                writer.newLine();
            }
            writer.write(account);
            writer.flush();
            writer.close();
            
            System.out.println("\nAdded " + userInput1 + "(" + userInput2 + ")" +" to OT Bnb");
            
            // Write to daily_transaction.txt file
            dailyTransactionString = "01 "+account;

            while(dailyTransactionString.length()<53){
                dailyTransactionString+=" ";
            }
            daily_transaction.add(dailyTransactionString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String currentUser) {
        int index;
        List<String> lines = new ArrayList<String>();
        System.out.print("\nDelete User(Username): ");
        String userInput;
        userInput = scanner.nextLine();
        String dailyTransactionString = "";

        if (singleton.usernames.contains(userInput) && !(userInput.equals(currentUser))){
            index = singleton.usernames.indexOf(userInput);
            singleton.usernames.remove(index);

            try(FileReader fileReader = new FileReader("Phase_2/Files/current_users.txt")){

                // Remove the user from the text file
                lines = Files.readAllLines(Paths.get("Phase_2/Files/current_users.txt"));
                lines.remove(index);

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Phase_2/Files/current_users.txt"));
                for(int i=0; i<lines.size(); i++){
                    if (new File("Phase_2/Files/current_users.txt").length() != 0){
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.write(lines.get(i));
                    bufferedWriter.flush();
                }
                bufferedWriter.close();
                

                // Remove Rental Units From Deleted User
                Queue<JSONObject> delObj = new LinkedList<>();
                JSONArray rentList = readRentals();
                int rental_count = rentList.size();
                String fUser;
                for (int i = 0; i < rental_count; i++) {
                    JSONObject rObj = (JSONObject)rentList.get(i);
                    fUser = (String) rObj.get("owner");
                    if (userInput.equals(fUser)) {
                        delObj.add(rObj);
                    }
                }
                while (!(delObj.isEmpty())) {
                    rentList.remove(delObj.remove());
                }
                try(FileWriter rentalWriter = new FileWriter("Phase_2/Files/available_rental_unit.json")){
                    rentalWriter.write(rentList.toJSONString());
                } catch (IOException e) {
                    System.out.println("Failed To Update Deleted Rental Units");
                }

                System.out.println("Deleted " + userInput + " to OT Bnb");
                
                
                // Save To Daily Transaction File
                dailyTransactionString = "02 ";
                while(userInput.length() < 13){
                    userInput+=" ";
                }
                dailyTransactionString+=userInput;
                dailyTransactionString+=singleton.accountStatuses.get(index);
                singleton.accountStatuses.remove(index);
                while(dailyTransactionString.length()<53){
                    dailyTransactionString+=" ";
                }
                daily_transaction.add(dailyTransactionString);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (userInput.equals(currentUser)) {
            System.out.println("Deleting Yourself Is Not Prohibited");
        }
        else {
            System.out.println(userInput + " is NOT A USER");
        }
    }

    public JSONArray readRentals() {
        // read the array from the available_rental_unit.json files and return it
        JSONArray rentList = new JSONArray();
        try (FileReader fileReader = new FileReader("Phase_2/Files/available_rental_unit.json")) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            // Transfer all the data in the object into a JSONArray
            rentList = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rentList;
    }

    //post takes in city, rental price, number of bedrooms
    public void post(String currentUser) {
        //post will create a rent object and add it to the list of rental properties, then writes it to the rent file
        String city;
        float price;
        int num_brooms;
        System.out.print("Enter Rental City: ");
        city = scanner.nextLine();
        System.out.print("Enter Rental Price: ");
        price = Float.parseFloat(scanner.nextLine());
        System.out.print("Enter Avaliable Bedrooms: ");
        num_brooms = Integer.parseInt(scanner.nextLine());

        JSONArray rentList = readRentals();
        int id_count = rentList.size();

        // create jason object to store written file as a dictionary
        JSONObject obj = new JSONObject();
        obj.put("city",city);
        obj.put("price", price);
        obj.put("num_of_bedrooms", num_brooms);
        obj.put("rented", false);
        obj.put("owner", currentUser);

        // Insert Into Unused ID Or Create New ID If Not Avaliable
        int insert_id = 1;
        long fID;
        for (int i = 0; i < id_count; i++) {
            JSONObject rObj = (JSONObject)rentList.get(i);
            fID = (long) rObj.get("ID");
            if ((long) insert_id < fID) {
                break;
            }
            insert_id++;
        }
        obj.put("ID", insert_id);

        // add the new rental object to the array
        rentList.add(obj);
        try(FileWriter fileWriter = new FileWriter("Phase_2/Files/available_rental_unit.json")){
            fileWriter.write(rentList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        newRentals.add((long)(id_count + 1));
        System.out.println("Please note that transactions cannot be accepted until you next session");

        // Save To Daily Transaction File
        daily_transaction.add("Posted " + "Rental ID #"+ (id_count + 1) + ": "+ num_brooms + " Bedrooms, Location: " + city + ", Price Per Night: $" + price);
    }

    public void search() {
        // Serach will read the list of rent properties and will display the id and price of properties available

        // Request City, Price, Number of bedrooms
        String city;
        double price = 0.0;
        boolean wildPrice = false;
        long num_broom = 0;
        boolean wildBroom = false;
        System.out.println("\n* can be used as wildcard value or as \'All\'");
        System.out.print("Search City Name: ");
        city = scanner.nextLine();
        System.out.print("Search Price(Maximum): ");
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            wildPrice = true;
        }
        System.out.print("Search Number of Bedrooms(Minimum): ");
        try {
            num_broom = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            wildBroom = true;
        }

        JSONArray rentList = readRentals();
        //JSONArray resultList = new JSONArray();
        int id_count = rentList.size();

        System.out.print("\nSearch Result for ");
        if (wildBroom) { System.out.print("All Number of"); } else { System.out.print(num_broom); }
        System.out.print(" Bedrooms at ");
        if (wildPrice) { System.out.print("All Prices"); } else { System.out.print("$" + price); }
        System.out.print(" in ");
        if (city.equals("*")) { System.out.println("All Cities"); } else { System.out.println(city); }

        String result;
        for(int x =0; x < id_count; x++){
            JSONObject obj = (JSONObject)rentList.get(x);
            //fcity, fprice, fnob, frent, fID are the fields of the objects
            String fcity= (String) obj.get("city");
            Double fprice= (Double) obj.get("price");
            Long fnob = (Long) obj.get("num_of_bedrooms");
            boolean frent = (boolean) obj.get("rented");
            Long fID = (Long) obj.get("ID");
            if((Objects.equals(city, fcity) || city.equals("*")) && ((price >= fprice) || wildPrice) && ((num_broom <= fnob) || wildBroom) && (frent == false) && !(newRentals.contains(fID))){
                result = "Rental ID #"+ fID+ ": "+ fnob + " Bedrooms, Location: " + fcity + ", Price Per Night: $" + fprice;
                System.out.println(result);
                // Save To Daily Transaction File
                daily_transaction.add("Searched " + result);
            }
        }
        System.out.print("\nPress Enter to continue");
        scanner.nextLine();
    }

    public void rent() {
        //check to see if the property exist
        //if it does, display the poperty cost per night
        //and total cost overall for the amount of nights (num_nights * cost)

        // Request Rental Id & Number of nights
        int id = 0;
        int num_nights = 0;
        do {
            try {
                System.out.print("Insert Rental ID: ");
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("INVALID RENTAL ID");
            }
        } while (id < 1);
        do {
            try {
                System.out.print("Number of nights you will be staying: ");
                num_nights = Integer.parseInt(scanner.nextLine());
                if (num_nights < 1 || num_nights > 14) {
                    System.out.println("Rental Only Avaliable between 1-14 nights");
                }
            } catch (NumberFormatException e) {
                System.out.println("NEED A VALID NUMBER");
            }
        } while (num_nights < 1 || num_nights > 14);

        JSONArray rentList = readRentals();
        //JSONArray resultList = new JSONArray();
        int id_count = rentList.size();
        Double fprice =0.0;
        boolean frent = false;
        Long fID = 0L;
        JSONObject obj = new JSONObject();
        int x =0;

        if (id > id_count) {
            System.out.println("\nGiven Rental ID Does Not Exists");
            return;
        }

        for(x =0; x < id_count; x++){
            obj = (JSONObject)rentList.get(x);
            //fcity, fprice, fnob, frent, fID are the fields of the objects
            //String fcity= (String) obj.get("city");
            fprice= (Double) obj.get("price");
            //Long fnob = (Long) obj.get("num_of_bedrooms");
            frent = (boolean) obj.get("rented");
            fID = (Long) obj.get("ID");
            if((id == fID) && (!frent)){
               break;
            }
            if((id == fID) && (frent)){
                System.out.println("\nProperty is not available for rent");
                return;
            }
            if((id == fID) && newRentals.contains(fID)){
                System.out.println("\nNew Rentals Are Not Avaliable Until Your Next Session");
                return;
            }
        }

        if(!frent) {
            double cost = num_nights * fprice;
            System.out.println("\nRental #"+ fID+ ", Cost Per Night: $"+ fprice);
            System.out.println("Your Total Cost For " + num_nights + " Nights: $"+ cost);
            System.out.println("Confirm Rental? y/n");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("y")){
                frent = true;
                obj.remove("rented");
                obj.put("rented", frent);
                rentList.set(x,obj);
                // rewrite the file
                try(FileWriter fileWriter = new FileWriter("Phase_2/Files/available_rental_unit.json")){
                    fileWriter.write(rentList.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String rent = "Rented Rental #" + id + " For " + num_nights + " Nights From " + obj.get("owner");
                System.out.println("You Have " + rent);
                // Save To Daily Transaction File
                daily_transaction.add(rent);
            }
        }
    }

    public String toString() {
        return "Current User: "+userName + "\n" + "Account Status: "+accountStatus;
    }
}