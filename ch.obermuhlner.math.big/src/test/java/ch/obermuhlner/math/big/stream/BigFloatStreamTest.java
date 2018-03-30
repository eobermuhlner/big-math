package ch.obermuhlner.math.big.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;

public class BigFloatStreamTest {

	@Test(expected = IllegalArgumentException.class)
	public void testRangeStepZero() {
		Context context = BigFloat.context(20);
		BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRangeClosedStepZero() {
		Context context = BigFloat.context(20);
		BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(10), context.valueOf(0));
	}

	@Test
	public void testRangeStep1() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(1))
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}

	@Test
	public void testRangeStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(3))
			.collect(Collectors.toList());
		
		assertEquals(4, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));

		assertEquals(0, BigFloatStream.range(context.valueOf(0), context.valueOf(-1), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(0, BigFloatStream.range(context.valueOf(0), context.valueOf(0), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.range(context.valueOf(0), context.valueOf(1), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.range(context.valueOf(0), context.valueOf(2), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.range(context.valueOf(0), context.valueOf(3), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.range(context.valueOf(0), context.valueOf(4), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.range(context.valueOf(0), context.valueOf(5), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.range(context.valueOf(0), context.valueOf(6), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.range(context.valueOf(0), context.valueOf(7), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.range(context.valueOf(0), context.valueOf(8), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.range(context.valueOf(0), context.valueOf(9), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.range(context.valueOf(0), context.valueOf(11), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.range(context.valueOf(0), context.valueOf(12), context.valueOf(3)).collect(Collectors.toList()).size());
	}
	
	@Test
	public void testRangeLongStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(0, 10, 3, context)
			.collect(Collectors.toList());
		
		assertEquals(4, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));
	}

	@Test
	public void testRangeDoubleStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(0.0, 10.0, 3.0, context)
			.collect(Collectors.toList());
		
		assertEquals(4, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));
	}

	@Test
	public void testRangeClosedStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(12), context.valueOf(3))
			.collect(Collectors.toList());
		
		assertEquals(5, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));
		assertEquals(true, list.contains(context.valueOf(12)));

		assertEquals(0, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(-1), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(0), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(1), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(1, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(2), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(3), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(4), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(2, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(5), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(6), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(7), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(3, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(8), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(9), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(10), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(4, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(11), context.valueOf(3)).collect(Collectors.toList()).size());
		assertEquals(5, BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(12), context.valueOf(3)).collect(Collectors.toList()).size());
	}

	@Test
	public void testRangeClosedLongStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(0, 12, 3, context)
			.collect(Collectors.toList());
		
		assertEquals(5, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));
		assertEquals(true, list.contains(context.valueOf(12)));
	}
	
	@Test
	public void testRangeClosedDoubleStep3() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(0.0, 12.0, 3.0, context)
			.collect(Collectors.toList());
		
		assertEquals(5, list.size());
		assertEquals(true, list.contains(context.valueOf(0)));
		assertEquals(true, list.contains(context.valueOf(3)));
		assertEquals(true, list.contains(context.valueOf(6)));
		assertEquals(true, list.contains(context.valueOf(9)));
		assertEquals(true, list.contains(context.valueOf(12)));
	}
	
	@Test
	public void testRangeStepMinus1() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(-1))
			.collect(Collectors.toList());
		
		assertList(list, 0, 0);
	}

	@Test
	public void testRangeClosedStep1() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(10), context.valueOf(1))
			.collect(Collectors.toList());
		
		assertListClosed(list, 0, 10);
	}

	@Test
	public void testRangeClosedStepMinus1() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(context.valueOf(0), context.valueOf(10), context.valueOf(-1))
			.collect(Collectors.toList());
		
		assertList(list, 0, 0);
	}

	@Test
	public void testRangeParallel() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(context.valueOf(0), context.valueOf(10), context.valueOf(1))
			.parallel()
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}

	@Test
	public void testRangeDown() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.range(context.valueOf(9), context.valueOf(-1), context.valueOf(-1))
			.collect(Collectors.toList());
		
		assertList(list, 0, 10);
	}
	
	@Test
	public void testRangeClosedDown() {
		Context context = BigFloat.context(20);
		List<BigFloat> list = BigFloatStream.rangeClosed(context.valueOf(10), context.valueOf(0), context.valueOf(-1))
			.collect(Collectors.toList());
		
		assertListClosed(list, 0, 10);
	}

	@Test
	public void testSingleStep() {
		Context context = BigFloat.context(20);
		Stream<BigFloat> stream = BigFloatStream.range(0, 3, 1, context);
		Spliterator<BigFloat> spliterator = stream.spliterator();

		assertEquals(true, spliterator.tryAdvance(value -> assertEquals(context.valueOf(0), value)));
		assertEquals(true, spliterator.tryAdvance(value -> assertEquals(context.valueOf(1), value)));
		assertEquals(true, spliterator.tryAdvance(value -> assertEquals(context.valueOf(2), value)));
		assertEquals(false, spliterator.tryAdvance(value -> fail("Should not be called")));
	}

	private void assertList(List<BigFloat> list, long startInclusive, long endExclusive) {
		assertEquals(endExclusive - startInclusive, list.size());
		if (!list.isEmpty()) {
			Context context = list.get(0).getContext();
			for (long i = startInclusive; i < endExclusive; i++) {
				assertEquals(true, list.contains(context.valueOf(i)));
			}
		}
	}

	private void assertListClosed(List<BigFloat> list, long startInclusive, long endInclusive) {
		assertEquals(endInclusive - startInclusive + 1, list.size());
		if (!list.isEmpty()) {
			Context context = list.get(0).getContext();
			for (long i = startInclusive; i <= endInclusive; i++) {
				assertEquals(true, list.contains(context.valueOf(i)));
			}
		}
	}

}
