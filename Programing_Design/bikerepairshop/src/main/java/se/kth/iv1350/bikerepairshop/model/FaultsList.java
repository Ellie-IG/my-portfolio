package se.kth.iv1350.bikerepairshop.model;

import java.util.ArrayList;
import java.util.List;

/** A list containg all Faults observed
 */
public class FaultsList {
    private List<FaultInBike> faultsList= new ArrayList<>();

    /** Creates a new instance 
     */
    public FaultsList(){ 
    }

    /** Adds a fault to the end of the list if it is not in the list yet.
     * 
     * @param fault
     */
    public void addFaultToList(FaultInBike fault) {
        if (!faultsList.contains(fault)) {
            faultsList.add(fault);
        }
    }

    /** @return the fault list
     */
    public List<FaultInBike> getFaultsList() {
        return faultsList;
    }

    /** @return a string of the faults in the list
     */
    public String listToString() {
        if (faultsList.isEmpty()) {
            return "No faults recorded.";
        }

        String message = "Current bike faults: \n";

        for (FaultInBike fault : faultsList) {
            message = message.concat(" - " + fault + "\n");
        }
        return message;
    }   
    
}
