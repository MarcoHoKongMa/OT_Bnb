package Phase_3;

import java.util.ArrayList;

public class Singleton {
    private static Singleton single_instance = null;

    public ArrayList<String> usernames;
    public ArrayList<String> accountStatuses;

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


}
