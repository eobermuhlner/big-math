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
		private BigDecimal endExclusive;
		private BigDecimal step;
		private MathContext mathContext;

		public BigDecimalSpliterator(BigDecimal startInclusive, BigDecimal endExclusive, BigDecimal step, MathContext mathContext) {
    		super(estimatedCount(startInclusive, endExclusive, step, mathContext).intValue(),
    				Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
			
    		this.value = startInclusive;
			this.endExclusive = endExclusive;
			this.step = step;
			this.mathContext = mathContext;
		}
    	
		private static BigDecimal estimatedCount(BigDecimal startInclusive, BigDecimal endInclusive, BigDecimal step, MathContext mathContext) {
    		return endInclusive.subtract(startInclusive).divide(step, mathContext);
		}

		@Override
		public boolean tryAdvance(Consumer<? super BigDecimal> action) {
			if (value.compareTo(endExclusive) >= 0) {
				return false;
			}
			
			action.accept(value);
			value = value.add(step, mathContext);
			return true;
		}
    }
}
