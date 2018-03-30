package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A wrapper around {@link BigDecimalMath} which simplifies the consistent usage of the {@link MathContext}.
 */
public final class BigDecimalMathContext {
	private final MathContext mathContext;

	/**
	 * Constructs a {@link BigDecimalMathContext} with the specified {@link MathContext}.
	 *
	 * @param mathContext the {@link MathContext} to be used for all functions of this {@link BigDecimalMathContext}
	 */
	public BigDecimalMathContext(MathContext mathContext) {
		this.mathContext = mathContext;
	}

	/**
	 * Constructs a {@link BigDecimalMathContext} with the specified {@link MathContext}, increasing the precision by the given delta.
	 *
	 * @param mathContext the {@link MathContext}
	 * @param deltaPrecision the delta precision
	 */
	public BigDecimalMathContext(MathContext mathContext, int deltaPrecision) {
		this(new MathContext(Math.max(1, mathContext.getPrecision() + deltaPrecision), mathContext.getRoundingMode()));
	}

	/**
	 * Constructs a {@link BigDecimalMathContext} with the specified {@link BigDecimalMathContext}, increasing the precision by the given delta.
	 *
	 * @param bigDecimalMathContext the {@link BigDecimalMathContext}
	 * @param deltaPrecision the delta precision
	 */
	public BigDecimalMathContext(BigDecimalMathContext bigDecimalMathContext, int deltaPrecision) {
		this(bigDecimalMathContext.mathContext, deltaPrecision);
	}

	/**
	 * Constructs a {@link BigDecimalMathContext} with the specified precision.
	 *
	 * @param precision the precision
	 */
	public BigDecimalMathContext(int precision) {
		this(new MathContext(precision));
	}

	/**
	 * Constructs a {@link BigDecimalMathContext} with the specified precision and {@link RoundingMode}.
	 *
	 * @param precision the precision
	 * @param roundingMode the {@link RoundingMode}
	 */
	public BigDecimalMathContext(int precision, RoundingMode roundingMode) {
		this(new MathContext(precision, roundingMode));
	}

	/**
	 * Returns the {@link MathContext} used by all functions of this {@link BigDecimalMathContext}.
	 *
	 * @return the {@link MathContext}
	 */
	public MathContext getMathContext() {
		return mathContext;
	}

