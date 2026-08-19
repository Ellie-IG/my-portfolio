package se.kth.iv1350.bikerepairshop.model;

import java.time.LocalDateTime;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;

/** Contains information about one specific Repair Order.
 */
public class RepairOrderDTO implements RepairOrderInterface{
    private StateOfOrder stateOfOrder;
    private CustomerDTO customer;
    private FaultsList problemDescription;
    private FaultsList diagnosticReport;
    private ProposedRepairs propRepairs;
    private String date;
    private int repOrderID;

    /** Creates a new instance
     */
    public RepairOrderDTO(StateOfOrder stateOfOrder, CustomerDTO customer, 
                          FaultsList problemDescription, FaultsList diagnosticReport,
                          ProposedRepairs proposedRepairs, int repOrderID) { 
        this.stateOfOrder = stateOfOrder;
        this.customer = customer;
        this.problemDescription = problemDescription;
        this.diagnosticReport = diagnosticReport;
        this.propRepairs = proposedRepairs;
        this.date = LocalDateTime.now().toString();
        this.repOrderID = repOrderID;
    }

    /** @return The current state of the repair order.
     */
    public StateOfOrder getStateOfOrder() {
        return stateOfOrder;
    }

    /** @return The customer associated with this repair order.
     */
    public CustomerDTO getCustomer() {
        return customer;
    }

    /** @return The list describing the reported problems.
     */
    public FaultsList getProblemDescription() {
        return problemDescription;
    }

    /** @return The diagnostic report containing identified faults.
     */
    public FaultsList getDiagnosticReport() {
        return diagnosticReport;
    }

    /** @return The proposed repairs for this repair order.
     */
    public ProposedRepairs getPropRepairs() {
        return propRepairs;
    }

    /** @return The date of the repair order .
     */
    public String getDate() {
        return date;
    }

    /** @return The unique ID of the repair order.
     */
    public int getRepOrderID() {
        return repOrderID;
    }

}
