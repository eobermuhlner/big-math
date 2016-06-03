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
	protected BigDecimal initialPower(BigDecimal x, MathContext mathContext) {
		return BigDecimal.ONE;
	}

	@Override
	protected BigDecimal nextPower(BigDecimal xToThePreviousPower, BigDecimal x, MathContext mathContext) {
		return xToThePreviousPower.multiply(x, mathContext);
	}
}
