package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigRational;

/**
 * Calculates sinus hyperbolicus using the Taylor series.
 * 
 * <p>See <a href="https://en.wikipedia.org/wiki/Taylor_series">Wikipedia: Taylor series</a></p>
 * 
 * <p>No argument checking or optimizations are done.
 * This implementation is <strong>not</strong> intended to be called directly.</p>
 */
public class SinhCalculator extends SeriesCalculator {

	public static final SinhCalculator INSTANCE = new SinhCalculator();
	
	private int n = 0;

	private BigRational factorial2nPlus1 = BigRational.ONE;
	
	private SinhCalculator() {
		super(true);
	}
	
	@Override
	protected BigRational getCurrentFactor() {
		return factorial2nPlus1.reciprocal();
	}
	
	@Override
	protected void calculateNextFactor() {
		n++;
		factorial2nPlus1 = factorial2nPlus1.multiply(2 * n);
		factorial2nPlus1 = factorial2nPlus1.multiply(2 * n + 1);
	}
	
	@Override
	protected PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext) {
		return new PowerTwoNPlusOneIterator(x, mathContext);
	}
}
