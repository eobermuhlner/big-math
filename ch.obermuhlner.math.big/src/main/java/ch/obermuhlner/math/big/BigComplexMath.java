package ch.obermuhlner.math.big;

import static ch.obermuhlner.math.big.BigComplex.I;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigComplexMath {

	private static final BigDecimal TWO = BigDecimal.valueOf(2);

	public static BigComplex reciprocal(BigComplex x, MathContext mathContext) {
		return x.reciprocal(mathContext);
	}

	public static BigComplex conjugate(BigComplex x) {
		return x.conjugate();
	}
	
	public static BigDecimal abs(BigComplex x, MathContext mathContext) {
		return x.abs(mathContext);
	}
	
	public static BigDecimal angle(BigComplex x, MathContext mathContext) {
		return x.angle(mathContext);
	}
	
	public static BigComplex exp(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal expRe = BigDecimalMath.exp(x.re, mc);
		return BigComplex.valueOf(
				expRe.multiply(BigDecimalMath.cos(x.im, mc), mathContext),
				expRe.multiply(BigDecimalMath.sin(x.im, mc), mathContext));
	}
	
	public static BigComplex sin(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		
		return BigComplex.valueOf(
				BigDecimalMath.sin(x.re, mc).multiply(BigDecimalMath.cosh(x.im, mc), mathContext),
				BigDecimalMath.cos(x.re, mc).multiply(BigDecimalMath.sinh(x.im, mc), mathContext));
	}
	
	public static BigComplex cos(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return BigComplex.valueOf(
				BigDecimalMath.cos(x.re, mc).multiply(BigDecimalMath.cosh(x.im, mc), mathContext),
				BigDecimalMath.sin(x.re, mc).multiply(BigDecimalMath.sinh(x.im, mc), mathContext).negate());
	}
	
	// 
	// http://scipp.ucsc.edu/~haber/archives/physics116A10/arc_10.pdf
	
	public static BigComplex tan(BigComplex x, MathContext mathContext) {
		return sin(x, mathContext).divide(cos(x, mathContext), mathContext);
	}

	public static BigComplex atan(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return log(I.subtract(x, mc).divide(I.add(x, mc), mc), mc).divide(I, mc).divide(TWO, mathContext);
	}
	
	public static BigComplex acot(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return log(x.add(I, mc).divide(x.subtract(I, mc), mc), mc).divide(I, mc).divide(TWO, mathContext);
	}
	
	// https://en.wikipedia.org/wiki/Inverse_trigonometric_functions#Extension_to_complex_plane
	public static BigComplex asin(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return I.negate().multiply(log(I.multiply(x, mc).add(sqrt(BigComplex.ONE.subtract(x.multiply(x, mc), mc), mc), mc), mc), mathContext);
	}

	public static BigComplex acos(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return I.negate().multiply(log(x.add(sqrt(x.multiply(x, mc).subtract(BigComplex.ONE, mc), mc), mc), mc), mathContext);
	}
	

	// https://math.stackexchange.com/questions/44406/how-do-i-get-the-square-root-of-a-complex-number
	public static BigComplex sqrt(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal magnitude = x.abs(mc);

		BigComplex a = x.add(magnitude, mc);
		return a.divide(a.abs(mc), mc).multiply(BigDecimalMath.sqrt(magnitude, mc), mathContext);
	}
	
	// https://en.wikipedia.org/wiki/Complex_logarithm
	public static BigComplex log(BigComplex x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return BigComplex.valueOf(
				BigDecimalMath.log(x.abs(mc), mathContext),
				toRangePi(x.angle(mathContext), mathContext));
	}

	public static BigComplex pow(BigComplex x, long y, MathContext mathContext) {
		if (y < 0) {
			return BigComplex.ONE.divide(pow(x, -y, mathContext), mathContext);
		}
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());

		BigComplex result = BigComplex.ONE;
		while (y > 0) {
			if ((y & 1) == 1) {
				// odd exponent -> multiply result with x
				result = result.multiply(x, mc);
				y -= 1;
			}
			
			if (y > 0) {
				// even exponent -> square x
				x = x.multiply(x, mc);
			}
			
			y >>= 1;
		}

		return result.round(mathContext);
	}
	
	public static BigComplex pow(BigComplex x, BigDecimal y, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal angleTimesN = x.angle(mc).multiply(y, mc);
		return BigComplex.valueOf(
				BigDecimalMath.cos(angleTimesN, mc),
				BigDecimalMath.sin(angleTimesN, mc)).multiply(BigDecimalMath.pow(x.abs(mc), y, mc), mathContext);
	}

	public static BigComplex pow(BigComplex x, BigComplex y, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return exp(y.multiply(log(x, mc), mc), mathContext);
	}
	
	public static BigComplex root_ALTERNATE(BigComplex x, BigDecimal n, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return BigComplex.valueOfPolar(BigDecimalMath.root(x.abs(mc), n, mc), x.angle(mc).divide(n, mc), mathContext);
	}
	
	public static BigComplex root(BigComplex x, BigDecimal n, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return pow(x, BigDecimal.ONE.divide(n, mc), mathContext);
	}

	public static BigComplex root(BigComplex x, BigComplex n, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return pow(x, BigComplex.ONE.divide(n, mc), mathContext);
	}
	
	// TODO add root() for the k'th root - https://math.stackexchange.com/questions/322481/principal-nth-root-of-a-complex-number 
	
	private static BigDecimal toRangePi(BigDecimal angle, MathContext mathContext) {
		BigDecimal pi = BigDecimalMath.pi(mathContext);
		
		if (angle.compareTo(pi.negate()) < 0) {
			return angle.add(pi, mathContext).add(pi, mathContext);
		}
		if (angle.compareTo(pi) > 0) {
			return angle.subtract(pi, mathContext).subtract(pi, mathContext);
		}
		
		return angle;
	}
}
