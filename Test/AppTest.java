package Test;

import Phase_4.App;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {
    
    private final PrintStream standardOutput = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void readUserFileZeroLoopWrongPathTest() {
        App.readUserFile("non.txt", false);
        Assertions.assertEquals("App: Current Users File Not Found", outputStreamCaptor.toString().trim());
    }

    @Test
    public void readUserFileZeroLoopInvalidTest() {
        App.readUserFile("D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Test\\current_users_invalid.txt", false);
        Assertions.assertEquals("App: Invalid Current Users File", outputStreamCaptor.toString().trim());
    }

    @Test
    public void readUserFileNoSpaceTest() {
        App.readUserFile("D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Test\\no_space_users.txt", false);
        Assertions.assertEquals("Tinggggggg", App.singleton.usernames.get(0));
    }

    @Test
    public void readUserFileOneLoopTest() {
        App.readUserFile("D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Phase_4\\Files\\current_users.txt", false);
        Assertions.assertEquals(1, App.singleton.usernames.size());
    }

    @Test
    public void readUserFileTwoLoopTest() {
        App.readUserFile("D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Test\\current_users_2.txt", false);
        Assertions.assertEquals(2, App.singleton.usernames.size());
    }

    @Test
    public void readUserFileNLoopTest() {
        App.readUserFile("D:\\Schools Document\\Year_3\\CSCI_3060U_Software_Quality_Assurance\\OT_Bnb\\Test\\current_users_5.txt", false);
        Assertions.assertEquals(5, App.singleton.usernames.size());
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(standardOutput);
        App.singleton.usernames.clear(); App.singleton.accountStatuses.clear();
    }
}
