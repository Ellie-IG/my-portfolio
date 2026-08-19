package se.kth.iv1350.bikerepairshop.logger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FileLoggerTest {
    private FileLogger fileLog;
    private String fileName;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now(); 
        String identifyingTime = String.valueOf(now.getDayOfYear()) + //
                "-" + String.valueOf(now.getHour()) + //
                "." + String.valueOf(now.getMinute()) + //
                "." + String.valueOf(now.getSecond());      
        fileName = "log" + identifyingTime + ".txt";
        fileLog = new FileLogger("TestFileLogger");
    }

    @AfterEach 
    public void tearDown(){
        //close the PrintWriter
    }

    @Test 
    public void rightFileCreated() {  
        // Reading File
        try {
            new File(fileName);
        } catch (Exception exc) {
            fail("The file was not created, or has the wrong name. It should be: " + fileName);
        }
        
    }

    @Test 
    public void canLogIntoFile(){
        fileLog.log("hello, this is some text\nCan it be read?");
        try {
            File Obj = new File("fileName");
            Scanner Reader = new Scanner(Obj);
          
            String allLogged = "";
            // Traversing File Data
          	while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                allLogged += data;
            }          
            Reader.close();
            assertEquals("hello, this is some text\\nCan it be read?", allLogged, "The correct textis not Logged");
        } catch (Exception exc) {
        }
    }
    
}

