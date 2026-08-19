package se.kth.iv1350.bikerepairshop.payment;

/**
 * Runs the standard suite against the Composition implementation.
 */
public class Discount15Test extends DiscountStrategyTest {

    @Override
    protected DiscountStrategy createStrategy(int maxUsages) {
        return new Discount15();
    }
}