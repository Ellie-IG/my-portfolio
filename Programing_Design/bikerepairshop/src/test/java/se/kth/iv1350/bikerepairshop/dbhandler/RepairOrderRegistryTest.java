package se.kth.iv1350.bikerepairshop.dbhandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.data.Bike;
import se.kth.iv1350.bikerepairshop.model.RepairOrder;

/**
 * Contains all calls to the object RepairOrder 
 */
public class RepairOrderRegistryTest {
    private RepairOrderRegistry repOrderReg;
    private Bike bike;
    private CustomerDTO customer;
    


    @BeforeEach
    public void setUp() {
        repOrderReg = new RepairOrderRegistry();
        while (true) {
            try {
                repOrderReg.connect();
                break;
            } catch (DataBaseConnectionException exc) {
            }
        }
        bike = new Bike(23490, "dam", "helkama");
        customer = new CustomerDTO("0733976995", "ellen.gronholm@hotmail.com", "ellen", bike);
    }

    @AfterEach 
    public void tearDown() {
        bike = null;
        customer = null;
        repOrderReg = null;
    }

    @Test
    public void canConnect(){
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        while (true) {
            System.out.println("canConnect ");
            try {
                repOrderReg.connect();
                if (repOrderReg.isConnect() == true){
                    break;
                }
            } catch(DataBaseConnectionException exc){
                if (repOrderReg.isConnect() == true){
                    fail("No DataBaseConnectionError should be sent");
                }
            }
        }
    }

    @Test
    public void canhandleNoConnection(){
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        while (true) {
            System.out.print("NoConnection ");
            try {
                System.out.println(".");
                repOrderReg = new RepairOrderRegistry();
                repOrderReg.connect();
            } catch(DataBaseConnectionException exc){
                if (repOrderReg.isConnect() == false){
                    assertNotNull(exc, "No exception is thrown");
                    assertEquals("Customer Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
                    assertTrue(exc.getErrorMessage().contains("Could not connect to the Database"), "The error message is for the wrong");
                    break;
                }
                break;
            }
        }
    }

    @Test
    public void canAddRepairOrderTest1(){
        try {
            repOrderReg.addRepairOrder(customer);
            RepairOrder temp = repOrderReg.getRepairOrderList().get(0);
            assertNotNull(temp, "There is no object in the list");
        } catch (DataBaseConnectionException exp) {
            fail("A Data base exception is thrown when it shouldn't be");
        }
    }


    @Test
    public void canAddRepairOrderTest2(){
        try {
            repOrderReg.addRepairOrder(customer);
            int length = repOrderReg.getRepairOrderList().size();
            assertEquals(1, length, "The list is of size " + length + ". It should contain 1 customer");
        } catch (DataBaseConnectionException exp) { 
            fail("A Data base exception is thrown when it shouldn't be");
        }
    }

    @Test
    public void addRepairOrderThrowsException(){
        RepairOrderRegistry repOrderR = new RepairOrderRegistry();
        try {
            repOrderR.addRepairOrder(customer);
        } catch (DataBaseConnectionException exp) { 
            assertNotNull(exp, "Data Base Connection Exception was not thrown");
            assertTrue(exp.getErrorMessage().contains("Connection to the Database was not established"), "Data base Connection Exception had the wrong message");
            assertEquals("Repair Order Registry", exp.getConnectionRegistry(), "Data Base Connection Exception was thrown for the wrong Registry");
        }
    }

    @Test 
    public void aRepairOrderAdded(){
        try {
            repOrderReg.addRepairOrder(customer);
            CustomerDTO customer = repOrderReg.getRepairOrderList().get(0).getCustomer();
            assertNotNull(customer, "There is no customer in the RepairOrder");
        } catch (DataBaseConnectionException exp){
            fail("A Data base exception is thrown when it shouldn't be");
        }
    }

    @Test 
    public void correctRepairOrderAdded(){
        try {
            repOrderReg.addRepairOrder(customer);
            CustomerDTO customer = repOrderReg.getRepairOrderList().get(0).getCustomer();
            String phone = customer.getPhoneNumber();
            assertEquals("0733976995", phone, "The phonenumber saved (" + phone + ") doesn't match, the customer is thus not correct.");
        } catch (DataBaseConnectionException exp) {
            fail("A Data base exception is thrown when it shouldn't be");
        }
    }

    @Test 
    public void givesCorrectID(){
        try {
            repOrderReg.addRepairOrder(customer);
            repOrderReg.addRepairOrder(customer);
            repOrderReg.addRepairOrder(customer);
            int repOrderID = repOrderReg.getRepairOrderList().get(2).getRepOrderID();
            assertEquals(2, repOrderID, "The ID gives is wrong: " + repOrderID);
        } catch (DataBaseConnectionException exp) {
            fail("A Data base exception is thrown when it shouldn't be");
        }
    }

    @Test
    public void getRepairOrderListThrowsException(){
        RepairOrderRegistry repOrderR = new RepairOrderRegistry();
        try {
            repOrderR.getRepairOrderList();
        } catch (DataBaseConnectionException exp) { 
            assertNotNull(exp, "Data Base Connection Exception was not thrown");
            assertTrue(exp.getErrorMessage().contains("Connection to the Database was not established"), "Data base Connection Exception had the wrong message");
            assertEquals("Repair Order Registry", exp.getConnectionRegistry(), "Data Base Connection Exception was thrown for the wrong Registry");
        }
    }

    @Test 
    public void getRepOrderTest(){
        try {            
            int repOrderID = repOrderReg.addRepairOrder(customer).getRepOrderID();
            RepairOrder temp = repOrderReg.getRepairOrder(repOrderID);
            assertNotNull(temp, "No Repair order is returned when it should be.");
        } catch (ObjectNotFoundException exc){
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        }
    }

    @Test 
    public void getsTheCorrectRepOrderTest(){
        CustomerDTO customer2 = new CustomerDTO("0733976997", "ellen.gronholm@hotmail.com", "Helen", bike);
        try {            
            repOrderReg.addRepairOrder(customer);
            int repOrderID = repOrderReg.addRepairOrder(customer2).getRepOrderID();
            repOrderReg.addRepairOrder(customer);
            RepairOrder temp = repOrderReg.getRepairOrder(repOrderID);
            String foundPhone = temp.getCustomer().getPhoneNumber();
            String foundName = temp.getCustomer().getName();
            assertEquals("0733976997", foundPhone, "The customer is wrong. The found phone number is " + foundPhone);
            assertEquals("Helen", foundName, "The customer is wrong. The found name is " + foundName);
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        }
    }

    @Test 
    public void noRepOrderFoundHandled() {
        try {
            repOrderReg.addRepairOrder(customer);
            repOrderReg.getRepairOrder(13);
            fail("An error was not thrown when no rep order is found");
        } catch (ObjectNotFoundException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Repair Order", exc.getObjectName(), "The error message is for the wrong database");
            assertEquals("13", exc.getWrongData(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("The repair order id was not found in the database."), "The error message is for the wrong");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        }
    }

    @Test 
    public void emptyRegistryHandled() {
        try {
            repOrderReg.getRepairOrder(0);
            fail("An error was not thrown when no data base is connected");
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseException Error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Repair Order Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("there are no yet recorded repair orders"), "The error message is for the wrong");
        } 
    }


    @Test 
    public void noDataBaseconnectionEstablishedWhenGettingRepOrder() {
    RepairOrderRegistry repOrderR = new RepairOrderRegistry();
        try {
            repOrderR.getRepairOrder(0);
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Repair Order Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("Connection to the Database was never established"), "The error message is for the wrong");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } 
    }


}