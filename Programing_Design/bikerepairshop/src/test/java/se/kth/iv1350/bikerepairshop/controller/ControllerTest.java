package se.kth.iv1350.bikerepairshop.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.OperationFailedException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;
import se.kth.iv1350.bikerepairshop.logger.ErrorMessageUserLogger;
import se.kth.iv1350.bikerepairshop.logger.FileLogger;
import se.kth.iv1350.bikerepairshop.logger.LoggerBike;
import se.kth.iv1350.bikerepairshop.model.FaultInBike;
import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;
import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.dbhandler.CustomerRegistrySingleton;
import se.kth.iv1350.bikerepairshop.dbhandler.RepairOrderRegistry;
import se.kth.iv1350.bikerepairshop.data.*;

public class ControllerTest {
    private Controller controller;
    private RepairOrderRegistry repairOrderReg;
    private CustomerRegistrySingleton customerReg;
    private Bike bike;

    @BeforeEach
    public void setUp() {
        // We use the real objects (or stubs if the real ones require a DB connection)
        repairOrderReg = new RepairOrderRegistry();
        customerReg = CustomerRegistrySingleton.getInstance();
        while (true){
            try{ 
                customerReg.connect();
                repairOrderReg.connect();
                break;
            } catch (DataBaseConnectionException exp) {
            }
        }
        bike = new Bike(23490, "dam", "helkama");
        try {
            LoggerBike logger = new ErrorMessageUserLogger();
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            controller = new Controller(repairOrderReg, customerReg, logger);
        } catch (DataBaseConnectionException exp) {
        }
    }

    @AfterEach 
    public void tearDown() {
        bike = null;
        repairOrderReg = null;
        customerReg = null;
        controller = null;
    }

