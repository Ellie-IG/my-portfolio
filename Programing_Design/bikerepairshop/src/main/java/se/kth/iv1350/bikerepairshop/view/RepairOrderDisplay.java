package se.kth.iv1350.bikerepairshop.view;

import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.RepairOrderObserver;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;

public abstract class RepairOrderDisplay implements RepairOrderObserver{

    @Override
    /**Invoked When the repair order has been updated. Will only notify when
     * the mechinic or receptionist needs to do work.
     * 
     * @param repOrderDTO the repair order that was updated as a string
     */
    public void repairOrderIsUpdated(RepairOrderDTO repOrderDTO) {
        handleRepairOrderUpdate(repOrderDTO);
    }
    
    private void handleRepairOrderUpdate(RepairOrderDTO repOrderDTO) {
        try {
            if(repOrderDTO.getStateOfOrder() == StateOfOrder.MECHANIC_REVIEW){
                orderIsUpdatedForMechanic(repOrderDTO);
            } else if(repOrderDTO.getStateOfOrder() == StateOfOrder.MECHANIC_COMPLETED){
                orderIsUpdatedForClient(repOrderDTO);
            }
        } catch (Exception e) {
            handleErrors(e);
        }
    }
    
    /** Is called when the Mechanic should be updated
     * 
     * @param repOrderDTO the repair order that has been updtaed
     * @throws Exception if something goes wrng when calling the Repair Order
     */
    protected abstract void orderIsUpdatedForMechanic(RepairOrderDTO repOrderDTO) throws Exception;

    /** Is called when the receptionst and client should be updated
     * 
     * @param repOrderDTO the repair order that has been updtaed
     * @throws Exception if something goes wrng when calling the Repair Order
     */
    protected abstract void orderIsUpdatedForClient(RepairOrderDTO repOrderDTO) throws Exception;

    protected abstract void handleErrors(Exception e);


}
