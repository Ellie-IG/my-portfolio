package se.kth.iv1350.bikerepairshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.data.Bike;

/**
 * This object contains all information about the repair: Customer, bike, 
 * problems observed, problems measured, recomened fixes, the date, and an ID
 */
public class RepairOrderTest {
    private RepairOrder repOrder;
    /*
    private StateOfOrder stateOfOrder;
    private CustomerDTO customerRepOrder;
    private FaultsList problemDescription;
    private FaultsList diagnosticReport;
    private ProposedRepairs propRepairs;
    private int date;
    private int repOrderID;*/

    @BeforeEach
    public void setUp() {
        Bike bike = new Bike(23490, "dam", "helkama");
        CustomerDTO customer = new CustomerDTO("0733976992", "ellen.gronholm@hotmail.com", "ellen", bike);
        repOrder = new RepairOrder(customer, 0);
    }

    @AfterEach 
    public void tearDown() {
        repOrder = null;
    }

    @Test
    public void setStateOfOrderTest(){
        repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
        assertEquals(StateOfOrder.MECHANIC_REVIEW ,StateOfOrder.MECHANIC_REVIEW, "The state of the order is not set correctly");
    }

    @Test
    public void faultListIsCreated(){
        repOrder.addProblemDescription();
        FaultsList faultList = repOrder.getProblemDescription();
        assertNotNull(faultList, "No fault list is created");
    }

    @Test
    public void observedFaultCanBeAddedToProblemDescription(){
        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        FaultsList faultList = repOrder.getProblemDescription();
        FaultInBike fault = faultList.getFaultsList().get(0);
        assertNotNull(fault, "The fault was not registered in the list");
    }

    @Test
    public void observedFaultCantBeAddedTwiceToProblemDescription(){
        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.addObservedFault(FaultInBike.BRAKES);
        FaultsList faultList = repOrder.getProblemDescription();
        int size = faultList.getFaultsList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size + ". Doubles were not handled");

    }

    @Test
    public void observedFaultCanBeAddedToDiagnosticReport(){
        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
        repOrder.addDiagnosticReport();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        FaultsList faultList = repOrder.getDiagnosticReport();
        assertNotNull(faultList, "A faultList is not created in the right place");
    }

    @Test
    public void observedFaultCanBeAddedToDiagnosticReport2(){
        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
        repOrder.addDiagnosticReport();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        FaultsList faultList = repOrder.getDiagnosticReport();
        FaultInBike fault = faultList.getFaultsList().get(0);
        assertNotNull(fault, "The fault was not registered in the list");
    }

    @Test
    public void observedFaultCanBeAddedTwiceToDiagnosticReport(){
        repOrder.addProblemDescription();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
        repOrder.addDiagnosticReport();
        repOrder.addObservedFault(FaultInBike.BRAKES);
        repOrder.addObservedFault(FaultInBike.BRAKES);
        FaultsList faultList = repOrder.getDiagnosticReport();
        int size = faultList.getFaultsList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size + ". Doubles were not handled");

    }

}