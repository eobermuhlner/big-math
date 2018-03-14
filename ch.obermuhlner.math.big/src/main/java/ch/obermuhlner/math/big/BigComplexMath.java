package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;

import static ch.obermuhlner.math.big.BigComplex.I;

public class BigComplexMath {

	private static final BigDecimal TWO = BigDecimal.valueOf(2);

	public static BigComplex reciprocal(BigComplex value, MathContext mathContext) {
		return value.reciprocal(mathContext);
	}

	public static BigComplex conjugate(BigComplex value) {
		return value.conjugate();
	}
	
	public static BigDecimal abs(BigComplex value, MathContext mathContext) {
		return value.abs(mathContext);
	}
	
	public static BigDecimal angle(BigComplex value, MathContext mathContext) {
		return value.angle(mathContext);
	}
	
	public static BigComplex exp(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal expRe = BigDecimalMath.exp(value.re, mc);
		return BigComplex.valueOf(
				expRe.multiply(BigDecimalMath.cos(value.im, mc), mathContext),
				expRe.multiply(BigDecimalMath.sin(value.im, mc), mathContext));
	}
	
	public static BigComplex sin(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		
		return BigComplex.valueOf(
				BigDecimalMath.sin(value.re, mc).multiply(BigDecimalMath.cosh(value.im, mc), mathContext),
				BigDecimalMath.cos(value.re, mc).multiply(BigDecimalMath.sinh(value.im, mc), mathContext));
	}
	
	public static BigComplex cos(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return BigComplex.valueOf(
				BigDecimalMath.cos(value.re, mc).multiply(BigDecimalMath.cosh(value.im, mc), mathContext),
				BigDecimalMath.sin(value.re, mc).multiply(BigDecimalMath.sinh(value.im, mc), mathContext).negate());
	}
	
	// 
	// http://scipp.ucsc.edu/~haber/archives/physics116A10/arc_10.pdf
	
	public static BigComplex tan(BigComplex value, MathContext mathContext) {
		return sin(value, mathContext).divide(cos(value, mathContext), mathContext);
	}

	public static BigComplex atan(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return log(I.subtract(value, mc).divide(I.add(value, mc), mc), mc).divide(I, mc).divide(TWO, mathContext);
	}
	
	public static BigComplex acot(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return log(value.add(I, mc).divide(value.subtract(I, mc), mc), mc).divide(I, mc).divide(TWO, mathContext);
	}
	
	// https://en.wikipedia.org/wiki/Inverse_trigonometric_functions#Extension_to_complex_plane
	public static BigComplex asin(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return I.negate().multiply(log(I.multiply(value, mc).add(sqrt(BigComplex.ONE.subtract(value.multiply(value, mc), mc), mc), mc), mc), mathContext);
	}

	public static BigComplex acos(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return I.negate().multiply(log(value.add(sqrt(value.multiply(value, mc).subtract(BigComplex.ONE, mc), mc), mc), mc), mathContext);
	}
	

	// https://math.stackexchange.com/questions/44406/how-do-i-get-the-square-root-of-a-complex-number
	public static BigComplex sqrt(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal magnitude = value.abs(mc);

		BigComplex a = value.add(magnitude, mc);
		return a.divide(a.abs(mc), mc).multiply(BigDecimalMath.sqrt(magnitude, mc), mathContext);
	}
	
	// https://en.wikipedia.org/wiki/Complex_logarithm
	public static BigComplex log(BigComplex value, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		return BigComplex.valueOf(
				BigDecimalMath.log(value.abs(mc), mathContext),
				toRangePi(value.angle(mathContext), mathContext));
	}

	public static BigComplex pow(BigComplex x, long y, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal angleTimesN = x.angle(mc).multiply(BigDecimal.valueOf(y), mc);
		return BigComplex.valueOf(
				BigDecimalMath.cos(angleTimesN, mc),
				BigDecimalMath.sin(angleTimesN, mc)).multiply(BigDecimalMath.pow(x.abs(mc), y, mc), mathContext);
	}

	public static BigComplex pow(BigComplex x, BigDecimal y, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal angleTimesN = x.angle(mc).multiply(y, mc);
		return BigComplex.valueOf(
				BigDecimalMath.cos(angleTimesN, mc),
				BigDecimalMath.sin(angleTimesN, mc)).multiply(BigDecimalMath.pow(x.abs(mc), y, mc), mathContext);
	}

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
