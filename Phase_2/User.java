package Phase_2;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Objects;
import java.io.IOException;

public class User{
    private String userName;
    private String accountStatus;
    private String daily_transaction;
    public static Singleton singleton = Singleton.getInstance();

    public User(String userName, String accountStatus){
        this.userName = userName;
        this.accountStatus = accountStatus;
    }

    public void getTransactions(){
        if(this.accountStatus.equals("Admin")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Create");
            System.out.println("Delete");
            System.out.println("Post");
            System.out.println("Search");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("Full-Standard")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Post");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("Rent-Standard")){
            System.out.println("Logout");
            System.out.println("Rent\n");
        }
        else if(this.accountStatus.equals("Post-Standard")){
            System.out.println("Logout");
            System.out.println("Post\n");
        }
    }

    public void create(){
        boolean validUsername = false;
        Scanner scanner = new Scanner(System.in);
        String userInput1;
        String userInput2;
        JSONParser jsonParser = new JSONParser();
        JSONObject userDetails = new JSONObject();
        JSONObject userObject = new JSONObject();

        try(FileReader fileReader = new FileReader("Phase_2/Files/current_users.json")){
            Object obj = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) obj;

            while(validUsername == false){
                System.out.println("\nEnter new username:");
                userInput1 = scanner.nextLine();
    
                if (singleton.usernames.contains(userInput1)){
                    System.out.println(userInput1 + "already exists.\n");
                }else{
                    singleton.usernames.add(userInput1);
                    System.out.println("\nEnter account status:");
                    userInput2 = scanner.nextLine();
                    singleton.accountStatuses.add(userInput2);
    
                    userDetails.put("username", userInput1);
                    userDetails.put("accountStatus", userInput2);
                    userObject.put("user", userDetails);
    
                    jsonArray.add(userObject);
    
                    FileWriter fileWriter = new FileWriter("Phase_2/Files/current_users.json");
                    fileWriter.write(jsonArray.toJSONString());
                    fileWriter.flush();
    
                    fileWriter.close();
                    scanner.close();
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        boolean validUsername = false;
        Scanner scanner = new Scanner(System.in);
        int index;
        JSONParser jsonParser = new JSONParser();
        
        while(validUsername == false){
            System.out.println("\nEnter username:");
            String userInput;
            userInput = scanner.nextLine();

            if (singleton.usernames.contains(userInput)){
                index = singleton.usernames.indexOf(userInput);
                singleton.usernames.remove(index);
                singleton.accountStatuses.remove(index);

                try(FileReader fileReader = new FileReader("Phase_2/Files/current_users.json")){
                    Object obj = jsonParser.parse(fileReader);
                    JSONArray jsonArray = (JSONArray) obj;
                    jsonArray.remove(index);

                    FileWriter fileWriter = new FileWriter("Phase_2/Files/current_users.json");
                    fileWriter.write(jsonArray.toJSONString());
                    fileWriter.flush();
    
                    fileWriter.close();
                    scanner.close();
                }catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
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
    public void post(String city, float price, int num_brooms){
        //post will create a rent object and add it to the list of rental properties, then writes it to the rent file

        JSONArray rentList = readRentals();
        int id_count = rentList.size();

        // create jason object to store written file as a dictionary
        JSONObject obj = new JSONObject();
        obj.put("city",city);
        obj.put("price", price);
        obj.put("num_of_bedrooms", num_brooms);
        obj.put("rented", false);
        //increment id_count when adding the field for each rental unit
        id_count+=1;
        obj.put("ID", id_count);

        // add the new rental object to the array
        rentList.add(obj);
        try(FileWriter fileWriter = new FileWriter("Phase_2/Files/available_rental_unit.json")){
            fileWriter.write(rentList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void search(String city, double price, Long num_broom){
        // Serach will read the list of rent properties and will display the id and price of properties available
        JSONArray rentList = readRentals();
        //JSONArray resultList = new JSONArray();
        int id_count = rentList.size();

        for(int x =0; x < id_count; x++){
            JSONObject obj = (JSONObject)rentList.get(x);
            //fcity, fprice, fnob, frent, fID are the fields of the objects
            String fcity= (String) obj.get("city");
            Double fprice= (Double) obj.get("price");
            Long fnob = (Long) obj.get("num_of_bedrooms");
            boolean frent = (boolean) obj.get("rented");
            Long fID = (Long) obj.get("ID");
            if((Objects.equals(city, fcity)) && (price == fprice) && (Objects.equals(num_broom, fnob)) && (frent == false)){
                System.out.println("property "+ fID+ "in, "+ city+" with"+ fnob+ "meets the description" );

            }
        }


    }
    public void rent(int id, int num_nights){
        //check to see if the property exist
        //if it does, display the poperty cost per night
        //and total cost overall for the amount of nights (num_nights * cost)
        JSONArray rentList = readRentals();
        //JSONArray resultList = new JSONArray();
        int id_count = rentList.size();
        Double fprice =0.0;
        boolean frent = false;
        Long fID = new Long(0);
        JSONObject obj = new JSONObject();
        int x =0;

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
                System.out.println("property is not available for rent");
                break;
            }
        }

        if(num_nights < 15 && num_nights >0 && (!frent))
        {
            double cost = num_nights * fprice;
            System.out.println("property "+ fID+ " cost per night : "+ fprice);
            System.out.println("with the number of nights, the total cost is: "+ cost);
            System.out.println("Do you want to rent? Yes or No");
            //Scanner scanner = new Scanner(System.in);
            String input = "yes";
            if( input.equals("yes")){
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

            }
        }
        else if (num_nights > 14){
            System.out.println("The amount of nights exceeds the maximum allowed nights of 14");
        }





    }


    public String toString(){
        return "Current User: "+userName + "\n" + "Account Status: "+accountStatus;
    }
}