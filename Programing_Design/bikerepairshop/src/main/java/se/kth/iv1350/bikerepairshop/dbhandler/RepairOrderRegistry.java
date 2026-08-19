package se.kth.iv1350.bikerepairshop.dbhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.model.RepairOrder;

/**
 * Contains all calls to the object RepairOrder 
 */
public class RepairOrderRegistry implements DataBaseConnection{
    private List<RepairOrder> repairOrderList= new ArrayList<>();
    private int lastIndex;
    private boolean connected;

    /**
     * Creates a new instance
     */
    public RepairOrderRegistry() { 
        lastIndex = 0;

    }

    @Override
    /** Connects the system to the Repair Order database
     * 
     * @throws DataBaseConnectionException if connection to the database could not be established
     */
    public void connect() throws DataBaseConnectionException {
        Random r = new Random();
        int randomValue = r.nextInt(10);
        if (randomValue == 4) {
            throw new DataBaseConnectionException("Could not connect to the Database", "Repair Order Registry");
        }
        connected = true;
    }

    @Override
    /** Disconnects the system from the Repair Order database
     * 
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public void disconnect() throws DataBaseConnectionException{

    }

    @Override
    /** Checks if the system still has a connection to the Repair Order database
     * @return <code> true </code> if connection is up with the database. 
     * Otherwise <code> false </code> is returned.
     */
    public boolean isConnect(){
        return true;
    }

    /** @return the repair order list
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public List<RepairOrder> getRepairOrderList() 
                        throws DataBaseConnectionException{
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was not established", "Repair Order Registry");
        }
        return repairOrderList;
    }

    /** Adds a new Repair Order to the registryfor the customer who requested it.
     * It also increases the last index to know how many reports are in the registry.
     * 
     * @param customer the customer who has created an order
     * @return The instance of the last repair order 
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public RepairOrder addRepairOrder(CustomerDTO customer)
                        throws DataBaseConnectionException{
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was not established", "Repair Order Registry");
        }

        RepairOrder repOrder =  new RepairOrder(customer, lastIndex++);
        repairOrderList.add(repOrder);
        return repOrder;
    }

    /**Reurns the desired RepairOrder
     * 
     * @param repOrderID the identifier of the Repair order
     * @return the wished for Repair Order
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public RepairOrder getRepairOrder(int repOrderID)
                        throws ObjectNotFoundException, DataBaseConnectionException, EmptyRegistryException{
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was never established", "Repair Order Registry");
        }

        if(repairOrderList.size() == 0) {
            throw new EmptyRegistryException("there are no yet recorded repair orders", "Repair Order Registry");
        }

        try {
            return repairOrderList.get(repOrderID);
        } catch (Exception exc) {
            throw new ObjectNotFoundException("The repair order id was not found in the database.", String.valueOf(repOrderID), "Repair Order");
        }
    }
}