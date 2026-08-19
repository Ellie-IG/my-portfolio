package se.kth.iv1350.bikerepairshop.view;

import se.kth.iv1350.bikerepairshop.logger.FileLogger;
import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;
import se.kth.iv1350.bikerepairshop.controller.Controller;

public class RepairOrderLogger extends RepairOrderDisplay{
    private Controller contr;

    /** Creates a new instance
     */
    public RepairOrderLogger(Controller contr) { 
        this.contr = contr; 
    }

    @Override
    /**Invoked When the repair order has been updated. Will only notify when
     * the mechanic or receptionist needs to do work.
     * 
     * @param repOrderDTO the repair order that was updated as a string  
     * @throws Exeption if something goes wrong when calling the RepairOrder
     */  
    protected void orderIsUpdatedForMechanic(RepairOrderDTO repOrderDTO) 
                                                throws Exception{
        FileLogger logger = new FileLogger("MechanicReview");
        logger.log("Hi Mechanic, there is a new order to be reviewed: \n" + //
                                contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
    }
    
    protected void orderIsUpdatedForClient(RepairOrderDTO repOrderDTO) 
                                            throws Exception{
    FileLogger logger = new FileLogger("CustomerReview");
            logger.log("Hi, the order " + repOrderDTO.getRepOrderID() + //
                                " has been fixed by the mechanic and is ready to be reviewed: \n" + //
                                contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
    }

    @Override
    /** Handles any errors thrown by {@link doHandleRepairOrderUpdate}
     * 
     * @param e the exception thrown
     */
    protected void handleErrors(Exception e){
        FileLogger logger = new FileLogger("Error");
        logger.log("There was an error when accsessing the repairOrderDTO: " + e.getMessage());
    };
}
