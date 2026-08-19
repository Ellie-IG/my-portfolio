package se.kth.iv1350.bikerepairshop.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Runs the standard suite against the Inheritance implementation, 
 * plus unique tests for its encapsulation leaks.
 */
public class InheritanceDiscountStrategyTest extends DiscountStrategyTest {

    @Override
    protected DiscountStrategy createStrategy(int maxUsages) {
        return new DiscountRandom(maxUsages);
    }

    @Test
    public void testInheritanceExposesLeakySuperclassMethods() {
        DiscountRandom inheritanceStrategy = new DiscountRandom(5);
        
        // This test specifically checks for the inheritance flaw.
        // It wouldn't compile in the Composition test class!
        inheritanceStrategy.setSeed(999L); 
        
        assertNotNull(inheritanceStrategy);
    }
}