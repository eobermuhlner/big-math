package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * Represents a complex number consisting of a real and an imaginary {@link BigDecimal} part in the form {@code a + bi}.
 *
 * <p>It generally follows the design of {@link BigDecimal} with some convenience improvements like overloaded operator methods.</p>
 *
 * <p>The biggest difference to {@link BigDecimal} is that {@link BigComplex#equals(Object) BigComplex.equals(Object)} implements the <strong>mathematical</strong> equality
 * and <strong>not</strong> the strict technical equality.
 * This was a difficult decision because it means that {@code BigComplex} behaves slightly different than {@link BigDecimal}
 * but considering that the strange equality of {@link BigDecimal} is a major source of bugs we
 * decided it was worth the slight inconsistency.
 * If you need the strict equality use {@link BigComplex#strictEquals(Object)}`.</p>
 *
 * <p>This class is immutable and therefore inherently thread safe.</p>
 */
public final class BigComplex {

	/**
	 * Zero represented as complex number.
	 */
	public static final BigComplex ZERO = new BigComplex(BigDecimal.ZERO, BigDecimal.ZERO);

	/**
	 * Real 1 represented as complex number.
	 */
	public static final BigComplex ONE = new BigComplex(BigDecimal.ONE, BigDecimal.ZERO);

	/**
	 * Imaginary 1 represented as complex number.
	 */
	public static final BigComplex I = new BigComplex(BigDecimal.ZERO, BigDecimal.ONE);

	/**
	 * The real {@link BigDecimal} part of this complex number.
	 */
	public final BigDecimal re;

	/**
	 * The imaginary {@link BigDecimal} part of this complex number.
	 */
	public final BigDecimal im;

	private BigComplex(BigDecimal re, BigDecimal im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates the addition of the given complex value to this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to add
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex add(BigComplex value) {
		return valueOf(
				re.add(value.re),
				im.add(value.im));
	}

	/**
	 * Calculates the addition of the given complex value to this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to add
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex add(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.add(value.re, mathContext),
				im.add(value.im, mathContext));
	}

	/**
	 * Calculates the addition of the given real {@link BigDecimal} value to this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to add
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex add(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.add(value, mathContext),
				im);
	}

	/**
	 * Calculates the addition of the given real {@link BigDecimal} value to this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to add
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex add(BigDecimal value) {
		return valueOf(
				re.add(value),
				im);
	}

	/**
	 * Calculates the addition of the given real {@code double} value to this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@code double} value to add
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex add(double value) {
		return add(BigDecimal.valueOf(value));
	}

	/**
	 * Calculates the subtraction of the given complex value from this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to subtract
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex subtract(BigComplex value) {
		return valueOf(
				re.subtract(value.re),
				im.subtract(value.im));
	}

	/**
	 * Calculates the subtraction of the given complex value from this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to subtract
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex subtract(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.subtract(value.re, mathContext),
				im.subtract(value.im, mathContext));
	}

	/**
	 * Calculates the subtraction of the given real {@link BigDecimal} value from this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to add
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex subtract(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.subtract(value, mathContext),
				im);
	}

	/**
	 * Calculates the subtraction of the given real {@link BigDecimal} value from this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to subtract
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex subtract(BigDecimal value) {
		return valueOf(
				re.subtract(value),
				im);
	}

	/**
	 * Calculates the subtraction of the given real {@code double} value from this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@code double} value to subtract
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex subtract(double value) {
		return subtract(BigDecimal.valueOf(value));
	}

	/**
	 * Calculates the multiplication of the given complex value to this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to multiply
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex multiply(BigComplex value) {
		return valueOf(
				re.multiply(value.re).subtract(im.multiply(value.im)),
				re.multiply(value.im).add(im.multiply(value.re)));
	}

	/**
	 * Calculates the multiplication of the given complex value with this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to multiply
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex multiply(BigComplex value, MathContext mathContext) {
		return valueOf(
				re.multiply(value.re, mathContext).subtract(im.multiply(value.im, mathContext), mathContext),
				re.multiply(value.im, mathContext).add(im.multiply(value.re, mathContext), mathContext));
	}

	/**
	 * Calculates the multiplication of the given real {@link BigDecimal} value with this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to multiply
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex multiply(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.multiply(value, mathContext),
				im.multiply(value, mathContext));
	}

	/**
	 * Calculates the multiplication of the given real {@link BigDecimal} value with this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@link BigDecimal} value to multiply
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex multiply(BigDecimal value) {
		return valueOf(
				re.multiply(value),
				im.multiply(value));
	}

	/**
	 * Calculates the multiplication of the given real {@code double} value with this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the real {@code double} value to multiply
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex multiply(double value) {
		return multiply(BigDecimal.valueOf(value));
	}

	/**
	 * Calculates this complex number divided by the given complex value using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigComplex} value to divide by
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex divide(BigComplex value, MathContext mathContext) {
		return multiply(value.reciprocal(mathContext), mathContext);
	}

	/**
	 * Calculates this complex number divided by the given real {@link BigDecimal} value using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@link BigDecimal} value to divide by
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex divide(BigDecimal value, MathContext mathContext) {
		return valueOf(
				re.divide(value, mathContext),
				im.divide(value, mathContext));
	}

	/**
	 * Calculates this complex number divided by the given real {@code double} value using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param value the {@code double} value to divide by
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex divide(double value, MathContext mathContext) {
		return divide(BigDecimal.valueOf(value), mathContext);
	}

	/**
	 * Calculates the reciprocal of this complex number using the specified {@link MathContext}.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex reciprocal(MathContext mathContext) {
		BigDecimal scale = absSquare(mathContext);
		return valueOf(
				re.divide(scale, mathContext),
				im.negate().divide(scale, mathContext));
	}

	/**
	 * Calculates the conjugate {@code a - bi} of this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex conjugate() {
		return valueOf(re, im.negate());
	}

	/**
	 * Calculates the negation {@code -a - bi} of this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @return the calculated {@link BigComplex} result
	 */
	public BigComplex negate() {
		return valueOf(re.negate(), im.negate());
	}

	/**
	 * Calculates the absolute value (also known as magnitude, length or radius) of this complex number.
	 *
	 * <p>This method is slower than {@link #absSquare(MathContext)} since it needs to calculate the {@link BigDecimalMath#sqrt(BigDecimal, MathContext)}.</p>
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 * @see #absSquare(MathContext)
	 */
	public BigDecimal abs(MathContext mathContext) {
		return BigDecimalMath.sqrt(absSquare(mathContext), mathContext);
	}

	/**
	 * Calculates the angle in radians (also known as argument) of this complex number.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 */
	public BigDecimal angle(MathContext mathContext) {
		return BigDecimalMath.atan2(im, re, mathContext);
	}

	/**
	 * Calculates the square of the absolute value of this complex number.
	 *
	 * <p>This method is faster than {@link #abs(MathContext)} since it does not need to calculate the {@link BigDecimalMath#sqrt(BigDecimal, MathContext)}.</p>
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the calculated {@link BigComplex} result
	 * @see #abs(MathContext)
	 */
	public BigDecimal absSquare(MathContext mathContext) {
		return re.multiply(re, mathContext).add(im.multiply(im, mathContext), mathContext);
	}

	/**
	 * Returns whether this complex number only has a real part (the imaginary part is 0).
	 *
	 * @return {@code true} if this complex number only has a real part, {@code false} if the imaginary part is not 0
	 */
	public boolean isReal() {
		return im.signum() == 0;
	}

	/**
	 * Returns the real part of this complex number as {@link BigComplex} number.
	 *
	 * @return the real part as as {@link BigComplex} number
	 */
	public BigComplex re() {
		return valueOf(re, BigDecimal.ZERO);
	}

	/**
	 * Returns the imaginary part of this complex number as {@link BigComplex} number.
	 *
	 * @return the imaginary part as as {@link BigComplex} number
	 */
	public BigComplex im() {
		return valueOf(BigDecimal.ZERO, im);
	}

	/**
	 * Returns this complex nuber rounded to the specified precision.
	 *
	 * <p>This methods <strong>does not</strong> modify this instance.</p>
	 *
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the rounded {@link BigComplex} result
	 */
	public BigComplex round(MathContext mathContext) {
		return valueOf(re.round(mathContext), im.round(mathContext));
	}

	@Override
	public int hashCode() {
		return Objects.hash(re, im);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Contrary to {@link BigDecimal#equals(Object)} this method implements <strong>mathematical</strong> equality
	 * (by calling {@link BigDecimal#compareTo(BigDecimal)} on the real and imaginary parts)
	 * instead of strict equality.</p>
	 *
	 * @see #strictEquals(Object)
	 */
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

	/**
	 * Returns whether the real and imaginary parts of this complex number are strictly equal.
	 *
	 * <p>This method uses the strict equality as defined by {@link BigDecimal#equals(Object)} on the real and imaginary parts.</p>
	 * <p>Please note that {@link #equals(Object) BigComplex.equals(Object)} implements <strong>mathematical</strong> equality instead
	 * (by calling {@link BigDecimal#compareTo(BigDecimal) on the real and imaginary parts}).</p>
	 *
	 * @param obj the object to compare for strict equality
	 * @return {@code true} if the specified object is strictly equal to this complex number
	 * @see #equals(Object)
	 */
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

	/**
	 * Returns a complex number with the specified real {@link BigDecimal} part.
	 *
	 * @param real the real {@link BigDecimal} part
	 * @return the complex number
	 */
	public static BigComplex valueOf(BigDecimal real) {
		return valueOf(real, BigDecimal.ZERO);
	}

	/**
	 * Returns a complex number with the specified real {@code double} part.
	 *
	 * @param real the real {@code double} part
	 * @return the complex number
	 */
	public static BigComplex valueOf(double real) {
		return valueOf(BigDecimal.valueOf(real), BigDecimal.ZERO);
	}

	/**
	 * Returns a complex number with the specified real and imaginary {@code double} parts.
	 *
	 * @param real the real {@code double} part
	 * @param imaginary the imaginary {@code double} part
	 * @return the complex number
	 */
	public static BigComplex valueOf(double real, double imaginary) {
		return valueOf(BigDecimal.valueOf(real), BigDecimal.valueOf(imaginary));
	}

	/**
	 * Returns a complex number with the specified real and imaginary {@link BigDecimal} parts.
	 *
	 * @param real the real {@link BigDecimal} part
	 * @param imaginary the imaginary {@link BigDecimal} part
	 * @return the complex number
	 */
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

	/**
	 * Returns a complex number with the specified polar {@link BigDecimal} radius and angle using the specified {@link MathContext}.
	 *
	 * @param radius the {@link BigDecimal} radius of the polar representation
	 * @param angle the {@link BigDecimal} angle in radians of the polar representation
	 * @param mathContext the {@link MathContext} used to calculate the result
	 * @return the complex number
	 */
	public static BigComplex valueOfPolar(BigDecimal radius, BigDecimal angle, MathContext mathContext) {
		if (radius.signum() == 0) {
			return ZERO;
		}

		return valueOf(
				radius.multiply(BigDecimalMath.cos(angle, mathContext), mathContext),
				radius.multiply(BigDecimalMath.sin(angle, mathContext), mathContext));
	}

	public static BigComplex valueOfPolar(double radius, double angle, MathContext mathContext) {
		return valueOfPolar(BigDecimal.valueOf(radius), BigDecimal.valueOf(angle), mathContext);
	}
}
