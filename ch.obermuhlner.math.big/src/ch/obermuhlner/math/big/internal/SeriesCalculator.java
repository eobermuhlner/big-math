package ch.obermuhlner.math.big.internal;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import ch.obermuhlner.math.big.BigRational;

public abstract class SeriesCalculator {

	private List<BigRational> factors = new ArrayList<>();
	
	public BigDecimal calculate(BigDecimal x, MathContext mathContext) {
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal xToThePower = initialPower(x, mathContext);
		BigDecimal step;
		int i = 0;
		do {
			BigRational factor = getFactor(i);
			step = factor.toBigDecimal(mathContext).multiply(xToThePower, mathContext);
			xToThePower = nextPower(xToThePower, x, mathContext);
			sum = sum.add(step, mathContext);
			i++;
		} while (step.abs().compareTo(acceptableError) > 0);
		
		return sum.round(mathContext);
	}
	
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

	protected abstract BigDecimal initialPower(BigDecimal x, MathContext mathContext);
	protected abstract BigDecimal nextPower(BigDecimal xToThePreviousPower, BigDecimal x, MathContext mathContext);
}