	/**
	 * Calculates the Bernoulli number for the specified index.
	 *
	 * @return the Bernoulli number for the specified index with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#bernoulli(int, MathContext)
	 */
	public BigDecimal bernoulli(int n) {
		return BigDecimalMath.bernoulli(n, mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>).
	 *
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the {@link BigDecimal} value to serve as exponent
	 * @return the calculated x to the power of y with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)
	 */
	public BigDecimal pow(BigDecimal x, BigDecimal y) {
		return BigDecimalMath.pow(x, y, mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>).
	 *
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the <code>long</code> value to serve as exponent
	 * @return the calculated x to the power of y with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#pow(BigDecimal, long, MathContext)
	 */
	public BigDecimal pow(BigDecimal x, long y) {
		return BigDecimalMath.pow(x, y, mathContext);
	}

	/**
	 * Calculates the square root of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} value to calculate the square root
	 * @return the calculated square root of x with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#sqrt(BigDecimal, MathContext)
	 */
	public BigDecimal sqrt(BigDecimal x) {
		return BigDecimalMath.sqrt(x, mathContext);
	}

	/**
	 * Calculates the n'th root of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} value to calculate the n'th root
	 * @param n the {@link BigDecimal} defining the root
	 *
	 * @return the calculated n'th root of x with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &lt; 0
	 * @see BigDecimalMath#root(BigDecimal, BigDecimal, MathContext)
	 */
	public BigDecimal root(BigDecimal x, BigDecimal n) {
		return BigDecimalMath.root(x, n, mathContext);
	}

	/**
	 * Calculates the natural logarithm of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the natural logarithm for
	 * @return the calculated natural logarithm {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &lt;= 0
	 * @see BigDecimalMath#log(BigDecimal, MathContext)
	 */
	public BigDecimal log(BigDecimal x) {
		return BigDecimalMath.log(x, mathContext);
	}

	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 2.
	 *
	 * @param x the {@link BigDecimal} to calculate the logarithm base 2 for
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 2 with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &lt;= 0
	 * @see BigDecimalMath#log2(BigDecimal, MathContext)
	 */
	public BigDecimal log2(BigDecimal x) {
		return BigDecimalMath.log2(x, mathContext);
	}

	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 10.
	 *
	 * @param x the {@link BigDecimal} to calculate the logarithm base 10 for
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 10 with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &lt;= 0
	 * @see BigDecimalMath#log10(BigDecimal, MathContext)
	 */
	public BigDecimal log10(BigDecimal x) {
		return BigDecimalMath.log10(x, mathContext);
	}

	/**
	 * Returns the constant pi with this context.
	 *
	 * @return pi with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#pi(MathContext)
	 */
	public BigDecimal pi() {
		return BigDecimalMath.pi(mathContext);
	}

	/**
	 * Returns the constant e with this context.
	 *
	 * @return e with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#e(MathContext)
	 */
	public BigDecimal e() {
		return BigDecimalMath.e(mathContext);
	}

	/**
	 * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>).
	 *
	 * @param x the {@link BigDecimal} to calculate the exponent for
	 * @return the calculated exponent {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#exp(BigDecimal, MathContext)
	 */
	public BigDecimal exp(BigDecimal x) {
		return BigDecimalMath.exp(x, mathContext);
	}

	/**
	 * Calculates the sine (sinus) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the sine for
	 * @return the calculated sine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#sin(BigDecimal, MathContext)
	 */
	public BigDecimal sin(BigDecimal x) {
		return BigDecimalMath.sin(x, mathContext);
	}

	/**
	 * Calculates the arc sine (inverted sine) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc sine for
	 * @return the calculated arc sine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &gt; 1 or x &lt; -1
	 * @see BigDecimalMath#asin(BigDecimal, MathContext)
	 */
	public BigDecimal asin(BigDecimal x) {
		return BigDecimalMath.asin(x, mathContext);
	}

	/**
	 * Calculates the cosine (cosinus) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the cosine for
	 * @return the calculated cosine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#cos(BigDecimal, MathContext)
	 */
	public BigDecimal cos(BigDecimal x) {
		return BigDecimalMath.cos(x, mathContext);
	}

	/**
	 * Calculates the arc cosine (inverted cosine) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc cosine for
	 * @return the calculated arc sine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x &gt; 1 or x &lt; -1
	 * @see BigDecimalMath#acos(BigDecimal, MathContext)
	 */
	public BigDecimal acos(BigDecimal x) {
		return BigDecimalMath.acos(x, mathContext);
	}

	/**
	 * Calculates the tangens of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the tangens for
	 * @return the calculated tangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#tan(BigDecimal, MathContext)
	 */
	public BigDecimal tan(BigDecimal x) {
		return BigDecimalMath.tan(x, mathContext);
	}

	/**
	 * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc tangens for
	 * @return the calculated arc tangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#atan(BigDecimal, MathContext)
	 */
	public BigDecimal atan(BigDecimal x) {
		return BigDecimalMath.atan(x, mathContext);
	}

	/**
	 * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} y / x in the range -<i>pi</i> to <i>pi</i>.
	 *
	 * @param y the {@link BigDecimal}
	 * @param x the {@link BigDecimal}
	 * @return the calculated arc tangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x = 0 and y = 0
	 * @see BigDecimalMath#atan2(BigDecimal, BigDecimal, MathContext)
	 */
	public BigDecimal atan2(BigDecimal y, BigDecimal x) {
		return BigDecimalMath.atan2(x, y, mathContext);
	}

	/**
	 * Calculates the cotangens of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the cotangens for
	 * @return the calculated cotanges {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @throws ArithmeticException if x = 0
	 * @see BigDecimalMath#cot(BigDecimal, MathContext)
	 */
	public BigDecimal cot(BigDecimal x) {
		return BigDecimalMath.cot(x, mathContext);
	}

	/**
	 * Calculates the inverse cotangens (arc cotangens) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc cotangens for
	 * @return the calculated arc cotangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#acot(BigDecimal, MathContext)
	 */
	public BigDecimal acot(BigDecimal x) {
		return BigDecimalMath.acot(x, mathContext);
	}

	/**
	 * Calculates the hyperbolic sine of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the hyperbolic sine for
	 * @return the calculated hyperbolic sine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#sinh(BigDecimal, MathContext)
	 */
	public BigDecimal sinh(BigDecimal x) {
		return BigDecimalMath.sinh(x, mathContext);
	}

	/**
	 * Calculates the hyperbolic cosine of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the hyperbolic cosine for
	 * @return the calculated hyperbolic cosine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#cosh(BigDecimal, MathContext)
	 */
	public BigDecimal cosh(BigDecimal x) {
		return BigDecimalMath.cosh(x, mathContext);
	}

	/**
	 * Calculates the hyperbolic tangens of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the hyperbolic tangens for
	 * @return the calculated hyperbolic tangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#tanh(BigDecimal, MathContext)
	 */
	public BigDecimal tanh(BigDecimal x) {
		return BigDecimalMath.tanh(x, mathContext);
	}

	/**
	 * Calculates the hyperbolic cotangens of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the hyperbolic cotangens for
	 * @return the calculated hyperbolic cotangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#coth(BigDecimal, MathContext)
	 */
	public BigDecimal coth(BigDecimal x) {
		return BigDecimalMath.coth(x, mathContext);
	}

	/**
	 * Calculates the arc hyperbolic sine (inverse hyperbolic sine) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic sine for
	 * @return the calculated arc hyperbolic sine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#asinh(BigDecimal, MathContext)
	 */
	public BigDecimal asinh(BigDecimal x) {
		return BigDecimalMath.asinh(x, mathContext);
	}

	/**
	 * Calculates the arc hyperbolic cosine (inverse hyperbolic cosine) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic cosine for
	 * @return the calculated arc hyperbolic cosine {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#acosh(BigDecimal, MathContext)
	 */
	public BigDecimal acosh(BigDecimal x) {
		return BigDecimalMath.acosh(x, mathContext);
	}

	/**
	 * Calculates the arc hyperbolic tangens (inverse hyperbolic tangens ) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic tanges for
	 * @return the calculated arc hyperbolic tangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#atanh(BigDecimal, MathContext)
	 */
	public BigDecimal atanh(BigDecimal x) {
		return BigDecimalMath.atanh(x, mathContext);
	}

	/**
	 * Calculates the arc hyperbolic cotangens (inverse hyperbolic cotangens) of {@link BigDecimal} x.
	 *
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic cotangens for
	 * @return the calculated arc hyperbolic cotangens {@link BigDecimal} with this context (rounded to the precision of this context)
	 * @see BigDecimalMath#acoth(BigDecimal, MathContext)
	 */
	public BigDecimal acoth(BigDecimal x) {
		return BigDecimalMath.acoth(x, mathContext);
	}
}
