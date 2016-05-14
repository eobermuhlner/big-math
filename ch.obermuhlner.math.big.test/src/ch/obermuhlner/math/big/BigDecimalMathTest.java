package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigDecimalMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	private static final MathContext MC_CHECK_DOUBLE = MathContext.DECIMAL32;

	@Test
	public void testInternals() {
		assertEquals(toCheck(2.0), toCheck(BigDecimal.valueOf(2)));
		assertEquals(toCheck(2.0), toCheck(BigDecimal.valueOf(2.0)));
		
		assertEquals(null, toCheck(Double.NaN));
		assertEquals(null, toCheck(Double.NEGATIVE_INFINITY));
		assertEquals(null, toCheck(Double.POSITIVE_INFINITY));
	}
	
	@Test
	public void testIsIntValue() {
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MIN_VALUE)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MAX_VALUE)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(0)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(-55)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(33)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(-55.0)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(33.0)));

		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MIN_VALUE - 1L)));
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MAX_VALUE + 1L)));
		
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(3.333)));
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(-5.555)));
	}
	
	@Test
	public void testFactorial() {
		assertEquals(new BigDecimal("1"), BigDecimalMath.factorial(0));
		assertEquals(new BigDecimal("1"), BigDecimalMath.factorial(1));
		assertEquals(new BigDecimal("2"), BigDecimalMath.factorial(2));
		assertEquals(new BigDecimal("6"), BigDecimalMath.factorial(3));
		assertEquals(new BigDecimal("24"), BigDecimalMath.factorial(4));
		assertEquals(new BigDecimal("120"), BigDecimalMath.factorial(5));
		
		assertEquals(
				new BigDecimal("9425947759838359420851623124482936749562312794702543768327889353416977599316221476503087861591808346911623490003549599583369706302603264000000000000000000000000"),
				BigDecimalMath.factorial(101));
	}

	@Test(expected = ArithmeticException.class)
	public void testPowIntZeroPowerNegative() {
		BigDecimalMath.pow(BigDecimal.valueOf(0), -5, MC);
	}

	@Test
	public void testPow() {
		for(double x : new double[] { 1, 2, 3, 4, 5 }) {
			for(double y : new double[] { -5, -4, -3, -2.5, -2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2, 2.5, 3, 4, 5 }) {
				assertEquals(
						x + "^" + y,
						toCheck(Math.pow(x, y)),
						toCheck(BigDecimalMath.pow(BigDecimal.valueOf(x), BigDecimal.valueOf(y), MC)));
			}
		}
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

	@Test
	public void testExp() {
		for(double value : new double[] { -5, -1, 0.1, 2, 10 }) {
			assertEquals("exp(" + value + ")",
					toCheck(Math.exp(value)),
					toCheck(BigDecimalMath.exp(BigDecimal.valueOf(value), MC)));
		}
	}

	@Test
	public void testSin() {
		for(double value : new double[] { -5, -1, -0.3, 0, 0.1, 2, 10 }) {
			assertEquals("sin(" + value + ")",
					toCheck(Math.sin(value)),
					toCheck(BigDecimalMath.sin(BigDecimal.valueOf(value), MC)));
		}
	}

	@Test
	public void testCos() {
		for(double value : new double[] { -5, -1, -0.3, 0, 0.1, 2, 10 }) {
			assertEquals("cos(" + value + ")",
					toCheck(Math.cos(value)),
					toCheck(BigDecimalMath.cos(BigDecimal.valueOf(value), MC)));
		}
	}

	private static BigDecimal toCheck(double value) {
		long longValue = (long) value;
		if (value == (double)longValue) {
			return toCheck(BigDecimal.valueOf(longValue));
		}
		
		if (Double.isFinite(value)) {
			return toCheck(BigDecimal.valueOf(value));
		}
		
		return null;
	}

	private static BigDecimal toCheck(BigDecimal value) {
		return value.round(MC_CHECK_DOUBLE);
	}
}
