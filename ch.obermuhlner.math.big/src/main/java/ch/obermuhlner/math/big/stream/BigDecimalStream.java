package ch.obermuhlner.math.big.stream;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Provides constructor methods for streams of {@link BigDecimal} elements. 
 */
public class BigDecimalStream {

    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     *
     * <p>An equivalent sequence of increasing values can be produced
     * sequentially using a {@code for} loop as follows:
     * <pre>for (BigDecimal i = startInclusive; i.compareTo(endExclusive) &lt; 0; i = i.add(step, mathContext)) {
    // ...
}</pre>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     */
    public static Stream<BigDecimal> range(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
    	if (step.signum() == 0) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endExclusive.subtract(startInclusive).signum() != step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endExclusive, false, step, mathContext), false);
    }

    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     * 
     * <p>The {@code long} arguments are converted using {@link BigDecimal#valueOf(long)}.</p>
     * 
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     * @see #range(BigDecimal, BigDecimal, BigDecimal, MathContext)
     */
    public static Stream<BigDecimal> range(long startInclusive, long endExclusive, long step, MathContext mathContext) {
    	return range(BigDecimal.valueOf(startInclusive), BigDecimal.valueOf(endExclusive), BigDecimal.valueOf(step), mathContext);
    }

    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     * 
     * <p>The {@code double} arguments are converted using {@link BigDecimal#valueOf(double)}.</p>
     * 
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     * @see #range(BigDecimal, BigDecimal, BigDecimal, MathContext)
     */
    public static Stream<BigDecimal> range(double startInclusive, double endExclusive, double step, MathContext mathContext) {
    	return range(BigDecimal.valueOf(startInclusive), BigDecimal.valueOf(endExclusive), BigDecimal.valueOf(step), mathContext);
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>An equivalent sequence of increasing values can be produced
     * sequentially using a {@code for} loop as follows:
     * <pre>for (BigDecimal i = startInclusive; i.compareTo(endInclusive) &lt;= 0; i = i.add(step, mathContext)) {
    // ...
}</pre>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     * @see #range(BigDecimal, BigDecimal, BigDecimal, MathContext)
     */
    public static Stream<BigDecimal> rangeClosed(BigDecimal startInclusive, BigDecimal endInclusive, BigDecimal step, MathContext mathContext) {
    	if (step.signum() == 0) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endInclusive.subtract(startInclusive).signum() == -step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigDecimalSpliterator(startInclusive, endInclusive, true, step, mathContext), false);
    }

    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>The {@code long} arguments are converted using {@link BigDecimal#valueOf(long)}.</p>
     * 
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     * @see #rangeClosed(BigDecimal, BigDecimal, BigDecimal, MathContext)
     */
    public static Stream<BigDecimal> rangeClosed(long startInclusive, long endInclusive, long step, MathContext mathContext) {
    	return rangeClosed(BigDecimal.valueOf(startInclusive), BigDecimal.valueOf(endInclusive), BigDecimal.valueOf(step), mathContext);
    }

    /**
     * Returns a sequential ordered {@code Stream<BigDecimal>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>The {@code double} arguments are converted using {@link BigDecimal#valueOf(double)}.</p>
     * 
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @param mathContext the {@link MathContext} used for all mathematical operations
     * @return a sequential {@code Stream<BigDecimal>}
     * @see #rangeClosed(BigDecimal, BigDecimal, BigDecimal, MathContext)
     */
    public static Stream<BigDecimal> rangeClosed(double startInclusive, double endInclusive, double step, MathContext mathContext) {
    	return rangeClosed(BigDecimal.valueOf(startInclusive), BigDecimal.valueOf(endInclusive), BigDecimal.valueOf(step), mathContext);
    }

    private static class BigDecimalSpliterator extends AbstractSpliterator<BigDecimal> {

		private BigDecimal value;
		private BigDecimal step;
		private long count;
		private MathContext mathContext;

		public BigDecimalSpliterator(BigDecimal startInclusive, BigDecimal step, long count, MathContext mathContext) {
    		super(count,
    				Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
			
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
    		} else {
    			if (inclusive) {
    				result++;
    			}
    		}
    		return result;
		}

		@Override
		public Comparator<? super BigDecimal> getComparator() {
			if (step.signum() < 0) {
				return Comparator.reverseOrder();
			}
			return null;
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
		public void forEachRemaining(Consumer<? super BigDecimal> action) {
			while (count > 0) {
				action.accept(value);
				value = value.add(step, mathContext);
				count--;
			}
		}
		
		@Override
		public Spliterator<BigDecimal> trySplit() {
			long firstHalfCount = count / 2;
			
			if (firstHalfCount == 0) {
				return null;
			}
			
			long secondHalfCount = count - firstHalfCount;
			
			count = firstHalfCount;
			BigDecimal startSecondHalf = value.add(step.multiply(new BigDecimal(firstHalfCount), mathContext), mathContext);
			
			return new BigDecimalSpliterator(startSecondHalf, step, secondHalfCount, mathContext);
		}
    }
}
