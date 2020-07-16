package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigComplexTest {

	private static final MathContext MC = MathContext.DECIMAL64;
	 private static final MathContext MC_LARGE = MathContext.DECIMAL128;
	 private static final MathContext MC_SMALL = MathContext.DECIMAL32;

	@Test
	public void testConstants() {
		assertEquals(BigDecimal.ZERO, BigComplex.ZERO.re);
		assertEquals(BigDecimal.ZERO, BigComplex.ZERO.im);
		
		assertEquals(BigDecimal.ONE, BigComplex.ONE.re);
		assertEquals(BigDecimal.ZERO, BigComplex.ONE.im);
		
		assertEquals(BigDecimal.ZERO, BigComplex.I.re);
		assertEquals(BigDecimal.ONE, BigComplex.I.im);
	}
	
	@Test
	public void testValueOf() {
		assertEquals(BigDecimal.valueOf(1.2), BigComplex.valueOf(BigDecimal.valueOf(1.2), BigDecimal.valueOf(3.4)).re);
		assertEquals(BigDecimal.valueOf(3.4), BigComplex.valueOf(BigDecimal.valueOf(1.2), BigDecimal.valueOf(3.4)).im);
		
		assertEquals(BigDecimal.valueOf(1.2), BigComplex.valueOf(1.2, 3.4).re);
		assertEquals(BigDecimal.valueOf(3.4), BigComplex.valueOf(1.2, 3.4).im);
		
		assertEquals(BigDecimal.valueOf(1.2), BigComplex.valueOf(BigDecimal.valueOf(1.2)).re);
		assertEquals(BigDecimal.ZERO, BigComplex.valueOf(BigDecimal.valueOf(1.2)).im);
		
		assertEquals(BigDecimal.valueOf(1.2), BigComplex.valueOf(1.2).re);
		assertEquals(BigDecimal.ZERO, BigComplex.valueOf(1.2).im);
	}

	@Test
	public void testValueOfConstants() {
		assertSame(BigComplex.ZERO, BigComplex.valueOf(0.0));
		assertSame(BigComplex.ZERO, BigComplex.valueOf(0.0, 0.0));
		assertSame(BigComplex.ZERO, BigComplex.valueOf(BigDecimal.ZERO, BigDecimal.ZERO));
		
		assertSame(BigComplex.ONE, BigComplex.valueOf(BigDecimal.ONE, BigDecimal.ZERO));
		assertSame(BigComplex.ONE, BigComplex.valueOf(1.0, 0.0));
		assertSame(BigComplex.ONE, BigComplex.valueOf(1.0, 0.0));
		
		assertSame(BigComplex.I, BigComplex.valueOf(BigDecimal.ZERO, BigDecimal.ONE));
		assertSame(BigComplex.I, BigComplex.valueOf(0.0, 1.0));
	}

	@Test
	public void testValueOfPolar() {
		assertDelta(BigComplex.valueOf(1.0, 0.0), BigComplex.valueOfPolar(1.0, 0.0, MC), BigDecimal.valueOf(1E-10));
		assertDelta(BigComplex.valueOf(-1.0, 0.0), BigComplex.valueOfPolar(BigDecimal.valueOf(1.0), BigDecimalMath.pi(MC), MC), BigDecimal.valueOf(1E-10));
	}

	@Test
	public void testValueOfPolarConstants() {
		assertSame(BigComplex.ZERO, BigComplex.valueOfPolar(0.0, 1.1, MC));
		assertSame(BigComplex.ZERO, BigComplex.valueOfPolar(0.0, -9.999, MC));
	}

	@Test
	public void testRe() {
		assertEquals(BigComplex.valueOf(1.1, 0.0), BigComplex.valueOf(1.1, 2.2).re());
	}

	@Test
	public void testIm() {
		assertEquals(BigComplex.valueOf(0.0, 2.2), BigComplex.valueOf(1.1, 2.2).im());
	}

	@Test
	public void testAdd() {
		assertEquals(BigComplex.valueOf(1.7, 4.0), BigComplex.valueOf(1.2, 3.4).add(BigComplex.valueOf(0.5, 0.6), MC));
		assertEquals(BigComplex.valueOf(1.7, 3.4), BigComplex.valueOf(1.2, 3.4).add(BigDecimal.valueOf(0.5), MC));

		assertEquals(BigComplex.valueOf(1.7, 4.0), BigComplex.valueOf(1.2, 3.4).add(BigComplex.valueOf(0.5, 0.6)));
		assertEquals(BigComplex.valueOf(1.7, 3.4), BigComplex.valueOf(1.2, 3.4).add(BigDecimal.valueOf(0.5)));
		assertEquals(BigComplex.valueOf(1.7, 3.4), BigComplex.valueOf(1.2, 3.4).add(0.5));
	}
	
	@Test
	public void testSubtract() {
		assertEquals(BigComplex.valueOf(0.7, 2.8), BigComplex.valueOf(1.2, 3.4).subtract(BigComplex.valueOf(0.5, 0.6), MC));
		assertEquals(BigComplex.valueOf(0.7, 3.4), BigComplex.valueOf(1.2, 3.4).subtract(BigDecimal.valueOf(0.5), MC));

		assertEquals(BigComplex.valueOf(0.7, 2.8), BigComplex.valueOf(1.2, 3.4).subtract(BigComplex.valueOf(0.5, 0.6)));
		assertEquals(BigComplex.valueOf(0.7, 3.4), BigComplex.valueOf(1.2, 3.4).subtract(BigDecimal.valueOf(0.5)));
		assertEquals(BigComplex.valueOf(0.7, 3.4), BigComplex.valueOf(1.2, 3.4).subtract(0.5));
	}
	
	@Test
	public void testMultiply() {
		assertEquals(BigComplex.valueOf(-7.8, 10.4), BigComplex.valueOf(1.2, 3.4).multiply(BigComplex.valueOf(2.0, 3.0), MC));
		assertEquals(BigComplex.valueOf(2.4, 6.8), BigComplex.valueOf(1.2, 3.4).multiply(BigComplex.valueOf(2.0, 0.0), MC));
		assertEquals(BigComplex.valueOf(2.4, 6.8), BigComplex.valueOf(1.2, 3.4).multiply(BigDecimal.valueOf(2.0), MC));

		assertEquals(BigComplex.valueOf(-7.8, 10.4), BigComplex.valueOf(1.2, 3.4).multiply(BigComplex.valueOf(2.0, 3.0)));
		assertEquals(BigComplex.valueOf(2.4, 6.8), BigComplex.valueOf(1.2, 3.4).multiply(BigComplex.valueOf(2.0, 0.0)));
		assertEquals(BigComplex.valueOf(2.4, 6.8), BigComplex.valueOf(1.2, 3.4).multiply(BigDecimal.valueOf(2.0)));
		assertEquals(BigComplex.valueOf(2.4, 6.8), BigComplex.valueOf(1.2, 3.4).multiply(2.0));
	}
	
	@Test
	public void testDivide() {
		assertEquals(BigComplex.valueOf(0.8, 0.1), BigComplex.valueOf(1.2, 3.4).divide(BigComplex.valueOf(2.0, 4.0), MC));
		assertEquals(BigComplex.valueOf(0.6, 1.7), BigComplex.valueOf(1.2, 3.4).divide(BigDecimal.valueOf(2.0), MC));
		assertEquals(BigComplex.valueOf(0.6, 1.7), BigComplex.valueOf(1.2, 3.4).divide(2.0, MC));
	}

	@Test public void testReciprocal () {
		assertEquals(BigComplex.valueOf(2.0 / (2 * 2 + 3 * 3), -3.0 / (2 * 2 + 3 * 3)).round(MC_SMALL), BigComplex.valueOf(2, 3).reciprocal(MC_SMALL));
	}

	@Test
	public void testConjugate() {
		BigComplex orig = BigComplex.valueOf(1.2, 3.4);
		
		assertEquals(orig.re, orig.conjugate().re);
		assertEquals(orig.im.negate(), orig.conjugate().im);
		
		assertEquals(BigDecimal.valueOf(1.2), orig.conjugate().re);
		assertEquals(BigDecimal.valueOf(-3.4), orig.conjugate().im);
	}

	@Test
	public void testNegate() {
		assertEquals(BigComplex.ZERO, BigComplex.ZERO.negate());
		assertEquals(BigComplex.valueOf(-1.0), BigComplex.ONE.negate());
		assertEquals(BigComplex.valueOf(0.0, -1.0), BigComplex.I.negate());
		assertEquals(BigComplex.valueOf(-1.2, -3.4), BigComplex.valueOf(1.2, 3.4).negate());
	}

	@Test
	public void testAbs() {
		assertCompareTo(BigDecimal.ZERO, BigComplex.ZERO.abs(MC));
		assertCompareTo(BigDecimal.ONE, BigComplex.ONE.abs(MC));
		assertCompareTo(BigDecimal.ONE, BigComplex.I.abs(MC));
		assertCompareTo(BigDecimalMath.sqrt(BigDecimal.valueOf(2*2+3*3), MC), BigComplex.valueOf(2, 3).abs(MC));
	}

	@Test
	public void testAbsSquare() {
		assertCompareTo(BigDecimal.ZERO, BigComplex.ZERO.absSquare(MC));
		assertCompareTo(BigDecimal.ONE, BigComplex.ONE.absSquare(MC));
		assertCompareTo(BigDecimal.ONE, BigComplex.I.absSquare(MC));
		assertCompareTo(BigDecimal.valueOf(2*2+3*3), BigComplex.valueOf(2, 3).absSquare(MC));
	}

	@Test(expected = ArithmeticException.class)
	public void testAngleFailZero() {
		assertCompareTo(BigDecimal.ZERO, BigComplex.ZERO.angle(MC));
	}
	
	@Test
	public void testAngle() {
		assertCompareTo(BigDecimal.ZERO, BigComplex.ONE.angle(MC));
		assertCompareTo(BigDecimalMath.pi(MC_LARGE).divide(BigDecimal.valueOf(2), MC), BigComplex.I.angle(MC));
	}
	
	@Test
	public void testIsReal() {
		assertEquals(true, BigComplex.ZERO.isReal());
		assertEquals(true, BigComplex.ONE.isReal());
		assertEquals(false, BigComplex.I.isReal());
	}
	
	@Test
	public void testEquals() {
		assertEquals(false, BigComplex.ONE.equals(null));
		assertEquals(false, BigComplex.ONE.equals("string"));
		assertEquals(false, BigComplex.ONE.equals(BigComplex.ZERO));
		assertEquals(false, BigComplex.valueOf(1, 2).equals(BigComplex.valueOf(1, 999)));
		assertEquals(false, BigComplex.valueOf(1, 2).equals(BigComplex.valueOf(999, 2)));
		
		assertEquals(true, BigComplex.ZERO.equals(BigComplex.ZERO));
		assertEquals(true, BigComplex.valueOf(1, 2).equals(BigComplex.valueOf(1, 2)));
		assertEquals(true, BigComplex.valueOf(1, 2).equals(BigComplex.valueOf(1.0, 2.0)));
		assertEquals(true, BigComplex.valueOf(1, 2).equals(BigComplex.valueOf(new BigDecimal("1.00000"), new BigDecimal("2.0000"))));
	}
	
	@Test
	public void testStrictEquals() {
		assertEquals(false, BigComplex.ONE.strictEquals(null));
		assertEquals(false, BigComplex.ONE.strictEquals("string"));
		assertEquals(false, BigComplex.ONE.equals(BigComplex.ZERO));
		assertEquals(false, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(1, 999)));
		assertEquals(false, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(999, 2)));
		
		assertEquals(true, BigComplex.ZERO.strictEquals(BigComplex.ZERO));
		assertEquals(true, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(1, 2)));
		assertEquals(true, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(1.0, 2.0)));
		assertEquals(false, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(new BigDecimal("1"), new BigDecimal("2"))));
		assertEquals(false, BigComplex.valueOf(1, 2).strictEquals(BigComplex.valueOf(new BigDecimal("1.00000"), new BigDecimal("2.0000"))));
	}

	@Test
	public void testHashCode() {
		assertEquals(BigComplex.ZERO.hashCode(), BigComplex.ZERO.hashCode());
		assertEquals(BigComplex.ONE.hashCode(), BigComplex.ONE.hashCode());
		assertEquals(BigComplex.I.hashCode(), BigComplex.I.hashCode());

		assertNotEquals(BigComplex.ZERO.hashCode(), BigComplex.ONE.hashCode());
		assertNotEquals(BigComplex.ONE.hashCode(), BigComplex.I.hashCode());
		assertNotEquals(BigComplex.I.hashCode(), BigComplex.ZERO.hashCode());
	}
	
	@Test
	public void testToString() {
		assertEquals("(1.2 + 3.4 i)", BigComplex.valueOf(1.2, 3.4).toString());
		assertEquals("(1.2 - 3.4 i)", BigComplex.valueOf(1.2, -3.4).toString());
		
		assertEquals("(1.2 + 0.0 i)", BigComplex.valueOf(1.2, 0.0).toString());
		
		assertEquals("(0.0 + 3.4 i)", BigComplex.valueOf(0.0, 3.4).toString());
	}

	private void assertCompareTo(BigDecimal expected, BigDecimal actual) {
		assertEquals(expected + " compareTo(" + actual + ")", 0, expected.compareTo(actual));
	}

	private void assertDelta(BigDecimal expected, BigDecimal actual, BigDecimal allowedDelta) {
		BigDecimal diff = actual.subtract(expected).abs();
		assertEquals("diff=" +diff + " <= allowed=" + allowedDelta + ")", true, diff.compareTo(allowedDelta) <= 0);
	}

	private void assertDelta(BigComplex expected, BigComplex actual, BigDecimal allowedDelta) {
		BigDecimal diffRe = actual.re.subtract(expected.re).abs();
		BigDecimal diffIm = actual.im.subtract(expected.im).abs();
		assertEquals("Re diff=" +diffRe + " <= allowed=" + allowedDelta + ")", true, diffRe.compareTo(allowedDelta) <= 0);
		assertEquals("Im diff=" +diffIm + " <= allowed=" + allowedDelta + ")", true, diffIm.compareTo(allowedDelta) <= 0);
	}
}
