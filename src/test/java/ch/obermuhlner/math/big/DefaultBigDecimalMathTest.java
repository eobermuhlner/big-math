package ch.obermuhlner.math.big;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

import static ch.obermuhlner.util.ThreadUtil.runMultiThreaded;
import static org.junit.Assert.assertEquals;

public class DefaultBigDecimalMathTest {
    @Test
    public void testDefaultMathContext() {
        assertEquals(MathContext.DECIMAL128, DefaultBigDecimalMath.getDefaultMathContext());

        MathContext mc = new MathContext(100);
        DefaultBigDecimalMath.setDefaultMathContext(mc);
        assertEquals(mc, DefaultBigDecimalMath.getDefaultMathContext());

        DefaultBigDecimalMath.setDefaultMathContext(MathContext.DECIMAL128);
    }

    @Test
    public void testRound() {
        assertEquals(
                BigDecimalMath.round(BigDecimal.valueOf(1.23456), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.round(BigDecimal.valueOf(1.23456)));
    }

    @Test
    public void testRoundWithTrailingZeroes() {
        assertEquals(
                BigDecimalMath.roundWithTrailingZeroes(BigDecimal.valueOf(1.23456), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.roundWithTrailingZeroes(BigDecimal.valueOf(1.23456)));
    }

    @Test
    public void testAdd() {
        assertEquals(
                BigDecimal.valueOf(2).add(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.add(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testSubtract() {
        assertEquals(
                BigDecimal.valueOf(2).subtract(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.subtract(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testMultiply() {
        assertEquals(
                BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.multiply(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testDivide() {
        assertEquals(
                BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.divide(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testRemainder() {
        assertEquals(
                BigDecimal.valueOf(2).remainder(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.remainder(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testE() {
        assertEquals(
                BigDecimalMath.e(DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.e());
    }

    @Test
    public void testPi() {
        assertEquals(
                BigDecimalMath.pi(DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.pi());
    }

    @Test
    public void testBernoulli() {
        assertEquals(
                BigDecimalMath.bernoulli(12, DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.bernoulli(12));
    }

    @Test
    public void testReciprocal() {
        assertEquals(
                BigDecimalMath.reciprocal(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.reciprocal(BigDecimal.valueOf(3)));
    }

    @Test
    public void testFactorial() {
        assertEquals(
                BigDecimalMath.factorial(BigDecimal.valueOf(1.1), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.factorial(BigDecimal.valueOf(1.1)));
    }

    @Test
    public void testGamma() {
        assertEquals(
                BigDecimalMath.gamma(BigDecimal.valueOf(1.1), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.gamma(BigDecimal.valueOf(1.1)));
    }

    @Test
    public void testPowInt() {
        assertEquals(
                BigDecimalMath.pow(BigDecimal.valueOf(1.1), 33, DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.pow(BigDecimal.valueOf(1.1), 33));
    }

    @Test
    public void testPow() {
        assertEquals(
                BigDecimalMath.pow(BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.pow(BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.3)));
    }

    @Test
    public void testSqrt() {
        assertEquals(
                BigDecimalMath.sqrt(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(3)));
    }

    @Test
    public void testRoot() {
        assertEquals(
                BigDecimalMath.root(BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.root(BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.3)));
    }

    @Test
    public void testLog() {
        assertEquals(
                BigDecimalMath.log(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.log(BigDecimal.valueOf(3)));
    }

    @Test
    public void testLog2() {
        assertEquals(
                BigDecimalMath.log2(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.log2(BigDecimal.valueOf(3)));
    }

    @Test
    public void testLog10() {
        assertEquals(
                BigDecimalMath.log10(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.log10(BigDecimal.valueOf(3)));
    }

    @Test
    public void testExp() {
        assertEquals(
                BigDecimalMath.exp(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.exp(BigDecimal.valueOf(3)));
    }

    @Test
    public void testSin() {
        assertEquals(
                BigDecimalMath.sin(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.sin(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAsin() {
        assertEquals(
                BigDecimalMath.asin(BigDecimal.valueOf(0.3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.asin(BigDecimal.valueOf(0.3)));
    }

    @Test
    public void testCos() {
        assertEquals(
                BigDecimalMath.cos(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.cos(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAcos() {
        assertEquals(
                BigDecimalMath.acos(BigDecimal.valueOf(0.3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.acos(BigDecimal.valueOf(0.3)));
    }

    @Test
    public void testTan() {
        assertEquals(
                BigDecimalMath.tan(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.tan(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAtan() {
        assertEquals(
                BigDecimalMath.atan(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.atan(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAtan2() {
        assertEquals(
                BigDecimalMath.atan2(BigDecimal.valueOf(2), BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.atan2(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
    }

    @Test
    public void testCot() {
        assertEquals(
                BigDecimalMath.cot(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.cot(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAcot() {
        assertEquals(
                BigDecimalMath.acot(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.acot(BigDecimal.valueOf(3)));
    }

    @Test
    public void testSinh() {
        assertEquals(
                BigDecimalMath.sinh(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.sinh(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAsinh() {
        assertEquals(
                BigDecimalMath.asinh(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.asinh(BigDecimal.valueOf(3)));
    }

    @Test
    public void testCosh() {
        assertEquals(
                BigDecimalMath.cosh(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.cosh(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAcosh() {
        assertEquals(
                BigDecimalMath.acosh(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.acosh(BigDecimal.valueOf(3)));
    }

    @Test
    public void testTanh() {
        assertEquals(
                BigDecimalMath.tanh(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.tanh(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAtanh() {
        assertEquals(
                BigDecimalMath.atanh(BigDecimal.valueOf(0.222), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.atanh(BigDecimal.valueOf(0.222)));
    }

    @Test
    public void testCoth() {
        assertEquals(
                BigDecimalMath.coth(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.coth(BigDecimal.valueOf(3)));
    }

    @Test
    public void testAcoth() {
        assertEquals(
                BigDecimalMath.acoth(BigDecimal.valueOf(3), DefaultBigDecimalMath.getDefaultMathContext()),
                DefaultBigDecimalMath.acoth(BigDecimal.valueOf(3)));
    }

    @Test
    public void testCreateLocalMathContext() {
        try (DefaultBigDecimalMath.LocalMathContext context = DefaultBigDecimalMath.createLocalMathContext(5)) {
            assertEquals(BigDecimal.valueOf(3.1416), DefaultBigDecimalMath.pi());
        }

        try (DefaultBigDecimalMath.LocalMathContext context = DefaultBigDecimalMath.createLocalMathContext(5, RoundingMode.DOWN)) {
            assertEquals(BigDecimal.valueOf(3.1415), DefaultBigDecimalMath.pi());
        }

        try (DefaultBigDecimalMath.LocalMathContext context = DefaultBigDecimalMath.createLocalMathContext(new MathContext(3))) {
            assertEquals(BigDecimal.valueOf(3.14), DefaultBigDecimalMath.pi());
        }
    }

    @Test
    public void testWithLocalMathContext() {
        DefaultBigDecimalMath.withLocalMathContext(5, () -> {
            assertEquals(BigDecimal.valueOf(3.1416), DefaultBigDecimalMath.pi());
        });

        DefaultBigDecimalMath.withLocalMathContext(5, RoundingMode.DOWN, () -> {
            assertEquals(BigDecimal.valueOf(3.1415), DefaultBigDecimalMath.pi());
        });

        DefaultBigDecimalMath.withLocalMathContext(new MathContext(3), () -> {
            assertEquals(BigDecimal.valueOf(3.14), DefaultBigDecimalMath.pi());
        });
    }

    @Test
    public void testNestedWithLocalMathContext() {
        assertNestedWithLocalMathContext(5, 3);
    }

    private void assertNestedWithLocalMathContext(int precision1, int precision2) {
        assertEquals(DefaultBigDecimalMath.getDefaultMathContext(), DefaultBigDecimalMath.currentMathContext());

        DefaultBigDecimalMath.withLocalMathContext(precision1, () -> {
            assertEquals(precision1, DefaultBigDecimalMath.currentMathContext().getPrecision());
            assertEquals(BigDecimalMath.pi(new MathContext(precision1)), DefaultBigDecimalMath.pi());

            DefaultBigDecimalMath.withLocalMathContext(precision2, () -> {
                assertEquals(precision2, DefaultBigDecimalMath.currentMathContext().getPrecision());
                assertEquals(BigDecimalMath.pi(new MathContext(precision2)), DefaultBigDecimalMath.pi());
            });

            assertEquals(precision1, DefaultBigDecimalMath.currentMathContext().getPrecision());
            assertEquals(BigDecimalMath.pi(new MathContext(precision1)), DefaultBigDecimalMath.pi());
        });

        assertEquals(DefaultBigDecimalMath.getDefaultMathContext(), DefaultBigDecimalMath.currentMathContext());
    }

    @Test
    public void testNestedWithLocalMathContextMultiThreaded() throws Throwable {
        Random random = new Random(1);
        runMultiThreaded(100, () -> {
            assertNestedWithLocalMathContext(random.nextInt(100) + 1, random.nextInt(100) + 1);
        });
    }
}
