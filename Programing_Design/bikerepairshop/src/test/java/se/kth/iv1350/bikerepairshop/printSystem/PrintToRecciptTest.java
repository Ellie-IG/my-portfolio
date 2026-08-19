package se.kth.iv1350.bikerepairshop.printSystem;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.data.Bike;
import se.kth.iv1350.bikerepairshop.model.FaultInBike;
import se.kth.iv1350.bikerepairshop.model.RepairOrder;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;
import se.kth.iv1350.bikerepairshop.printsystem.PrintToReccipt;

public class PrintToRecciptTest {
    private RepairOrder repOrder;
    private PrintToReccipt printToReccipt;

    @BeforeEach
    public void setUp() {
        Bike bike = new Bike(40609, "Dam", "Helkama");
        CustomerDTO customer = new CustomerDTO("0733976992", "ellen.gronholm@hotmail.com", "Ellen", bike);
        repOrder = new RepairOrder(customer, 12);

        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.addObservedFault(FaultInBike.CHAIN);
        repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
        
        repOrder.addDiagnosticReport();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.addObservedFault(FaultInBike.CHAIN);
        repOrder.addObservedFault(FaultInBike.PEDALS);
        
        repOrder.addProposedRepairs();
        repOrder.addTaskToRepairs("Clean brakes", 30, 20);
        repOrder.addTaskToRepairs("Exchange chain", 60, 30);
        repOrder.propRepairsListDone();

        printToReccipt = new PrintToReccipt();
    }

    @AfterEach 
    public void tearDown() {
        repOrder = null;
    }    

    @Test
    public void recieptCompiles(){
        String orderRecipt = "";
        orderRecipt += printToReccipt.showReceipt(repOrder);
        assertTrue(orderRecipt != "", "The reciept is not created. Nothing is sent back");
    }

    @Test
    public void recieptCustomerInfoIsCorrect(){
        String orderRecipt = "";
        orderRecipt += printToReccipt.showReceipt(repOrder);
        assertTrue(orderRecipt.contains("Customer Info:\n" + //
                        " -------------------------------\n" + // 
                        "Name:\t\tEllen\n" + //
                        "Phone Number:\t0733976992\n" + //
                        "Email Address:\tellen.gronholm@hotmail.com\n" + //
                        "Bike:\n" + //
                        "\t SerialNumber:\t40609\n" + //
                        "\t Model:\t\tDam\n" + //
                        "\t Brand:\t\tHelkama"
                         ), "The Customer information is not correct on the reciept." );
        
    }

    @Test 
    public void recieptFaultListingsInfoIsCorrect(){
        String orderRecipt = "";
        orderRecipt += printToReccipt.showReceipt(repOrder);
        assertTrue(orderRecipt.contains("Problem Description: \n" + //
                        " -------------------------------\n" + //
                        "Current bike faults: \n" + //
                        " - BRAKES\n" + //
                        " - CHAIN\n" + //
                        "\n\n" + //
                        "Diagnostic Report: \n" + //
                        " -------------------------------\n" + //
                        "Current bike faults: \n" + //
                        " - BRAKES\n" + //
                        " - CHAIN\n" + //
                        " - PEDALS\n" + //
                        "\n\n")
                        , "The some of the faults are not correctly recorded on the reciept.");
        
    }

    @Test 
    public void recieptDateIsCorrect(){
        String orderRecipt = "";
        Date d1 = new Date();
        String st = "" + d1;
        orderRecipt += printToReccipt.showReceipt(repOrder);
        assertTrue(orderRecipt.contains(st), "The time is incorrect.");
        
    }

    @Test 
    public void recieptIDIsCorrect(){
        String orderRecipt = "";
        orderRecipt += printToReccipt.showReceipt(repOrder);
        assertTrue(orderRecipt.contains("ID:\t\t12"), "The ID is incorrect.");
    }

    //test writing in file
    //test sending to printer
    //test that show recpeit doesn't show task list if none exists

}
