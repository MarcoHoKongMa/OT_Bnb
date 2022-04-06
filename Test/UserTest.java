package Test;

import Phase_4.*;

import java.io.ByteArrayOutputStream;
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
    public void getTransactionsTest(String accountStatus){
        User user = new User("Ting", accountStatus, "sfsfs", new Scanner(System.in));
        user.getTransactions();
        Assertions.assertEquals("Available Transactions:\nLogout\nCreate\nDelete\nPost\nSearch\nRent\n", outputStreamCaptor.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOutput);
    }
}
