package Phase_3;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

import java.nio.file.Files;
import java.nio.file.Paths;

public class User {
    private String userName;
    private String accountStatus;
    private String rentalFile;
    private ArrayList<Rental> rentals = new ArrayList<>();
    private Queue<String> daily_transaction = new LinkedList<>();
    public static Singleton singleton = Singleton.getInstance();
    private Scanner scanner;

    public User(String userName, String accountStatus, String rentalFile, Scanner scanner) {
        this.userName = userName;
        this.accountStatus = accountStatus;
        this.scanner = scanner;
        this.rentalFile = rentalFile; readRentals(rentals);
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

    public void logout(String transactionFile) {
        // Save Logout Transaction
        String username = userName;
        while (username.length() < 10) {
            username += " ";
        }
        int index = singleton.usernames.indexOf(userName);
        String endSession = "00 " + username + " " + singleton.accountStatuses.get(index);
        while (endSession.length() < 53) {
            endSession += " ";
        }
        daily_transaction.add(endSession);
        
        boolean saved = false;
        do {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(transactionFile, true));
                while (!(daily_transaction.isEmpty())) {
                    if (new File(transactionFile).length() != 0){
                        bufferedWriter.newLine();
                        bufferedWriter.write(daily_transaction.remove());
                        bufferedWriter.flush();
                    }else{
                        bufferedWriter.write(daily_transaction.remove());
                        bufferedWriter.flush();
                    }
                }
                bufferedWriter.close();
                saved = true;
            } catch (IOException e) {
                System.out.println("App: Failed To Save Daily Transactions");
                System.out.println("App: Daily Transaction File Not Found");
                System.out.print("Insert Valid Daily Transaction File Path: ");
                transactionFile = scanner.nextLine();
            }
        } while (!saved);
    }

    public void create(String userFile) {
        boolean validUsername = false;
        boolean validAccountStatus = false;
        String userInput1="";
        String userInput2="";
        String account;
        String dailyTransactionString = "";

        try(FileReader fileReader = new FileReader(userFile)){
            // Provide user name and check to see if it exists
            while(validUsername == false){
                System.out.print("\nEnter New Username: ");
                userInput1 = scanner.nextLine();
    
                if (singleton.usernames.contains(userInput1)){
                    System.out.println(userInput1 + "already exists.\n");
                    System.exit(1);
                }
                else if (userInput1.length() > 10) {
                    System.out.println("Username Can Only Have 10 Characters At Most");
                    System.exit(1);
                }
                else if (userInput1.isEmpty() || userInput1.isBlank()) {
                    System.out.println("No Username Provided");
                    System.exit(1);
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
                    else{
                        System.exit(1);
                    }
            }

            account = userInput1;
            while(account.length() < 11) {
                account+=" ";
            }
            account+=userInput2;
            
            // Write to the current_users.txt file
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true));
            if (new File(userFile).length() != 0){
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
            System.out.println("\nApp: Failed To Create " + userInput1 + " To OT Bnb");
            System.out.println("\nApp: Current Users File Not Found");
            System.out.println("Make Sure The Current Users File Is Present Before Running Create Transaction Again");
            System.exit(1);
        }
    }

    public void delete(String userFile) {
        int index;
        List<String> lines = new ArrayList<String>();
        System.out.print("\nDelete User(Username): ");
        String userInput;
        userInput = scanner.nextLine();
        String dailyTransactionString = "";

        if (singleton.usernames.contains(userInput) && !(userInput.equals(userName))){
            index = singleton.usernames.indexOf(userInput);
            singleton.usernames.remove(index);

            try(FileReader fileReader = new FileReader(userFile)){

                // Remove the user from the text file
                lines = Files.readAllLines(Paths.get(userFile));
                lines.remove(index);

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userFile));
                for(int i=0; i<lines.size(); i++){
                    if (new File(userFile).length() != 0){
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.write(lines.get(i));
                    bufferedWriter.flush();
                }
                bufferedWriter.close();
                
                // Remove Rental Units From Deleted User
                Queue<String> availableTickets = new LinkedList<>();
                String cUser;
                String cCity;
                int num_brooms;
                float cPrice;
                String rPrice;
                Boolean cStatus;
                String rStatus = "F";
                int cNon;
                String non;
                for (int j = 0; j < rentals.size(); j++) {
                    Rental rentUnit = rentals.get(j);
                    cUser = rentUnit.getOwner(); cCity = rentUnit.getCity(); num_brooms = rentUnit.getNumOfBedrooms();
                    while (cUser.length() < 10) {
                        cUser += " ";
                    }
                    while (cCity.length() < 15) {
                        cCity += " ";
                    }

                    cPrice = rentUnit.getPrice(); cStatus = rentUnit.getRented(); cNon = rentUnit.getNightRemain();
                    rPrice = String.format("%.02f", cPrice);
                    while (rPrice.length() < 6) {
                        rPrice = "0" + rPrice;
                    }
                    if (cStatus) { rStatus = "T"; }
                    non = Integer.toString(cNon);
                    if (non.length() < 2) {
                        non = "0" + non;
                    }
                    if (cUser.trim().equals(userInput)) {
                        // Save To Daily Transaction File
                        daily_transaction.add("02 " + cUser + " " + singleton.accountStatuses.get(index) + " " + rentUnit.getId() + " " + cCity + " " + num_brooms + " " + rPrice + " " + non);
                    }
                    else {
                        availableTickets.add(rentUnit.getId() + " " + cUser + " " + cCity + " " + num_brooms + " " + rPrice + " " + rStatus + " " + non);
                    }
                }
                readRentals(rentals);

                try {
                    BufferedWriter ticketWriter = new BufferedWriter(new FileWriter(rentalFile));
                    while (!(availableTickets.isEmpty())) {
                        ticketWriter.write(availableTickets.remove());
                        if(availableTickets.size() > 0) { ticketWriter.newLine(); }
                        ticketWriter.flush();
                    }
                    ticketWriter.close();
                } catch (FileNotFoundException e) {
                    System.out.println("\nFailed To Delete User's Rentals: File Not Found");
                    System.exit(1);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                System.out.println("\nDeleted " + userInput + " to OT Bnb");
                
                // Save To Daily Transaction File
                dailyTransactionString = "02 ";
                while(userInput.length() < 11){
                    userInput+=" ";
                }
                dailyTransactionString+=userInput;
                dailyTransactionString+=singleton.accountStatuses.get(index);
                singleton.accountStatuses.remove(index);
                while(dailyTransactionString.length()<53){
                    dailyTransactionString+=" ";
                }
                daily_transaction.add(dailyTransactionString);
            } catch (IOException e) {
                System.out.println("\nApp: Failed To Delete " + userInput + " To OT Bnb");
                System.out.println("\nApp: Current Users File Not Found");
                System.out.println("Make Sure The Current Users File Is Present Before Running Delete Transaction Again");
                System.exit(1);
            }
        }
        else if (userInput.equals(userName)) {
            System.out.println("\nDeleting Yourself Is Prohibited");
            System.exit(1);
        }
        else {
            System.out.println("\n" + userInput + " is NOT A USER");
            System.exit(1);
        }
    }

    public void readRentals(ArrayList<Rental> r) {
        // Read Available Rentals
        boolean validRentals = true;
        do {
            try {
                File file = new File(rentalFile);
                Scanner rScanner = new Scanner(file);
                Rental rent;
                String id;
                String owner;
                String city;
                int num_of_bedrooms;
                float price;
                boolean rented;
                int nights_remain;
                validRentals = true;
                while (rScanner.hasNextLine()) {
                    String line = rScanner.nextLine();
                    if (line.length() != 49) {
                        System.out.println("\nApp: Invalid Available Tickets File");
                        System.out.print("Insert Valid Tickets File Path(Or type \"exit\" if you would not wish to do so at the current time): ");
                        rentalFile = scanner.nextLine();
                        if (rentalFile.equals("exit")) { System.exit(1); }
                        validRentals = false;
                    }
                    id = line.substring(0, 8);
                    owner = line.substring(9, 19).trim();
                    city = line.substring(20, 35).trim();
                    num_of_bedrooms = Integer.parseInt(line.substring(36, 37));
                    price = Float.parseFloat(line.substring(38, 44));
                    if (line.substring(45,46).equals("T")) {
                        rented = true;
                    }
                    else {
                        rented = false;
                    }
                    nights_remain = Integer.parseInt(line.substring(47));
                    rent = new Rental(id, owner, city, num_of_bedrooms, price, rented, nights_remain);
                    r.add(rent);
                }
                rScanner.close();
            } catch (IOException e) {
                System.out.println("\nApp: Available Tickets File Not Found");
                System.out.print("Insert Valid Tickets File Path(Or type \"exit\" if you would not wish to do so at the current time): ");
                rentalFile = scanner.nextLine();
                if (rentalFile.equals("exit")) { System.exit(1); }
                validRentals = false;
            }
        } while (!validRentals);
    }

    //post takes in city, rental price, number of bedrooms
    public void post() {
        //post will create a rent object and add it to the list of rental properties, then writes it to the rent file
        String city;
        float price;
        int num_brooms;
        System.out.print("Enter Rental City: ");
        city = scanner.nextLine();
        do {
            System.out.print("Enter Rental Price: ");
            try {
                price = Float.parseFloat(scanner.nextLine());
                if (price < 0f || price > 999f) {
                    System.out.println("You Can Only Post Price Between $0 - $999");
                }
            } catch (NumberFormatException e) {
                System.out.println("INVALID SYNTAX(Numbers Only)");
                price = -1f;
            }
        } while (price < 0f || price > 999f);
        do {
            System.out.print("Enter Avaliable Bedrooms: ");
            try {
                num_brooms = Integer.parseInt(scanner.nextLine());
                if (num_brooms < 1 || num_brooms > 9) {
                    System.out.println("You Can Only Post 1 - 9 Bedrooms");
                }
            } catch (NumberFormatException e) {
                System.out.println("INVALID SYNTAX(Numbers Only)");
                num_brooms = 0;
            }
        } while (num_brooms < 1 || num_brooms > 9);

        // Create Unique Alpha-Numeric IDs
        ArrayList<Rental> updatedRental = new ArrayList<>();
        readRentals(updatedRental);
        ArrayList<String> rentalIDs = new ArrayList<>();
        for (int i = 0; i < updatedRental.size(); i++) {
            rentalIDs.add(updatedRental.get(i).getId());
        }
        StringBuilder id = new StringBuilder("00000000");
        while (rentalIDs.contains(id.toString())) {
            for (int i = id.length() - 1; i >= 0; i--) {
                if (id.charAt(i) == 'z') {
                    if (i == 0) {       //Rentals List Is Full
                        System.out.println("Unable To Post Rental: Rentals List Is FULL");
                        break;
                    }
                    id.setCharAt(i, '0');
                }
                else {
                    if (id.charAt(i) == '9') {
                        id.setCharAt(i, 'A');
                    }
                    else if (id.charAt(i) == 'Z') {
                        id.setCharAt(i, 'a');
                    }
                    else {
                        char c = id.charAt(i);
                        id.setCharAt(i, ++c);
                    }
                    break;
                }
            }
        }

        // Adding Space To Unused Fields
        String username = userName;
        while (username.length() < 10) {
            username += " ";
        }
        while (city.length() < 15) {
            city += " ";
        }
        String rPrice = String.format("%.02f", price);
        while (rPrice.length() < 6) {
            rPrice = "0" + rPrice;
        }

        // Update Available Rentals
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(rentalFile, true));
            if (new File(rentalFile).length() != 0){
                bufferedWriter.newLine();
                bufferedWriter.write(id.toString() + " " + username + " " + city + " " + num_brooms + " " + rPrice + " F 00");
                bufferedWriter.flush();
            }else{
                bufferedWriter.write(id.toString() + " " + username + " " + city + " " + num_brooms + " " + rPrice + " F 00");
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("\nApp: Failed To Post New Rental");
            System.out.println("\nApp: Available Tickets File Not Found");
            System.out.println("Make Sure The Available Tickets File Is Present Before Running Post Transaction Again");
            return;
        }
        System.out.println("\nPlease note that transactions on new rentals cannot be accepted until you next session");

        // Save To Daily Transaction File
        int index = singleton.usernames.indexOf(userName);
        daily_transaction.add("03 " + username + " " + singleton.accountStatuses.get(index) + " " + id.toString() + " " + city + " " + num_brooms + " " + rPrice + " 00");
    }

    public void search() {
        // Serach will read the list of rent properties and will display the id and price of properties available

        // Request City, Price, Number of bedrooms
        String city;
        float price = 0f;
        boolean wildPrice = false;
        int num_broom = 0;
        boolean wildBroom = false;
        System.out.println("\n* can be used as wildcard value or as \'All\'");
        System.out.print("Search City Name: ");
        city = scanner.nextLine();
        System.out.print("Search Price(Maximum): ");
        try {
            price = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            wildPrice = true;
        }
        System.out.print("Search Number of Bedrooms(Minimum): ");
        try {
            num_broom = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            wildBroom = true;
        }

        System.out.print("\nSearch Result for ");
        if (wildBroom) { System.out.print("All Number of"); } else { System.out.print(num_broom); }
        System.out.print(" Bedrooms at ");
        if (wildPrice) { System.out.print("All Prices"); } else { System.out.print("$" + price); }
        System.out.print(" in ");
        if (city.equals("*")) { System.out.println("All Cities"); } else { System.out.println(city); }

        String fID;
        String fcity;
        float fprice;
        int fnob;
        String username = userName;
        while (username.length() < 10) {
            username += " ";
        }
        String accStatus = singleton.accountStatuses.get(singleton.usernames.indexOf(userName));
        String rPrice;
        for (int i = 0; i < rentals.size(); i++) {
            Rental rent = rentals.get(i);
            if (!(rent.getRented())) {
                if ((rent.getCity().equals(city) || city.equals("*")) && (rent.getPrice() == price || wildPrice) && (rent.getNumOfBedrooms() == num_broom || wildBroom)) {
                    fID = rent.getId(); fcity = rent.getCity(); fprice = rent.getPrice(); fnob = rent.getNumOfBedrooms();
                    System.out.println("Rental ID #"+ fID+ ": "+ fnob + " Bedrooms, Location: " + fcity + ", Price Per Night: $" + fprice);

                    // Save To Daily Transaction File
                    while (fcity.length() < 15) {
                        fcity += " ";
                    }
                    rPrice = String.format("%.02f", fprice);
                    while (rPrice.length() < 6) {
                        rPrice = "0" + rPrice;
                    }
                    daily_transaction.add("04 " + username + " " + accStatus + " " + fID + " " + fcity + " " + fnob + " " + rPrice + " 00");
                }
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
        String id;
        int num_nights = 0;
        do {
            System.out.print("Insert Rental ID: ");
            id = scanner.nextLine();
            if (!(id.length() == 8)) {
                System.out.println("INVALID ID(Requires A 8 Characters Rental ID)");
            }
        } while (!(id.length() == 8));
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

        String fID;
        float fprice;
        for (int i = 0; i < rentals.size(); i++) {
            Rental rent = rentals.get(i);
            if (!(rent.getRented()) && rent.getId().equals(id)) {
                fID = rent.getId();
                fprice = rent.getPrice();
                float cost = fprice * (float) num_nights;
                System.out.println("\nRental #"+ fID+ ", Cost Per Night: $"+ fprice);
                System.out.println("Your Total Cost For " + num_nights + " Nights: $"+ cost);
                System.out.println("Confirm Rental? y/n");
                String input = scanner.nextLine().toLowerCase();
                if (input.equals("y")) {
                    // Update Rented Status & Number Of Nights
                    try {
                        String cUser;
                        String cCity;
                        int num_brooms;
                        float cPrice;
                        String rPrice;
                        Boolean cStatus;
                        String rStatus = "F";
                        int cNon;
                        String non;
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(rentalFile));
                        for (int j = 0; j < rentals.size(); j++) {
                            Rental rentUnit = rentals.get(j);
                            cUser = rentUnit.getOwner(); cCity = rentUnit.getCity(); num_brooms = rentUnit.getNumOfBedrooms();
                            while (cUser.length() < 10) {
                                cUser += " ";
                            }
                            while (cCity.length() < 15) {
                                cCity += " ";
                            }

                            if (rentUnit.getId() != fID) {
                                cPrice = rentUnit.getPrice(); cStatus = rentUnit.getRented(); cNon = rentUnit.getNightRemain();
                                rPrice = String.format("%.02f", cPrice);
                                while (rPrice.length() < 6) {
                                    rPrice = "0" + rPrice;
                                }
                                if (cStatus) { rStatus = "T"; }
                                non = Integer.toString(cNon);
                                if (non.length() < 2) {
                                    non = "0" + non;
                                }
                            }
                            else {
                                rPrice = String.format("%.02f", fprice);
                                while (rPrice.length() < 6) {
                                    rPrice = "0" + rPrice;
                                }
                                rStatus = "T";
                                non = Integer.toString(num_nights);
                                if (non.length() < 2) {
                                    non = "0" + non;
                                }
                                int index = singleton.usernames.indexOf(userName);
                                String username = userName;
                                while (username.length() < 10) {
                                    username += " ";
                                }
                                // Save To Daily Transaction File
                                daily_transaction.add("05 " + username + " " + singleton.accountStatuses.get(index) + " " + rent.getId() + " " + cCity + " " + num_brooms + " " + rPrice + " " + non);
                                System.out.println("\nYou Have Rented Rental #" + id + " For " + num_nights + " Nights From " + cUser);
                            }
                            bufferedWriter.write(rentUnit.getId() + " " + cUser + " " + cCity + " " + num_brooms + " " + rPrice + " " + rStatus + " " + non);
                            if (j < rentals.size() - 1) { bufferedWriter.newLine(); }
                            bufferedWriter.flush();
                        }
                        bufferedWriter.close();
                    } catch (IOException e) {
                        System.out.println("\nApp: Failed To Rent Rental #" + id);
                        System.out.println("\nApp: Available Tickets File Not Found");
                        System.out.println("Make Sure The Available Tickets File Is Present Before Running Rent Transaction Again");
                        return;
                    }
                }
                return;
            }
            else if (rent.getRented() && rent.getId().equals(id)) {
                System.out.println("Rental #" + id + " Is Currently Not Available");
                return;
            }
        }
        System.out.println("Rental #" + id + " Does Not Exist");
    }

    public String toString() {
        return "Current User: "+userName + "\n" + "Account Status: "+accountStatus;
    }
}