package se.kth.iv1350.bikerepairshop.data;

public class Bike {
    private final int serialNumber; // = 23490;
    private final String model; // = "dam";
    private final String brand; // = "Hellkama";

    /** Creates a new instance 
     * 
     * @param serialNumber The serial number on the bike
     * @param model The model of the bike
     * @param brand The brand of the bike 
     */
    public Bike(int serialNumber, String model, String brand){
        this.serialNumber = serialNumber;
        this.model = model;
        this.brand = brand;
    }

    public int getSerialNumber(){
        return this.serialNumber;
    }

    public String getModel(){
        return this.model;
    }

    public String getBrand(){
        return this.brand;
    }
}
