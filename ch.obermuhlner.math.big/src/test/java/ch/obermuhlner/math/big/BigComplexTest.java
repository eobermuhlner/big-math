package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;

import org.junit.Test;

public class BigComplexTest {


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
		
		assertEquals(BigDecimal.valueOf(1.2), BigComplex.valueOf("1.2", "3.4").re);
		assertEquals(BigDecimal.valueOf(3.4), BigComplex.valueOf("1.2", "3.4").im);
		
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
	public void testAdd() {
		assertEquals(BigComplex.valueOf(1.7, 4.0), BigComplex.valueOf(1.2, 3.4).add(BigComplex.valueOf(0.5, 0.6)));
		assertEquals(BigComplex.valueOf(1.7, 3.4), BigComplex.valueOf(1.2, 3.4).add(BigDecimal.valueOf(0.5)));
		assertEquals(BigComplex.valueOf(1.7, 3.4), BigComplex.valueOf(1.2, 3.4).add(0.5));
	}
	
	@Test
	public void testSubtract() {
		assertEquals(BigComplex.valueOf(0.7, 2.8), BigComplex.valueOf(1.2, 3.4).subtract(BigComplex.valueOf(0.5, 0.6)));
		assertEquals(BigComplex.valueOf(0.7, 3.4), BigComplex.valueOf(1.2, 3.4).subtract(BigDecimal.valueOf(0.5)));
		assertEquals(BigComplex.valueOf(0.7, 3.4), BigComplex.valueOf(1.2, 3.4).subtract(0.5));
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
	public void testToString() {
		assertEquals("(1.2 + 3.4 i)", BigComplex.valueOf(1.2, 3.4).toString());
		assertEquals("(1.2 - 3.4 i)", BigComplex.valueOf(1.2, -3.4).toString());
		
		assertEquals("(1.2 + 0.0 i)", BigComplex.valueOf(1.2, 0.0).toString());
		
		assertEquals("(0.0 + 3.4 i)", BigComplex.valueOf(0.0, 3.4).toString());

	}
}
