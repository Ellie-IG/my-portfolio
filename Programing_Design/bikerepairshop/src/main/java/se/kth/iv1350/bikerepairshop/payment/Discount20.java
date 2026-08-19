package se.kth.iv1350.bikerepairshop.payment;

/** Will handle prices during 20% discount
 * 
 */
public class Discount20 implements DiscountStrategy {


    /** specify how much a price should be after discount. 
     * This will be a whole number, based on the ceiling value as money should not be lost
     * 
     * @param priceBefore the price before discount
     * @return price should be after discount. Will be a whole number (ceiling)
     */
    @Override
    public int priceAfterDiscount(int priceBefore){
        return (int) Math.ceil(0.8*priceBefore);
    }
    
}
