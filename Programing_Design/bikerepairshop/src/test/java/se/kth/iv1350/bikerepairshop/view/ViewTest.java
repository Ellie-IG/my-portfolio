package se.kth.iv1350.bikerepairshop.view;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.bikerepairshop.controller.Controller;
import se.kth.iv1350.bikerepairshop.data.Bike;
import se.kth.iv1350.bikerepairshop.dbhandler.CustomerRegistrySingleton;
import se.kth.iv1350.bikerepairshop.dbhandler.DataBaseConnection;
import se.kth.iv1350.bikerepairshop.dbhandler.RepairOrderRegistry;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.logger.ErrorMessageUserLogger;
import se.kth.iv1350.bikerepairshop.logger.FileLogger;

public class ViewTest {
    View view;
    String allLogged = "";

    private void connect(DataBaseConnection con){
        try {
            con.connect();
        } catch (DataBaseConnectionException exp) {
            FileLogger f = new FileLogger("Error");
            f.log("In the " + exp.getConnectionRegistry() + " the following error occured: " +  exp.getErrorMessage());
            ErrorMessageUserLogger e = new ErrorMessageUserLogger();
            e.log("The " + exp.getConnectionRegistry() + " could not be connected");
        }
    }

    private String getIndentifyingTime(){
        LocalDateTime now = LocalDateTime.now();      
        return String.valueOf(now.getDayOfYear()) + //
                "-" + String.valueOf(now.getHour()) + //
                "." + String.valueOf(now.getMinute()) + //
                "." + String.valueOf(now.getSecond());
    }

