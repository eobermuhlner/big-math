package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class SinhCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.sinhAsSingleton(true);

        SinhCalculator firstInstance = SinhCalculator.instance();
        SinhCalculator secondInstance = SinhCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.sinhAsSingleton(false);

        SinhCalculator firstInstance = SinhCalculator.instance();
        SinhCalculator secondInstance = SinhCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}