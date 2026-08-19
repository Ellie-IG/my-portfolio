package se.kth.iv1350.bikerepairshop.payment;

import java.util.Random;

/**A random discount strategy that enforces a usage limit using composition.
 * This class encapsulates a {@link Random} instance.
 */
public class DiscountGenerator implements DiscountStrategy {
    private final Random randomSource = new Random();
    private final int maxUsages;
    private int currentUsages = 0;

    /**Constructs a new random discount strategy with a specific usage limit.
     *
     * @param maxUsages the maximum number of times a random discount can be applied
     */
    public DiscountGenerator(int maxUsages) {
        this.maxUsages = maxUsages;
    }

    /**Calculates the price after a random percentage discount, 
     * up to a limited number of executions.
     *
     * @param priceBefore the price before discount
     * @return the price after the random discount, or the original price if the limit is reached
     */
    @Override
    public int priceAfterDiscount(int priceBefore) {
        if (currentUsages >= maxUsages) {
            return priceBefore;
        }
        currentUsages++;
        
        int discountPercent = randomSource.nextInt(51);
        double discountMultiplier = (100.0 - discountPercent) / 100.0;
        
        return (int) Math.ceil(priceBefore * discountMultiplier);
    }
}