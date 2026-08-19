
package se.kth.iv1350.bikerepairshop.controller;


import java.io.IOException;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.dbhandler.CustomerRegistrySingleton;
import se.kth.iv1350.bikerepairshop.dbhandler.RepairOrderRegistry;
import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.OperationFailedException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;
import se.kth.iv1350.bikerepairshop.logger.LoggerBike;
import se.kth.iv1350.bikerepairshop.model.FaultInBike;
import se.kth.iv1350.bikerepairshop.model.RepairOrder;
import se.kth.iv1350.bikerepairshop.model.RepairOrderDTO;
import se.kth.iv1350.bikerepairshop.model.RepairOrderObserver;
import se.kth.iv1350.bikerepairshop.model.StateOfOrder;
import se.kth.iv1350.bikerepairshop.payment.DiscountStrategy;


/** Relays information between the view and the model. 
 * This is the application's only controller class.
 */
public class Controller {
    private RepairOrderRegistry repairOrderReg;
    private CustomerRegistrySingleton customerReg;
    private RepairOrder repOrder; 
    private LoggerBike logger;     
        
    /** Creates an new instance 
     * 
     * @param repairOrderReg handels call to the repairOrder database.
     * @param customerReg handels calls to the customer database.
     */
    public Controller(RepairOrderRegistry repairOrderReg, CustomerRegistrySingleton customerReg, LoggerBike logger){
        this.repairOrderReg = repairOrderReg;
        this.customerReg = customerReg;
        this.logger = logger;
    }

    /**@return setts the logger used to log */
    public void setLogger(LoggerBike logger) {
        this.logger = logger;
    }
        
    /** Finds a customer in the database from their phonenumber.
     * 
     * @param phoneNumer The phonenumber of the customer that should be found in data base
     * @return If found, returns the correct CustomerDTO. If not found returns <code>NULL</code> 
     * @throws OperationFailedException if an Exception is intercepted
     */
    public CustomerDTO findCustomerFromPhoneNumber(String phoneNumer) 
                        throws OperationFailedException{
        try {
            return customerReg.findCustomerFromPhoneNumberCR(phoneNumer);
        } catch (ObjectNotFoundException exc) {
            logger.log("The object " + exc.getObjectName() + " could not be reached with the id: " + //
                exc.getWrongData() + ". \nError message: " + exc.getErrorMessage());
            throw new OperationFailedException("The " + exc.getObjectName() + //
                " was not found. The data you entered was: " + exc.getWrongData(), 
                exc.getWrongData()) ;
        } catch (DataBaseConnectionException exc) {
            logger.log("In the " + exc.getConnectionRegistry() + " the following error occured: " +  exc.getErrorMessage());
            throw new OperationFailedException(exc.getConnectionRegistry() + " failed to connect. Please try again");
        } catch (EmptyRegistryException exc) {
            logger.log("The " + exc.getConnectionRegistry() + " " + exc.getErrorMessage());
            throw new OperationFailedException(exc.getConnectionRegistry() + " has no of data.");
        } catch (IOException exc) {
            logger.log("An internal error happened: " + exc.getMessage());
            throw new OperationFailedException("Something went wrong. Please try again.");
        }
    }

    /** @param customer the customer for which the repair order is created.
     *  @throws OperationFailedException if connection to the database is not established
     */
    private void createRepairOrder(CustomerDTO customer) 
                    throws OperationFailedException {
        try {
            this.repOrder = repairOrderReg.addRepairOrder(customer);
        } catch (DataBaseConnectionException exc) {
            logger.log("In the " + exc.getConnectionRegistry() + " the following error occured: "  +  exc.getErrorMessage());
            throw new OperationFailedException( exc.getConnectionRegistry() + " failed to connect. Please try again");
        }
    }

    /**Creates a Repair Order for the customer with a Faults list 
     * to contain all problems observed by the customer.
     * Also sets the repairOrder to this present to add faults to it
     * 
     * @param customer the customer for which the repair order is created.
     * @return The id of the repair order created in relation to the faults. 
     * @throws OperationFailedException if connection to the database is not established
     */
    public int addProbelmDescription(CustomerDTO customer)
                    throws OperationFailedException{
        createRepairOrder(customer);
        repOrder.addProblemDescription();
        return repOrder.getRepOrderID();
    }

    /** Adds a fault to the Fault list currently working on.
     *
     * @param fault the observed fault
     */
    public RepairOrderDTO observedFault(FaultInBike fault){
        return repOrder.addObservedFault(fault);
    }

