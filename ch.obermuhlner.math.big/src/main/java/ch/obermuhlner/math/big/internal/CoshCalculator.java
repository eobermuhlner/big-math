package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigRational;

/**
 * Calculates cosinus hyperbolicus using the Taylor series.
 * 
 * <p>See <a href="https://en.wikipedia.org/wiki/Taylor_series">Wikipedia: Taylor series</a></p>
 * 
 * <p>No argument checking or optimizations are done.
 * This implementation is <strong>not</strong> intended to be called directly.</p>
 */
public class CoshCalculator extends SeriesCalculator {

	public static final CoshCalculator INSTANCE = new CoshCalculator();
	
	private int n = 0;

	private BigRational factorial2n = BigRational.ONE;
	
	private CoshCalculator() {
		super(true);
	}
	
	@Override
	protected BigRational getCurrentFactor() {
		return factorial2n.reciprocal();
	}
	
	@Override
	protected void calculateNextFactor() {
		n++;
		factorial2n = factorial2n.multiply(2 * n - 1).multiply(2 * n);
	}
	
	@Override
	protected PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext) {
		return new PowerTwoNIterator(x, mathContext);
	}
}
