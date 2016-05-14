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
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 10; y++) {
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
		for (int x = -5; x <= 5; x++) {
			for (int y = -10; y < 0; y++) {
				if (x != 0) {
					assertEquals(
							x + "^" + y,
							BigDecimal.ONE.divide(BigDecimal.valueOf((int) Math.pow(x, -y)), MC),
							BigDecimalMath.pow(BigDecimal.valueOf(x), y, MC));
				}
			}
		}
	}
	
	@Test
	public void testLog() {
		for(double value : new double[] { 0.1, 2, 10, 33.3333 }) {
			assertEquals("log(" + value + ")", toCheck(Math.log(value)), BigDecimalMath.log(BigDecimal.valueOf(value), MC).round(MC_CHECK_DOUBLE));
		}
	}

	private static BigDecimal toCheck(double value) {
		return toCheck(BigDecimal.valueOf(value));
	}

	private static BigDecimal toCheck(BigDecimal value) {
		return value.round(MC_CHECK_DOUBLE);
	}
}
