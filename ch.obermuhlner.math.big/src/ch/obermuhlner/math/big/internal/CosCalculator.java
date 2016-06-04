package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigRational;

public class CosCalculator extends SeriesCalculator {

	public static final CosCalculator INSTANCE = new CosCalculator();
	
	private int n = 0;
	private boolean negative = false;
	private BigRational factorial2n = BigRational.ONE;
	
	private CosCalculator() {
		super(true);
	}
	
	@Override
	protected BigRational getCurrentFactor() {
		BigRational factor = factorial2n.reciprocal();
		if (negative) {
			factor = factor.negate();
		}
		return factor;
	}
	
	@Override
	protected void calculateNextFactor() {
		n++;
		factorial2n = factorial2n.multiply(2 * n - 1);
		factorial2n = factorial2n.multiply(2 * n);
		negative = !negative;
	}
	
	@Override
	protected PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext) {
		return new PowerTwoNIterator(x, mathContext);
	}
}
