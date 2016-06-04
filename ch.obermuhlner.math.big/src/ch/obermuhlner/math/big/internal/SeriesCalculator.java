package ch.obermuhlner.math.big.internal;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import ch.obermuhlner.math.big.BigRational;

public abstract class SeriesCalculator {

	private boolean calculateInPairs;

	private List<BigRational> factors = new ArrayList<>();
	
	protected SeriesCalculator() {
		this(false);
	}
	
	protected SeriesCalculator(boolean calculateInPairs) {
		this.calculateInPairs = calculateInPairs;
	}
	
	public BigDecimal calculate(BigDecimal x, MathContext mathContext) {
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		PowerIterator powerIterator = createPowerIterator(x, mathContext);
		
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal step;
		int i = 0;
		do {
			BigRational factor = getFactor(i);
			BigDecimal xToThePower  = powerIterator.getCurrentPower();
			powerIterator.calculateNextPower();
			step = factor.getNumeratorBigDecimal().multiply(xToThePower, mathContext).divide(factor.getDenominatorBigDecimal(), mathContext);
			i++;

			if (calculateInPairs) {
				xToThePower  = powerIterator.getCurrentPower();
				powerIterator.calculateNextPower();
				factor = getFactor(i);
				BigDecimal step2 = factor.getNumeratorBigDecimal().multiply(xToThePower, mathContext).divide(factor.getDenominatorBigDecimal(), mathContext);
				step = step.add(step2, mathContext);
				i++;
			}

			sum = sum.add(step, mathContext);
		} while (step.abs().compareTo(acceptableError) > 0);
		
		return sum.round(mathContext);
	}
	
	protected abstract PowerIterator createPowerIterator(BigDecimal x, MathContext mathContext);

	protected BigRational getFactor(int index) {
		while (factors.size() <= index) {
			BigRational factor = getCurrentFactor();
			factors.add(factor);
			calculateNextFactor();
		}
		return factors.get(index);
	}
	
	protected abstract BigRational getCurrentFactor();
	protected abstract void calculateNextFactor();
}
