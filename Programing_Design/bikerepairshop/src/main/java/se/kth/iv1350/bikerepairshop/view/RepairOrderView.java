package se.kth.iv1350.bikerepairshop.view;

import se.kth.iv1350.bikerepairshop.controller.Controller;
import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;

public class RepairOrderView extends RepairOrderDisplay{
    private Controller contr;

    /** Creates a new instance
     */
    public RepairOrderView(Controller contr) { 
        this.contr = contr;
    }

    @Override
    protected void orderIsUpdatedForMechanic(RepairOrderDTO repOrderDTO) 
                                            throws Exception{
        System.out.println("Hi Mechanic, there is a new order to be reviewed: \n" + //
                                contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
    }

    protected void orderIsUpdatedForClient(RepairOrderDTO repOrderDTO) 
                                            throws Exception{
        System.out.println("Hi, the order " + repOrderDTO.getRepOrderID() + //
                            " has been fixed by the mechanic and is ready to be reviewed: \n" + //
                            contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));

    }

    @Override
    /** Handles any errors thrown by {@link OrderIsUpdatedForMechanic} and {@link OrderIsUpdatedForClient}
     * 
     * @param e the exception thrown
     */
    protected void handleErrors(Exception e){
        System.out.println("There was an error when accsessing the repairOrderDTO: " + e.getMessage());
    };
}
