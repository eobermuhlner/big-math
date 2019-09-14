package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class ExpCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.expAsSingleton(true);

        ExpCalculator firstInstance = ExpCalculator.instance();
        ExpCalculator secondInstance = ExpCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.expAsSingleton(false);

        ExpCalculator firstInstance = ExpCalculator.instance();
        ExpCalculator secondInstance = ExpCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}