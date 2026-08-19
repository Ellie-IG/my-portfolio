package se.kth.iv1350.bikerepairshop.payment;

import java.util.Random;

/**A random number generator that attempts to enforce a usage limit via inheritance.
 * This class extends {@link Random} to override a specific generation method.
 */
public class DiscountRandom extends Random implements DiscountStrategy{
    private final int maxUsages;
    private int currentUsages = 0;

    /** Constructs a new generator with a specified maximum usage limit.
     *
     * @param maxUsages the maximum number of times random integers can be generated
     */
    public DiscountRandom(int maxUsages) {
        this.maxUsages = maxUsages;
    }

    /**Returns a random integer between 0 and the specified upper limit,
     * as long as the usage limit has not been exceeded.
     *
     * @param bound the upper limit. Must be positive.
     * @return the next random integer, or 0 if the limit is reached
     */
    @Override
    public int nextInt(int bound) {
        if (currentUsages >= maxUsages) {
            return 0;
        }
        currentUsages++;
        return super.nextInt(bound);
    }

    /**
     * Calculates the price after a random percentage discount (0-50%), 
     * up to a limited number of executions.
     *
     * @param priceBefore the price before discount
     * @return the price after the random discount, or the original price if the limit is reached
     */
    @Override
    public int priceAfterDiscount(int priceBefore) {
        
        int discountPercent = nextInt(51); //Up to 50%
        double discountMultiplier = (100.0 - discountPercent) / 100.0;
        
        return (int) Math.ceil(discountMultiplier*priceBefore);
    }
}