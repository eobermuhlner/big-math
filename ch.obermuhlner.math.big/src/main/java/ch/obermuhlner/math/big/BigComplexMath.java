package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigComplexMath {

	public static BigComplex reciprocal(BigComplex value, MathContext mathContext) {
		return value.reciprocal(mathContext);
	}

	public static BigComplex conjugate(BigComplex value) {
		return value.conjugate();
	}
	
	public static BigComplex exp(BigComplex value, MathContext mathContext) {
		BigDecimal expRe = BigDecimalMath.exp(value.re, mathContext);
		return BigComplex.valueOf(
				expRe.multiply(BigDecimalMath.cos(value.im, mathContext), mathContext),
				expRe.multiply(BigDecimalMath.sin(value.im, mathContext), mathContext));
	}
	
	public static BigComplex sin(BigComplex value, MathContext mathContext) {
		return BigComplex.valueOf(
				BigDecimalMath.sin(value.re, mathContext).multiply(BigDecimalMath.cosh(value.im, mathContext), mathContext),
				BigDecimalMath.cos(value.re, mathContext).multiply(BigDecimalMath.sinh(value.im, mathContext), mathContext));
	}
	
	public static BigComplex cos(BigComplex value, MathContext mathContext) {
		return BigComplex.valueOf(
				BigDecimalMath.cos(value.re, mathContext).multiply(BigDecimalMath.cosh(value.im, mathContext), mathContext),
				BigDecimalMath.sin(value.re, mathContext).multiply(BigDecimalMath.sinh(value.im, mathContext), mathContext).negate());
	}
	
	public static BigComplex tan(BigComplex value, MathContext mathContext) {
		return sin(value, mathContext).divide(cos(value, mathContext), mathContext);
	}
	
	//Handbook of Mathematics and Computational Science, Harris & Stocker, Springer, 2006
	// https://github.com/fjug/imglib/tree/master/ops/src/main/java/net/imglib2/ops/operation/complex/unary
	
	
	// https://en.wikipedia.org/wiki/Complex_logarithm
	public static BigComplex log(BigComplex value, MathContext mathContext) {
		return BigComplex.valueOf(
				BigDecimalMath.log(value.getLength(mathContext), mathContext),
				toRangePi(value.getAngle(mathContext), mathContext));
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
