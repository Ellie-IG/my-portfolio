package se.kth.iv1350.bikerepairshop.payment;

/** Interface to specify how much a price should be after discount
 * 
 */
public interface DiscountStrategy {

    /** specify how much a price should be after discount
     * 
     * @param priceBefore the price before discount
     * @return price should be after discount. Will be a whole number (ceiling)
     */
    public int priceAfterDiscount(int priceBefore);
    
}
