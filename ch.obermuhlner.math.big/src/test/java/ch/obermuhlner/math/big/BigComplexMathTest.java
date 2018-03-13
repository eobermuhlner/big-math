package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.MathContext;

import org.junit.Test;

public class BigComplexMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	@Test
	public void testSin() {
		//Result from Wolfram Alpha
		assertEquals(
				BigComplex.valueOf("3.16577851321616814674073461719190553837911076789146893228", "1.95960104142160589707035204998935827843632016018455965880", MC), 
				BigComplexMath.sin(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testCos() {
		//Result from Wolfram Alpha
		assertEquals(
				BigComplex.valueOf("2.03272300701966552943634344849951426373199040663875238194", "-3.05189779915180005751211568689510545288843761773331964466", MC), 
				BigComplexMath.cos(BigComplex.valueOf(1, 2), MC));
	}
}
