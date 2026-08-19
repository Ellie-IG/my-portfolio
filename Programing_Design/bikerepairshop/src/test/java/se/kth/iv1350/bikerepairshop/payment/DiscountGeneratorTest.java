package se.kth.iv1350.bikerepairshop.payment;

/**
 * Runs the standard suite against the Composition implementation.
 */
public class DiscountGeneratorTest extends DiscountStrategyTest {

    @Override
    protected DiscountStrategy createStrategy(int maxUsages) {
        return new DiscountGenerator(maxUsages);
    }
}