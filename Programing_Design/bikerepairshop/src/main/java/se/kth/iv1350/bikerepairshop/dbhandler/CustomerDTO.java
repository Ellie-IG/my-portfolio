package se.kth.iv1350.bikerepairshop.dbhandler;

import se.kth.iv1350.bikerepairshop.data.Bike;

/**
 * Contains information about one specific customer.
 */
public class CustomerDTO {
    private final String phoneNumber;
    private final String emailAdress;
    private final String name;
    private final BikeDTO bike;

    /**
     * Creates a new instance
     * 
     * @param phoneNumber the customers phone number.
     * @param emailAdress the customers email adress.
     * @param name the customers name.
     * @param bike the bike owned by the customer. 
     */
    public CustomerDTO(String phoneNumber, String emailAdress, String name, 
                        Bike bike){
        this.phoneNumber = phoneNumber;
        this.emailAdress = emailAdress;
        this.name = name;
        this.bike = new BikeDTO(bike.getSerialNumber(), bike.getModel(), bike.getBrand());
    }

    /**
     * Returns the customers phone number
     * 
     * @return the customers phone number. Must contain a value
     */
    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    /**
     * Returns the customers email adress
     * 
     * @return the customers email adress. 
     *         <code> null </code> if missing from customer data.
     */
    public String getEmailAdress(){
        return this.emailAdress;
    }

    /**
     * Returns the customers name
     * 
     * @return the customers name. 
     *         <code> null </code> if missing from customer data.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the customers bike
     * 
     * @return the customers bike. 
     *         <code> null </code> if missing from customer data.
     */
    public BikeDTO getBike(){
        return this.bike;
    }
}
