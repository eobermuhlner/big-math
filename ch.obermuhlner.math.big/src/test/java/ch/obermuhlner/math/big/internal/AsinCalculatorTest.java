package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class AsinCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.asinAsSingleton(true);

        AsinCalculator firstInstance = AsinCalculator.instance();
        AsinCalculator secondInstance = AsinCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.asinAsSingleton(false);

        AsinCalculator firstInstance = AsinCalculator.instance();
        AsinCalculator secondInstance = AsinCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }
}