package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class CosCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.cosAsSingleton(true);

        CosCalculator firstInstance = CosCalculator.instance();
        CosCalculator secondInstance = CosCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.cosAsSingleton(false);

        CosCalculator firstInstance = CosCalculator.instance();
        CosCalculator secondInstance = CosCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}