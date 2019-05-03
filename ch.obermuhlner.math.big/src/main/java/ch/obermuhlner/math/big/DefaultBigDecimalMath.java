package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A wrapper around {@link BigDecimalMath} that passes always the same default {@link MathContext} to the
 * functions that need a {@link MathContext} argument.
 *
 * <p>This class is designed for applications that will always need the same precision in all calculations.</p>
 *
 * <p>The initial default {@link MathContext} is equivalent to {@link MathContext#DECIMAL128}
 * but this can be overridden by setting the following system properties:</p>
 * <ul>
 *     <li><code>ch.obermuhlner.math.big.default.precision</code> to a positive integer precision (default=34)</li>
 *     <li><code>ch.obermuhlner.math.big.default.rounding</code> to a {@link RoundingMode} name (default=HALF_UP) </li>
 * </ul>
 *
 * <p>It is also possible to set the default {@link MathContext} using {@link #setDefaultMathContext(MathContext)}.
 * It is recommended to set the desired precision in the {@link MathContext} early in the startup of the application.</p>
 *
 * <p>Important: Avoid the pitfall of setting the precision temporarily for a calculation.
 * This can lead to race conditions and calculations with the wrong precision
 * if other threads in your application do the same thing.</p>
 */
public class DefaultBigDecimalMath {

    private static MathContext defaultMathContext = createDefaultMathContext();

    private static MathContext createDefaultMathContext () {
        int precision = getIntSystemProperty("ch.obermuhlner.math.big.default.precision", MathContext.DECIMAL128.getPrecision());
        RoundingMode rounding = getRoundingModeSystemProperty("ch.obermuhlner.math.big.default.rounding", MathContext.DECIMAL128.getRoundingMode());

        return new MathContext(precision, rounding);
    }

    private static int getIntSystemProperty(String propertyKey, int defaultValue) {
        String propertyValue = System.getProperty(propertyKey, Integer.toString(defaultValue));
        try {
            return Integer.parseInt(propertyValue);
        } catch(NumberFormatException ex) {
            return propertyException(propertyKey,propertyValue,defaultValue);
        }
    }

    private static RoundingMode getRoundingModeSystemProperty(String propertyKey, RoundingMode defaultValue) {
        String propertyValue = System.getProperty(propertyKey, defaultValue.name());
        try {
            return RoundingMode.valueOf(propertyValue);
        } catch(IllegalArgumentException ex) {
            return propertyException(propertyKey,propertyValue,defaultValue);
        }
    }

    private static <T> T propertyException(String propertyKey,String propertyValue,T defaultValue){
        System.err.println("Property '" + propertyKey + "' is not valid: " + propertyValue + " (using " + defaultValue + " instead)");
        return defaultValue;
    }

    /**
     * Sets the default {@link MathContext} used for all mathematical functions in this class.
     *
     * @param defaultMathContext the default {@link MathContext}
     */
    public static void setDefaultMathContext(MathContext defaultMathContext) {
        Objects.requireNonNull(defaultMathContext);
        DefaultBigDecimalMath.defaultMathContext = defaultMathContext;
    }

    /**
     * Returns the default {@link MathContext} used for all mathematical functions in this class.
     *
     * @return the default {@link MathContext}
     */
    public static MathContext getDefaultMathContext() {
        return defaultMathContext;
    }

    /**
     * Rounds the specified {@link BigDecimal} to the precision of the default {@link MathContext}.
     *
     * @param value the {@link BigDecimal} to round
     * @return the rounded {@link BigDecimal} value
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#round(BigDecimal, MathContext)
     */
    public static BigDecimal round(BigDecimal value) {
        return  BigDecimalMath.round(value, defaultMathContext);
    }

    /**
     * Rounds the specified {@link BigDecimal} to the precision of the default {@link MathContext} including trailing zeroes.
     *
     * @param value the {@link BigDecimal} to round
     * @return the rounded {@link BigDecimal} value including trailing zeroes
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#roundWithTrailingZeroes(BigDecimal, MathContext)
     */
    public static BigDecimal roundWithTrailingZeroes(BigDecimal value) {
        return BigDecimalMath.roundWithTrailingZeroes(value, defaultMathContext);
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x + y</code> using the default {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to add
     * @return the resulting {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimal#add(BigDecimal, MathContext)
     */
    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        return x.add(y, defaultMathContext);
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x - y</code> using the default {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to subtract
     * @return the resulting {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimal#subtract(BigDecimal, MathContext)
     */
    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
        return x.subtract(y, defaultMathContext);
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x * y</code> using the default {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to multiply
     * @return the resulting {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimal#multiply(BigDecimal, MathContext)
     */
    public static BigDecimal multiply(BigDecimal x, BigDecimal y) {
        return x.multiply(y, defaultMathContext);
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x / y</code> using the default {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to divide
     * @return the resulting {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimal#divide(BigDecimal, MathContext)
     */
    public static BigDecimal divide(BigDecimal x, BigDecimal y) {
        return x.divide(y, defaultMathContext);
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x % y</code> using the default {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to divide
     * @return the resulting {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimal#remainder(BigDecimal, MathContext)
     */
    public static BigDecimal remainder(BigDecimal x, BigDecimal y) {
        return x.remainder(y, defaultMathContext);
    }

    /**
     * Calculates the reciprocal of the specified {@link BigDecimal} using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the reciprocal {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#reciprocal(BigDecimal, MathContext)
     */
    public static BigDecimal reciprocal(BigDecimal x) {
        return BigDecimalMath.reciprocal(x, defaultMathContext);
    }

    /**
     * Calculates the factorial of the specified {@link BigDecimal} using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the factorial {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#factorial(BigDecimal, MathContext)
     */
    public static BigDecimal factorial(BigDecimal x) {
        return BigDecimalMath.factorial(x, defaultMathContext);
    }

    /**
     * Calculates the gamma function of the specified {@link BigDecimal} using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the gamma {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#gamma(BigDecimal, MathContext)
     */
    public static BigDecimal gamma(BigDecimal x) {
        return BigDecimalMath.gamma(x, defaultMathContext);
    }

    /**
     * Calculates the Bernoulli number for the specified index using the default {@link MathContext}.
     *
     * @param n the index of the Bernoulli number to be calculated (starting at 0)
     * @return the Bernoulli number for the specified index with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#bernoulli(int, MathContext)
     */
    public static BigDecimal bernoulli(int n) {
        return BigDecimalMath.bernoulli(n, defaultMathContext);
    }

    /**
     * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>) using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to take to the power
     * @param y the {@link BigDecimal} value to serve as exponent
     * @return the calculated x to the power of y with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)
     */
    public static BigDecimal pow(BigDecimal x, BigDecimal y) {
        return BigDecimalMath.pow(x, y, defaultMathContext);
    }

    /**
     * Calculates {@link BigDecimal} x to the power of <code>long</code> y (x<sup>y</sup>) using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to take to the power
     * @param y the <code>long</code> value to serve as exponent
     * @return the calculated x to the power of y with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#pow(BigDecimal, long, MathContext)
     */
    public static BigDecimal pow(BigDecimal x, long y) {
        return BigDecimalMath.pow(x, y, defaultMathContext);
    }

    /**
     * Calculates the square root of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to calculate the square root
     * @return the calculated square root of x with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#sqrt(BigDecimal, MathContext)
     */
    public static BigDecimal sqrt(BigDecimal x) {
        return BigDecimalMath.sqrt(x, defaultMathContext);
    }

    /**
     * Calculates the n'th root of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to calculate the n'th root
     * @param n the {@link BigDecimal} defining the root
     *
     * @return the calculated n'th root of x with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#root(BigDecimal, BigDecimal, MathContext)
     */
    public static BigDecimal root(BigDecimal x, BigDecimal n) {
        return BigDecimalMath.root(x, n, defaultMathContext);
    }

    /**
     * Calculates the natural logarithm of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the natural logarithm for
     * @return the calculated natural logarithm {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#log(BigDecimal, MathContext)
     */
    public static BigDecimal log(BigDecimal x) {
        return BigDecimalMath.log(x, defaultMathContext);
    }

    /**
     * Calculates the logarithm of {@link BigDecimal} x to the base 2 using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the logarithm base 2 for
     * @return the calculated natural logarithm {@link BigDecimal} to the base 2 with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#log2(BigDecimal, MathContext)
     */
    public static BigDecimal log2(BigDecimal x) {
        return BigDecimalMath.log2(x, defaultMathContext);
    }

    /**
     * Calculates the logarithm of {@link BigDecimal} x to the base 10 using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the logarithm base 10 for
     * @return the calculated natural logarithm {@link BigDecimal} to the base 10 with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#log10(BigDecimal, MathContext)
     */
    public static BigDecimal log10(BigDecimal x) {
        return BigDecimalMath.log10(x, defaultMathContext);
    }

    /**
     * Returns the number pi using the default {@link MathContext}.
     *
     * @return the number pi with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#pi(MathContext)
     */
    public static BigDecimal pi() {
        return BigDecimalMath.pi(defaultMathContext);
    }

    /**
     * Returns the number e using the default {@link MathContext}.
     *
     * @return the number e with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#e(MathContext)
     */
    public static BigDecimal e() {
        return BigDecimalMath.e(defaultMathContext);
    }

    /**
     * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>) using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the exponent for
     * @return the calculated exponent {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#exp(BigDecimal, MathContext)
     */
    public static BigDecimal exp(BigDecimal x) {
        return BigDecimalMath.exp(x, defaultMathContext);
    }

    /**
     * Calculates the sine (sinus) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the sine for
     * @return the calculated sine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#sin(BigDecimal, MathContext)
     */
    public static BigDecimal sin(BigDecimal x) {
        return BigDecimalMath.sin(x, defaultMathContext);
    }

    /**
     * Calculates the arc sine (inverted sine) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc sine for
     * @return the calculated arc sine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#asin(BigDecimal, MathContext)
     */
    public static BigDecimal asin(BigDecimal x) {
        return BigDecimalMath.asin(x, defaultMathContext);
    }

    /**
     * Calculates the cosine (cosinus) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the cosine for
     * @return the calculated cosine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     */
    public static BigDecimal cos(BigDecimal x) {
        return BigDecimalMath.cos(x, defaultMathContext);
    }

    /**
     * Calculates the arc cosine (inverted cosine) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc cosine for
     * @return the calculated arc sine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#acos(BigDecimal, MathContext)
     */
    public static BigDecimal acos(BigDecimal x) {
        return BigDecimalMath.acos(x, defaultMathContext);
    }

    /**
     * Calculates the tangens of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the tangens for
     * @return the calculated tangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#tan(BigDecimal, MathContext)
     */
    public static BigDecimal tan(BigDecimal x) {
        return BigDecimalMath.tan(x, defaultMathContext);
    }

    /**
     * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc tangens for
     * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#atan(BigDecimal, MathContext)
     */
    public static BigDecimal atan(BigDecimal x) {
        return BigDecimalMath.atan(x, defaultMathContext);
    }

    /**
     * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} y / x in the range -<i>pi</i> to <i>pi</i> using the default {@link MathContext}.
     *
     * @param y the {@link BigDecimal}
     * @param x the {@link BigDecimal}
     * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see #atan2(BigDecimal, BigDecimal)
     */
    public static BigDecimal atan2(BigDecimal y, BigDecimal x) {
        return BigDecimalMath.atan2(y, x, defaultMathContext);
    }

    /**
     * Calculates the cotangens of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the cotangens for
     * @return the calculated cotanges {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#cot(BigDecimal, MathContext)
     */
    public static BigDecimal cot(BigDecimal x) {
        return BigDecimalMath.cot(x, defaultMathContext);
    }

    /**
     * Calculates the inverse cotangens (arc cotangens) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc cotangens for
     * @return the calculated arc cotangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#acot(BigDecimal, MathContext)
     */
    public static BigDecimal acot(BigDecimal x) {
        return BigDecimalMath.acot(x, defaultMathContext);
    }

    /**
     * Calculates the hyperbolic sine of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic sine for
     * @return the calculated hyperbolic sine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#sinh(BigDecimal, MathContext)
     */
    public static BigDecimal sinh(BigDecimal x) {
        return BigDecimalMath.sinh(x, defaultMathContext);
    }

    /**
     * Calculates the hyperbolic cosine of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic cosine for
     * @return the calculated hyperbolic cosine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#cosh(BigDecimal, MathContext)
     */
    public static BigDecimal cosh(BigDecimal x) {
        return BigDecimalMath.cosh(x, defaultMathContext);
    }

    /**
     * Calculates the hyperbolic tangens of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic tangens for
     * @return the calculated hyperbolic tangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#tanh(BigDecimal, MathContext)
     */
    public static BigDecimal tanh(BigDecimal x) {
        return BigDecimalMath.tanh(x, defaultMathContext);
    }

    /**
     * Calculates the hyperbolic cotangens of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic cotangens for
     * @return the calculated hyperbolic cotangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#coth(BigDecimal, MathContext)
     */
    public static BigDecimal coth(BigDecimal x) {
        return BigDecimalMath.coth(x, defaultMathContext);
    }

    /**
     * Calculates the arc hyperbolic sine (inverse hyperbolic sine) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic sine for
     * @return the calculated arc hyperbolic sine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#asinh(BigDecimal, MathContext)
     */
    public static BigDecimal asinh(BigDecimal x) {
        return BigDecimalMath.asinh(x, defaultMathContext);
    }

    /**
     * Calculates the arc hyperbolic cosine (inverse hyperbolic cosine) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic cosine for
     * @return the calculated arc hyperbolic cosine {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#acosh(BigDecimal, MathContext)
     */
    public static BigDecimal acosh(BigDecimal x) {
        return BigDecimalMath.acosh(x, defaultMathContext);
    }

    /**
     * Calculates the arc hyperbolic tangens (inverse hyperbolic tangens) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic tangens for
     * @return the calculated arc hyperbolic tangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#atanh(BigDecimal, MathContext)
     */
    public static BigDecimal atanh(BigDecimal x) {
        return BigDecimalMath.atanh(x, defaultMathContext);
    }

    /**
     * Calculates the arc hyperbolic cotangens (inverse hyperbolic cotangens) of {@link BigDecimal} x using the default {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic cotangens for
     * @return the calculated arc hyperbolic cotangens {@link BigDecimal} with the precision specified in the default {@link MathContext}
     * @see #setDefaultMathContext(MathContext)
     * @see BigDecimalMath#acoth(BigDecimal, MathContext)
     */
    public static BigDecimal acoth(BigDecimal x) {
        return BigDecimalMath.acoth(x, defaultMathContext);
    }

}
