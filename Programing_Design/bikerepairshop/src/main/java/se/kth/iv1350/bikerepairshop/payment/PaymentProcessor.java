package se.kth.iv1350.bikerepairshop.payment;

/**Processes payment by appliying discounts as needed
 * 
 */
public class PaymentProcessor {
    private DiscountStrategy strategy;

    public PaymentProcessor(){
        strategy = new Discount0();
    }

    /** Sets the discount through the strategy
     * 
     * @param strategy the discount strategy that should be used.
     */
    public void setDiscountStrategy(DiscountStrategy strategy){
        this.strategy = strategy;
    }

    /** Applies a discount based on strategy to a price 
     * 
     * @param priceBefore the brife before discount
     * @return price after discount as an int
     */
    public int applyDiscount(int priceBefore){
        return strategy.priceAfterDiscount(priceBefore);
    }

}
