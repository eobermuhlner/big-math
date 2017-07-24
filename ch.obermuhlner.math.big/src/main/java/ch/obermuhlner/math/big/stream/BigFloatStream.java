package ch.obermuhlner.math.big.stream;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;

/**
 * Provides constructor methods for streams of {@link BigFloat} elements. 
 */
public class BigFloatStream {

    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     *
     * <p>An equivalent sequence of increasing values can be produced
     * sequentially using a {@code for} loop as follows:
     * <pre>for (BigFloat i = startInclusive; i.isLessThan(endExclusive); i = i.add(step)) {
    // ...
}</pre>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @return a sequential {@code Stream<BigFloat>}
     */
    public static Stream<BigFloat> range(BigFloat startInclusive, BigFloat endExclusive, BigFloat step) {
    	if (step.isZero()) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endExclusive.subtract(startInclusive).signum() != step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigFloatSpliterator(startInclusive, endExclusive, false, step), false);
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     *
     * <p>{@link Context#valueOf(long)} is used to convert the {@code long} values.</p>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @param context the {@link Context} used to convert the {@code long} values
     * @return a sequential {@code Stream<BigFloat>}
     * @see #range(BigFloat, BigFloat, BigFloat)
     */
    public static Stream<BigFloat> range(long startInclusive, long endExclusive, long step, Context context) {
    	return range(context.valueOf(startInclusive), context.valueOf(endExclusive), context.valueOf(step));
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endExclusive} (exclusive) by an incremental {@code step}.
     *
     * <p>{@link Context#valueOf(double)} is used to convert the {@code double} values.</p>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endExclusive the exclusive upper bound
     * @param step the step between elements
     * @param context the {@link Context} used to convert the {@code double} values
     * @return a sequential {@code Stream<BigFloat>}
     * @see #range(BigFloat, BigFloat, BigFloat)
     */
    public static Stream<BigFloat> range(double startInclusive, double endExclusive, double step, Context context) {
    	return range(context.valueOf(startInclusive), context.valueOf(endExclusive), context.valueOf(step));
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>An equivalent sequence of increasing values can be produced
     * sequentially using a {@code for} loop as follows:
     * <pre>for (BigFloat i = startInclusive; i.isLessThanOrEqual(endInclusive); i = i.add(step)) {
    //...
}
</pre>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @return a sequential {@code Stream<BigFloat>}
     */
    public static Stream<BigFloat> rangeClosed(BigFloat startInclusive, BigFloat endInclusive, BigFloat step) {
    	if (step.isZero()) {
    		throw new IllegalArgumentException("invalid step: 0");
    	}
		if (endInclusive.subtract(startInclusive).signum() == -step.signum()) {
			return Stream.empty();
		}
    	return StreamSupport.stream(new BigFloatSpliterator(startInclusive, endInclusive, true, step), false);
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>{@link Context#valueOf(long)} is used to convert the {@code long} values.</p>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @param context the {@link Context} used to convert the {@code long} values
     * @return a sequential {@code Stream<BigFloat>}
     * @see #rangeClosed(BigFloat, BigFloat, BigFloat)
     */
    public static Stream<BigFloat> rangeClosed(long startInclusive, long endInclusive, long step, Context context) {
    	return rangeClosed(context.valueOf(startInclusive), context.valueOf(endInclusive), context.valueOf(step));
    }
    
    /**
     * Returns a sequential ordered {@code Stream<BigFloat>} from {@code startInclusive}
     * (inclusive) to {@code endInclusive} (inclusive) by an incremental {@code step}.
     *
     * <p>{@link Context#valueOf(double)} is used to convert the {@code double} values.</p>
     *
     * @param startInclusive the (inclusive) initial value
     * @param endInclusive the inclusive upper bound
     * @param step the step between elements
     * @param context the {@link Context} used to convert the {@code double} values
     * @return a sequential {@code Stream<BigFloat>}
     * @see #rangeClosed(BigFloat, BigFloat, BigFloat)
     */
    public static Stream<BigFloat> rangeClosed(double startInclusive, double endInclusive, double step, Context context) {
    	return rangeClosed(context.valueOf(startInclusive), context.valueOf(endInclusive), context.valueOf(step));
    }

    private static class BigFloatSpliterator extends AbstractSpliterator<BigFloat> {

		private BigFloat value;
		private BigFloat step;
		private long count;

		public BigFloatSpliterator(BigFloat startInclusive, BigFloat step, long count) {
    		super(count,
    				Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
			
    		this.value = startInclusive;
			this.step = step;
			this.count = count;
		}
    	
		public BigFloatSpliterator(BigFloat startInclusive, BigFloat end, boolean inclusive, BigFloat step) {
			this(startInclusive, step, estimatedCount(startInclusive, end, inclusive, step));
		}
		
		private static long estimatedCount(BigFloat startInclusive, BigFloat end, boolean inclusive, BigFloat step) {
			BigFloat count = end.subtract(startInclusive).divide(step);
    		long result = count.toLong();
    		if (count.getFractionalPart().signum() != 0) {
    			result++;
    		} else {
    			if (inclusive) {
    				result++;
    			}
    		}
    		return result;
		}

		@Override
		public Comparator<? super BigFloat> getComparator() {
			if (step.signum() < 0) {
				return Comparator.reverseOrder();
			}
			return null;
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
		public void forEachRemaining(Consumer<? super BigFloat> action) {
			while (count > 0) {
				action.accept(value);
				value = value.add(step);
				count--;
			}
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
