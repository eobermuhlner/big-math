package ch.obermuhlner.math.big.internal;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * {@link PowerIterator} to calculate x<sup>2*n-1</sup>.
 */
public class PowerTwoNMinusOneIterator implements PowerIterator {

	private final MathContext mathContext;

	private final BigDecimal xPowerTwo;

	private BigDecimal powerOfX;

	public PowerTwoNMinusOneIterator(BigDecimal x, MathContext mathContext) {
		this.mathContext = mathContext;
		
		xPowerTwo = x.multiply(x, mathContext);
		powerOfX = BigDecimalMath.reciprocal(x, mathContext);
	}
	
	@Override
	public BigDecimal getCurrentPower() {
		return powerOfX;
	}

	@Override
	public void calculateNextPower() {
		powerOfX = powerOfX.multiply(xPowerTwo, mathContext);
	}
}