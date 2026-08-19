package se.kth.iv1350.bikerepairshop.dbhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.kth.iv1350.bikerepairshop.data.*;
import se.kth.iv1350.bikerepairshop.exeptionhandler.ObjectNotFoundException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;
import se.kth.iv1350.bikerepairshop.exeptionhandler.EmptyRegistryException;


/**
 * Contains all calls to the object Customer. The data base is a Singleton
 */
public class CustomerRegistrySingleton implements DataBaseConnection{
    private static final CustomerRegistrySingleton INSTANCE = new CustomerRegistrySingleton();

    private final List<Customer> customerList= new ArrayList<>();
    private boolean connected = false;

    private CustomerRegistrySingleton() { 
    }

    /** @return the singleton instance.
     */
    public static CustomerRegistrySingleton getInstance() {
        return INSTANCE;
    }

    @Override
    /** Connects the system to the Customer Registry database
     * 
     * @throws DataBaseConnectionException if connection to the database could not be established
     */
    public void connect() throws DataBaseConnectionException {
        Random r = new Random();
        int randomValue = r.nextInt(10);
        if (randomValue == 4) {
            throw new DataBaseConnectionException("Could not connect to the Database", "Customer Registry");
        }
        connected = true;
    }

    @Override
    /** Disconnects the system from the Customer Registry database
     * 
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public void disconnect() throws DataBaseConnectionException{
    }

    @Override
    /** Checks if the system still has a connection to the Customer Registry database
     * 
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public boolean isConnect() {
        return connected;
    }

    /** Finds a customer in the database from their phonenumber.
     * 
     * @param phoneNumer The phonenumber of the customer that should be found in data base
     * @return If found, returns the correct CustomerDTO. If not found returns <code>NULL</code> 
     * @throws CustomerNotFoundException if the customer is not found in the database
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public CustomerDTO findCustomerFromPhoneNumberCR(String phoneNumber)
                                    throws DataBaseConnectionException, ObjectNotFoundException, EmptyRegistryException, IOException {
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was never established", "Customer Registry");
        }

        if(customerList.size() == 0) {
            throw new EmptyRegistryException("there are no yet recorded customers", "Customer Registry");
        }
                
        for (Customer customer : customerList) {
            if (customer.getPhoneNumber().equals(phoneNumber)){
                return new CustomerDTO(customer.getPhoneNumber(), customer.getEmailAdress(), 
                                       customer.getName(), customer.getBike());
            }
        }
        throw new ObjectNotFoundException("The phone number was not found in the database.", phoneNumber, "Customer");
    }

    /** @return the customer list
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public List<Customer> getCustomerList() throws DataBaseConnectionException{
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was never established", "Customer Registry");
        }
        return customerList;
    }

    /**Creates and adds a customer to the registry
     * 
     * @param phoneNumber of the customer
     * @param emailAdress of the customer
     * @param name of the customer
     * @param bike of the customer
     * @return The customer object just created and added
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public Customer addCustomer(String phoneNumber, String emailAdress, String name, 
                        Bike bike) throws DataBaseConnectionException {
        if (!connected) {
            throw new DataBaseConnectionException("Connection to the Database was never established", "Customer Registry");
        }

        Customer customer =  new Customer(phoneNumber, emailAdress, name, bike);
        customerList.add(customer);
        return customer;
    }


}