    private String readFromFile(String identifyingTime){
        try {
            allLogged = "";
            File Obj = new File("TestView" + identifyingTime + ".txt");
            Scanner Reader = new Scanner(Obj);
          
            
            // Traversing File Data
          	while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                allLogged += data;
            }          
            Reader.close();
            return allLogged;
        } catch (Exception exc) {
            fail("The file could not be found. The logger is thus not changed to FileLogger");
        }
        return "Nothing was read";
    }

    @BeforeEach
    public void setUp() {
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        CustomerRegistrySingleton customerReg = CustomerRegistrySingleton.getInstance();
        while(true) {
            connect(repOrderReg);
            connect(customerReg);
            if(repOrderReg.isConnect() && customerReg.isConnect()){
                break;
            }
        }

        // 2. Insert mock startup data (moved from your old View.main)
        try {
            Bike bike = new Bike(23490, "dam", "helkama");
            customerReg.addCustomer("0733976999", "ellen.gronholm@hotmail.com", "ellen", bike);
        } catch (DataBaseConnectionException exc) {
            new ErrorMessageUserLogger().log("Startup data error: " + exc.getErrorMessage());
        }

        // 3. Initialize Controller
        Controller contr = new Controller(repOrderReg, customerReg, new FileLogger("Error"));
        
        // 4. Initialize View and hand control over to it!
        view = new View(contr, new FileLogger("TestView"));
        view.runFakeExecution();

        // 5. Fix reader
        String identifyingTime = getIndentifyingTime();
        allLogged = readFromFile(identifyingTime);

    }

    @AfterEach
    public void tearDown() {
        view.setLogger(null);
        view = null;
    }

    @Test
    public void startsCorrect(){
        assertTrue(allLogged.contains("The program simulation has started in the View!"), "The views 'runFakeExecution' method is never started");
    }

    @Test
    public void customerIsFound(){
        assertTrue(allLogged.contains("//Find customer with the number 0733976999"), "The view never searched for customer");
        assertTrue(allLogged.contains("The system returned: se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO"), "The 'contr.findCustomerFromPhoneNumber() didn't return a DTO");
    }

    @Disabled //Don't know how to do it as it is a singleton
    @Test 
    public void customerNotFoundHandled(){
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        CustomerRegistrySingleton customerReg = CustomerRegistrySingleton.getInstance();
        while(true) {
            connect(repOrderReg);
            connect(customerReg);
            if(repOrderReg.isConnect() && customerReg.isConnect()){
                break;
            }
        }

        try {
            Bike bike = new Bike(23490, "dam", "helkama");
            customerReg.addCustomer("0733976960", "ellen.gronholm@hotmail.com", "ellen", bike);
        } catch (DataBaseConnectionException exc) {
            new ErrorMessageUserLogger().log("Startup data error: " + exc.getErrorMessage());
        }

        // 3. Initialize Controller
        Controller contr = new Controller(repOrderReg, customerReg, new FileLogger("Error"));
        
        // 4. Initialize View and hand control over to it!
        view = new View(contr, new FileLogger("TestViewFaulty"));
        view.runFakeExecution();

        // 5. Fix reader
        String identifyingTime = getIndentifyingTime();
        allLogged = readFromFile(identifyingTime);

        assertTrue(allLogged.contains("The Customer was not found. The data you entered was: 0733976999"), "The error message is not correct.");
        assertFalse(allLogged.contains("Customer Info:"), "The program continued even thoough the customer was not found.");
    }

    @Test 
    public void createdNewRepairOrder(){
        assertTrue(allLogged.contains("Created Repair Order ID:"), "The repairOrder was not created.");
        assertTrue(allLogged.contains("Created Repair Order ID: 0"), "The incorrect repairOrder was created.");
    }

    @Test 
    public void faultsCouldBeAdded(){
        assertTrue(allLogged.contains("//Add the fault TIRE"), "The system did not start adding the Tire");
        assertTrue(allLogged.contains("Current bike faults:  - TIRE"), "The Tire fault was not added to the system");
        assertTrue(allLogged.contains("//Add the fault BRAKES"), "The system did not start adding the Brakes");
        assertTrue(allLogged.contains("Current bike faults:  - TIRE - BRAKES"), "The brakes fault was not added to the system");
    }

    @Test 
    public void faultListCanBeFinished(){
        assertTrue(allLogged.contains("//The list of observed faults is deemed complete"), "The fault list didn't start to be completed");
        assertTrue(allLogged.contains("Problem Description completed:"),"The fault list is not completed");
        assertTrue(allLogged.contains("Problem Description completed: Current bike faults:  - TIRE - BRAKES"),"The fault list is not completed");
    }

        @Test 
    public void diagnosticReportCouldBeAdded(){
        assertTrue(allLogged.contains("Diagnostic report started."), "The system did not add a diagnostic report");
        assertTrue(allLogged.contains("//The mechanic adds the observation TIRE"), "The system did not start adding the tire");
        assertTrue(allLogged.contains("Diagnostic Report:  -------------------------------Current bike faults:  - TIRE"), "The Tire fault was not added to the diagnostic report");
        assertTrue(allLogged.contains("//The mechanic adds the observation BRAKES"), "The system did not start adding the Brakes");
        assertTrue(allLogged.contains("Current bike faults:  - TIRE - BRAKES"), "The brakes fault was not added to the system");
    }

    @Test 
    public void diagnosticListCanBeFinished(){
        assertTrue(allLogged.contains("Diagnostic report completed:"),"The diagnostic report list is not completed");
        assertTrue(allLogged.contains("Diagnostic report completed: Current bike faults:  - TIRE - BRAKES"),"The fault list is not completed");
    }

    @Test 
    public void proposdRepairsCanBeAdded(){
        assertTrue(allLogged.contains("//The mechanic adds the fix to replace a tire"), "The system did not start adding the tire");
        assertTrue(allLogged.contains("Current recommended bike repairs:  - Replace tire: 	200	 total: 	200"), "The Tire repair was not added to the diagnostic report");
        assertTrue(allLogged.contains("//The mechanic adds to fix the chain"), "The system did not start adding 'to fix the brakes'");
        assertTrue(allLogged.contains("Current recommended bike repairs:  - Replace tire: 	200 - Fix chain: 	150	 total: 	350"), "The chain repair was not added to the system");

    }

    @Test 
    public void proposedRepairsCanBeFinished(){
        assertTrue(allLogged.contains("Proposed repairs completed:"),"The diagnostic report list is not completed");
        assertTrue(allLogged.contains("Proposed repairs completed: Current bike faults: - Replace tire- Fix chain"),"The fault list is not completed");
    }

    @Disabled
    @Test
    public void theCustomeCanAccept(){
        assertTrue(allLogged.contains("The repair order is shown to the customer:"), "The repair order is not initaited to be shown to customer");
        assertTrue(allLogged.contains("============Reciept============Customer Info: -------------------------------Name:		ellen" + //
                                    "Phone Number:	0733976999Email Address:	ellen.gronholm@hotmail.comBike:	 SerialNumber:	23490" + //
                                    "	 Model:		dam	 Brand:		helkamaProblem Description:  -------------------------------Current bike faults: " + //
                                    " - TIRE - BRAKESDiagnostic Report:  -------------------------------Current bike faults:  - TIRE - BRAKES" + //
                                    "Propossed Repairs:  -------------------------------Current recommended bike repairs:  - Replace tire: 	200 - Fix chain: 	150" + //
                                    "	 total: 	350===============================Date:		Sun May 31 13:33:33 CEST 2026ID:		0==============================="), "The repair order shown to the customer is incorrect");

    }
}