    /** Gives the repOrder from ID
     * 
     * @param repOrderID Theid of the report that should be found
     * @throws OperationFailedException if an Exception is intercepted
     */
    private RepairOrderDTO specifyReport(int repOrderID)
                        throws OperationFailedException{

        try {
            this.repOrder = repairOrderReg.getRepairOrder(repOrderID);
        } catch (ObjectNotFoundException exc) {
            // logger.log("The object " + exc.getObjectName() + " could not be reached with the id: " + //
            // exc.getWrongData() + ". \nError message: " + exc.getErrorMessage());
            throw new OperationFailedException("The " + exc.getObjectName() + " " + //
                exc.getErrorMessage() + ". \nThe data you entered was: " + exc.getWrongData(), 
                exc.getWrongData()) ;
        } catch (DataBaseConnectionException exc) {
            logger.log("In the " + exc.getConnectionRegistry() + " the following error occured: " +  exc.getErrorMessage());
            throw new OperationFailedException(exc.getConnectionRegistry() + " failed to connect. Please try again");
        } catch (EmptyRegistryException exc) {
            logger.log("The " + exc.getConnectionRegistry() + " " + exc.getErrorMessage());
            throw new OperationFailedException(exc.getConnectionRegistry() + " has no of data.");
        }
        return repOrder.getRepairOrderDTO();
    }

    /** If something goes wrong the correct RepOrder to work on can be reset
     * 
     * @param repOrderID The ID of the report working with
     * @throws OperationFailedException if connection to the database is not established
     */
    public RepairOrderDTO specifyCustomer(int repOrderID)
                        throws OperationFailedException{
        
        specifyReport(repOrderID);
        return repOrder.getRepairOrderDTO();
    }

    /**Decides if the state of the Rep Order should change
     * 
     * @return <code> true </code> if the state was new, <code> false</code> if the state already was awaiting review.
     */
    private boolean setStateAwaitingReview(){
        if (repOrder.getStateOfOrder() == StateOfOrder.NEW){
            return true;
        } else {
            return false;
        }
    } 

    /** Updates the StateOfOrder and reset the work order currently working on to Null. 
    */
    public RepairOrderDTO faultListDone(){
        if (setStateAwaitingReview() == true){
            repOrder.setStateOfOrder(StateOfOrder.MECHANIC_REVIEW);
       }
        RepairOrderDTO repOrderDTO = repOrder.getRepairOrderDTO();
        this.repOrder = null;
        return repOrderDTO;
    }

    /** Creates a Faults List where the mechanic can note the faults observed
     * 
     * @param repOrderID The ID of the Report
     * @throws OperationFailedException if connection to the database is not established
     */
    public RepairOrderDTO addDiagnosticReport(int repOrderID)
                        throws OperationFailedException{
        specifyReport(repOrderID);
        return repOrder.addDiagnosticReport();
    }

    /**Creates a Task List where the mechanic can note the recomended repairs
     * 
     * @param repOrderID the Repair order id being worked on
     * @throws OperationFailedException if connection to the database is not established
     */
    public RepairOrderDTO addProposedRepairs(int repOrderID)
                        throws OperationFailedException{
        specifyReport(repOrderID);
        return repOrder.addProposedRepairs();
    }

    /** Adds a task to the Task list currently working on.
     * 
     * @param nameTask The recomened repair task
     * @param cost the cost of that task
     * @param timeMin the time it takes to fix that task
     */
    public RepairOrderDTO addTaskToRepairs(String nameTask, int cost, int timeMin){
        return repOrder.addTaskToRepairs(nameTask, cost, timeMin);    
    }

    /** Resets the work order currently working on to Null 
     * and initiates the final calculations for the Proposed Repairs
    */
    public RepairOrderDTO propRepairsListDone(){
        repOrder.propRepairsListDone();
        RepairOrderDTO repOrderDTO = repOrder.getRepairOrderDTO();
        this.repOrder = null;
        return repOrderDTO;
    }


    /**Sets the orders Status, 
     * returns the Order reciept if the status is 'StateOfOrder.CUSTOMER_REVIEW'
     * 
     * @param repOrderID ID of the order to be changed
     * @param stateOfOrder The state of order to change to
     * @return A string confirming the change of state, 
     * or the Order reciept if the state being <code> CUSTOMER_REVIEW </code>
     * @throws OperationFailedException if connection to the database is not established
     */
    public String setOrderStatus(int repOrderID, StateOfOrder stateOfOrder)
                        throws OperationFailedException{
        specifyReport(repOrderID);
        return repOrder.setOrderStatus(stateOfOrder);
    }

    /** Adds observers to the list. The specified observer will be notified
     *  when this repair order has been updated with new information.
     * 
     * @param rov The observer to notify. 
     */
    public void addRepOrderObserver(RepairOrderObserver rov) {
        repOrder.addRepOrderObserver(rov);
    }

    /** Sets the discount based on strategy
     * 
     * @param strategy the discount strategy to be set
     * @throws OperationFailedException if the database is not connected
     */
    public void setDiscount(int repOrderID, DiscountStrategy strategy)
                        throws OperationFailedException{
        specifyReport(repOrderID);
        repOrder.setDiscount(strategy);
        repOrder = null;
    }

}
