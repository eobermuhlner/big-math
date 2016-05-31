package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * A rational number represented as a quotient of two values.
 * 
 * <p>Basic calculations with rational numbers (+ - * /) have no loss of precision.
 * This allows to use {@link BigRational} as a replacement for {@link BigDecimal} if absolute accuracy is desired.</p>
 * 
 * <p><a href="http://en.wikipedia.org/wiki/Rational_number">Wikipedia: Rational number</a></p>
 * 
 * <p>The values are internally stored as {@link BigDecimal} (for performance optimizations) but represented
 * as {@link BigInteger} (for mathematical correctness)
 * when accessed with {@link #getNumerator()} and {@link #getDenominator()}.</p>
 * 
 * <p>The following basic calculations have no loss of precision:
 * <ul>
 * <li>{@link #add(BigRational)}</li>
 * <li>{@link #subtract(BigRational)}</li>
 * <li>{@link #multiply(BigRational)}</li>
 * <li>{@link #divide(BigRational)}</li>
 * <li>{@link #pow(int)}</li>
 * <li>{@link #pow(BigRational, int)} with integer arguments</li>
 * </ul>
 * </p>
 * 
 * <p>The following calculations are special cases of the ones listed above and have no loss of precision:
 * <ul>
 * <li>{@link #negate()}</li>
 * <li>{@link #reciprocal()}</li>
 * <li>{@link #increment()}</li>
 * <li>{@link #decrement()}</li>
 * <li>{@link #factorial(int)}</li>
 * </ul>
 * </p>
 * 
 * <p>Any {@link BigRational} value can be converted into an arbitrary {@link #withPrecision(int) precision} (number of significant digits)
 * or {@link #withScale(int) scale} (number of digits after the decimal point).</p>
 */
public class BigRational implements Comparable<BigRational> {

	/**
	 * The value 0 as {@link BigRational}.
	 */
	public static final BigRational ZERO = new BigRational(0);
	/**
	 * The value 1 as {@link BigRational}.
	 */
	public static final BigRational ONE = new BigRational(1);
	/**
	 * The value 2 as {@link BigRational}.
	 */
	public static final BigRational TWO = new BigRational(2);
	/**
	 * The value 10 as {@link BigRational}.
	 */
	public static final BigRational TEN = new BigRational(10);

	private static final BigDecimal BIGDECIMAL_MAX_INT = BigDecimal.valueOf(Integer.MAX_VALUE);

	private final BigDecimal numerator;

	private final BigDecimal denominator;

	private BigRational(int value) {
		this(BigDecimal.valueOf(value), BigDecimal.ONE);
	}

	private BigRational(BigDecimal num, BigDecimal denom) {
		BigDecimal n = num;
		BigDecimal d = denom;

		if (d.signum() == 0) {
			throw new ArithmeticException("Divide by zero");
		}

		if (d.signum() < 0) {
			n = n.negate();
			d = d.negate();
		}

		numerator = n;
		denominator = d;
	}

	/**
	 * Returns the numerator of this rational number.
	 * 
	 * @return the numerator
	 */
	public BigInteger getNumerator() {
		return numerator.toBigInteger();
	}

	/**
	 * Returns the denominator of this rational number.
	 * 
	 * <p>Guaranteed to not be 0.</p>
	 * <p>Guaranteed to be positive.</p>
	 * 
	 * @return the denominator
	 */
	public BigInteger getDenominator() {
		return denominator.toBigInteger();
	}

	/**
	 * Reduces this rational number to the smallest numerator/denominator with the same value.
	 * 
	 * @return the reduced rational number
	 */
	public BigRational reduce() {
		BigInteger n = numerator.toBigInteger();
		BigInteger d = denominator.toBigInteger();

		BigInteger gcd = n.gcd(d);
		n = n.divide(gcd);
		d = d.divide(gcd);

		return valueOf(n, d);
	}

	/**
	 * Returns the integer part of this rational number.
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(3.5).integerPart()</code> returns <code>BigRational.valueOf(3)</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @return the integer part of this rational number
	 */
	public BigRational integerPart() {
		return valueOf(numerator.subtract(numerator.remainder(denominator)), denominator);
	}

	/**
	 * Returns the fraction part of this rational number.
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(3.5).integerPart()</code> returns <code>BigRational.valueOf(0.5)</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @return the fraction part of this rational number
	 */
	public BigRational fractionPart() {
		return valueOf(numerator.remainder(denominator), denominator);
	}
	
	/**
	 * Negates this rational number (inverting the sign).
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(3.5).negate()</code> returns <code>BigRational.valueOf(-3.5)</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @return the negated rational number
	 */
	public BigRational negate() {
		if (isZero()) {
			return this;
		}

		return valueOf(numerator.negate(), denominator);
	}

	/**
	 * Calculates the reciprocal of this rational number (1/x).
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(0.5).reciprocal()</code> returns <code>BigRational.valueOf(2)</code></li>
	 * <li><code>BigRational.valueOf(-2).reciprocal()</code> returns <code>BigRational.valueOf(-0.5)</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @return the reciprocal rational number
	 * @throws ArithmeticException if the argument is 0 (division by zero)
	 */
	public BigRational reciprocal() {
		return valueOf(denominator, numerator);
	}

	/**
	 * Returns the absolute value of this rational number.
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(-2).abs()</code> returns <code>BigRational.valueOf(2)</code></li>
	 * <li><code>BigRational.valueOf(2).abs()</code> returns <code>BigRational.valueOf(2)</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @return the absolute rational number (positive, or 0 if this rational is 0)
	 */
	public BigRational abs() {
		return isPositive() ? this : negate();
	}

	/**
	 * Returns the signum function of this rational number.
	 *
	 * @return -1, 0 or 1 as the value of this rational number is negative, zero or positive.
	 */
	public int signum() {
		return numerator.signum();
	}

	/**
	 * Calculates the increment of this rational number (+ 1).
	 * 
	 * <p>This is functionally identical to
	 * <code>this.add(BigRational.ONE)</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @return the incremented rational number
	 */
	public BigRational increment() {
		return valueOf(numerator.add(denominator), denominator);
	}

	/**
	 * Calculates the decrement of this rational number (- 1).
	 * 
	 * <p>This is functionally identical to
	 * <code>this.subtract(BigRational.ONE)</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @return the decremented rational number
	 */
	public BigRational decrement() {
		return valueOf(numerator.subtract(denominator), denominator);
	}

	/**
	 * Calculates the addition (+) of this rational number and the specified argument.
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the rational number to add
	 * @return the resulting rational number
	 */
	public BigRational add(BigRational value) {
		if (denominator.equals(value.denominator)) {
			return valueOf(numerator.add(value.numerator), denominator);
		}

		BigDecimal n = numerator.multiply(value.denominator).add(value.numerator.multiply(denominator));
		BigDecimal d = denominator.multiply(value.denominator);
		return valueOf(n, d);
	}

	private BigRational add(BigDecimal value) {
		return valueOf(numerator.add(value.multiply(denominator)), denominator);
	}
	
	/**
	 * Calculates the addition (+) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.add(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the {@link BigInteger} to add
	 * @return the resulting rational number
	 */
	public BigRational add(BigInteger value) {
		if (value.equals(BigInteger.ZERO)) {
			return this;
		}
		return add(new BigDecimal(value));
	}

	/**
	 * Calculates the addition (+) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.add(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the int value to add
	 * @return the resulting rational number
	 */
	public BigRational add(int value) {
		if (value == 0) {
			return this;
		}
		return add(BigInteger.valueOf(value));
	}

	/**
	 * Calculates the subtraction (-) of this rational number and the specified argument.
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the rational number to subtract
	 * @return the resulting rational number
	 */
	public BigRational subtract(BigRational value) {
		if (denominator.equals(value.denominator)) {
			return valueOf(numerator.subtract(value.numerator), denominator);
		}

		BigDecimal n = numerator.multiply(value.denominator).subtract(value.numerator.multiply(denominator));
		BigDecimal d = denominator.multiply(value.denominator);
		return valueOf(n, d);
	}

	private BigRational subtract(BigDecimal value) {
		return valueOf(numerator.subtract(value.multiply(denominator)), denominator);
	}
	
	/**
	 * Calculates the subtraction (-) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.subtract(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the {@link BigInteger} to subtract
	 * @return the resulting rational number
	 */
	public BigRational subtract(BigInteger value) {
		if (value.equals(BigInteger.ZERO)) {
			return this;
		}
		return subtract(new BigDecimal(value));
	}

	/**
	 * Calculates the subtraction (-) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.subtract(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the int value to subtract
	 * @return the resulting rational number
	 */
	public BigRational subtract(int value) {
		if (value == 0) {
			return this;
		}
		return subtract(BigInteger.valueOf(value));
	}

	/**
	 * Calculates the multiplication (*) of this rational number and the specified argument.
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the rational number to multiply
	 * @return the resulting rational number
	 */
	public BigRational multiply(BigRational value) {
		if (isZero() || value.isZero()) {
			return ZERO;
		}
		if (equals(ONE)) {
			return value;
		}
		if (value.equals(ONE)) {
			return this;
		}

		BigDecimal n = numerator.multiply(value.numerator);
		BigDecimal d = denominator.multiply(value.denominator);
		return valueOf(n, d);
	}

	// private, because we want to hide that we use BigDecimal internally
	private BigRational multiply(BigDecimal value) {
		BigDecimal n = numerator.multiply(value);
		BigDecimal d = denominator;
		return valueOf(n, d);
	}
	
	/**
	 * Calculates the multiplication (*) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.multiply(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the {@link BigInteger} to multiply
	 * @return the resulting rational number
	 */
	public BigRational multiply(BigInteger value) {
		if (isZero() || value.signum() == 0) {
			return ZERO;
		}
		if (equals(ONE)) {
			return valueOf(value);
		}
		if (value.equals(BigInteger.ONE)) {
			return this;
		}

		return multiply(new BigDecimal(value));
	}

	/**
	 * Calculates the multiplication (*) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.multiply(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the int value to multiply
	 * @return the resulting rational number
	 */
	public BigRational multiply(int value) {
		return multiply(BigInteger.valueOf(value));
	}

	/**
	 * Calculates the division (/) of this rational number and the specified argument.
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the rational number to divide (0 is not allowed)
	 * @return the resulting rational number
	 * @throws ArithmeticException if the argument is 0 (division by zero)
	 */
	public BigRational divide(BigRational value) {
		if (value.equals(ONE)) {
			return this;
		}

		BigDecimal n = numerator.multiply(value.denominator);
		BigDecimal d = denominator.multiply(value.numerator);
		return valueOf(n, d);
	}

	private BigRational divide(BigDecimal value) {
		BigDecimal n = numerator;
		BigDecimal d = denominator.multiply(value);
		return valueOf(n, d);
	}
	
	/**
	 * Calculates the division (/) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.divide(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the {@link BigInteger} to divide (0 is not allowed)
	 * @return the resulting rational number
	 * @throws ArithmeticException if the argument is 0 (division by zero)
	 */
	public BigRational divide(BigInteger value) {
		if (value.equals(BigInteger.ONE)) {
			return this;
		}

		return divide(new BigDecimal(value));
	}

	/**
	 * Calculates the division (/) of this rational number and the specified argument.
	 * 
	 * <p>This is functionally identical to
	 * <code>this.divide(BigRational.valueOf(value))</code>
	 * but slightly faster.</p>
	 * 
	 * <p>The result has no loss of precision.</p>
	 * 
	 * @param value the int value to divide (0 is not allowed)
	 * @return the resulting rational number
	 * @throws ArithmeticException if the argument is 0 (division by zero)
	 */
	public BigRational divide(int value) {
		return divide(BigInteger.valueOf(value));
	}

	/**
	 * Returns whether this rational number is zero.
	 * 
	 * @return <code>true</code> if this rational number is zero (0), <code>false</code> if it is not zero
	 */
	public boolean isZero() {
		return numerator.signum() == 0;
	}

	private boolean isOne() {
		return numerator.equals(denominator);
	}

	private boolean isPositive() {
		return numerator.signum() > 0;
	}

	/**
	 * Returns whether this rational number is an integer number without fraction part.
	 * 
	 * @return <code>true</code> if this rational number is an integer number, <code>false</code> if it has a fraction part
	 */
	public boolean isInteger() {
		return isIntegerInternal() || reduce().isIntegerInternal();
	}

	/**
	 * Returns whether this rational number is an integer number without fraction part.
	 * 
	 * <p>Will return <code>false</code> if this number is not reduced to the integer representation yet (e.g. 4/4 or 4/2)</p>
	 * 
	 * @return <code>true</code> if this rational number is an integer number, <code>false</code> if it has a fraction part
	 * @see #isInteger()
	 */
	private boolean isIntegerInternal() {
		return denominator.compareTo(BigDecimal.ONE) == 0;
	}

	/**
	 * Calculates this rational number to the power (x<sup>y</sup>) of the specified argument.
	 * 
	 * <p>The result has no loss of precision.</p>
	 *
	 * @param exponent exponent to which this rational number is to be raised
	 * @return the resulting rational number
	 * @see #pow(BigRational, int)
	 */
	public BigRational pow(int exponent) {
		if (exponent == 0) {
			return ONE;
		}
		if (exponent == 1) {
			return this;
		}

		final BigInteger n;
		final BigInteger d;
		if (exponent > 0) {
			n = numerator.toBigInteger().pow(exponent);
			d = denominator.toBigInteger().pow(exponent);
		}
		else {
			n = denominator.toBigInteger().pow(-exponent);
			d = numerator.toBigInteger().pow(-exponent);
		}
		return valueOf(n, d);
	}

	/**
	 * Finds the minimum (smaller) of two rational numbers.
	 * 
	 * @param value the rational number to compare with
	 * @return the minimum rational number, either <code>this</code> or the argument <code>value</code>
	 */
	private BigRational min(BigRational value) {
		return compareTo(value) <= 0 ? this : value;
	}

	/**
	 * Finds the maximum (larger) of two rational numbers.
	 * 
	 * @param value the rational number to compare with
	 * @return the minimum rational number, either <code>this</code> or the argument <code>value</code>
	 */
	private BigRational max(BigRational value) {
		return compareTo(value) >= 0 ? this : value;
	}

	/**
	 * Returns a rational number with approximatively <code>this</code> value and the specified precision.
	 * 
	 * @param precision the precision (number of significant digits) of the calculated result, or 0 for unlimited precision
	 * @return the calculated rational number with the specified precision
	 */
	public BigRational withPrecision(int precision) {
		return valueOf(toBigDecimal(new MathContext(precision)));
	}

	/**
	 * Returns a rational number with approximatively <code>this</code> value and the specified scale.
	 * 
	 * @param scale the scale (number of digits after the decimal point) of the calculated result
	 * @return the calculated rational number with the specified scale
	 */
	public BigRational withScale(int scale) {
		return valueOf(toBigDecimal().setScale(scale, RoundingMode.HALF_UP));
	}

	private static int countDigits(BigInteger number) {
		double factor = Math.log(2) / Math.log(10);
		int digitCount = (int) (factor * number.bitLength() + 1);
		if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
			return digitCount - 1;
		}
		return digitCount;
	}

	// TODO what is precision of a rational?
	private int precision() {
		return countDigits(numerator.toBigInteger()) + countDigits(denominator.toBigInteger());
	}

	/**
	 * Returns this rational number as a double value.
	 * 
	 * @return the double value
	 */
	public double toDouble() {
		// TODO best accuracy or maybe bigDecimalValue().doubleValue() is better?
		return numerator.doubleValue() / denominator.doubleValue();
	}

	/**
	 * Returns this rational number as a float value.
	 * 
	 * @return the float value
	 */
	public float toFloat() {
		return numerator.floatValue() / denominator.floatValue();
	}

	/**
	 * Returns this rational number as a {@link BigDecimal}.
	 * 
	 * @return the {@link BigDecimal} value
	 */
	public BigDecimal toBigDecimal() {
		int precision = Math.max(precision(), MathContext.DECIMAL128.getPrecision());
		return toBigDecimal(new MathContext(precision));
	}

	/**
	 * Returns this rational number as a {@link BigDecimal} with the precision specified by the {@link MathContext}.
	 * 
	 * @param mc the {@link MathContext} specifying the precision of the calculated result
	 * @return the {@link BigDecimal}
	 */
	public BigDecimal toBigDecimal(MathContext mc) {
		return numerator.divide(denominator, mc);
	}

	@Override
	public int compareTo(BigRational other) {
		if (this == other) {
			return 0;
		}
		return numerator.multiply(other.denominator).compareTo(denominator.multiply(other.numerator));
	}

	@Override
	public int hashCode() {
		if (isZero()) {
			return 0;
		}
		return numerator.hashCode() + denominator.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof BigRational)) {
			return false;
		}

		BigRational other = (BigRational) obj;
		if (!numerator.equals(other.numerator)) {
			return false;
		}
		return denominator.equals(other.denominator);
	}

	@Override
	public String toString() {
		if (isZero()) {
			return "0";
		}
		if (isIntegerInternal()) {
			return numerator.toString();
		}
		return toBigDecimal().toString();
	}

	/**
	 * Returns a plain string representation of this rational number without any exponent.
	 * 
	 * @return the plain string representation
	 * @see BigDecimal#toPlainString()
	 */
	public String toPlainString() {
		if (isZero()) {
			return "0";
		}
		if (isIntegerInternal()) {
			return numerator.toPlainString();
		}
		return toBigDecimal().toPlainString();
	}

	/**
	 * Returns the string representation of this rational number in the form "numerator/denominator".
	 * 
	 * <p>The resulting string is a valid input of the {@link #valueOf(String)} method.</p>
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li><code>BigRational.valueOf(0.5).toRationalString()</code> returns <code>"1/2"</code></li>
	 * <li><code>BigRational.valueOf(2).toRationalString()</code> returns <code>"2"</code></li>
	 * <li><code>BigRational.valueOf(4, 4).toRationalString()</code> returns <code>"4/4"</code> (not reduced)</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the rational number string representation in the form "numerator/denominator", or "0" if the rational number is 0.
	 * @see #valueOf(String) 
	 * @see #valueOf(int, int) 
	 */
	public String toRationalString() {
		if (isZero()) {
			return "0";
		}
		if (isIntegerInternal()) {
			return numerator.toString();
		}
		return numerator + "/" + denominator;
	}

	/**
	 * Returns the string representation of this rational number as integer and fraction parts in the form "integerPart fractionNominator/fractionDenominator".
	 * 
	 * <p>The integer part is omitted if it is 0 (when this absolute rational number is smaller than 1).</p>
	 * <p>The fraction part is omitted it it is 0 (when this rational number is an integer).</p>
	 * <p>If this rational number is 0, then "0" is returned.</p>
	 * 
	 * <p>Example: <code>BigRational.valueOf(3.5).toIntegerRationalString()</code> returns <code>"3 1/2"</code>.</p>
	 * 
	 * @return the integer and fraction rational string representation
	 * @see #valueOf(int, int, int)
	 */
	public String toIntegerRationalString() {
		BigDecimal fractionNumerator = numerator.remainder(denominator);
		BigDecimal integerNumerator = numerator.subtract(fractionNumerator);
		BigDecimal integerPart = integerNumerator.divide(denominator);

		StringBuilder result = new StringBuilder();
		if (integerPart.signum() != 0) {
			result.append(integerPart);
		}
		if (fractionNumerator.signum() != 0) {
			if (result.length() > 0) {
				result.append(' ');
			}
			result.append(fractionNumerator.abs());
			result.append('/');
			result.append(denominator);
		}
		if (result.length() == 0) {
			result.append('0');
		}
		
		return result.toString();
	}
	
	/**
	 * Creates a rational number of the specified int value.
	 * 
	 * @param value the int value
	 * @return the rational number
	 */
	public static BigRational valueOf(int value) {
		if (value == 0) {
			return ZERO;
		}
		if (value == 1) {
			return ONE;
		}
		return new BigRational(value);
	}

	/**
	 * Creates a rational number of the specified numerator/denominator int values.
	 * 
	 * @param numerator the numerator int value
	 * @param denominator the denominator int value (0 not allowed)
	 * @return the rational number
	 * @throws ArithmeticException if the denominator is 0 (division by zero)
	 */
	public static BigRational valueOf(int numerator, int denominator) {
		return valueOf(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
	}

	/**
	 * Creates a rational number of the specified integer and fraction parts.
	 * 
	 * <p>Useful to create numbers like 3 1/2 (= three and a half = 3.5) by calling
	 * <code>BigRational.valueOf(3, 1, 2)</code>.</p>
	 * <p>To create a negative rational only the integer part argument is allowed to be negative:
	 * to create -3 1/2 (= minus three and a half = -3.5) call <code>BigRational.valueOf(-3, 1, 2)</code>.</p> 
	 * 
	 * @param integer the integer part int value
	 * @param fractionNumerator the fraction part numerator int value (negative not allowed)
	 * @param fractionDenominator the fraction part denominator int value (0 or negative not allowed)
	 * @return the rational number
	 * @throws ArithmeticException if the fraction part denominator is 0 (division by zero),
	 * or if the fraction part numerator or denominator is negative
	 */
	public static BigRational valueOf(int integer, int fractionNumerator, int fractionDenominator) {
		if (fractionNumerator < 0 || fractionDenominator < 0) {
			throw new ArithmeticException("Negative value");
		}
		
		BigRational integerPart = valueOf(integer);
		BigRational fractionPart = valueOf(fractionNumerator, fractionDenominator);
		return integerPart.isPositive() ? integerPart.add(fractionPart) : integerPart.subtract(fractionPart);
	}

	/**
	 * Creates a rational number of the specified numerator/denominator BigInteger values.
	 * 
	 * @param numerator the numerator {@link BigInteger} value
	 * @param denominator the denominator {@link BigInteger} value (0 not allowed)
	 * @return the rational number
	 * @throws ArithmeticException if the denominator is 0 (division by zero)
	 */
	public static BigRational valueOf(BigInteger numerator, BigInteger denominator) {
		return valueOf(new BigDecimal(numerator), new BigDecimal(denominator));
	}

	/**
	 * Creates a rational number of the specified {@link BigInteger} value.
	 * 
	 * @param value the {@link BigInteger} value
	 * @return the rational number
	 */
	public static BigRational valueOf(BigInteger value) {
		if (value.compareTo(BigInteger.ZERO) == 0) {
			return ZERO;
		}
		if (value.compareTo(BigInteger.ONE) == 0) {
			return ONE;
		}
		return valueOf(value, BigInteger.ONE);
	}

	/**
	 * Creates a rational number of the specified double value.
	 * 
	 * @param value the double value
	 * @return the rational number
	 * @throws NumberFormatException if the double value is Infinite or NaN.
	 */
	public static BigRational valueOf(double value) {
		if (value == 0.0) {
			return ZERO;
		}
		if (value == 1.0) {
			return ONE;
		}
		if (Double.isInfinite(value)) {
			throw new NumberFormatException("Infinite");
		}
		if (Double.isNaN(value)) {
			throw new NumberFormatException("NaN");
		}
		return valueOf(new BigDecimal(String.valueOf(value)));
	}

	/**
	 * Creates a rational number of the specified {@link BigDecimal} value.
	 * 
	 * @param value the double value
	 * @return the rational number
	 */
	public static BigRational valueOf(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) == 0) {
			return ZERO;
		}
		if (value.compareTo(BigDecimal.ONE) == 0) {
			return ONE;
		}
		if (value.scale() < 0) {
			BigDecimal n = new BigDecimal(value.unscaledValue().multiply(BigInteger.TEN.pow(-value.scale())));
			return new BigRational(n, BigDecimal.ONE);
		}
		else {
			BigDecimal n = new BigDecimal(value.unscaledValue());
			BigDecimal d = new BigDecimal(BigInteger.TEN.pow(value.scale()));
			return new BigRational(n, d);
		}
	}

	/**
	 * Creates a rational number of the specified string representation.
	 * 
	 * <p>The accepted string representations are:
	 * <ul>
	 * <li>Output of {@link BigRational#toString()} : "integerPart.fractionPart"</li>
	 * <li>Output of {@link BigRational#toRationalString()} : "numerator/denominator"</li>
	 * <li>Output of <code>toString()</code> of {@link BigDecimal}, {@link BigInteger}, {@link Integer}, ...</li>
	 * <li>Output of <code>toString()</code> of {@link Double}, {@link Float} - except "Infinity", "-Infinity" and "NaN"</li>
	 * </ul>
	 * </p>
	 * 
	 * @param string the string representation to convert
	 * @return the rational number
	 * @throws ArithmeticException if the denominator is 0 (division by zero)
	 */
	public static BigRational valueOf(String string) {
		String[] strings = string.split("/");
		BigRational result = valueOfSimple(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			result = result.divide(valueOfSimple(strings[i]));
		}
		return result;
	}

	private static BigRational valueOfSimple(String string) {
		return valueOf(new BigDecimal(string));
	}

	public static BigRational valueOfSimple2(String string) {
		boolean positive = true;
		String integerPart = "";
		String fractionPart = "";
		String fractionRepeatPart = "";
		String exponentPart = "";

		int index = 0;
		if (string.charAt(index) == '+') {
			index++;
		}
		else if (string.charAt(index) == '-') {
			index++;
			positive = false;
		}

		int pointPos = string.indexOf('.', index);
		if (pointPos >= 0) {
			integerPart = string.substring(index, pointPos);
			index = pointPos;
		}

		int repeatPos = string.indexOf('[', index);
		int exponentPos = string.indexOf('E', index);

		if (repeatPos >= 0) {
			fractionPart = string.substring(index, repeatPos);

			int repeatEndPos = string.indexOf(']', index);
			if (repeatEndPos == -1) {
				repeatEndPos = string.length();
			}
			fractionRepeatPart = string.substring(repeatPos, repeatEndPos);
			index = repeatEndPos;
		}
		else {
			if (exponentPos >= 0) {
				fractionPart = string.substring(repeatPos, exponentPos);
			}
			else {
				fractionPart = string.substring(index);
			}
		}

		if (exponentPos >= 0) {
			exponentPart = string.substring(exponentPos);
		}

		return valueOf(positive, integerPart, fractionPart, fractionRepeatPart, exponentPart);
	}

	public static BigRational valueOf(boolean positive, String integerPart, String fractionPart, String fractionRepeatPart, String exponentPart) {
		BigRational result = ZERO;

		if (fractionRepeatPart != null && fractionRepeatPart.length() > 0) {
			BigInteger lotsOfNines = BigInteger.TEN.pow(fractionRepeatPart.length()).subtract(BigInteger.ONE);
			result = valueOf(new BigInteger(fractionRepeatPart), lotsOfNines);
		}

		if (fractionPart != null && fractionPart.length() > 0) {
			result = result.add(valueOf(new BigInteger(fractionPart)));
			result = result.divide(BigInteger.TEN.pow(fractionPart.length()));
		}

		if (integerPart != null && integerPart.length() > 0) {
			result = result.add(new BigInteger(integerPart));
		}

		if (exponentPart != null && exponentPart.length() > 0) {
			int exponent = Integer.parseInt(exponentPart);
			BigInteger powerOfTen = BigInteger.TEN.pow(Math.abs(exponent));
			result = exponent >= 0 ? result.multiply(powerOfTen) : result.divide(powerOfTen);
		}

		if (!positive) {
			result = result.negate();
		}

		return result;
	}

	private static BigRational valueOf(BigDecimal numerator, BigDecimal denominator) {
		if (numerator.signum() == 0 && denominator.signum() != 0) {
			return ZERO;
		}
		if (numerator.compareTo(BigDecimal.ONE) == 0 && denominator.compareTo(BigDecimal.ONE) == 0) {
			return ONE;
		}
		return new BigRational(numerator, denominator);
	}

	/**
	 * Returns the smallest of the specified rational numbers.
	 * 
	 * @param values the rational numbers to compare
	 * @return the smallest rational number, 0 if no numbers are specified
	 */
	public static BigRational min(BigRational... values) {
		if (values.length == 0) {
			return BigRational.ZERO;
		}
		BigRational result = values[0];
		for (int i = 1; i < values.length; i++) {
			result = result.min(values[i]);
		}
		return result;
	}

	/**
	 * Returns the largest of the specified rational numbers.
	 * 
	 * @param values the rational numbers to compare
	 * @return the largest rational number, 0 if no numbers are specified
	 * @see #max(BigRational)
	 */
	public static BigRational max(BigRational... values) {
		if (values.length == 0) {
			return BigRational.ZERO;
		}
		BigRational result = values[0];
		for (int i = 1; i < values.length; i++) {
			result = result.max(values[i]);
		}
		return result;
	}

	private static List<BigRational> bernoulliCache = new ArrayList<>();
	
    public static BigRational bernoulli(int n) {
    	if (n == 1) {
    		return valueOf(-1, 2);
    	} else if (n % 2 == 1) {
    		return ZERO;
    	}
    	
    	synchronized (bernoulliCache) {
    		int index = n / 2;
    		
    		if (bernoulliCache.size() <= index) {
    			for (int i = bernoulliCache.size(); i <= index; i++) {
    				BigRational b = calculateBernoulli(i * 2);
					bernoulliCache.add(b);
				}
    		}
    		
    		return bernoulliCache.get(index);
		}
    }
    
    private static BigRational calculateBernoulli(int n) {
        BigRational result = ZERO ;
        for(int k=0 ; k <= n ; k++) {
            BigRational jSum = ZERO ;
            BigRational bin = ONE ;
            for(int j=0 ; j <= k ; j++) {
                BigRational jPowN = valueOf(j).pow(n);
                if (j % 2 == 0) {
                	jSum = jSum.add(bin.multiply(jPowN)) ;
                } else {
                	jSum = jSum.subtract(bin.multiply(jPowN)) ;
                }

                bin = bin.multiply(valueOf(k-j).divide(valueOf(j+1)));
            }
            result = result.add(jSum.divide(valueOf(k+1)));
        }
        return result;
    }

}
