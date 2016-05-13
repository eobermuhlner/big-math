package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigDecimalMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

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
		assertEquals(BigDecimal.valueOf(3.14), BigDecimalMath.log(BigDecimal.valueOf(2), MC));
	}

}
