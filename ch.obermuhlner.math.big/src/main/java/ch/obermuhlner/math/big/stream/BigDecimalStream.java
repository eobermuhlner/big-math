package ch.obermuhlner.math.big.stream;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BigDecimalStream {

    public static Stream<BigDecimal> range(BigDecimal startInclusive, BigDecimal endExclusive, MathContext mathContext) {
    	return range(startInclusive, endExclusive, BigDecimal.ONE, mathContext);
    }

    public static Stream<BigDecimal> rangeClosed(BigDecimal startInclusive, BigDecimal endInclusive, MathContext mathContext) {
    	return rangeClosed(startInclusive, endInclusive, BigDecimal.ONE, mathContext);
    }

    public static Stream<BigDecimal> range(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endExclusive, step, mathContext), false);
    }
    
    public static Stream<BigDecimal> rangeClosed(BigDecimal startInclusive, BigDecimal endInclusive, BigDecimal step, MathContext mathContext) {
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endInclusive.add(step, mathContext), step, mathContext), false);
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
    	
		public BigDecimalSpliterator(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
			this(startInclusive, step, estimatedCount(startInclusive, endExclusive, step, mathContext), mathContext);
		}
		
		private static long estimatedCount(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
    		return endExclusive.subtract(startInclusive).divide(step, mathContext).longValue();
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
