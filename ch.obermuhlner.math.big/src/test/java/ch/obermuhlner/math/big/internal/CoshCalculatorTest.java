package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class CoshCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.coshAsSingleton(true);

        CoshCalculator firstInstance = CoshCalculator.instance();
        CoshCalculator secondInstance = CoshCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.coshAsSingleton(false);

        CoshCalculator firstInstance = CoshCalculator.instance();
        CoshCalculator secondInstance = CoshCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}