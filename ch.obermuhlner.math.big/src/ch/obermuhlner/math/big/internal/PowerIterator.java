package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;

/**
 * Iterator over the powers of a value x.
 * 
 * <p>This API allows to efficiently calculate the various powers of x in a taylor series by storing intermediate results.</p>
 * <p>For example x<sup>n</sup> can be calculated using one multiplication by storing the previously calculated x<sup>n-1</sup> and x.</p>
 * 
 * <p>{@link #getCurrentPower()} will be called first to retrieve the initial value.</p>
 * 
 * For later iterations {@link #calculateNextPower()} will be called before {@link #getCurrentPower()}.
 */
public interface PowerIterator {
	
	/**
	 * Returns the current power.
	 * 
	 * @return the current power.
	 */
	BigDecimal getCurrentPower();
	
	/**
	 * Calculates the next power.
	 */
	void calculateNextPower();
}