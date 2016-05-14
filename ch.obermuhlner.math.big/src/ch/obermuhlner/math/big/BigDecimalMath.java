package ch.obermuhlner.math.big;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalMath {

	private static final BigDecimal TWO = valueOf(2);
	
	private static BigDecimal[] factorialCache = new BigDecimal[100];
	static {
		BigDecimal result = ONE;
		factorialCache[0] = result;
		for (int i = 1; i < factorialCache.length; i++) {
			result = result.multiply(valueOf(i));
			factorialCache[i] = result;
		}
	}

	private BigDecimalMath() {
		// prevent instances
	}
	
	public static BigDecimal factorial(int n) {
		if (n < 0) {
			// TODO document
			throw new ArithmeticException("Negative value");
		}
		if (n < factorialCache.length) {
			return factorialCache[n];
		}

		BigDecimal result = factorialCache[factorialCache.length - 1];
		for (int i = factorialCache.length; i <= n; i++) {
			result = result.multiply(valueOf(i));
		}
		return result;
	}

	public static BigDecimal pow(BigDecimal x, int y, MathContext mathContext) {
		if (y < 0) {
			return ONE.divide(pow(x, -y, mathContext), mathContext);
		}
		
		BigDecimal result = ONE;
		while (y > 0) {
			if ((y & 1) == 1) {
				// odd exponent -> multiply result with x
				result = result.multiply(x, mathContext);
				y -= 1;
			}
			
			if (y > 0) {
				// even exponent -> square x
				x = x.multiply(x, mathContext);
			}
			
			y >>= 1;
		}

		return result;
	}
	
	/**
	 * Calculates the square root of a {@link BigDecimal} value.
	 * 
	 * <p><a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * 
	 * <p>The result has loss of precision, the desired precision must be specified by the {@link MathContext} argument.</p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Newton%27s_method">Newtown's method</a>
	 * until the resulting value has reached the specified precision.</p>
	 *
	 * @param x the {@link BigDecimal} value to calculate the square root
	 * @param mathContext the {@link MathContext} used for all calculations and the desired precision of the result
	 * @return the calculated square root of x
	 */
	public static BigDecimal sqrt(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ZERO;
		}

		BigDecimal last;
		BigDecimal result = x.divide(TWO);

		do {
			last = result;
			result = x.divide(result, mathContext).add(last, mathContext).divide(TWO, mathContext);
		} while (result.compareTo(last) != 0);
		
		return result;
	}

	public static BigDecimal log(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Natural_logarithm
		if (x.signum() <= 0) {
			throw new ArithmeticException("Log 0");
		}
		if (x.compareTo(ONE) == 0) {
			return ZERO;
		}
		return logAreaHyperbolicTangent(x, mathContext);
	}

	private static BigDecimal logAreaHyperbolicTangent(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Logarithm#Calculation
		BigDecimal magic = x.subtract(ONE).divide(x.add(ONE), mathContext);
		
		BigDecimal last; 
		BigDecimal result = ZERO;
		BigDecimal step;
		int i = 0;
		do {
			int doubleIndexPlusOne = i * 2 + 1; 
			step = pow(magic, doubleIndexPlusOne, mathContext).divide(valueOf(doubleIndexPlusOne), mathContext);

			last = result;
			result = result.add(step, mathContext);
			
			i++;
		} while (result.compareTo(last) != 0);
		
		result = result.multiply(TWO, mathContext);
		
		return result;
	}
}
