package se.kth.iv1350.bikerepairshop.printsystem;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import se.kth.iv1350.bikerepairshop.model.RepairOrderInterface;

/** Handles commmunicate between the RepairOrder and the printer to print the receipt
 * 
 */
public class PrintToReccipt {

    /** Sends the reciept of a Repair order back as a String
     * 
     * @param repOrder the Repair order to be converted
     * @return a STring of the order
     */
    public String showReceipt(RepairOrderInterface repOrder) {
        PrintToString message = new PrintToString();
        return message.printFullOrder(repOrder);
    }

    /** moves the string to a txt file
     * 
     * @param orderReciept The string containing the data printed
     * @param fileName the filename it should be written in
     * @throws IOException 
     */
    private void moveDataToFile(String orderReciept, String fileName)
                                throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(orderReciept);
        printWriter.close();
    }

    private void sendToPrinter(String filename)
                                throws FileNotFoundException {
        //This method will send the file to the printer and print it
    }

    /** Prints the recpeit from printer 
     * 
     * @param repOrder the repair order to be printed
     * @return a String notifying if the code sucessfully printed or not.
     */
    public String print(RepairOrderInterface repOrder){
        PrintToString message = new PrintToString();
        String orderReciept = message.printFullOrder(repOrder);
        String fileName = "recieptRepairOrder" + repOrder.getRepOrderID() + ".txt";
        
        try {
            moveDataToFile(orderReciept, fileName);
        } catch (IOException e) {
            return "Error when writing the order to the file: " + e;
        } 

        try {
            sendToPrinter(fileName);
        } catch (FileNotFoundException e) {
            
        }

        
        // Add try-if for the printing system so errors with printing machine can be handled.
        return "The reciept has been printed";

    }
    
}
