package se.kth.iv1350.bikerepairshop.view;


import se.kth.iv1350.bikerepairshop.controller.Controller;
import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.exeptionhandler.OperationFailedException;
import se.kth.iv1350.bikerepairshop.logger.LoggerBike;
import se.kth.iv1350.bikerepairshop.model.FaultInBike;
import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;
import se.kth.iv1350.bikerepairshop.payment.Discount0;

/**
 * This program has no view, instead, this class is a
 * placeholder for the entire view.
 */
public class View{
    private Controller contr;
    private LoggerBike logger;


    /**Creates a new instance.
     *
     * @param contr The controller that is used for all operations.
     */
    public View(Controller contr, LoggerBike logger) { 
        this.contr = contr;
        this.logger = logger;
    }

    /** @param logger sets logger to a object Logger
     */
    public void setLogger(LoggerBike logger) {
        this.logger = logger;
    }



    /**
     * Simulates user input and screen outputs.
     * This triggers the controller operations from the View layer.
     */
    public void runFakeExecution() {
        logger.log("The program simulation has started in the View! \n ----------------------\n");
        RepairOrderDTO repOrderDTO;

        // 1. Find customer
        logger.log("//Find customer with the number 0733976999");
        CustomerDTO customer;
        while(true){
            try {
                customer = contr.findCustomerFromPhoneNumber("0733976999");
                break;
            } catch (OperationFailedException exc) {
                logger.log(exc.getMessage());
            }
        }
        logger.log("The system returned: " + customer + ", so a new report is created for the customer");

        // 2. Create repair order
        try {
            int orderId = contr.addProbelmDescription(customer);
            // Note: You might need to adjust what RepairOrderView/Logger accept if they are nested classes
            contr.addRepOrderObserver(new RepairOrderView(contr));
            contr.addRepOrderObserver(new RepairOrderLogger(contr));
            logger.log("Created Repair Order ID: " + orderId);

            // 3. Add observed faults
            logger.log("//Add the fault TIRE");
            repOrderDTO = contr.observedFault(FaultInBike.TIRE);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
            logger.log("//Add the fault BRAKES");
            repOrderDTO = contr.observedFault(FaultInBike.BRAKES);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));

            // 4. Finish fault list
            logger.log("//The list of observed faults is deemed complete");
            repOrderDTO = contr.faultListDone();
            logger.log("Problem Description completed: " + repOrderDTO.getProblemDescription().listToString() + "\n\n");

            // 5. Add diagnostic report
            logger.log("The mechanic gets a notification about the report");
            repOrderDTO = contr.addDiagnosticReport(orderId);
            logger.log("Diagnostic report started.");

            // 6. Add faults to diagnostic
            logger.log("//The mechanic adds the observation TIRE");
            repOrderDTO = contr.observedFault(FaultInBike.TIRE);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
            logger.log("//The mechanic adds the observation BRAKES");
            repOrderDTO = contr.observedFault(FaultInBike.BRAKES);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
            
            // 4. Finish fault list
            repOrderDTO = contr.faultListDone();
            logger.log("Diagnostic report completed: " + repOrderDTO.getDiagnosticReport().listToString() + "\n");

            // 6. Add proposed repairs
            contr.addProposedRepairs(orderId);
            logger.log("//The mechanic adds the fix to replace a tire");
            repOrderDTO = contr.addTaskToRepairs("Replace tire", 200, 30);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));
            logger.log("//The mechanic adds to fix the chain");
            repOrderDTO = contr.addTaskToRepairs("Fix chain", 150, 20);
            logger.log(contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW));

            // 7. Finish proposed repairs
            repOrderDTO = contr.propRepairsListDone();
            logger.log("Proposed repairs completed: " + repOrderDTO.getPropRepairs().taskListToString() + "\n\n");

            // 8. Ask customer to accept
            contr.setDiscount(orderId, new Discount0());
            repOrderDTO = contr.specifyCustomer(orderId); // make sure correct order is active
            logger.log("The repair order is shown to the customer: \n" + contr.setOrderStatus(repOrderDTO.getRepOrderID(), StateOfOrder.NEW) + "\n");
            logger.log(contr.setOrderStatus(orderId, StateOfOrder.CUSTOMER_REVIEWING));

            // 9. Print repair order
            contr.specifyCustomer(orderId); // make sure correct order is active
            String reOrderString = contr.setOrderStatus(orderId, StateOfOrder.ACCEPTED);
            System.out.print(reOrderString + "\n\n");

            logger.log(contr.setOrderStatus(orderId, StateOfOrder.ACCEPTED));

        } catch (OperationFailedException exc) {
            logger.log(exc.getMessage());
        }
    }
}