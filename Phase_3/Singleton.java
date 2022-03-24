package Phase_3;

import java.util.*;
import java.io.*;

public class Singleton {
    private static Singleton single_instance = null;

    public ArrayList<String> usernames;
    public ArrayList<String> accountStatuses;
    public Queue<String> inputs = new LinkedList<>();

    // Private Constructor
    private Singleton(){
        usernames = new ArrayList<>();
        accountStatuses = new ArrayList<>();
    }

    public static Singleton getInstance(){
        if (single_instance == null) {
            single_instance = new Singleton();
        }
        return single_instance;
    }   

    public void printError(String error) {
        try {
            FileWriter errWriter = new FileWriter("Phase_3/output.txt", true);
            if (new File("Phase_3/output.txt").length() != 0) { errWriter.write("\n"); }
            errWriter.write(error);
            errWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
