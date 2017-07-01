package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import ch.obermuhlner.math.big.BigFloat.Context;
import static ch.obermuhlner.math.big.BigFloat.*;

public class BigFloatTest {

	@Test
	public void testValueOf() {
		Context context = context(MathContext.DECIMAL32);
		assertEquals(true, BigDecimal.ONE.compareTo(context.valueOf(BigDecimal.ONE).toBigDecimal()) == 0);
		assertEquals(true, BigDecimal.ONE.compareTo(context.valueOf(1).toBigDecimal()) == 0);
		assertEquals(true, BigDecimal.ONE.compareTo(context.valueOf(1.0).toBigDecimal()) == 0);
	}
	
	
	@Test
	public void testAdd() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(5), context.valueOf(2).add(3));
		assertIsEqual(context.valueOf(5), context.valueOf(2).add(3.0));
		assertIsEqual(context.valueOf(5), context.valueOf(2).add(context.valueOf(3)));
	}

	@Test
	public void testSubtract() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(2), context.valueOf(5).subtract(3));
		assertIsEqual(context.valueOf(2), context.valueOf(5).subtract(context.valueOf(3)));
	}

	@Test
	public void testMultiply() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(6), context.valueOf(2).multiply(3));
		assertIsEqual(context.valueOf(6), context.valueOf(2).multiply(context.valueOf(3)));
	}

	@Test
	public void testDivide() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(2), context.valueOf(6).divide(3));
		assertIsEqual(context.valueOf(2), context.valueOf(6).divide(context.valueOf(3)));
	}

	@Test
	public void testAbs() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(3), abs(context.valueOf(-3)));
	}

	@Test
	public void testPow() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(8), context.valueOf(2).pow(3));
	}

	@Test
	public void testMax() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(9), max(context.valueOf(9), context.valueOf(2)));
		assertIsEqual(context.valueOf(9), max(context.valueOf(2), context.valueOf(9)));
		
		assertIsEqual(context.valueOf(9), max(context.valueOf(2), context.valueOf(9), context.valueOf(3)));
		assertIsEqual(context.valueOf(9), max(context.valueOf(9), context.valueOf(2), context.valueOf(3)));
	}

	@Test
	public void testMin() {
		Context context = context(MathContext.DECIMAL32);
		assertIsEqual(context.valueOf(1), min(context.valueOf(1), context.valueOf(2)));
		assertIsEqual(context.valueOf(1), min(context.valueOf(2), context.valueOf(1)));
		
		assertIsEqual(context.valueOf(1), min(context.valueOf(2), context.valueOf(1), context.valueOf(3)));
		assertIsEqual(context.valueOf(1), min(context.valueOf(1), context.valueOf(2), context.valueOf(3)));
	}
	
	private static void assertIsEqual(BigFloat expected, BigFloat actual) {
		assertEquals(expected + "=" + actual, true, expected.isEqual(actual));
	}
}
