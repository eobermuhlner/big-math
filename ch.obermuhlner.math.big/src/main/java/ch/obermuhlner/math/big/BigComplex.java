package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public class BigComplex {

	public final BigDecimal re;
	
	public final BigDecimal im;
	
	private BigComplex(BigDecimal re, BigDecimal im) {
		this.re = re;
		this.im = im;
	}
	
	public BigComplex add(BigComplex value) {
		return valueOf(
				re.add(value.re),
				im.add(value.im));
	}
	
	public BigComplex add(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.add(value.re, mathContext),
				im.add(value.im, mathContext));
	}
	
	public BigComplex subtract(BigComplex value) {
		return valueOf(
				re.subtract(value.re),
				im.subtract(value.im));
	}
	
	public BigComplex subtract(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.subtract(value.re, mathContext),
				im.subtract(value.im, mathContext));
	}

	public BigComplex multiply(BigComplex value) {
		return valueOf(
				re.multiply(value.re).subtract(im.multiply(value.im)),
				re.multiply(value.im).add(im.multiply(value.re)));
	}

	public BigComplex multiply(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.multiply(value.re, mathContext).subtract(im.multiply(value.im, mathContext), mathContext),
				re.multiply(value.im, mathContext).add(im.multiply(value.re, mathContext), mathContext));
	}

	public BigComplex multiply(BigDecimal value) {
		return valueOf(
				re.multiply(value),
				im.multiply(value));
	}

	public BigComplex multiply(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.multiply(value, mathContext),
				im.multiply(value, mathContext));
	}

	public BigComplex divide(BigComplex value, MathContext mathContext) {
		return multiply(value.reciprocal(mathContext), mathContext);
	}

	public BigComplex conjugate() {
		return valueOf(im, re);
	}
	
	public BigComplex reciprocal(MathContext mathContext) {
		BigDecimal scale = getScale(mathContext);
		return valueOf(
				re.divide(scale, mathContext),
				im.negate().divide(scale, mathContext));
	}
	
	public BigDecimal getLength(MathContext mathContext) {
		return BigDecimalMath.sqrt(getScale(mathContext), mathContext);
	}
	
	public BigDecimal getAngle(MathContext mathContext) {
		return BigDecimalMath.atan2(im, re, mathContext);
	}
	
	private BigDecimal getScale(MathContext mathContext) {
		return re.multiply(re, mathContext).add(im.multiply(im, mathContext), mathContext);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(re, im);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigComplex other = (BigComplex) obj;
		
		return re.equals(other.re) && im.equals(other.im);
	}
	
	@Override
	public String toString() {
		return "(" + re + ", " + im + "i)";
	}
	
	public static BigComplex valueOf(BigDecimal real) {
		return valueOf(real, BigDecimal.ZERO);
	}

	public static BigComplex valueOf(double real) {
		return valueOf(BigDecimal.valueOf(real), BigDecimal.ZERO);
	}

	public static BigComplex valueOf(double real, double imaginary) {
		return valueOf(BigDecimal.valueOf(real), BigDecimal.valueOf(imaginary));
	}

	public static BigComplex valueOf(BigDecimal real, BigDecimal imaginary) {
		return new BigComplex(real, imaginary);
	}

	public static BigComplex valueOfPolar(BigDecimal length, BigDecimal angle, MathContext mathContext) {
		return valueOf(
				length.multiply(BigDecimalMath.cos(angle, mathContext), mathContext),
				length.multiply(BigDecimalMath.sin(angle, mathContext), mathContext));
	}

}
