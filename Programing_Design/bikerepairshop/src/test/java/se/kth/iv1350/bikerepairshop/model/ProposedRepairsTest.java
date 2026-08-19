package se.kth.iv1350.bikerepairshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProposedRepairsTest {
     private ProposedRepairs propRepairs;

    @BeforeEach
    public void setUp() {
        propRepairs = new ProposedRepairs();
    }

    @AfterEach 
    public void tearDown() {
        propRepairs = null;
    }

    @Test 
    public void canAddTask(){
        propRepairs.addTaskToList("Replace tire", 200, 30);
        Task task = propRepairs.getTaskList().get(0);
        assertNotNull(task, "The fault was not registered in the list");
        int size = propRepairs.getTaskList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size);
    }

    @Test 
    public void canAddCorrectAMountOfTasks(){
        propRepairs.addTaskToList("Replace tire", 200, 30);
        propRepairs.addTaskToList("Replace chain", 200, 30);
        propRepairs.addTaskToList("Replace pedals", 200, 30);
        int size = propRepairs.getTaskList().size();
        assertEquals(3, size, "The wrong amount of faults exist in bike: " + size);
    }

    @Test
    public void addsSameTask(){
        propRepairs.addTaskToList("Replace tire", 200, 30);
        propRepairs.addTaskToList("Replace tire", 200, 30);
        int size = propRepairs.getTaskList().size();
        assertEquals(1, size, "The wrong amount of faults exist in bike: " + size + ". Doubles were not handled");
    }

    @Test
    public void canGetTaskCost(){
        propRepairs.addTaskToList("Replace tire", 1, 30);
        propRepairs.addTaskToList("Replace pedal", 2, 30);
        propRepairs.addTaskToList("Replace chain", 3, 30);
        propRepairs.propRepairsListDone();
        int[] indivCost = propRepairs.getIndividualTaskCost();
        assertEquals(3, indivCost.length, "The individual costs array is not the correct legth.");
        assertEquals(1, indivCost[0], "The code can not get the correct individual costs");
        assertEquals(2, indivCost[1], "The code can not get the correct individual costs for index 1");
        assertEquals(3, indivCost[2], "The code can not get the correct individual costs for index 2");
    }

    @Test
    public void canGetTotalCost(){
        propRepairs.addTaskToList("Replace tire", 1, 30);
        propRepairs.addTaskToList("Replace pedal", 2, 30);
        propRepairs.addTaskToList("Replace chain", 3, 30);
        propRepairs.propRepairsListDone();
        int totCost = propRepairs.getTotalCost();
        assertEquals(6, totCost, "The total cost is wrong: " + totCost + ". It should be 6 (1+2+3)");
    }

    @Test 
    public void taskListToStringTest() {
        propRepairs.addTaskToList("Replace tire", 1, 30);
        propRepairs.addTaskToList("Replace bike", 2, 30);
        String st = propRepairs.taskListToString();
        assertEquals("Current bike faults: \n" + "- Replace tire\n- Replace bike\n", st, "The string is not correctly created.");
    }

    @Test 
    public void taskCostToStringTest() {
        propRepairs.addTaskToList("Replace tire", 1, 30);
        propRepairs.addTaskToList("Replace bike", 2, 30);
        propRepairs.propRepairsListDone();
        String st = propRepairs.taskCostToString();
        assertEquals("Current recommended bike repairs: \n" + //
                    " - Replace tire: \t1\n - Replace bike: \t2\n" + //
                    "\t total: \t3", st, "The string is not correctly created.");
    }

}

