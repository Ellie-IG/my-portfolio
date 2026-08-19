package se.kth.iv1350.bikerepairshop.payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the discount strategy implementations to verify usage limits
 * and highlight architectural differences.
 */
public abstract class DiscountStrategyTest {

    private DiscountStrategy strategy;
    private int originalPrice;

    /**
     * Factory method to be implemented by concrete test classes.
     */
    protected abstract DiscountStrategy createStrategy(int maxUsages);


    @BeforeEach 
    public void setUp(){
        originalPrice = 100;
        int maxUsages = 2;
        strategy = createStrategy(maxUsages);
    }

    @AfterEach
    public void tearDown(){
        strategy = null;
    }
    /**
     * Tests that the composition strategy successfully caps the number of times
     * a discount is applied and reverts to the original price.
     */
    @Test
    public void testCompositionEnforcesLimit() {

        // First two calls should apply some random discount (price should be <= 100)
        int price1 = strategy.priceAfterDiscount(originalPrice);
        int price2 = strategy.priceAfterDiscount(originalPrice);
        
        // Third call exceeds the limit of 2, must return the exact original price (0% discount)
        int price3 = strategy.priceAfterDiscount(originalPrice);

        assertTrue(price1 <= originalPrice, "First roll should be a valid price.");
        assertTrue(price2 <= originalPrice, "Second roll should be a valid price.");
        assertEquals(originalPrice, price3, "Third roll must return original price (limit reached).");
    }

}