package se.kth.iv1350.bikerepairshop.model;

/** A listener interface for recieving notifications about uppdated repair orders. Classes 
 * interested in these notifications implement this interface. When the repair orders are 
 * updated, the object's {@link #repairOrderIsUpdated repairOrderIsUpdated} method is invoked.
 * 
 */
public interface RepairOrderObserver {
    /**Invoked When the repair order has been updated.
     * 
     * @param repOrderDTO the repair order that was updated as a string
     * @param stateOfOrder the state of teh order when notified
     */
    void repairOrderIsUpdated(RepairOrderDTO repOrderDTO);
}
