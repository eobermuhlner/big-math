package ch.obermuhlner.math.big.internal;

import ch.obermuhlner.math.big.BigRational;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Calculates sinus hyperbolicus using the Taylor series.
 * 
 * <p>See <a href="https://en.wikipedia.org/wiki/Taylor_series">Wikipedia: Taylor series</a></p>
 * 
 * <p>No argument checking or optimizations are done.
 * This implementation is <strong>not</strong> intended to be called directly.</p>
 */
public class AtanhCalculator extends SeriesCalculator {

	public static final AtanhCalculator INSTANCE = new AtanhCalculator();

	private int n = 0;

	private AtanhCalculator() {
		super(true);
	}
	
	@Override
	protected BigRational getCurrentFactor() {
		return BigRational.valueOf(1, 2 * n + 1);
	}
	
	@Override
	protected void calculateNextFactor() {
		n++;
	}
	
	@Override
	protected PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext) {
		return new PowerTwoNPlusOneIterator(x, mathContext);
	}
}
