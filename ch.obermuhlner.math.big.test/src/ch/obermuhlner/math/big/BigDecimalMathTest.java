package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigDecimalMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	private static final MathContext MC_CHECK_DOUBLE = MathContext.DECIMAL32;

	@Test(expected = ArithmeticException.class)
	public void testPowIntZeroPowerNegative() {
		BigDecimalMath.pow(BigDecimal.valueOf(0), -5, MC);
	}
	
	@Test
	public void testPowIntPositive() {
		// positive exponents
		for(int x : new int[] { -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5 }) {
			for(int y : new int[] { 0, 1, 2, 3, 4, 5 }) {
				assertEquals(
						x + "^" + y,
						BigDecimal.valueOf((int) Math.pow(x, y)),
						BigDecimalMath.pow(BigDecimal.valueOf(x), y, MC));
			}
		}
	}
	
	@Test
	public void testPowIntNegative() {
		// positive exponents
		for(int x : new int[] { -5, -4, -3, -2, -1, 1, 2, 3, 4, 5 }) { // no x=0 !
			for(int y : new int[] { -5, -4, -3, -2, -1}) {
				assertEquals(
						x + "^" + y,
						BigDecimal.ONE.divide(BigDecimal.valueOf((int) Math.pow(x, -y)), MC),
						BigDecimalMath.pow(BigDecimal.valueOf(x), y, MC));
			}
		}
	}

	@Test
	public void testSqrt() {
		for(double value : new double[] { 0, 0.1, 2, 10, 33.3333 }) {
			assertEquals(
					"sqrt(" + value + ")",
					toCheck(Math.sqrt(value)),
					toCheck(BigDecimalMath.sqrt(BigDecimal.valueOf(value), MC)));
		}
	}

	@Test
	public void testLog() {
		for(double value : new double[] { 0.1, 2, 10, 33.3333 }) {
			assertEquals("log(" + value + ")",
					toCheck(Math.log(value)),
					toCheck(BigDecimalMath.log(BigDecimal.valueOf(value), MC)));
		}
	}

	private static BigDecimal toCheck(double value) {
		long longValue = (long) value;
		if (value == (double)longValue) {
			return toCheck(BigDecimal.valueOf(longValue));
		}
		
		return toCheck(BigDecimal.valueOf(value));
	}

	private static BigDecimal toCheck(BigDecimal value) {
		return value.round(MC_CHECK_DOUBLE);
	}
}
