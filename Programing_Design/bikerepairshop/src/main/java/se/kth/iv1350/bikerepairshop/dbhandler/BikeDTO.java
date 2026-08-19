package se.kth.iv1350.bikerepairshop.dbhandler;

/**
 * Contains information about one specific bike.
 */
public class BikeDTO {
    private final int serialNumber;
    private final String model;
    private final String brand;

    /**
     * Creates a new instance 
     * 
     * @param serialNumber The serial number on the bike
     * @param model The model of the bike
     * @param brand The brand of the bike 
     */
    public BikeDTO(int serialNumber, String model, String brand){
        this.serialNumber = serialNumber;
        this.model = model;
        this.brand = brand;
    }

    /**
     * Returns the serial number on the bike
     * 
     * @return the serial number on the bike. Must contain a value.
     */
    public int getSerialNumber(){
        return this.serialNumber;
    }

    /**
     * Returns the model of the bike.
     * 
     * @return the model of the bike. Must contain a value.
     */
    public String getModel(){
        return this.model;
    }

    /**
     * Returns the brand of the bike.
     * 
     * @return the brand of the bike. Must contain a value.
     */
    public String getBrand(){
        return this.brand;
    }
}
