package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class SinCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.sinAsSingleton(true);

        SinCalculator firstInstance = SinCalculator.instance();
        SinCalculator secondInstance = SinCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.sinAsSingleton(false);

        SinCalculator firstInstance = SinCalculator.instance();
        SinCalculator secondInstance = SinCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}