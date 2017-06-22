package ch.obermuhlner.math.big;

import static ch.obermuhlner.math.big.BigDecimalMath.DECIMAL32;
import static org.junit.Assert.assertEquals;

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

	@Test(expected = ArithmeticException.class)
	public void testDivideToIntegralValue() {
		DECIMAL32.divideToIntegralValue(new BigDecimal("100000000"), new BigDecimal("3"));
	}
	
	@Test
	public void testLog() {
		assertEquals(DECIMAL32.valueOf(Math.log(2)), DECIMAL32.log(new BigDecimal("2")));
	}
	
	@Test
	public void testSqrt() {
		assertEquals(DECIMAL32.valueOf(Math.sqrt(2)), DECIMAL32.sqrt(new BigDecimal("2")));
	}
	
	@Test
	public void testExp() {
		assertEquals(DECIMAL32.valueOf(Math.exp(2)), DECIMAL32.exp(new BigDecimal("2")));
	}
	
	@Test
	public void testSin() {
		assertEquals(DECIMAL32.valueOf(Math.sin(2)), DECIMAL32.sin(new BigDecimal("2")));
	}
	
	@Test
	public void testCos() {
		assertEquals(DECIMAL32.valueOf(Math.cos(2)), DECIMAL32.cos(new BigDecimal("2")));
	}
}
