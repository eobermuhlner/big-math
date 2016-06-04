package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigRational;

public class ExpCalculator extends SeriesCalculator {

	public static final ExpCalculator INSTANCE = new ExpCalculator();
	
	private int n = 0;
	private BigRational oneOverFactorialOfN = BigRational.ONE;
	
	private ExpCalculator() {
		// prevent instances
	}

	@Override
	protected BigRational getCurrentFactor() {
		return oneOverFactorialOfN;
	}
	
	@Override
	protected void calculateNextFactor() {
		n++;
		oneOverFactorialOfN = oneOverFactorialOfN.divide(n);
	}

	@Override
	protected PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext) {
		return new PowerNIterator(x, mathContext);
	}
}
