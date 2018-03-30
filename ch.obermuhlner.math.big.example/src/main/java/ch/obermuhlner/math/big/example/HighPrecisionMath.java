package ch.obermuhlner.math.big.example;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Example class for a fictive project that needs a specific precision in its calculations.
 */
public class HighPrecisionMath {
	public static final MathContext MATH_CONTEXT = new MathContext(100);

	public static final BigDecimal PI = BigDecimalMath.pi(MATH_CONTEXT);

	public static BigDecimal pow(BigDecimal x, BigDecimal y) {
		return BigDecimalMath.pow(x, y, MATH_CONTEXT);
	}

	public static BigDecimal sin(BigDecimal x) {
		return BigDecimalMath.sin(x, MATH_CONTEXT);
	}
}