    @Test 
    public void canFindCustomerFromPhone(){
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "ellen", bike);
            CustomerDTO temp = customerReg.findCustomerFromPhoneNumberCR("0733976993");
            assertNotNull(temp, "No customer is returned when it should be.");
        } catch (ObjectNotFoundException exp) {
            fail("An Object not found error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exp) {
            fail("An DataBasee error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }
    
    @Test 
    public void canHandleNotFoundCustomerFromPhone(){
        try {
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.findCustomerFromPhoneNumberCR("0733976996");
        } catch (ObjectNotFoundException exp) {
            assertTrue(exp.getErrorMessage().contains("The phone number was not found in the database"), "Wrong exception message, does not contain the reason: "+ exp.getErrorMessage());  
            assertTrue(exp.getErrorMessage().contains("0733976996"), "The wong phonenumber is not correct: " + exp.getWrongData());
        } catch (DataBaseConnectionException exp) {
            fail("A database Exception was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

    @Test 
    public void canFindCorrectCustomerFromPhone(){
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "ellen", bike);
            CustomerDTO temp = customerReg.findCustomerFromPhoneNumberCR("0733976993");
            String foundPhone = temp.getPhoneNumber();
            assertEquals("0733976993", foundPhone, "The customer is wrong. The found phone number is " + foundPhone);

        } catch (ObjectNotFoundException exc) {
            fail("An objectNotFound Exception was thrown when it shouldn't");
        } catch (DataBaseConnectionException exp) {
             fail("A database Exception was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test
    public void checkFindCustomerReturnsNullWhenNotFound() {
        try {
            CustomerDTO customer = controller.findCustomerFromPhoneNumber("non-existent-number");
            assertNull(customer, "Should return null for a phone number not in the registry.");
        } catch (OperationFailedException exc) {
            fail("An objectNotFound Exception was thrown when it shouldn't");
        } 
    }

    @Test
    public void checkThatAddProblemDescriptionInitiatesOrder() {
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            assertEquals(0, resultId, "The repair order ID should be the 0 for first order. It is: " + resultId);
        } catch (OperationFailedException exp ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        }
    }

    @Test 
    public void checkThatRepairOrderIsCreated(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains("Customer Info:"), "No customer Info is in the repairOrder.");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatProblemDescriptionIsCreated(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains("Problem Description:"), "No Problem Description is in the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatobservedFaultCanBeAddedToProblemDescription(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.observedFault(FaultInBike.BRAKES);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains(" - BRAKES"), "The Problem isn't added to the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatMultipleObservedFaultCanBeAddedToProblemDescription(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.observedFault(FaultInBike.BRAKES);
            controller.observedFault(FaultInBike.CHAIN);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains(" - BRAKES\n - CHAIN"), "The Problem isn't added to the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void addingObservedFaultShouldReturnRepairOrderDTO(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            controller.addProbelmDescription(customer);
            RepairOrderDTO repOrderDTO = controller.observedFault(FaultInBike.BRAKES);
            assertNotNull(repOrderDTO, "No Repair Order DTO returned");
            assertEquals("ellen", repOrderDTO.getCustomer().getName(), "The customer in the RepairOrderDTO has an incorrect name");
            assertNotNull(repOrderDTO.getProblemDescription(), "There is no Problem List in the RepairOrder DTO");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test
    public void canSpecifyRepairOrder(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            controller.addProbelmDescription(customer);
            RepairOrderDTO repOrderDTO = controller.specifyCustomer(0);
            assertNotNull(repOrderDTO, "No Repair Order DTO returned");
            assertEquals("ellen", repOrderDTO.getCustomer().getName(), "The customer in the RepairOrderDTO has an incorrect name");
            assertNotNull(repOrderDTO.getProblemDescription(), "There is no Problem List in the RepairOrder DTO");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test
    public void canSpecifyRepairOrderWhenNeedingToFindTheCorrectOne(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        CustomerDTO customer2 = new CustomerDTO("0733976997", "ellen.gronholm@hotmail.fi", "Helen", bike);
        CustomerDTO customer3 = new CustomerDTO("0733976998", "ellen.gronholm@hotmail.net", "Denis", bike);
        try {
            controller.addProbelmDescription(customer);
            controller.addProbelmDescription(customer2);
            controller.addProbelmDescription(customer3);
            RepairOrderDTO repOrderDTO = controller.specifyCustomer(1);
            assertNotNull(repOrderDTO, "No Repair Order DTO returned");
            assertEquals("Helen", repOrderDTO.getCustomer().getName(), "The customer in the RepairOrderDTO has an incorrect name. Is the report Specified?");
            assertNotNull(repOrderDTO.getProblemDescription(), "There is no Problem List in the RepairOrder DTO");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatSpecifyReportSetsTheCorrectReport(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        CustomerDTO customer2 = new CustomerDTO("0733976997", "ellen.gronholm@hotmail.fi", "Helen", bike);
        CustomerDTO customer3 = new CustomerDTO("0733976998", "ellen.gronholm@hotmail.net", "Denis", bike);
        try {
            int repOrderID0 = controller.addProbelmDescription(customer);
            int repOrderID1 = controller.addProbelmDescription(customer2);
            controller.addProbelmDescription(customer3);
            controller.specifyCustomer(repOrderID1);
            controller.observedFault(FaultInBike.BRAKES);
            String repOrderString = controller.setOrderStatus(repOrderID0, StateOfOrder.CUSTOMER_REVIEWING);
            assertFalse(repOrderString.contains(" - BRAKES"), "The program never set the right repair order to work on");
            String repOrderString2 = controller.setOrderStatus(repOrderID1, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString2.contains(" - BRAKES"), "The program never set the right repair order to work on");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        }          
    }

    @Test
    public void checkThatFaultListDoneSetsRepOrderToNull(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            controller.addProbelmDescription(customer);
            controller.observedFault(FaultInBike.BRAKES);
            controller.faultListDone();
            controller.observedFault(FaultInBike.CHAIN);
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } catch (Exception exc) {
            assertTrue(exc.getMessage().contains("is null"));
        } 
    }

    @Test 
    public void checkThatProposedRepairsIsCreated(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.faultListDone();
            controller.addProposedRepairs(resultId);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains("Propossed Repairs:"), "No Proposed Repairs is in the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatTaskCanBeAddedToPropRepairs(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.faultListDone();
            controller.addProposedRepairs(resultId);
            controller.addTaskToRepairs("Clean", 300, 20);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            assertTrue(repOrderString.contains(" - Clean"), "The Task isn't added to the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatMultipleTasksCanBeAddedToPropRepairs(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.faultListDone();
            controller.addProposedRepairs(resultId);
            controller.addTaskToRepairs("Clean", 300, 20);
            controller.addTaskToRepairs("Fix", 300, 20);
            String repOrderString = controller.setOrderStatus(resultId, StateOfOrder.CUSTOMER_REVIEWING);
            System.out.print(repOrderString);
            assertTrue(repOrderString.contains("- Clean: \t300\n - Fix"), "The Task isn't added to the repair Order");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void addingTaskShouldReturnRepairOrderDTO(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.faultListDone();
            controller.addProposedRepairs(resultId);
            RepairOrderDTO repOrderDTO = controller.addTaskToRepairs("Clean", 300, 20);
            assertNotNull(repOrderDTO, "No Repair Order DTO returned");
            assertEquals("ellen", repOrderDTO.getCustomer().getName(), "The customer in the RepairOrderDTO has an incorrect name");
            assertNotNull(repOrderDTO.getPropRepairs(), "There is no Problem List in the RepairOrder DTO");
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } 
    }

    @Test 
    public void checkThatPropListDoneSetsRepOrderToNull(){
        CustomerDTO customer = new CustomerDTO("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
        try {
            int resultId = controller.addProbelmDescription(customer);
            controller.observedFault(FaultInBike.BRAKES);
            controller.faultListDone();
            controller.addProposedRepairs(resultId);
            controller.addTaskToRepairs("Clean", 300, 20);
            controller.propRepairsListDone();
            controller.addTaskToRepairs("Fix", 300, 20);
        } catch (OperationFailedException exc ) {
            fail("A OperationFailed Exception was thrown when it shouldn't");
        } catch (Exception exc) {
            assertTrue(exc.getMessage().contains("is null"));
        } 
    }

    @Test 
    public void canSetLogger(){
        FileLogger fLogger = new FileLogger("TestController");
        controller.setLogger(fLogger);
        try{
            controller.findCustomerFromPhoneNumber("wrong");
        } catch (Exception exc) {
        }

        LocalDateTime now = LocalDateTime.now();      
        String identifyingTime = String.valueOf(now.getDayOfYear()) + //
                "-" + String.valueOf(now.getHour()) + //
                "." + String.valueOf(now.getMinute()) + //
                "." + String.valueOf(now.getSecond());
        String loggerString = "The phone number was not found in the database.";
        try {
            File Obj = new File("TestController" + identifyingTime + ".txt");
            Scanner Reader = new Scanner(Obj);
          
            String allLogged = "";
            // Traversing File Data
          	while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                allLogged += data;
            }          
            Reader.close();
            assertTrue(allLogged.contains(loggerString), "The correct text is not Logged");
        } catch (Exception exc) {
            fail("The file could not be found. The logger is thus not changed to FileLogger");
        }
    }

    

}