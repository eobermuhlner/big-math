package ch.obermuhlner.math.big.internal;

import org.junit.Assert;
import org.junit.Test;

public class AtanhCalculatorTest {

    @Test
    public void testIsSingleton() {
        CalculatorConfiguration.atanhAsSingleton(true);

        AtanhCalculator firstInstance = AtanhCalculator.instance();
        AtanhCalculator secondInstance = AtanhCalculator.instance();

        Assert.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testIsNotSingleton() {
        CalculatorConfiguration.atanhAsSingleton(false);

        AtanhCalculator firstInstance = AtanhCalculator.instance();
        AtanhCalculator secondInstance = AtanhCalculator.instance();

        Assert.assertNotEquals(firstInstance, secondInstance);
    }

}