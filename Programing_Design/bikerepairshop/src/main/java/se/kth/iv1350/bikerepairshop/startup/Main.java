package se.kth.iv1350.bikerepairshop.startup;

import se.kth.iv1350.bikerepairshop.dbhandler.CustomerRegistrySingleton;
import se.kth.iv1350.bikerepairshop.dbhandler.RepairOrderRegistry;
import se.kth.iv1350.bikerepairshop.dbhandler.DataBaseConnection;
import se.kth.iv1350.bikerepairshop.controller.Controller;
import se.kth.iv1350.bikerepairshop.data.Bike;
import se.kth.iv1350.bikerepairshop.view.View;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.logger.ErrorMessageUserLogger;
import se.kth.iv1350.bikerepairshop.logger.FileLogger;

/**
 * Contains the <code>main</code> method. 
 * Performs all startup of the application.
 */

public class Main {

    /** Establishes connections to databases
     * 
     * @param con the dataBase to connect to
     */
    private void connect(DataBaseConnection con){
        try {
            con.connect();
        } catch (DataBaseConnectionException exp) {
            FileLogger f = new FileLogger("Error");
            f.log("In the " + exp.getConnectionRegistry() + " the following error occured: " +  exp.getErrorMessage());
            ErrorMessageUserLogger e = new ErrorMessageUserLogger();
            e.log("The " + exp.getConnectionRegistry() + " could not be connected");
        }
    }

    /**
     * Performs all startup of the application.
     * 
     *  @param args The application does not take any command line parameters.
     */
    public void startUp(String[] args) {
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        connect(repOrderReg);
        CustomerRegistrySingleton customerReg = CustomerRegistrySingleton.getInstance();
        connect(customerReg);
        
        Controller contr = new Controller(repOrderReg, customerReg, new FileLogger("Error"));
        View view = new View(contr, new ErrorMessageUserLogger());
    }

    /** A mock version of the program
     * 
     * @param args
     */
    public void main(String[] args) {
        // 1. Initialize DB Registries
        RepairOrderRegistry repOrderReg = new RepairOrderRegistry();
        CustomerRegistrySingleton customerReg = CustomerRegistrySingleton.getInstance();
        while(true) {
            connect(repOrderReg);
            connect(customerReg);
            if(repOrderReg.isConnect() && customerReg.isConnect()){
                break;
            }
        }

        // 2. Insert mock startup data (moved from your old View.main)
        try {
            Bike bike = new Bike(23490, "dam", "helkama");
            customerReg.addCustomer("0733976999", "ellen.gronholm@hotmail.com", "ellen", bike);
        } catch (DataBaseConnectionException exc) {
            new ErrorMessageUserLogger().log("Startup data error: " + exc.getErrorMessage());
        }

        // 3. Initialize Controller
        Controller contr = new Controller(repOrderReg, customerReg, new FileLogger("Error"));
        
        // 4. Initialize View and hand control over to it!
        View view = new View(contr, new ErrorMessageUserLogger());
        
        // 5. The view runs the simulation flow and triggers the controller.
        view.runFakeExecution(); 
    }
    
}
