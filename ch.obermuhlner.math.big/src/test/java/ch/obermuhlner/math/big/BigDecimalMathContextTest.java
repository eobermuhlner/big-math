package ch.obermuhlner.math.big;

import static ch.obermuhlner.math.big.BigDecimalMath.DECIMAL32;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.junit.Test;

public class BigDecimalMathContextTest {

	@Test
	public void testValueOf() {
		assertEquals(new BigDecimal("55555560", MathContext.DECIMAL32), DECIMAL32.valueOf("55555555"));		
		assertEquals(new BigDecimal("55555560", MathContext.DECIMAL32), DECIMAL32.valueOf(55555555));		
		assertEquals(new BigDecimal("55555560", MathContext.DECIMAL32), DECIMAL32.valueOf(55555555L));		
		assertEquals(new BigDecimal("55555560", MathContext.DECIMAL32), DECIMAL32.valueOf(55555555.0));		
		assertEquals(new BigDecimal("55555560", MathContext.DECIMAL32), DECIMAL32.valueOf(new BigInteger("55555555")));		
	}
	
	@Test
	public void testAdd() {
		assertEquals(DECIMAL32.valueOf("55555560"), DECIMAL32.add(new BigDecimal("22222222"), new BigDecimal("33333333")));
	}

	@Test
	public void testSubtract() {
		assertEquals(DECIMAL32.valueOf("22222220"), DECIMAL32.subtract(new BigDecimal("55555555"), new BigDecimal("33333333")));
	}

	@Test
	public void testMultiply() {
		assertEquals(DECIMAL32.valueOf("66666670"), DECIMAL32.multiply(new BigDecimal("2"), new BigDecimal("33333333")));
	}

	@Test
	public void testDivide() {
		assertEquals(DECIMAL32.valueOf("0.3333333"), DECIMAL32.divide(new BigDecimal("1"), new BigDecimal("3")));
	}

	public void testDivideToIntegralValue() {
		BigDecimal x = new BigDecimal("7777");
		BigDecimal y = new BigDecimal("3");
		assertEquals(x.divideToIntegralValue(y, MathContext.DECIMAL32), DECIMAL32.divideToIntegralValue(x, y));
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideToIntegralValueFail() {
		DECIMAL32.divideToIntegralValue(new BigDecimal("100000000"), new BigDecimal("3"));
	}

	public void testDivideAndRemainder() {
		BigDecimal x = new BigDecimal("7777");
		BigDecimal y = new BigDecimal("3");
		assertArrayEquals(x.divideAndRemainder(y, MathContext.DECIMAL32), DECIMAL32.divideAndRemainder(x, y));
	}

	@Test
	public void testLog() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.log(x, MathContext.DECIMAL32), DECIMAL32.log(x));
	}
	
	@Test
	public void testLog2() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.log2(x, MathContext.DECIMAL32), DECIMAL32.log2(x));
	}
	
	@Test
	public void testLog10() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.log10(x, MathContext.DECIMAL32), DECIMAL32.log10(x));
	}
	
	@Test
	public void testSqrt() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.sqrt(x, MathContext.DECIMAL32), DECIMAL32.sqrt(x));
	}
	
	@Test
	public void testRoot() {
		BigDecimal x = new BigDecimal("2");
		BigDecimal y = new BigDecimal("3");
		assertEquals(BigDecimalMath.root(x, y, MathContext.DECIMAL32), DECIMAL32.root(x, y));
	}
	
	@Test
	public void testExp() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.exp(x, MathContext.DECIMAL32), DECIMAL32.exp(x));
	}
	
	@Test
	public void testPow() {
		BigDecimal x = new BigDecimal("2");
		BigDecimal y = new BigDecimal("3");
		assertEquals(BigDecimalMath.pow(x, y, MathContext.DECIMAL32), DECIMAL32.pow(x, y));
	}
	
	@Test
	public void testPowInt() {
		BigDecimal x = new BigDecimal("2");
		int y = 3;
		assertEquals(BigDecimalMath.pow(x, y, MathContext.DECIMAL32), DECIMAL32.pow(x, y));
	}
	
	@Test
	public void testSin() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.sin(x, MathContext.DECIMAL32), DECIMAL32.sin(x));
	}
	
	@Test
	public void testCos() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.cos(x, MathContext.DECIMAL32), DECIMAL32.cos(x));
	}
	
	@Test
	public void testTan() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.tan(x, MathContext.DECIMAL32), DECIMAL32.tan(x));
	}
	
	@Test
	public void testCot() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.cot(x, MathContext.DECIMAL32), DECIMAL32.cot(x));
	}
	
	@Test
	public void testAsin() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.asin(x, MathContext.DECIMAL32), DECIMAL32.asin(x));
	}
	
	@Test
	public void testAcos() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.acos(x, MathContext.DECIMAL32), DECIMAL32.acos(x));
	}
	
	@Test
	public void testAtan() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.atan(x, MathContext.DECIMAL32), DECIMAL32.atan(x));
	}
	
	@Test
	public void testAcot() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.acot(x, MathContext.DECIMAL32), DECIMAL32.acot(x));
	}

	@Test
	public void testSinh() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.sinh(x, MathContext.DECIMAL32), DECIMAL32.sinh(x));
	}
	
	@Test
	public void testCosh() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.cosh(x, MathContext.DECIMAL32), DECIMAL32.cosh(x));
	}
	
	@Test
	public void testTanh() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.tanh(x, MathContext.DECIMAL32), DECIMAL32.tanh(x));
	}
	
	@Test
	public void testAsinh() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.asinh(x, MathContext.DECIMAL32), DECIMAL32.asinh(x));
	}
	
	@Test
	public void testAcosh() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.acosh(x, MathContext.DECIMAL32), DECIMAL32.acosh(x));
	}
	
	@Test
	public void testAtanh() {
		BigDecimal x = new BigDecimal("0.1");
		assertEquals(BigDecimalMath.atanh(x, MathContext.DECIMAL32), DECIMAL32.atanh(x));
	}
	
	@Test
	public void testAcoth() {
		BigDecimal x = new BigDecimal("2");
		assertEquals(BigDecimalMath.acoth(x, MathContext.DECIMAL32), DECIMAL32.acoth(x));
	}
	
	@Test
	public void testPi() {
		assertEquals(BigDecimalMath.pi(MathContext.DECIMAL32), DECIMAL32.pi());
	}
	
	@Test
	public void testE() {
		assertEquals(BigDecimalMath.e(MathContext.DECIMAL32), DECIMAL32.e());
	}
	
	@Test
	public void testBernoulli() {
		int n = 4;
		assertEquals(BigDecimalMath.bernoulli(n, MathContext.DECIMAL32), DECIMAL32.bernoulli(n));
	}
}
