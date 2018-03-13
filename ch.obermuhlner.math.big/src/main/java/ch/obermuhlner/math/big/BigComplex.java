package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public final class BigComplex {
	
	public static final BigComplex ZERO = new BigComplex(BigDecimal.ZERO, BigDecimal.ZERO);
	public static final BigComplex ONE = new BigComplex(BigDecimal.ONE, BigDecimal.ZERO);
	public static final BigComplex I = new BigComplex(BigDecimal.ZERO, BigDecimal.ONE);

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
	
	public BigComplex add(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.add(value, mathContext),
				im);
	}
	
	public BigComplex add(BigDecimal value) {
		return valueOf(
				re.add(value),
				im);
	}
	
	public BigComplex add(double value) {
		return add(BigDecimal.valueOf(value));
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

	public BigComplex subtract(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.subtract(value, mathContext),
				im);
	}
	
	public BigComplex subtract(BigDecimal value) {
		return valueOf(
				re.subtract(value),
				im);
	}
	
	public BigComplex subtract(double value) {
		return subtract(BigDecimal.valueOf(value));
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

	public BigComplex multiply(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.multiply(value, mathContext),
				im.multiply(value, mathContext));
	}

	public BigComplex multiply(BigDecimal value) {
		return valueOf(
				re.multiply(value),
				im.multiply(value));
	}

	public BigComplex multiply(double value) {
		return multiply(BigDecimal.valueOf(value));
	}
	
	public BigComplex divide(BigComplex value, MathContext mathContext) {
		return multiply(value.reciprocal(mathContext), mathContext);
	}

	public BigComplex divide(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.divide(value),
				im.divide(value));
	}

	public BigComplex divide(double value, MathContext mathContext) {
		return divide(BigDecimal.valueOf(value), mathContext);
	}
	
	public BigComplex reciprocal(MathContext mathContext) {
		BigDecimal scale = getScale(mathContext);
		return valueOf(
				re.divide(scale, mathContext),
				im.negate().divide(scale, mathContext));
	}
	
	public BigComplex conjugate() {
		return valueOf(re, im.negate());
	}
	
	public BigDecimal abs(MathContext mathContext) {
		return BigDecimalMath.sqrt(getScale(mathContext), mathContext);
	}
	
	public BigDecimal angle(MathContext mathContext) {
		return BigDecimalMath.atan2(im, re, mathContext);
	}
	
	private BigDecimal getScale(MathContext mathContext) {
		return re.multiply(re, mathContext).add(im.multiply(im, mathContext), mathContext);
	}
	
	public boolean isReal() {
		return im.signum() == 0;
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
		
		return re.compareTo(other.re) == 0 && im.compareTo(other.im) == 0;
	}
	
	public boolean strictEquals(Object obj) {
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
		if (im.signum() >= 0) {
			return "(" + re + " + " + im + " i)";
		} else {
			return "(" + re + " - " + im.negate() + " i)";
		}
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

	public static BigComplex valueOf(String real, String imaginary) {
		return valueOf(new BigDecimal(real), new BigDecimal(imaginary));
	}
	
	public static BigComplex valueOf(String real, String imaginary, MathContext mathContext) {
		return valueOf(new BigDecimal(real, mathContext), new BigDecimal(imaginary, mathContext));
	}
	
	public static BigComplex valueOf(BigDecimal real, BigDecimal imaginary) {
		if (real.signum() == 0) {
			if (imaginary.signum() == 0) {
				return ZERO;
			}
			if (imaginary.compareTo(BigDecimal.ONE) == 0) {
				return I;
			}
		}
		if (imaginary.signum() == 0 && real.compareTo(BigDecimal.ONE) == 0) {
			return ONE;
		}
		
		return new BigComplex(real, imaginary);
	}

	public static BigComplex valueOfPolar(BigDecimal length, BigDecimal angle, MathContext mathContext) {
		return valueOf(
				length.multiply(BigDecimalMath.cos(angle, mathContext), mathContext),
				length.multiply(BigDecimalMath.sin(angle, mathContext), mathContext));
	}

}
