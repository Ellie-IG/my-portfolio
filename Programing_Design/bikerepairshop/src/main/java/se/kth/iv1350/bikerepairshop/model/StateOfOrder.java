package se.kth.iv1350.bikerepairshop.model;

public enum StateOfOrder {
    /** The order is just created and has no information to it.
     */
    NEW, 

    /** The order contains customer Information and complaints.
     */
    MECHANIC_REVIEW, 
    
    /** Mechanic has done diagnostic and assesed tasks to fix.
     */
    MECHANIC_COMPLETED,  
    
    /** The customer must review the diagnostic and propossed repairs.
     */
    CUSTOMER_REVIEWING, 
    
    /** The customer has accepted the proposed repairs.
     */
    ACCEPTED,
    
    /** The customer has rejected the proposed repairs.
     */
    REJECTED, 
    
    /** The customer has paid.
     */
    PAID
}
