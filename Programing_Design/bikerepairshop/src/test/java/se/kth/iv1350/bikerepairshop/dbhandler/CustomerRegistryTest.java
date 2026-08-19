package se.kth.iv1350.bikerepairshop.dbhandler;

import se.kth.iv1350.bikerepairshop.data.*;
import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CustomerRegistryTest {
    private CustomerRegistrySingleton customerReg;
    private Bike bike;



    @BeforeEach
    public void setUp() {
        customerReg = CustomerRegistrySingleton.getInstance();
        while (true) {
            try {
                customerReg.connect();
                break;
            } catch (DataBaseConnectionException exc) {
            }
        }
        bike = new Bike(23490, "dam", "helkama");
    }

    @AfterEach 
    public void tearDown() {
        bike = null;
        customerReg = null;
    }
  
    @Disabled
    @Test
    public void canConnect(){
        CustomerRegistrySingleton custReg = CustomerRegistrySingleton.getInstance();
        while (true) {
            System.out.println("canConnect ");
            try {
                custReg.connect();
                if (custReg.isConnect() == true){
                    break;
                }
            } catch(DataBaseConnectionException exc){
                if (custReg.isConnect() == true){
                    fail("No DataBaseConnectionError should be sent");
                }
            }
        }
    }

    @Disabled
    @Test
    public void canhandleNoConnection(){
        CustomerRegistrySingleton custReg = CustomerRegistrySingleton.getInstance();
        while (true) {
            System.out.println("NoConnection ");
            try {
                custReg = CustomerRegistrySingleton.getInstance();
                custReg.connect();
            } catch(DataBaseConnectionException exc){
                if (custReg.isConnect() == false){
                    assertNotNull(exc, "No exception is thrown");
                    assertEquals("Customer Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
                    assertTrue(exc.getErrorMessage().contains("Could not connect to the Database"), "The error message is for the wrong");
                    break;
                }
            }
        }
    }

    @Disabled    
    @Test
    public void canAddCustomerTest(){
        try{ 
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            Customer temp = customerReg.getCustomerList().get(0);
            assertNotNull(temp, "There is no object in the list");
            int length = customerReg.getCustomerList().size();
            assertEquals(1, length, "The list is of size " + length + ". It should contain 1 customer");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test
    public void canAddMultipleCustomersSingleton(){
        try{ 
            customerReg.addCustomer("0733976997", "ellen.gronholm@hotmail.fi", "amanda", bike);
            customerReg.addCustomer("0733976998", "ellen.gronholm@hotmail.net", "kiana", bike);
            Customer temp = customerReg.getCustomerList().get(0);
            assertNotNull(temp, "There is no object in the list");
            int length = customerReg.getCustomerList().size();
            assertEquals(3, length, "The list is of size " + length + ". It should contain 3 customer");
        } catch (DataBaseConnectionException exc) {
            fail("A DataBaseConnection error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test
    public void canAddMultipleCustomerTest(){
        try{ 
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.fi", "amanda", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.net", "kiana", bike);
            Customer temp = customerReg.getCustomerList().get(0);
            assertNotNull(temp, "There is no object in the list");
            int length = customerReg.getCustomerList().size();
            assertEquals(3, length, "The list is of size " + length + ". It should contain 3 customer");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        }
    }

    @Test 
    public void correctPhoneAdded(){
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            String phone = customerReg.getCustomerList().get(0).getPhoneNumber();
            assertEquals("0733976996", phone, "The phonenumber saved (" + phone + ") doesn't match");
        } catch (DataBaseConnectionException exc) {
        }
    }
    
    // public void correctEmail/NameAdded()

    @Disabled
    @Test 
    public void canFindCustomerFromPhone(){
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "ellen", bike);
            CustomerDTO temp = customerReg.findCustomerFromPhoneNumberCR("0733976994");
            assertNotNull(temp, "No customer is returned when it should be.");
        } catch (ObjectNotFoundException exc){
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
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
            customerReg.addCustomer("0733976995", "ellen.gronholm@hotmail.com", "Amanda", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "kiana", bike);
            CustomerDTO temp = customerReg.findCustomerFromPhoneNumberCR("0733976995");
            String foundPhone = temp.getPhoneNumber();
            String foundName = temp.getName();
            assertEquals("0733976995", foundPhone, "The customer is wrong. The found phone number is " + foundPhone);
            assertEquals("Amanda", foundName, "The customer is wrong. The found name is " + foundName);
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test 
    public void noCustomerFoundHandled() {
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.findCustomerFromPhoneNumberCR("0733976993");
            fail("An error was not thrown when no data base is connected");
        } catch (ObjectNotFoundException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Customer", exc.getObjectName(), "The error message is for the wrong database");
            assertEquals("0733976993", exc.getWrongData(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("The phone number was not found in the database"), "The error message is for the wrong");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseConnection error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test 
    public void emptyRegistryHandled() {
        try {
            customerReg.findCustomerFromPhoneNumberCR("0733976993");
            fail("An error was not thrown when no data base is connected");
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            fail("An DataBaseException Error was thrown when it shouldn't");
        } catch (EmptyRegistryException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Customer Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("there are no yet recorded customers"), "The error message is for the wrong");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

    @Disabled
    @Test 
    public void noDataBaseconnectionEstablishedWhenFindingCustomer() {
        CustomerRegistrySingleton custReg = CustomerRegistrySingleton.getInstance();
        while (true) {
            System.out.println("201  ");
            try {
                custReg = CustomerRegistrySingleton.getInstance();
                custReg.connect();
            } catch(DataBaseConnectionException exc){
                    break;
            }
        }
        try {
            customerReg.addCustomer("0733976996", "ellen.gronholm@hotmail.com", "ellen", bike);
            customerReg.addCustomer("0733976993", "ellen.gronholm@hotmail.com", "Amanda", bike);
            customerReg.addCustomer("0733976994", "ellen.gronholm@hotmail.com", "kiana", bike);
            custReg.findCustomerFromPhoneNumberCR("0733976993");
            fail("An error was not thrown when no data base is connected");
        } catch (ObjectNotFoundException exc) {
            fail("An ObjectNotFound error was thrown when it shouldn't");
        } catch (DataBaseConnectionException exc) {
            assertNotNull(exc, "No exception is thrown");
            assertEquals("Customer Registry", exc.getConnectionRegistry(), "The error message is for the wrong database");
            assertTrue(exc.getErrorMessage().contains("Connection to the Database was never established"), "The error message is for the wrong");
        } catch (EmptyRegistryException exc) {
            fail("An EmptyRegistry error was thrown when it shouldn't");
        } catch (IOException exc) {
            fail("An IOException error was thrown when it shouldn't");
        }
    }

}
