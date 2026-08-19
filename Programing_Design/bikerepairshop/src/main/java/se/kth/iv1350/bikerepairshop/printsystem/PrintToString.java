package se.kth.iv1350.bikerepairshop.printsystem;

import java.util.*;

import se.kth.iv1350.bikerepairshop.model.RepairOrderInterface;

/** Gathers all material from the RepairOrder and creates a printable string from it
 */
public class PrintToString {
    
    private RepairOrderInterface repOrder;

    /** @param repOrder Sets the repOrder that should be converted
     */
    private void setRepOrder(RepairOrderInterface repOrder){
        this.repOrder = repOrder;
    }
    
    /**Builds a formatted string containing bike details.
     *
     * @return a string with the bikes serial number, model and brand
     */
    private StringBuilder printBikeInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bike:\n");
        sb.append("\t SerialNumber:\t").append(repOrder.getCustomer().getBike().getSerialNumber()).append("\n");
        sb.append("\t Model:\t\t").append(repOrder.getCustomer().getBike().getModel()).append("\n");
        sb.append("\t Brand:\t\t").append(repOrder.getCustomer().getBike().getBrand()).append("\n\n");
        return sb;
    }

    /**Builds a formatted string containing customer details.
     *
     * @return a string with the customer's name, phone number, email, and bike
     */
    private StringBuilder printCustomerInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer Info:\n -------------------------------\n");
        sb.append("Name:\t\t").append(repOrder.getCustomer().getName()).append("\n");
        sb.append("Phone Number:\t").append(repOrder.getCustomer().getPhoneNumber()).append("\n");
        sb.append("Email Address:\t").append(repOrder.getCustomer().getEmailAdress()).append("\n");
        sb.append(printBikeInfo());
        return sb;
    }

    /**Builds a formatted string describing the reported problems.
     *
     * @return a string containing the problem description
     */
    private StringBuilder printProblemDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Problem Description: \n -------------------------------\n");
        return sb.append(repOrder.getProblemDescription().listToString() + "\n\n");
    }

    /** Builds a formatted string containing the diagnostic report.
     *
     * @return a string with diagnostic details
     */
    private StringBuilder printDiagnosticReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagnostic Report: \n -------------------------------\n");
        return sb.append(repOrder.getDiagnosticReport().listToString() + "\n\n");
    }

    /**Builds a formatted string describing the proposed repairs.
     *
     * @return a string with proposed repair actions
     */
    private StringBuilder printProposedRepairs() {
        StringBuilder sb = new StringBuilder();
        sb.append("Propossed Repairs: \n -------------------------------\n");
        return sb.append(repOrder.getPropRepairs().taskCostToString() + "\n\n");
    }

    /** Builds a formatted string representing the order date.
     *
     * @return a string containing the date value
     */
    private StringBuilder printDate() {
        Date d1 = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("Date:\t\t");
        return sb.append(d1).append("\n\n");
        }

    /** Builds a formatted string containing the repair order ID.
     *
     * @return a string with the repair order identifier
     */
    private StringBuilder printRepairOrderID() {
        StringBuilder sb = new StringBuilder();
        return sb.append("ID:\t\t").append(repOrder.getRepOrderID()).append("\n");
    }

    /** Combines all order-related sections into one formatted string.
     * @param repOrder the repair order that should be converted to text
     * @return a complete string representation of the order
     */
    String printFullOrder(RepairOrderInterface repOrder) {
        setRepOrder(repOrder);

        StringBuilder repairOrder = new StringBuilder();
        repairOrder.append("============Reciept============\n");
        repairOrder.append(printCustomerInfo()).append(printProblemDescription());
        
        if (repOrder.getDiagnosticReport() != null) {
            repairOrder.append(printDiagnosticReport());
        }

        if (repOrder.getPropRepairs() != null) {
            repairOrder.append(printProposedRepairs());
        }
        
        repairOrder.append("===============================\n");
        repairOrder.append(printDate()).append(printRepairOrderID());
        repairOrder.append("===============================\n\n");

        return repairOrder.toString(); 
    }

}
