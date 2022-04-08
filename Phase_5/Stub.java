package Phase_5;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

public class Stub {
    private static Queue<String> transactions = new LinkedList<>();

    public static void main(String[] args) {

        // Check For Sufficient Arguments
        if (args.length != 1) {
            System.out.println("Usage: Phase_5.Stub <daily_transaction_path>");
            System.exit(1);
        }

        // Read & Test Mock Daily Transaction File
        testTransaction(args[0]);

        // Produce Mock Transactions
        while (!(transactions.isEmpty())) {
            
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