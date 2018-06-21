package ch.obermuhlner.math.big;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import ch.obermuhlner.math.big.internal.AsinCalculator;
import ch.obermuhlner.math.big.internal.CosCalculator;
import ch.obermuhlner.math.big.internal.CoshCalculator;
import ch.obermuhlner.math.big.internal.ExpCalculator;
import ch.obermuhlner.math.big.internal.SinCalculator;
import ch.obermuhlner.math.big.internal.SinhCalculator;

/**
 * Provides advanced functions operating on {@link BigDecimal}s.
 */
public class BigDecimalMath {

	private static final BigDecimal TWO = valueOf(2);
	private static final BigDecimal THREE = valueOf(3);
	private static final BigDecimal MINUS_ONE = valueOf(-1);

	private static final BigDecimal DOUBLE_MAX_VALUE = BigDecimal.valueOf(Double.MAX_VALUE);

	private static volatile BigDecimal log2Cache;
	private static final Object log2CacheLock = new Object();

	private static volatile BigDecimal log3Cache;
	private static final Object log3CacheLock = new Object();

	private static volatile BigDecimal log10Cache;
	private static final Object log10CacheLock = new Object();

	private static volatile BigDecimal piCache;
	private static final Object piCacheLock = new Object();

	private static volatile BigDecimal eCache;
	private static final Object eCacheLock = new Object();

	private static final BigDecimal ROUGHLY_TWO_PI = new BigDecimal("3.141592653589793").multiply(TWO);

	private static final int EXPECTED_INITIAL_PRECISION = 17;

	private static BigDecimal[] factorialCache = new BigDecimal[100];

	static {
		BigDecimal result = ONE;
		factorialCache[0] = result;
		for (int i = 1; i < factorialCache.length; i++) {
			result = result.multiply(valueOf(i));
			factorialCache[i] = result;
		}
	}

	private static final Map<Integer, List<BigDecimal>> spougeFactorialConstantsCache = new HashMap<>();
	private static final Object spougeFactorialConstantsCacheLock = new Object();

	private BigDecimalMath() {
		// prevent instances
	}

	/**
	 * Returns whether the specified {@link BigDecimal} value can be represented as <code>int</code>.
	 *
	 * <p>If this returns <code>true</code> you can call {@link BigDecimal#intValueExact()} without fear of an {@link ArithmeticException}.</p>
	 *
	 * @param value the {@link BigDecimal} to check
	 * @return <code>true</code> if the value can be represented as <code>int</code> value
	 */
	public static boolean isIntValue(BigDecimal value) {
		// TODO impl isIntValue() without exceptions
		try {
			value.intValueExact();
			return true;
		} catch (ArithmeticException ex) {
			// ignored
		}
		return false;
	}

	/**
	 * Returns whether the specified {@link BigDecimal} value can be represented as <code>long</code>.
	 *
	 * <p>If this returns <code>true</code> you can call {@link BigDecimal#longValueExact()} without fear of an {@link ArithmeticException}.</p>
	 *
	 * @param value the {@link BigDecimal} to check
	 * @return <code>true</code> if the value can be represented as <code>long</code> value
	 */
	public static boolean isLongValue(BigDecimal value) {
		// TODO impl isLongValue() without exceptions
		try {
			value.longValueExact();
			return true;
		} catch (ArithmeticException ex) {
			// ignored
		}
		return false;
	}

