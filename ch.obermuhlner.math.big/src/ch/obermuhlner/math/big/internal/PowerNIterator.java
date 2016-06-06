package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * {@link PowerIterator} to calculate x<sup>n</sup>.
 */
public class PowerNIterator implements PowerIterator {

	private final BigDecimal x;

	private final MathContext mathContext;

	private BigDecimal powerOfX;

	public PowerNIterator(BigDecimal x, MathContext mathContext) {
		this.x = x;
		this.mathContext = mathContext;
		
		powerOfX = BigDecimal.ONE;
	}
	
	@Override
	public BigDecimal getCurrentPower() {
		return powerOfX;
	}

	@Override
	public void calculateNextPower() {
		powerOfX = powerOfX.multiply(x, mathContext);
	}
}