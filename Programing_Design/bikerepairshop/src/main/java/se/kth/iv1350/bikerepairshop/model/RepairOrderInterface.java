package se.kth.iv1350.bikerepairshop.model;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerDTO;

public interface RepairOrderInterface {

    /** @return The current state of the repair order.
     */
    public StateOfOrder getStateOfOrder();

    /** @return The customer associated with this repair order.
     */
    public CustomerDTO getCustomer();

    /** @return The list describing the reported problems.
     */
    public FaultsList getProblemDescription() ;

    /** @return The diagnostic report containing identified faults.
     */
    public FaultsList getDiagnosticReport() ;

    /** @return The proposed repairs for this repair order.
     */
    public ProposedRepairs getPropRepairs() ;

    /** @return The date of the repair order .
     */
    public String getDate();

    /** @return The unique ID of the repair order.
     */
    public int getRepOrderID() ;

}
