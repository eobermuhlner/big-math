package ch.obermuhlner.math.big.stream;

import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ch.obermuhlner.math.big.BigFloat;

public class BigFloatStream {

    public static Stream<BigFloat> range(BigFloat startInclusive, BigFloat endExclusive) {
    	return range(startInclusive, endExclusive, startInclusive.getContext().valueOf(1));
    }

    public static Stream<BigFloat> rangeClosed(BigFloat startInclusive, BigFloat endInclusive) {
    	return rangeClosed(startInclusive, endInclusive, startInclusive.getContext().valueOf(1));
    }

    public static Stream<BigFloat> range(BigFloat startInclusive, BigFloat endExclusive, BigFloat step) {
    	return StreamSupport.stream(new BigFloatSpliterator(startInclusive, endExclusive, step), false);
    }
    
    public static Stream<BigFloat> rangeClosed(BigFloat startInclusive, BigFloat endInclusive, BigFloat step) {
    	return StreamSupport.stream(new BigFloatSpliterator(startInclusive, endInclusive.add(step), step), false);
    }
    
    private static class BigFloatSpliterator extends AbstractSpliterator<BigFloat> {

		private BigFloat value;
		private BigFloat step;
		private long count;

		public BigFloatSpliterator(BigFloat startInclusive, BigFloat step, long count) {
    		super(count,
    				Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
			
    		this.value = startInclusive;
			this.step = step;
			this.count = count;
		}
    	
		public BigFloatSpliterator(BigFloat startInclusive, BigFloat endExclusive, BigFloat step) {
			this(startInclusive, step, estimatedCount(startInclusive, endExclusive, step));
		}
		
		private static long estimatedCount(BigFloat startInclusive, BigFloat endExclusive, BigFloat step) {
    		return endExclusive.subtract(startInclusive).divide(step).toLong();
		}

		@Override
		public boolean tryAdvance(Consumer<? super BigFloat> action) {
			if (count == 0) {
				return false;
			}
			
			action.accept(value);
			value = value.add(step);
			count--;
			return true;
		}
		
		@Override
		public Spliterator<BigFloat> trySplit() {
			long firstHalfCount = count / 2;
			
			if (firstHalfCount == 0) {
				return null;
			}
			
			long secondHalfCount = count - firstHalfCount;
			
			count = firstHalfCount;
			BigFloat startSecondHalf = value.add(step.multiply(firstHalfCount));
			
			return new BigFloatSpliterator(startSecondHalf, step, secondHalfCount);
		}
    }
}
