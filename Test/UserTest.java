package Test;

import Phase_4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserTest {

    private final PrintStream standardOutput = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @ParameterizedTest
    @ValueSource(strings = {"AA", "FS", "RS", "PS"})
    public void getTransactionsTest(String accountStatus) {
        System.setIn(new  ByteArrayInputStream("X".getBytes()));
        User user = new User("Ting", accountStatus, "Phase_4/Files/available_tickets.txt", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Available Transactions:\nLogout\nCreate\nDelete\nPost\nSearch\nRent", outputStreamCaptor.toString().trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOutput);
    }
}
