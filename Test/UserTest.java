package Test;

import Phase_4.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private final PrintStream standardOutput = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @Test
    public void getAATransactionTest() {
        System.setIn(new  ByteArrayInputStream("X".getBytes()));
        User user = new User("Ting", "AA", "D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Phase_4\\Files\\available_tickets.txt", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Available Transactions:\nLogout\nCreate\nDelete\nPost\nSearch\nRent", outputStreamCaptor.toString().trim());
    }

    @Test
    public void getFSTransactionTest() {
        System.setIn(new  ByteArrayInputStream("X".getBytes()));
        User user = new User("Ting", "FS", "D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Phase_4\\Files\\available_tickets.txt", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Available Transactions:\nLogout\nPost\nRent", outputStreamCaptor.toString().trim());
    }

    @Test
    public void getRSTransactionTest() {
        System.setIn(new  ByteArrayInputStream("X".getBytes()));
        User user = new User("Ting", "RS", "D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Phase_4\\Files\\available_tickets.txt", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Logout\nRent", outputStreamCaptor.toString().trim());
    }

    @Test
    public void getPSTransactionTest() {
        System.setIn(new  ByteArrayInputStream("X".getBytes()));
        User user = new User("Ting", "PS", "D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Phase_4\\Files\\available_tickets.txt", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Logout\nPost", outputStreamCaptor.toString().trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOutput);
    }
}
