package se.kth.iv1350.bikerepairshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



/** A list containg all Faults observed
 */
public class FaultsListTest {
    private FaultsList faultsList;

    @BeforeEach
    public void setUp() {
        faultsList = new FaultsList();
    }

    @AfterEach 
    public void tearDown() {
        faultsList = null;
    }

    @Test 
    public void canAddFault(){
        faultsList.addFaultToList(FaultInBike.BRAKES);
        FaultInBike fault = faultsList.getFaultsList().get(0);
        assertNotNull(fault, "The fault was not registered in the list");
    }

    @Test 
    public void canAddCorrectAMountOfFault(){
        faultsList.addFaultToList(FaultInBike.BRAKES);
        int size = faultsList.getFaultsList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size);
    }

    @Test
    public void addsSameFault(){
        faultsList.addFaultToList(FaultInBike.BRAKES);
        faultsList.addFaultToList(FaultInBike.BRAKES);
        int size = faultsList.getFaultsList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size + ". Doubles were not handled");
    }
}