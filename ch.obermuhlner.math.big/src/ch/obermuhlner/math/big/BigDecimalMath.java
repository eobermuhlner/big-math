package ch.obermuhlner.math.big;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalMath {

	private static final BigDecimal TWO = BigDecimal.valueOf(2);
	
	private BigDecimalMath() {
		// prevent instances
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
		
		BigDecimal result = ZERO;
		BigDecimal prevResult; 
		BigDecimal step;
		int i = 0;
		do {
			int doubleIndexPlusOne = i * 2 + 1; 
			step = pow(magic, doubleIndexPlusOne, mathContext).divide(BigDecimal.valueOf(doubleIndexPlusOne), mathContext);

			prevResult = result;
			result = result.add(step, mathContext);
			
			i++;
		} while (result.compareTo(prevResult) != 0);
		
		result = result.multiply(TWO, mathContext);
		
		return result;
	}
}
