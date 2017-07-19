package ch.obermuhlner.math.big.stream;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class BigDecimalStreamTest {

	@Test(expected = IllegalArgumentException.class)
	public void testRangeStepZero() {
		BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ZERO, MathContext.DECIMAL64);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRangeClosedStepZero() {
		BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ZERO, MathContext.DECIMAL64);
	}

	@Test
	public void testRange() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}

	@Test
	public void testRangeStep3() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(3), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertEquals(4, list.size());
		assertEquals(true, list.contains(BigDecimal.valueOf(0)));
		assertEquals(true, list.contains(BigDecimal.valueOf(3)));
		assertEquals(true, list.contains(BigDecimal.valueOf(6)));
		assertEquals(true, list.contains(BigDecimal.valueOf(9)));

		assertEquals(0, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(-1), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(0, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(1), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(3), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(4), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(5), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(6), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(7), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(8), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(9), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(11), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(12), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
	}

	@Test
	public void testRangeClosedStep3() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(12), BigDecimal.valueOf(3), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertEquals(4, list.size());
		assertEquals(true, list.contains(BigDecimal.valueOf(0)));
		assertEquals(true, list.contains(BigDecimal.valueOf(3)));
		assertEquals(true, list.contains(BigDecimal.valueOf(6)));
		assertEquals(true, list.contains(BigDecimal.valueOf(9)));

		assertEquals(0, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(-1), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(1), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(1, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(3), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(4), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(2, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(5), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(6), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(7), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(3, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(8), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(9), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(4, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(11), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
		assertEquals(5, BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(12), BigDecimal.valueOf(3), MathContext.DECIMAL64).collect(Collectors.toList()).size());
	}

	@Test
	public void testRangeStep1() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ONE, MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}

	@Test
	public void testRangeStepMinus1() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ONE.negate(), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertList(list, 0, 0);
	}

	@Test
	public void testRangeClosed() {
		List<BigDecimal> list = BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(10), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertListClosed(list, 0, 10);
	}

	@Test
	public void testRangeClosedStepMinus1() {
		List<BigDecimal> list = BigDecimalStream.rangeClosed(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ONE.negate(), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertList(list, 0, 0);
	}

	@Test
	public void testRangeParallel() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), MathContext.DECIMAL64)
			.parallel()
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}

	@Test
	public void testRangeDown() {
		List<BigDecimal> list = BigDecimalStream.range(BigDecimal.valueOf(9), BigDecimal.valueOf(-1), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}
	
	@Test
	public void testRangeClosedDown() {
		List<BigDecimal> list = BigDecimalStream.rangeClosed(BigDecimal.valueOf(10), BigDecimal.valueOf(0), MathContext.DECIMAL64)
			.collect(Collectors.toList());
		
		assertListClosed(list, 0, 10);
	}

	private void assertList(List<BigDecimal> list, long startInclusive, long endExclusive) {
		assertEquals(endExclusive - startInclusive, list.size());
		for (long i = startInclusive; i < endExclusive; i++) {
			assertEquals(true, list.contains(BigDecimal.valueOf(i)));
		}
	}

	private void assertListClosed(List<BigDecimal> list, long startInclusive, long endInclusive) {
		assertEquals(endInclusive - startInclusive + 1, list.size());
		for (long i = startInclusive; i <= endInclusive; i++) {
			assertEquals(true, list.contains(BigDecimal.valueOf(i)));
		}
	}

}
