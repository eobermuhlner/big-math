package ch.obermuhlner.math.big;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Provides advanced functions operating on {@link BigDecimal}s.
 */
public class BigDecimalMath {

	public static Context DECIMAL128 = new Context(MathContext.DECIMAL128);
	public static Context DECIMAL64 = new Context(MathContext.DECIMAL64);
	public static Context DECIMAL32 = new Context(MathContext.DECIMAL32);
	public static Context UNLIMITED = new Context(MathContext.UNLIMITED);
	
	private static final BigDecimal TWO = valueOf(2);

	private static BigDecimal[] factorialCache = new BigDecimal[100];
	static {
		BigDecimal result = ONE;
		factorialCache[0] = result;
		for (int i = 1; i < factorialCache.length; i++) {
			result = result.multiply(valueOf(i));
			factorialCache[i] = result;
		}
	}

	private BigDecimalMath() {
		// prevent instances
	}

	/**
	 * Returns whether the specified {@link BigDecimal} value can be represented as <code>int</code> without loss of precision.
	 * 
	 * <p>If this returns <code>true</code> you can call {@link BigDecimal#intValueExact()} without fear of an {@link ArithmeticException}.</p>
	 * 
	 * @param value the {@link BigDecimal} to check 
	 * @return <code>true</code> if the value can be represented as <code>int</code> without loss of precision
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
	 * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>).
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Newton%27s_method">Newtown's method</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 *
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the {@link BigDecimal} value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y
	 */
	public static BigDecimal pow(BigDecimal x, BigDecimal y, MathContext mathContext) {
		// x^y = exp(y*log(x))
		// TODO calculate with taylor series?

		if (x.signum() == 0) {
			switch (y.signum()) {
				case 0 : return ONE;
				case 1 : return ZERO;
			}
		}

		try {
			int intValue = y.intValueExact();
			return pow(x, intValue, mathContext);
		} catch (ArithmeticException ex) {
			// ignored
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (y.signum() < 0) {
			return ONE.divide(pow(x, y.negate(), mc), mc);
		}

		BigDecimal result = exp(y.multiply(log(x, mc)), mc);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of <code>int</code> y (x<sup>y</sup>).
	 * 
	 * <p>The implementation uses the minimum number of multiplications of {@link BigDecimal x} (using squares whenever possible).</p>
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the <code>int</code> value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y
	 */
	public static BigDecimal pow(BigDecimal x, int y, MathContext mathContext) {
		if (y < 0) {
			return ONE.divide(pow(x, -y, mathContext), mathContext);
		}
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

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
	 * Calculates the square root of {@link BigDecimal} x.
	 * 
	 * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Newton%27s_method">Newtown's method</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 *
	 * @param x the {@link BigDecimal} value to calculate the square root
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated square root of x
	 */
	public static BigDecimal sqrt(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ZERO;
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal last;
		BigDecimal result = x.divide(TWO);

		do {
			last = result;
			result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
		} while (result.compareTo(last) != 0);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates the natural logarithm of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a></p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Taylor_series">Taylor series</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 * 
	 * <p>For x < 1 the following series is used:</br>
	 * <code>sum(-1^(n+1)(x-1)^n/n)</code></p>
	 * <p>For x >= 1 the following series is used:</br>
	 * <code>sum(((x-1)/n)^n/n)</code></p>
	 *
	 * @param x the {@link BigDecimal} to calculate the natural logarithm for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal}
	 * @throws ArithmeticException for 0 or negative numbers
	 */
	public static BigDecimal log(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Natural_logarithm
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		
		if (x.signum() <= 0) {
			throw new ArithmeticException("Illegal log(x) for x <= 0: x = " + x);
		}
		if (x.compareTo(ONE) == 0) {
			return ZERO;
		}
		return logAreaHyperbolicTangent(x, mc).round(mathContext);
	}

	private static BigDecimal logAreaHyperbolicTangent(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Logarithm#Calculation
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		
		BigDecimal magic = x.subtract(ONE, mc).divide(x.add(ONE), mc);
		
		BigDecimal last; 
		BigDecimal result = ZERO;
		BigDecimal step;
		int i = 0;
		do {
			int doubleIndexPlusOne = i * 2 + 1; 
			step = pow(magic, doubleIndexPlusOne, mc).divide(valueOf(doubleIndexPlusOne), mc);

			last = result;
			result = result.add(step, mc);
			
			i++;
		} while (result.compareTo(last) != 0);
		
		result = result.multiply(TWO, mc);
		
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>).
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Exponent">Wikipedia: Exponent</a></p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Taylor_series">Taylor series</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 *
	 * @param x the {@link BigDecimal} to calculate the exponent for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated exponent {@link BigDecimal}
	 */
	public static BigDecimal exp(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal last; 
		BigDecimal result = ZERO;
		BigDecimal step;
		int i = 0;
		do {
			step = x.pow(i).divide(factorial(i), mc);

			last = result;
			result = result.add(step, mc);
			i++;
		} while (result.compareTo(last) != 0);

		return result.round(mathContext);
	}

	/**
	 * Calculates the sine (sinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Sine">Wikipedia: Sine</a></p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Taylor_series">Taylor series</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 *
	 * @param x the {@link BigDecimal} to calculate the sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated sine {@link BigDecimal}
	 */
	public static BigDecimal sin(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal last;
		BigDecimal result = ZERO;
		BigDecimal sign = ONE;
		BigDecimal step;
		int i = 0;
		do {
			step = sign.multiply(x.pow(2 * i + 1), mc).divide(factorial(2 * i + 1), mc);
			sign = sign.negate();

			last = result;
			result = result.add(step, mc);
			i++;
		} while (result.compareTo(last) != 0);

		return result.round(mathContext);
	}
	
	/**
	 * Calculates the cosine (cosinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Cosine">Wikipedia: Cosine</a></p>
	 * 
	 * <p>The implementation uses <a href="http://en.wikipedia.org/wiki/Taylor_series">Taylor series</a>
	 * until the resulting value has reached the specified precision + 4 (result is then rounded to the specified precision).</p>
	 *
	 * @param x the {@link BigDecimal} to calculate the cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated cosine {@link BigDecimal}
	 */
	public static BigDecimal cos(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal last; 
		BigDecimal result = ZERO;
		BigDecimal sign = ONE;
		BigDecimal step;
		int i = 0;
		do {
			step = sign.multiply(x.pow(2 * i), mc).divide(factorial(2 * i), mc);
			sign = sign.negate();

			last = result;
			result = result.add(step, mc);
			i++;
		} while (result.compareTo(last) != 0);

		return result.round(mathContext);
	}

	/**
	 * A context for {@link BigDecimal} calculations with a specific {@link MathContext}.
	 */
	public static class Context {
		private MathContext mathContext;

		/**
		 * Creates a context with the specified {@link MathContext}.
		 * 
		 * @param mathContext the {@link MathContext} to use
		 */
		public Context(MathContext mathContext) {
			this.mathContext = mathContext;
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>int</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the <code>int</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(int, MathContext)
		 */
		public BigDecimal valueOf(int value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>long</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the <code>long</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(long, MathContext)
		 */
		public BigDecimal valueOf(long value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>double</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * <p>To avoid unexpected results this method uses <br>
		 * {@link BigDecimal#valueOf(double)} and {@link BigDecimal#round(MathContext)}<br>
		 * and <strong>not</strong> {@link BigDecimal#BigDecimal(double, MathContext)}.</p>
		 * 
		 * @param value the <code>double</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#valueOf(double)
		 * @see BigDecimal#round(MathContext)
		 */
		public BigDecimal valueOf(double value) {
			return BigDecimal.valueOf(value).round(mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified {@link BigInteger} value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the {@link BigInteger} value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(BigInteger, MathContext)
		 */
		public BigDecimal valueOf(BigInteger value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified {@link String} value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the {@link String} value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(String, MathContext)
		 */
		public BigDecimal valueOf(String value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Adds two {@link BigDecimal} values using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#add(BigDecimal, MathContext)
		 */
		public BigDecimal add(BigDecimal x, BigDecimal y) {
			return x.add(y, mathContext);
		}
		
		/**
		 * Subtracts {@link BigDecimal} y from {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#subtract(BigDecimal, MathContext)
		 */
		public BigDecimal subtract(BigDecimal x, BigDecimal y) {
			return x.subtract(y, mathContext);
		}
		
		/**
		 * Multiples two {@link BigDecimal} values using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#multiply(BigDecimal, MathContext)
		 */
		public BigDecimal multiply(BigDecimal x, BigDecimal y) {
			return x.multiply(y, mathContext);
		}
		
		/**
		 * Divides {@link BigDecimal} y from {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#divide(BigDecimal, MathContext)
		 */
		public BigDecimal divide(BigDecimal x, BigDecimal y) {
			return x.divide(y, mathContext);
		}

		/**
		 * Calculates the division to integral value of {@link BigDecimal} y divided by {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#divideToIntegralValue(BigDecimal, MathContext)
		 */
		public BigDecimal divideToIntegralValue(BigDecimal x, BigDecimal y) {
			return x.divideToIntegralValue(y, mathContext);
		}

		/**
		 * Calculated the remainder of {@link BigDecimal} y divided by {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#remainder(BigDecimal, MathContext)
		 */
		public BigDecimal remainder(BigDecimal x, BigDecimal y) {
			return x.remainder(y, mathContext);
		}

		public BigDecimal[] divideAndRemainder(BigDecimal x, BigDecimal y) {
			return x.divideAndRemainder(y, mathContext);
		}

		/**
		 * Returns the absolute value of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#abs(MathContext)
		 */
		public BigDecimal abs(BigDecimal x) {
			return x.abs(mathContext);
		}

		/**
		 * Returns the negated value of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#negate(MathContext)
		 */
		public BigDecimal negate(BigDecimal x) {
			return x.negate(mathContext);
		}

		/**
		 * Returns the natural logarithm of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#log(BigDecimal, MathContext)
		 */
		public BigDecimal log(BigDecimal x) {
			return BigDecimalMath.log(x, mathContext);
		}
		
		/**
		 * Returns the {@link BigDecimal} x to the power of {@link BigDecimal} y using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)
		 */
		public BigDecimal pow(BigDecimal x, BigDecimal y) {
			return BigDecimalMath.pow(x, y, mathContext);
		}
		
		/**
		 * Returns the natural exponent of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#exp(BigDecimal, MathContext)
		 */
		public BigDecimal exp(BigDecimal x) {
			return BigDecimalMath.exp(x, mathContext);
		}
		
		/**
		 * Returns the square root of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#sqrt(BigDecimal, MathContext)
		 */
		public BigDecimal sqrt(BigDecimal x) {
			return BigDecimalMath.sqrt(x, mathContext);
		}
		
		/**
		 * Returns the sine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#sin(BigDecimal, MathContext)
		 */
		public BigDecimal sin(BigDecimal x) {
			return BigDecimalMath.sin(x, mathContext);
		}
		
		/**
		 * Returns the cosine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#cos(BigDecimal, MathContext)
		 */
		public BigDecimal cos(BigDecimal x) {
			return BigDecimalMath.cos(x, mathContext);
		}
	}}