	/**
	 * Returns whether the specified {@link BigDecimal} value can be represented as <code>double</code>.
	 *
	 * <p>If this returns <code>true</code> you can call {@link BigDecimal#doubleValue()}
	 * without fear of getting {@link Double#POSITIVE_INFINITY} or {@link Double#NEGATIVE_INFINITY} as result.</p>
	 *
	 * <p>Example: <code>BigDecimalMath.isDoubleValue(new BigDecimal("1E309"))</code> returns <code>false</code>,
	 * because <code>new BigDecimal("1E309").doubleValue()</code> returns <code>Infinity</code>.</p>
	 *
	 * <p>Note: This method does <strong>not</strong> check for possible loss of precision.</p>
	 *
	 * <p>For example <code>BigDecimalMath.isDoubleValue(new BigDecimal("1.23400000000000000000000000000000001"))</code> will return <code>true</code>,
	 * because <code>new BigDecimal("1.23400000000000000000000000000000001").doubleValue()</code> returns a valid double value,
	 * although it loses precision and returns <code>1.234</code>.</p>
	 *
	 * <p><code>BigDecimalMath.isDoubleValue(new BigDecimal("1E-325"))</code> will return <code>true</code>
	 * although this value is smaller than {@link Double#MIN_VALUE} (and therefore outside the range of values that can be represented as <code>double</code>)
	 * because <code>new BigDecimal("1E-325").doubleValue()</code> returns <code>0</code> which is a legal value with loss of precision.</p>
	 *
	 * @param value the {@link BigDecimal} to check
	 * @return <code>true</code> if the value can be represented as <code>double</code> value
	 */
	public static boolean isDoubleValue(BigDecimal value) {
		if (value.compareTo(DOUBLE_MAX_VALUE) > 0) {
			return false;
		}
		if (value.compareTo(DOUBLE_MAX_VALUE.negate()) < 0) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the mantissa of the specified {@link BigDecimal} written as <em>mantissa * 10<sup>exponent</sup></em>.
	 *
	 * <p>The mantissa is defined as having exactly 1 digit before the decimal point.</p>
	 *
	 * @param value the {@link BigDecimal}
	 * @return the mantissa
	 * @see #exponent(BigDecimal)
	 */
	public static BigDecimal mantissa(BigDecimal value) {
		int exponent = exponent(value);
		if (exponent == 0) {
			return value;
		}

		return value.movePointLeft(exponent);
	}

	/**
	 * Returns the exponent of the specified {@link BigDecimal} written as <em>mantissa * 10<sup>exponent</sup></em>.
	 *
	 * <p>The mantissa is defined as having exactly 1 digit before the decimal point.</p>
	 *
	 * @param value the {@link BigDecimal}
	 * @return the exponent
	 * @see #mantissa(BigDecimal)
	 */
	public static int exponent(BigDecimal value) {
		return value.precision() - value.scale() - 1;
	}

	/**
	 * Returns the number of significant digits of the specified {@link BigDecimal}.
	 *
	 * <p>The result contains the number of all digits before the decimal point and
	 * all digits after the decimal point excluding trailing zeroes.</p>
	 *
	 * <p>Examples:</p>
	 * <ul>
	 * <li><code>significantDigits(new BigDecimal("12300.00"))</code> returns 5</li>
	 * <li><code>significantDigits(new BigDecimal("1.23000"))</code> returns 3</li>
	 * <li><code>significantDigits(new BigDecimal("0.00012300"))</code> returns 3</li>
	 * <li><code>significantDigits(new BigDecimal("12300.4500"))</code> returns 7</li>
	 * </ul>
	 *
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Significant_figures">Wikipedia: Significant figures</a></p>
	 *
	 * @param value the {@link BigDecimal}
	 * @return the number of significant digits
	 * @see BigDecimal#stripTrailingZeros()
	 * @see BigDecimal#precision()
	 */
	public static int significantDigits(BigDecimal value) {
		BigDecimal stripped = value.stripTrailingZeros();
		if (stripped.scale() >= 0) {
			return stripped.precision();
		} else {
			return stripped.precision() - stripped.scale();
		}
	}

	/**
	 * Returns the integral part of the specified {@link BigDecimal} (left of the decimal point).
	 *
	 * @param value the {@link BigDecimal}
	 * @return the integral part
	 * @see #fractionalPart(BigDecimal)
	 */
	public static BigDecimal integralPart(BigDecimal value) {
		return value.setScale(0, BigDecimal.ROUND_DOWN);
	}

	/**
	 * Returns the fractional part of the specified {@link BigDecimal} (right of the decimal point).
	 *
	 * @param value the {@link BigDecimal}
	 * @return the fractional part
	 * @see #integralPart(BigDecimal)
	 */
	public static BigDecimal fractionalPart(BigDecimal value) {
		return value.subtract(integralPart(value));
	}

	/**
	 * Calculates the reciprocal of the specified {@link BigDecimal}.
	 *
	 * @param x the {@link BigDecimal}
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the reciprocal {@link BigDecimal}
	 * @throws ArithmeticException if x = 0
	 */
	public static BigDecimal reciprocal(BigDecimal x, MathContext mathContext) {
		return BigDecimal.ONE.divide(x, mathContext);
	}

	/**
	 * Calculates the factorial of the specified integer argument.
	 *
	 * <p>factorial = 1 * 2 * 3 * ... n</p>
	 *
	 * @param n the {@link BigDecimal}
	 * @return the factorial {@link BigDecimal}
	 * @throws ArithmeticException if x &lt; 0
	 */
	public static BigDecimal factorial(int n) {
		if (n < 0) {
			throw new ArithmeticException("Illegal factorial(n) for n < 0: n = " + n);
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

	/**
	 * Calculates the factorial of the specified {@link BigDecimal}.
	 *
	 * <p>This implementation uses
	 * <a href="https://en.wikipedia.org/wiki/Spouge%27s_approximation">Spouge's approximation</a>
	 * to calculate the factorial for non-integer values.</p>
	 *
	 * <p>This involves calculating a series of constants that depend on the desired precision.
	 * Since this constant calculation is quite expensive (especially for higher precisions),
	 * the constants for a specific precision will be cached
	 * and subsequent calls to this method with the same precision will be much faster.</p>
	 *
	 * <p>It is therefore recommended to do one call to this method with the standard precision of your application during the startup phase
	 * and to avoid calling it with many different precisions.</p>
	 *
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Factorial#Extension_of_factorial_to_non-integer_values_of_argument">Wikipedia: Factorial - Extension of factorial to non-integer values of argument</a></p>
	 *
	 * @param x the {@link BigDecimal}
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the factorial {@link BigDecimal}
	 * @throws ArithmeticException if x is a negative integer value (-1, -2, -3, ...)
	 * @see #factorial(int)
	 * @see #gamma(BigDecimal, MathContext)
	 */
	public static BigDecimal factorial(BigDecimal x, MathContext mathContext) {
		if (isIntValue(x)) {
			return factorial(x.intValueExact()).round(mathContext);
		}

		// https://en.wikipedia.org/wiki/Spouge%27s_approximation
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());

		int a = mathContext.getPrecision() * 13 / 10;
		List<BigDecimal> constants = getSpougeFactorialConstants(a);

		BigDecimal bigA = BigDecimal.valueOf(a);

		boolean negative = false;
		BigDecimal factor = constants.get(0);
		for (int k = 1; k < a; k++) {
			BigDecimal bigK = BigDecimal.valueOf(k);
			factor = factor.add(constants.get(k).divide(x.add(bigK), mc), mc);
			negative = !negative;
		}

		BigDecimal result = pow(x.add(bigA, mc), x.add(BigDecimal.valueOf(0.5), mc), mc);
		result = result.multiply(exp(x.negate().subtract(bigA, mc), mc), mc);
		result = result.multiply(factor, mc);

		return result.round(mathContext);
	}

	static List<BigDecimal> getSpougeFactorialConstants(int a) {
		synchronized (spougeFactorialConstantsCacheLock) {
			return spougeFactorialConstantsCache.computeIfAbsent(a, key -> {
				List<BigDecimal> constants = new ArrayList<>(a);
				MathContext mc = new MathContext(a * 15 / 10);

				BigDecimal c0 = sqrt(pi(mc).multiply(TWO, mc), mc);
				constants.add(c0);

				boolean negative = false;
				BigDecimal factor = c0;
				for (int k = 1; k < a; k++) {
					BigDecimal bigK = BigDecimal.valueOf(k);
					BigDecimal ck = pow(BigDecimal.valueOf(a - k), bigK.subtract(BigDecimal.valueOf(0.5), mc), mc);
					ck = ck.multiply(exp(BigDecimal.valueOf(a - k), mc), mc);
					ck = ck.divide(factorial(k - 1), mc);
					if (negative) {
						ck = ck.negate();
					}
					constants.add(ck);

					negative = !negative;
				}

				return Collections.unmodifiableList(constants);
			});
		}
	}

	/**
	 * Calculates the gamma function of the specified {@link BigDecimal}.
	 *
	 * <p>This implementation uses {@link #factorial(BigDecimal, MathContext)} internally,
	 * therefore the performance implications described there apply also for this method.
	 *
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Gamma_function">Wikipedia: Gamma function</a></p>
	 *
	 * @param x the {@link BigDecimal}
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the gamma {@link BigDecimal}
	 * @throws ArithmeticException if x-1 is a negative integer value (-1, -2, -3, ...)
	 * @see #factorial(BigDecimal, MathContext)
	 */
	public static BigDecimal gamma(BigDecimal x, MathContext mathContext) {
		return factorial(x.subtract(ONE), mathContext);
	}

	/**
	 * Calculates the Bernoulli number for the specified index.
	 *
	 * <p>This function calculates the <strong>first Bernoulli numbers</strong> and therefore <code>bernoulli(1)</code> returns -0.5</p>
	 * <p>Note that <code>bernoulli(x)</code> for all odd x &gt; 1 returns 0</p>
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Bernoulli_number">Wikipedia: Bernoulli number</a></p>
	 *
	 * @param n the index of the Bernoulli number to be calculated (starting at 0)
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the Bernoulli number for the specified index
	 * @throws ArithmeticException if x &lt; 0
	 */
	public static BigDecimal bernoulli(int n, MathContext mathContext) {
		if (n < 0) {
			throw new ArithmeticException("Illegal bernoulli(n) for n < 0: n = " + n);
		}
		
		BigRational b = BigRational.bernoulli(n);
		return b.toBigDecimal(mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>).
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the {@link BigDecimal} value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal pow(BigDecimal x, BigDecimal y, MathContext mathContext) {

		if (x.signum() == 0) {
			switch (y.signum()) {
				case 0 : return ONE;
				case 1 : return ZERO;
			}
		}

		// TODO optimize y=0, y=1, y=10^k, y=-1, y=-10^k

		try {
			long longValue = y.longValueExact();
			return pow(x, longValue, mathContext);
		} catch (ArithmeticException ex) {
			// ignored
		}
		
		if (fractionalPart(y).signum() == 0) {
			return powInteger(x, y, mathContext);
		}

		// x^y = exp(y*log(x))
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = exp(y.multiply(log(x, mc), mc), mc);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of <code>long</code> y (x<sup>y</sup>).
	 * 
	 * <p>The implementation tries to minimize the number of multiplications of {@link BigDecimal x} (using squares whenever possible).</p>
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Exponentiation#Efficient_computation_with_integer_exponents">Wikipedia: Exponentiation - efficient computation</a></p>
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the <code>long</code> value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal pow(BigDecimal x, long y, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());

		// TODO optimize y=0, y=1, y=10^k, y=-1, y=-10^k

		if (y < 0) {
			return ONE.divide(pow(x, -y, mc), mc).round(mathContext);
		}
		
		BigDecimal result = ONE;
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

	/**
	 * Calculates {@link BigDecimal} x to the power of the integer value y (x<sup>y</sup>).
	 * 
	 * <p>The value y MUST be an integer value.</p>
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param integerY the {@link BigDecimal} <strong>integer</strong> value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y with the precision specified in the <code>mathContext</code>
	 * @see #pow(BigDecimal, long, MathContext)
	 */
	private static BigDecimal powInteger(BigDecimal x, BigDecimal integerY, MathContext mathContext) {
		if (fractionalPart(integerY).signum() != 0) {
			throw new IllegalArgumentException("Not integer value: " + integerY);
		}
		
		if (integerY.signum() < 0) {
			return ONE.divide(powInteger(x, integerY.negate(), mathContext), mathContext);
		}

		MathContext mc = new MathContext(Math.max(mathContext.getPrecision(), -integerY.scale()) + 30, mathContext.getRoundingMode());

		BigDecimal result = ONE;
		while (integerY.signum() > 0) {
			BigDecimal halfY = integerY.divide(TWO, mc);

			if (fractionalPart(halfY).signum() != 0) {
				// odd exponent -> multiply result with x
				result = result.multiply(x, mc);
				integerY = integerY.subtract(ONE);
				halfY = integerY.divide(TWO, mc);
			}
			
			if (halfY.signum() > 0) {
				// even exponent -> square x
				x = x.multiply(x, mc);
			}
			
			integerY = halfY;
		}

		return result.round(mathContext);
	}

	/**
	 * Calculates the square root of {@link BigDecimal} x.
	 * 
	 * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * 
	 * @param x the {@link BigDecimal} value to calculate the square root
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated square root of x with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &lt; 0
	 */
	public static BigDecimal sqrt(BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
		}

		int maxPrecision = mathContext.getPrecision() + 6;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal result;
		if (isDoubleValue(x)) {
			result = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
		} else {
			result = x.divide(TWO, mathContext);
		}
		
		if (result.multiply(result, mathContext).compareTo(x) == 0) {
			return result.round(mathContext); // early exit if x is a square number
		}

		int adaptivePrecision = EXPECTED_INITIAL_PRECISION;
		BigDecimal last;

		do {
			last = result;
			adaptivePrecision = adaptivePrecision * 2;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
		} while (adaptivePrecision < maxPrecision || result.subtract(last).abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the n'th root of {@link BigDecimal} x.
	 * 
	 * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * @param x the {@link BigDecimal} value to calculate the n'th root
	 * @param n the {@link BigDecimal} defining the root
	 * @param mathContext the {@link MathContext} used for the result
	 * 
	 * @return the calculated n'th root of x with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &lt; 0
	 */
	public static BigDecimal root(BigDecimal x, BigDecimal n, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal root(x) for x < 0: x = " + x);
		}

		if (n.compareTo(BigDecimal.ONE) <= 0) {
			MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
			return pow(x, BigDecimal.ONE.divide(n, mc), mathContext);
		}
		
		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal nMinus1 = n.subtract(ONE);
		BigDecimal result = x.divide(TWO, MathContext.DECIMAL32);
		int adaptivePrecision = 2; // first approximation has really bad precision
		BigDecimal step;

		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			
			step = x.divide(pow(result, nMinus1, mc), mc).subtract(result, mc).divide(n, mc);
			result = result.add(step, mc);
		} while (adaptivePrecision < maxPrecision || step.abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates the natural logarithm of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the natural logarithm for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &lt;= 0
	 */
	public static BigDecimal log(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Natural_logarithm

		//System.out.println("log(" + x + " " + mathContext + ")");

		if (x.signum() <= 0) {
			throw new ArithmeticException("Illegal log(x) for x <= 0: x = " + x);
		}
		if (x.compareTo(ONE) == 0) {
			return ZERO;
		}
		
		BigDecimal result;
		switch (x.compareTo(TEN)) {
		case 0:
			result = logTen(mathContext);
			break;
		case 1:
			result = logUsingExponent(x, mathContext);
			break;
		default :
			//result = logUsingTwoThree(x, mathContext);
			result = logUsingNewton(x, mathContext);
		}

		return result.round(mathContext);
	}

	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 2.
	 * 
	 * @param x the {@link BigDecimal} to calculate the logarithm base 2 for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 2 with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &lt;= 0
	 */
	public static BigDecimal log2(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal result = log(x, mc).divide(logTwo(mc), mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 10.
	 * 
	 * @param x the {@link BigDecimal} to calculate the logarithm base 10 for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 10 with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &lt;= 0
	 */
	public static BigDecimal log10(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 2, mathContext.getRoundingMode());

		BigDecimal result = log(x, mc).divide(logTen(mc), mc);
		return result.round(mathContext);
	}
	
	private static BigDecimal logUsingNewton(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Natural_logarithm in chapter 'High Precision'
		// y = y + 2 * (x-exp(y)) / (x+exp(y))

		int maxPrecision = mathContext.getPrecision() + 20;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		//System.out.println("logUsingNewton(" + x + " " + mathContext + ") precision " + maxPrecision);

		BigDecimal result;
		int adaptivePrecision;
		double doubleX = x.doubleValue();
		if (doubleX > 0.0 && isDoubleValue(x)) {
			result = BigDecimal.valueOf(Math.log(doubleX));
			adaptivePrecision = EXPECTED_INITIAL_PRECISION;
		} else {
			result = x.divide(TWO, mathContext);
			adaptivePrecision = 1;
		}

		BigDecimal step;
		
		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			
			BigDecimal expY = BigDecimalMath.exp(result, mc);
			step = TWO.multiply(x.subtract(expY, mc), mc).divide(x.add(expY, mc), mc);
			//System.out.println("  step " + step + " adaptivePrecision=" + adaptivePrecision);
			result = result.add(step);
		} while (adaptivePrecision < maxPrecision || step.abs().compareTo(acceptableError) > 0);

		return result;
	}

	private static BigDecimal logUsingExponent(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());
		//System.out.println("logUsingExponent(" + x + " " + mathContext + ") precision " + mc);

		int exponent = exponent(x);
		BigDecimal mantissa = mantissa(x);

		//BigDecimal result = logUsingTwoThree(mantissa, mc);
		BigDecimal result = logUsingNewton(mantissa, mc);
		if (exponent != 0) {
			result = result.add(valueOf(exponent).multiply(logTen(mc), mc), mc);
		}
		return result;
	}

	/**
	 * Returns the number pi.
	 * 
	 * <p>See <a href="https://en.wikipedia.org/wiki/Pi">Wikipedia: Pi</a></p>
	 * 
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the number pi with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal pi(MathContext mathContext) {
		BigDecimal result = null;
		
		synchronized (piCacheLock) {
			if (piCache != null && mathContext.getPrecision() <= piCache.precision()) {
				result = piCache;
			} else {
				piCache = piChudnovski(mathContext);
				return piCache;
			}
		}
		
		return result.round(mathContext);
	}
	
	private static BigDecimal piChudnovski(MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());

		final BigDecimal value24 = BigDecimal.valueOf(24);
		final BigDecimal value640320 = BigDecimal.valueOf(640320);
		final BigDecimal value13591409 = BigDecimal.valueOf(13591409);
		final BigDecimal value545140134 = BigDecimal.valueOf(545140134);
		final BigDecimal valueDivisor = value640320.pow(3).divide(value24, mc);

		BigDecimal sumA = BigDecimal.ONE;
		BigDecimal sumB = BigDecimal.ZERO;

		BigDecimal a = BigDecimal.ONE;
		long dividendTerm1 = 5; // -(6*k - 5)
		long dividendTerm2 = -1; // 2*k - 1
		long dividendTerm3 = -1; // 6*k - 1
		BigDecimal kPower3 = BigDecimal.ZERO;
		
		long iterationCount = (mc.getPrecision()+13) / 14;
		for (long k = 1; k <= iterationCount; k++) {
			BigDecimal valueK = BigDecimal.valueOf(k);
			dividendTerm1 += -6;
			dividendTerm2 += 2;
			dividendTerm3 += 6;
			BigDecimal dividend = BigDecimal.valueOf(dividendTerm1).multiply(BigDecimal.valueOf(dividendTerm2)).multiply(BigDecimal.valueOf(dividendTerm3));
			kPower3 = valueK.pow(3);
			BigDecimal divisor = kPower3.multiply(valueDivisor, mc);
			a = a.multiply(dividend).divide(divisor, mc);
			BigDecimal b = valueK.multiply(a, mc);
			
			sumA = sumA.add(a);
			sumB = sumB.add(b);
		}
		
		final BigDecimal value426880 = BigDecimal.valueOf(426880);
		final BigDecimal value10005 = BigDecimal.valueOf(10005);
		final BigDecimal factor = value426880.multiply(sqrt(value10005, mc));
		BigDecimal pi = factor.divide(value13591409.multiply(sumA, mc).add(value545140134.multiply(sumB, mc)), mc);
		
		return pi.round(mathContext);
	}

	
	/**
	 * Returns the number e.
	 * 
	 * <p>See <a href="https://en.wikipedia.org/wiki/E_(mathematical_constant)">Wikipedia: E (mathematical_constant)</a></p>
	 * 
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the number e with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal e(MathContext mathContext) {
		BigDecimal result = null;
		
		synchronized (eCacheLock) {
			if (eCache != null && mathContext.getPrecision() <= eCache.precision()) {
				result = eCache;
			} else {
				eCache = exp(ONE, mathContext);
				return eCache;
			}
		}
		
		return result.round(mathContext);
	}
	
	private static BigDecimal logTen(MathContext mathContext) {
		BigDecimal result = null;
		
		synchronized (log10CacheLock) {
			if (log10Cache != null && mathContext.getPrecision() <= log10Cache.precision()) {
				result = log10Cache;
			} else {
				log10Cache = logUsingNewton(BigDecimal.TEN, mathContext);
				return log10Cache;
			}
		}
		
		return result.round(mathContext);
	}
	
	private static BigDecimal logTwo(MathContext mathContext) {
		BigDecimal result = null;
		
		synchronized (log2CacheLock) {
			if (log2Cache != null && mathContext.getPrecision() <= log2Cache.precision()) {
				result = log2Cache;
			} else {
				log2Cache = logUsingNewton(TWO, mathContext);
				return log2Cache;
			}
		}
		
		return result.round(mathContext);
	}

	private static BigDecimal logThree(MathContext mathContext) {
		BigDecimal result = null;
		
		synchronized (log3CacheLock) {
			if (log3Cache != null && mathContext.getPrecision() <= log3Cache.precision()) {
				result = log3Cache;
			} else {
				log3Cache = logUsingNewton(THREE, mathContext);
				return log3Cache;
			}
		}
		
		return result.round(mathContext);
	}

	/**
	 * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>).
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Exponent">Wikipedia: Exponent</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the exponent for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated exponent {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal exp(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ONE;
		}

		return expIntegralFractional(x, mathContext);
	}

	private static BigDecimal expIntegralFractional(BigDecimal x, MathContext mathContext) {
		BigDecimal integralPart = integralPart(x);
		
		if (integralPart.signum() == 0) {
			return expTaylor(x, mathContext);
		}
		
		BigDecimal fractionalPart = x.subtract(integralPart);

		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());

        BigDecimal z = ONE.add(fractionalPart.divide(integralPart, mc));
        BigDecimal t = expTaylor(z, mc);

        BigDecimal result = pow(t, integralPart.intValueExact(), mc);
        
		return result.round(mathContext);
	}
	
	private static BigDecimal expTaylor(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		x = x.divide(valueOf(256), mc);
		
		BigDecimal result = ExpCalculator.INSTANCE.calculate(x, mc);
		result = BigDecimalMath.pow(result, 256, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the sine (sinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Sine">Wikipedia: Sine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal sin(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.abs().compareTo(ROUGHLY_TWO_PI) > 0) {
			BigDecimal twoPi = TWO.multiply(pi(mc), mc);
			x = x.remainder(twoPi, mc);
		}

		BigDecimal result = SinCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the arc sine (inverted sine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arcsine">Wikipedia: Arcsine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &gt; 1 or x &lt; -1
	 */
	public static BigDecimal asin(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal asin(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal asin(x) for x < -1: x = " + x);
		}
		
		if (x.signum() == -1) {
			return asin(x.negate(), mathContext).negate();
		}
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.compareTo(BigDecimal.valueOf(0.707107)) >= 0) {
			BigDecimal xTransformed = sqrt(ONE.subtract(x.multiply(x, mc), mc), mc);
			return acos(xTransformed, mathContext);
		}

		BigDecimal result = AsinCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the cosine (cosinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Cosine">Wikipedia: Cosine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated cosine {@link BigDecimal}
	 */
	public static BigDecimal cos(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.abs().compareTo(ROUGHLY_TWO_PI) > 0) {
			BigDecimal twoPi = TWO.multiply(pi(mc), mc);
			x = x.remainder(twoPi, mc);
		}
		
		BigDecimal result = CosCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the arc cosine (inverted cosine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arccosine">Wikipedia: Arccosine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x &gt; 1 or x &lt; -1
	 */
	public static BigDecimal acos(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal acos(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal acos(x) for x < -1: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = pi(mc).divide(TWO, mc).subtract(asin(x, mc), mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the tangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Tangens">Wikipedia: Tangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the tangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal tan(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ZERO;
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		return sin(x, mc).divide(cos(x, mc), mc).round(mathContext);
	}
	
	/**
	 * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arctangens">Wikipedia: Arctangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc tangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal atan(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		x = x.divide(sqrt(ONE.add(x.multiply(x, mc), mc), mc), mc);

		BigDecimal result = asin(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} y / x in the range -<i>pi</i> to <i>pi</i>.
	 *
	 * <p>This is useful to calculate the angle <i>theta</i> from the conversion of rectangular
     * coordinates (<code>x</code>,&nbsp;<code>y</code>) to polar coordinates (r,&nbsp;<i>theta</i>).</p>
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Atan2">Wikipedia: Atan2</a></p>
	 * 
	 * @param y the {@link BigDecimal}
	 * @param x the {@link BigDecimal}
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x = 0 and y = 0
	 */
	public static BigDecimal atan2(BigDecimal y, BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 3, mathContext.getRoundingMode());

		if (x.signum() > 0) { // x > 0
			return atan(y.divide(x, mc), mathContext);
		} else if (x.signum() < 0) {
			if (y.signum() > 0) {  // x < 0 && y > 0
				return atan(y.divide(x, mc), mc).add(pi(mc), mathContext);
			} else if (y.signum() < 0) { // x < 0 && y < 0
				return atan(y.divide(x, mc), mc).subtract(pi(mc), mathContext);
			} else { // x < 0 && y = 0
				return pi(mathContext);
			}
		} else {
			if (y.signum() > 0) { // x == 0 && y > 0
				return pi(mc).divide(TWO, mathContext);
			} else if (y.signum() < 0) {  // x == 0 && y < 0
				return pi(mc).divide(TWO, mathContext).negate();				
			} else {
				throw new ArithmeticException("Illegal atan2(y, x) for x = 0; y = 0");
			}
		}
	}
	
	/**
	 * Calculates the cotangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Cotangens">Wikipedia: Cotangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated cotanges {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException if x = 0
	 */
	public static BigDecimal cot(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			throw new ArithmeticException("Illegal cot(x) for x = 0");
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = cos(x, mc).divide(sin(x, mc), mc).round(mathContext);
		return result.round(mathContext);
	}

	/**
	 * Calculates the inverse cotangens (arc cotangens) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arccotangens">Wikipedia: Arccotangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc cotangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal acot(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = pi(mc).divide(TWO, mc).subtract(atan(x, mc), mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the hyperbolic sine of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the hyperbolic sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated hyperbolic sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal sinh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = SinhCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the hyperbolic cosine of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the hyperbolic cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated hyperbolic cosine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal cosh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = CoshCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the hyperbolic tangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the hyperbolic tangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated hyperbolic tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal tanh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = sinh(x, mc).divide(cosh(x, mc), mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the hyperbolic cotangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the hyperbolic cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated hyperbolic cotangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal coth(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = cosh(x, mc).divide(sinh(x, mc), mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the arc hyperbolic sine (inverse hyperbolic sine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc hyperbolic sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal asinh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(sqrt(x.multiply(x, mc).add(ONE, mc), mc), mc), mc);
		return result.round(mathContext);
	}	
	
	/**
	 * Calculates the arc hyperbolic cosine (inverse hyperbolic cosine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc hyperbolic cosine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal acosh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(sqrt(x.multiply(x, mc).subtract(ONE, mc), mc), mc), mc);
		return result.round(mathContext);
	}	

	/**
	 * Calculates the arc hyperbolic tangens (inverse hyperbolic tangens ) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic tanges for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc hyperbolic tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal atanh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(ONE.add(x, mc).divide(ONE.subtract(x, mc), mc), mc).divide(TWO, mc);
		return result.round(mathContext);
	}	

	/**
	 * Calculates the arc hyperbolic cotangens (inverse hyperbolic cotangens) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic function</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc hyperbolic cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc hyperbolic cotangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal acoth(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(ONE, mc).divide(x.subtract(ONE, mc), mc), mc).divide(TWO, mc);
		return result.round(mathContext);
	}
}
