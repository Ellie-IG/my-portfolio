package se.kth.iv1350.bikerepairshop.model;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;
import se.kth.iv1350.bikerepairshop.payment.DiscountStrategy;
import se.kth.iv1350.bikerepairshop.printsystem.PrintToReccipt;

/**This object contains all information about the repair: Customer, bike, 
 * problems observed, problems measured, recomened fixes, the date, and an ID
 */
public class RepairOrder implements RepairOrderInterface{
    private StateOfOrder stateOfOrder;
    private CustomerDTO customer;
    private FaultsList problemDescription;
    private FaultsList diagnosticReport;
    private ProposedRepairs propRepairs;
    private String date;
    private int repOrderID;

    private List<RepairOrderObserver> repOrderObservers = new ArrayList<>();

    /** Creates a new instance
     */
    public RepairOrder(CustomerDTO customer, int repOrderID) { 
        this.customer = customer;
        this.repOrderID = repOrderID;
        this.stateOfOrder = StateOfOrder.NEW;
    }

    /** @param state sets the state 
     */
    public void setStateOfOrder(StateOfOrder state){
        stateOfOrder = state;
        if (state == StateOfOrder.MECHANIC_REVIEW) {
            notifyObservers();
        }
    }

    @Override
    /** @return The current state of the repair order.
     */
    public StateOfOrder getStateOfOrder() {
        return stateOfOrder;
    }

    @Override
    /** @return The customer associated with this repair order.
     */
    public CustomerDTO getCustomer() {
        return customer;
    }

    @Override
    /** @return The list describing the reported problems.
     */
    public FaultsList getProblemDescription() {
        return problemDescription;
    }

    @Override
    /** @return The diagnostic report containing identified faults.
     */
    public FaultsList getDiagnosticReport() {
        return diagnosticReport;
    }

    @Override
    /** @return The proposed repairs for this repair order.
     */
    public ProposedRepairs getPropRepairs() {
        return propRepairs;
    }

    @Override
    /** @return The date of the repair order .
     */
    public String getDate() {
        return date;
    }

    @Override
    /** @return The unique ID of the repair order.
     */
    public int getRepOrderID() {
        return repOrderID;
    }

    /** @return RepairOrderDTO Created a new instance of the repair order DTO
     */
    public RepairOrderDTO getRepairOrderDTO() {
        return new RepairOrderDTO(stateOfOrder, customer,
                                  problemDescription, 
                                  diagnosticReport, propRepairs,
                                  repOrderID);
    }

    /** @return a new instance of FaultsList
     */
    private FaultsList createFaultList(){
        return new FaultsList();
    }

    /** Initiates the Problem description
     */
    public RepairOrderDTO addProblemDescription(){
        this.problemDescription = createFaultList();
        return getRepairOrderDTO();
    }

    /** Initiates the Diagnostic report
     */
    public RepairOrderDTO addDiagnosticReport(){
        this.diagnosticReport = createFaultList();
        return getRepairOrderDTO();
    }

    /** Adds a fault to the Fault list currently working on.
     * @param fault the observed fault
     */
    public RepairOrderDTO addObservedFault(FaultInBike fault){
        if(stateOfOrder == StateOfOrder.NEW){
            problemDescription.addFaultToList(fault);
        } else {
            diagnosticReport.addFaultToList(fault);
        } 
        return getRepairOrderDTO();
    }

    /** Initiates the Proposed Repairs 
     */
    public RepairOrderDTO addProposedRepairs(){
        this.propRepairs = new ProposedRepairs();
        return getRepairOrderDTO();
    }

    /** Adds a task to the task list currently working on.
     * 
     * @param nameTask The recomened repair task
     * @param cost the cost of that task
     * @param timeMin the time it takes to fix that task
     */
    public RepairOrderDTO addTaskToRepairs(String nameTask, int cost, int timeMin){
        propRepairs.addTaskToList(nameTask, cost, timeMin);
        return getRepairOrderDTO();
    }
    
    /** Initiates the final calculations for the Proposed Repairs
     * and uppdates the state
    */
    public RepairOrderDTO propRepairsListDone(){
        propRepairs.propRepairsListDone();
        setStateOfOrder(StateOfOrder.MECHANIC_COMPLETED);
        notifyObservers();
        return getRepairOrderDTO();
    }

    /** @return the order reciept as a string 
     */
    public String getRepairOrderAsString(){
        PrintToReccipt printToReccipt = new PrintToReccipt();
        return printToReccipt.showReceipt(this.getRepairOrderDTO());
    }

    /** Sets the order status based on input
     * 
     * @param stateOfOrder the state that the order should be changed to. 
     * @return a confirmatuion of the change. The reciept as well if it is up for customer review.
    */
    public String setOrderStatus(StateOfOrder stateOfOrder){

        if(stateOfOrder == StateOfOrder.CUSTOMER_REVIEWING){
            setStateOfOrder(stateOfOrder);
            String printToReciept = getRepairOrderAsString();
            String confirmation = "order set to " + stateOfOrder;
            return confirmation + "\n\n" + printToReciept;
        } 
        else if(stateOfOrder == StateOfOrder.ACCEPTED){
            setStateOfOrder(stateOfOrder);
            PrintToReccipt printToReccipt = new PrintToReccipt();
            printToReccipt.print(this.getRepairOrderDTO());
        }
        else if(stateOfOrder == StateOfOrder.REJECTED){
            setStateOfOrder(stateOfOrder);
        }
        else if(stateOfOrder == StateOfOrder.PAID){
            setStateOfOrder(stateOfOrder);
        }
        return (getRepairOrderAsString());

    }

    private void notifyObservers() {
        for (RepairOrderObserver rov : repOrderObservers) {
            rov.repairOrderIsUpdated(getRepairOrderDTO());
        }
    }

    /** Adds observers to the list. The specified observer will be notified
     *  when this repair order has been updated with new information.
     * 
     * @param rov The observer to notify. 
     */
    public void addRepOrderObserver(RepairOrderObserver rov) {
        repOrderObservers.add(rov);
    }

    /** Sets the discount based on strategy
     * 
     * @param strategy the discount strategy to be set
     */
    public void setDiscount(DiscountStrategy strategy){
        propRepairs.setDiscount(strategy);
    }

}
