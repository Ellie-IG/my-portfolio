package se.kth.iv1350.bikerepairshop.data;

public class Customer {
    private final String phoneNumber; //= "0733976992";
    private final String emailAdress; // = "ellen.gronholm@hotmail.com";
    private final String name; // = "Ellen Gronholm";
    private final Bike bike; // = new Bike();¨

    /** Creates new instance
     * 
     * @param phoneNumber
     * @param emailAdress
     * @param name
     * @param bike
     */
    public Customer(String phoneNumber, String emailAdress, String name, 
                        Bike bike){
        this.phoneNumber = phoneNumber;
        this.emailAdress = emailAdress;
        this.name = name;
        this.bike = new Bike(bike.getSerialNumber(), bike.getModel(), bike.getBrand());
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
    public Bike getBike(){
        return this.bike;
    }

}
