package Phase_5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.io.*;

public class Stub {
    private static Queue<String> transactions = new LinkedList<>();
    private static Queue<String> users = new LinkedList<>();
    private static Queue<String> postUsers = new LinkedList<>();
    private static Queue<String> postings = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        // Check For Sufficient Arguments
        if (args.length != 1) {
            System.out.println("Usage: Phase_5.Stub <daily_transaction_path>");
            System.exit(1);
        }

        // Read & Test Mock Daily Transaction File
        testTransaction(args[0]);

        // Produce Mock Transactions
        while (!(transactions.isEmpty())) {
            // Create, Delete, Post, Rent
            String trans = transactions.poll();
            Random rand = new Random();
            String path = "Phase_5//Files//current_users.txt";
            int counter = 0;
            

            if (trans.substring(0, 2).equals("01")){
                String[] randomNames = {"Bob", "Jake", "Henry", "Jeffrey", "Richard"};
                String[] status = {"AA", "FS", "BS", "PS"};
                String account = "01 ";
                int randomName = rand.nextInt(5);
                int randomStatus = rand.nextInt(4);

                account+=randomNames[randomName];
                while(account.length() < 11) {
                    account+=" ";
                }
                account+=status[randomStatus];

                users.add(account);

                if (status[randomStatus].equals("PS") || status[randomStatus].equals("AA") || status[randomStatus].equals("FS")){
                    postUsers.add(account);
                }

                // Write to the current_users.txt file
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
                if (new File(path).length() != 0){
                    writer.newLine();
                }
                writer.write(account);
                writer.flush();
                writer.close();
            }
            else if(trans.substring(0, 2).equals("02")){
                // Assuming the delete transaction is not the first transaction to occur
                String account = "02 ";
                account+=users.poll().substring(3, 16);
                // Write to the current_users.txt file
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
                if (new File(path).length() != 0){
                    writer.newLine();
                }
                writer.write(account);
                writer.flush();
                writer.close();
            }
            else if(trans.substring(0, 2).equals("03")){
                String account = "03 ";
                account+=postUsers.poll().substring(3, 16)+" 0000000"+String.valueOf(counter)+" Oshawa          2 300.00 00";
                counter++;

                postings.add(account);

                BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
                if (new File(path).length() != 0){
                    writer.newLine();
                }
                writer.write(account);
                writer.flush();
                writer.close();
            }
            else if(trans.substring(0, 2).equals("05")){
                String account = "05 ";
                account+=postings.poll().substring(3, 53);

                BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
                if (new File(path).length() != 0){
                    writer.newLine();
                }
                writer.write(account);
                writer.flush();
                writer.close();
            }

        }
        
    }

    private static void testTransaction(String transactionFile) {
        try {
            File file = new File(transactionFile);
            Scanner fscanner = new Scanner(file);
            while (fscanner.hasNextLine()) {
                String line = fscanner.nextLine();
                if (line.length() != 53) {
                    System.out.println("Stub: Invalid Daily Transaction File");
                    System.exit(1);
                }
                transactions.add(line);
            }
            fscanner.close();
        } catch (IOException e) {
            System.out.println("Stub: Daily Transaction File Not Found");
            System.exit(1);
        }
    }
}