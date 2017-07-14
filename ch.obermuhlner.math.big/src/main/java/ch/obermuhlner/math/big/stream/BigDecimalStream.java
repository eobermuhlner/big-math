package ch.obermuhlner.math.big.stream;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ch.obermuhlner.math.big.BigDecimalMath;

public class BigDecimalStream {

    public static Stream<BigDecimal> range(BigDecimal startInclusive, BigDecimal endExclusive, MathContext mathContext) {
    	BigDecimal step = BigDecimal.ONE;
    	if (endExclusive.subtract(startInclusive).signum() < 0) {
    		step = step.negate();
    	}
    	return range(startInclusive, endExclusive, step, mathContext);
    }

    public static Stream<BigDecimal> rangeClosed(BigDecimal startInclusive, BigDecimal endInclusive, MathContext mathContext) {
    	BigDecimal step = BigDecimal.ONE;
    	if (endInclusive.subtract(startInclusive).signum() < 0) {
    		step = step.negate();
    	}
    	return rangeClosed(startInclusive, endInclusive, step, mathContext);
    }

    public static Stream<BigDecimal> range(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
    	if (step.signum() == 0) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endExclusive.subtract(startInclusive).signum() != step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endExclusive, false, step, mathContext), false);
    }
    
    public static Stream<BigDecimal> rangeClosed(BigDecimal startInclusive, BigDecimal endInclusive, BigDecimal step, MathContext mathContext) {
    	if (step.signum() == 0) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endInclusive.subtract(startInclusive).signum() == -step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endInclusive, true, step, mathContext), false);
    }
    
    private static class BigDecimalSpliterator extends AbstractSpliterator<BigDecimal> {

		private BigDecimal value;
		private BigDecimal step;
		private long count;
		private MathContext mathContext;

		public BigDecimalSpliterator(BigDecimal startInclusive, BigDecimal step, long count, MathContext mathContext) {
    		super(count,
    				Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
			
    		this.value = startInclusive;
			this.step = step;
			this.count = count;
			this.mathContext = mathContext;
		}
    	
		public BigDecimalSpliterator(BigDecimal startInclusive, BigDecimal end, boolean inclusive, BigDecimal step, MathContext mathContext) {
			this(startInclusive, step, estimatedCount(startInclusive, end, inclusive, step, mathContext), mathContext);
		}
		
		private static long estimatedCount(BigDecimal startInclusive, BigDecimal end, boolean inclusive, BigDecimal step, MathContext mathContext) {
			BigDecimal count = end.subtract(startInclusive).divide(step, mathContext);
    		long result = count.longValue();
    		if (BigDecimalMath.fractionalPart(count).signum() != 0) {
    			result++;
    		}
    		if (inclusive) {
    			result++;
    		}
    		return result;
		}

		@Override
		public boolean tryAdvance(Consumer<? super BigDecimal> action) {
			if (count == 0) {
				return false;
			}
			
			action.accept(value);
			value = value.add(step, mathContext);
			count--;
			return true;
		}
		
		@Override
		public Spliterator<BigDecimal> trySplit() {
			long firstHalfCount = count / 2;
			
			if (firstHalfCount == 0) {
				return null;
			}
			
			long secondHalfCount = count - firstHalfCount;
			
			count = firstHalfCount;
			BigDecimal startSecondHalf = value.add(step.multiply(new BigDecimal(firstHalfCount), mathContext));
			
			return new BigDecimalSpliterator(startSecondHalf, step, secondHalfCount, mathContext);
		}
    }
}
