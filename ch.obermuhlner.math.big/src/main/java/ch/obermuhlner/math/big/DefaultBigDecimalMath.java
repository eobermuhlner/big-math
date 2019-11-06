package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * A wrapper around {@link BigDecimalMath} that passes a current {@link MathContext} to the
 * functions that need a {@link MathContext} argument.
 *
 * <p>The initial default {@link MathContext} is equivalent to {@link MathContext#DECIMAL128}
 * but this can be overridden by setting the following system properties:</p>
 * <ul>
 *     <li><code>ch.obermuhlner.math.big.default.precision</code> to a positive integer precision (default=34)</li>
 *     <li><code>ch.obermuhlner.math.big.default.rounding</code> to a {@link RoundingMode} name (default=HALF_UP) </li>
 * </ul>
 *
 * <p>It is also possible to programmatically set the default {@link MathContext} using {@link #setDefaultMathContext(MathContext)}.
 * It is recommended to set the desired precision in the {@link MathContext} very early in the startup of the application and to not change it afterwards.</p>
 *
 * <p>Important: Avoid the pitfall of setting the precision temporarily using {@link #setDefaultMathContext(MathContext)} for a calculation.
 * This can lead to race conditions and calculations with the wrong precision
 * if other threads in your application do the same thing.</p>
 *
 * <p>To set a temporary {@link MathContext} you have to choice to use either:
 * <ul>
 *      <li><code>DefaultBigDecimalMath.createLocalMathContext()</code> in a try-with-resources statement</li>
 *      <li><code>DefaultBigDecimalMath.withLocalMathContext()</code> with a lambda function</li>
 * </ul>
 *
 * Example code using <code>DefaultBigDecimalMath.createLocalMathContext()</code>:
 * <pre>
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
try (DefaultBigDecimalMath.LocalMathContext context = DefaultBigDecimalMath.createLocalMathContext(5)) {
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
    try (DefaultBigDecimalMath.LocalMathContext context2 = DefaultBigDecimalMath.createLocalMathContext(10)) {
        System.out.println("Pi[10]: " + DefaultBigDecimalMath.pi());
    }
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
}
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
 </pre>
 *
 * Example code using <code>DefaultBigDecimalMath.withLocalMathContext()</code>:
 * <pre>
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
DefaultBigDecimalMath.withPrecision(5, () -&gt; {
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
    DefaultBigDecimalMath.withPrecision(10, () -&gt; {
        System.out.println("Pi[10]: " + DefaultBigDecimalMath.pi());
    });
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
});
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
</pre>
 *
 * Both snippets with give the following ouput:
 * <pre>
Pi[default]: 3.141592653589793238462643383279503
Pi[5]: 3.1416
Pi[10]: 3.141592654
Pi[5]: 3.1416
Pi[default]: 3.141592653589793238462643383279503
</pre>
 * <p>The temporary {@link MathContext} are stored in {@link ThreadLocal} variables
 * and will therefore not conflict with each other when used in multi-threaded use case.</p>
 *
 * <p>Important: Due to the {@link ThreadLocal} variables the local {@link MathContext} will
 * <strong>not</strong> be available in other threads.
 * This includes streams using <code>parallel()</code>, thread pools and manually started threads.
 * If you need temporary {@link MathContext} for calculations then you <strong>must</strong>
 * set the local {@link MathContext} inside <strong>every</strong> separate thread.</p>
 *
 * <pre>
try (DefaultBigDecimalMath.LocalMathContext context = DefaultBigDecimalMath.createLocalMathContext(5)) {
    BigDecimalStream.range(0.0, 1.0, 0.01, DefaultBigDecimalMath.currentMathContext())
        .map(b -&gt; DefaultBigDecimalMath.cos(b))
        .map(b -&gt; "sequential " + Thread.currentThread().getName() + " [5]: " + b)
        .forEach(System.out::println);

    BigDecimalStream.range(0.0, 1.0, 0.01, DefaultBigDecimalMath.currentMathContext())
        .parallel()
        .map(b -&gt; {
            try (DefaultBigDecimalMath.LocalMathContext context2 = DefaultBigDecimalMath.createLocalMathContext(5)) {
                return DefaultBigDecimalMath.cos(b);
            }
        })
        .map(b -&gt; "parallel " + Thread.currentThread().getName() + " [5]: " + b)
        .forEach(System.out::println);
}
</pre>
 */
public class DefaultBigDecimalMath {

    private static MathContext defaultMathContext = createDefaultMathContext();
    private static ThreadLocal<Deque<MathContext>> mathContextStack = new ThreadLocal<>();

    private static MathContext createDefaultMathContext () {
        int precision = getIntSystemProperty("ch.obermuhlner.math.big.default.precision", MathContext.DECIMAL128.getPrecision());
        RoundingMode rounding = getRoundingModeSystemProperty("ch.obermuhlner.math.big.default.rounding", MathContext.DECIMAL128.getRoundingMode());

        return new MathContext(precision, rounding);
    }

    private static void pushMathContext(MathContext mathContext) {
        Deque<MathContext> mathContexts = mathContextStack.get();
        if (mathContexts == null) {
            mathContexts = new ArrayDeque<>();
            mathContextStack.set(mathContexts);
        };
        mathContexts.addLast(mathContext);
    }

    private static MathContext popMathContext() {
        Deque<MathContext> mathContexts = mathContextStack.get();
        MathContext poppedMathContext = mathContexts.removeLast();
        if (mathContexts.isEmpty()) {
            mathContextStack.remove();
        }
        return poppedMathContext;
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
     * Sets the default {@link MathContext} used if no other {@link MathContext} is defined using {@link #withLocalMathContext(MathContext, Runnable)}.
     *
     * @param defaultMathContext the default {@link MathContext}
     * @see #currentMathContext()
     * @see #withLocalMathContext(int, Runnable)
     * @see #withLocalMathContext(int, RoundingMode, Runnable)
     * @see #withLocalMathContext(MathContext, Runnable)
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
     * Executes the given {@link Runnable} using the specified precision.
     *
     * @param precision the precision to use for calculations in the <code>runnable</code>
     * @param runnable the {@link Runnable} to execute
     */
    public static void withLocalMathContext(int precision, Runnable runnable) {
        withLocalMathContext(new MathContext(precision), runnable);
    }

    /**
     * Executes the given {@link Runnable} using the specified precision and {@link RoundingMode}.
     *
     * @param precision the precision to use for calculations in the <code>runnable</code>
     * @param roundingMode the {@link RoundingMode} to use for calculations in the <code>runnable</code>
     * @param runnable the {@link Runnable} to execute
     */
    public static void withLocalMathContext(int precision, RoundingMode roundingMode, Runnable runnable) {
        withLocalMathContext(new MathContext(precision, roundingMode), runnable);
    }

    /**
     * Executes the given {@link Runnable} using the specified {@link MathContext}.
     *
     * @param mathContext the {@link MathContext} to use for calculations in the <code>runnable</code>
     * @param runnable the {@link Runnable} to execute
     */
    public static void withLocalMathContext(MathContext mathContext, Runnable runnable) {
        try (LocalMathContext context = createLocalMathContext(mathContext)) {
            runnable.run();
        }
    }

    /**
     * Executes the given {@link Runnable} using the specified precision.
     *
     * @param precision the precision to use for calculations
     * @return the created {@link LocalMathContext} to be used in a try-with-resources statement
     */
    public static LocalMathContext createLocalMathContext(int precision) {
        return createLocalMathContext(new MathContext(precision));
    }

    /**
     * Executes the given {@link Runnable} using the specified precision and {@link RoundingMode}.
     *
     * @param precision the precision to use for calculations
     * @param roundingMode the {@link RoundingMode} to use for calculations in the <code>runnable</code>
     * @return the created {@link LocalMathContext} to be used in a try-with-resources statement
     */
    public static LocalMathContext createLocalMathContext(int precision, RoundingMode roundingMode) {
        return createLocalMathContext(new MathContext(precision, roundingMode));
    }

    /**
     * Executes the given {@link Runnable} using the specified {@link MathContext}.
     *
     * @param mathContext the {@link MathContext} to use for calculations
     * @return the created {@link LocalMathContext} to be used in a try-with-resources statement
     */
    public static LocalMathContext createLocalMathContext(MathContext mathContext) {
        return new LocalMathContext(mathContext);
    }

    /**
     * Returns the current {@link MathContext} used for all mathematical functions in this class.
     *
     * <p>The current {@link MathContext} is the last {@link MathContext} specified
     * using {@link #withLocalMathContext(MathContext, Runnable)}
     * or the default {@link MathContext} if none was specified.</p>
     *
     * @return the current {@link MathContext}
     * @see #currentMathContext()
     * @see #withLocalMathContext(int, Runnable)
     * @see #withLocalMathContext(int, RoundingMode, Runnable)
     * @see #withLocalMathContext(MathContext, Runnable)
     */
    public static MathContext currentMathContext() {
        Deque<MathContext> mathContexts = mathContextStack.get();
        if (mathContexts == null || mathContexts.isEmpty()) {
            return defaultMathContext;
        }

        return mathContexts.getLast();
    }
    
    /**
     * Rounds the specified {@link BigDecimal} to the precision of the current {@link MathContext}.
     *
     * @param value the {@link BigDecimal} to round
     * @return the rounded {@link BigDecimal} value
     * @see #currentMathContext()
     * @see BigDecimalMath#round(BigDecimal, MathContext)
     */
    public static BigDecimal round(BigDecimal value) {
        return  BigDecimalMath.round(value, defaultMathContext);
    }

    /**
     * Rounds the specified {@link BigDecimal} to the precision of the current {@link MathContext} including trailing zeroes.
     *
     * @param value the {@link BigDecimal} to round
     * @return the rounded {@link BigDecimal} value including trailing zeroes
     * @see #currentMathContext()
     * @see BigDecimalMath#roundWithTrailingZeroes(BigDecimal, MathContext)
     */
    public static BigDecimal roundWithTrailingZeroes(BigDecimal value) {
        return BigDecimalMath.roundWithTrailingZeroes(value, currentMathContext());
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x + y</code> using the current {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to add
     * @return the resulting {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimal#add(BigDecimal, MathContext)
     */
    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        return x.add(y, currentMathContext());
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x - y</code> using the current {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to subtract
     * @return the resulting {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimal#subtract(BigDecimal, MathContext)
     */
    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
        return x.subtract(y, currentMathContext());
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x * y</code> using the current {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to multiply
     * @return the resulting {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimal#multiply(BigDecimal, MathContext)
     */
    public static BigDecimal multiply(BigDecimal x, BigDecimal y) {
        return x.multiply(y, currentMathContext());
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x / y</code> using the current {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to divide
     * @return the resulting {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimal#divide(BigDecimal, MathContext)
     */
    public static BigDecimal divide(BigDecimal x, BigDecimal y) {
        return x.divide(y, currentMathContext());
    }

    /**
     * Returns the {@link BigDecimal} that is <code>x % y</code> using the current {@link MathContext}.
     *
     * @param x the x value
     * @param y the y value to divide
     * @return the resulting {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimal#remainder(BigDecimal, MathContext)
     */
    public static BigDecimal remainder(BigDecimal x, BigDecimal y) {
        return x.remainder(y, currentMathContext());
    }

    /**
     * Calculates the reciprocal of the specified {@link BigDecimal} using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the reciprocal {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#reciprocal(BigDecimal, MathContext)
     */
    public static BigDecimal reciprocal(BigDecimal x) {
        return BigDecimalMath.reciprocal(x, currentMathContext());
    }

    /**
     * Calculates the factorial of the specified {@link BigDecimal} using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the factorial {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#factorial(BigDecimal, MathContext)
     */
    public static BigDecimal factorial(BigDecimal x) {
        return BigDecimalMath.factorial(x, currentMathContext());
    }

    /**
     * Calculates the gamma function of the specified {@link BigDecimal} using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal}
     * @return the gamma {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#gamma(BigDecimal, MathContext)
     */
    public static BigDecimal gamma(BigDecimal x) {
        return BigDecimalMath.gamma(x, currentMathContext());
    }

    /**
     * Calculates the Bernoulli number for the specified index using the current {@link MathContext}.
     *
     * @param n the index of the Bernoulli number to be calculated (starting at 0)
     * @return the Bernoulli number for the specified index with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#bernoulli(int, MathContext)
     */
    public static BigDecimal bernoulli(int n) {
        return BigDecimalMath.bernoulli(n, currentMathContext());
    }

    /**
     * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>) using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to take to the power
     * @param y the {@link BigDecimal} value to serve as exponent
     * @return the calculated x to the power of y with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)
     */
    public static BigDecimal pow(BigDecimal x, BigDecimal y) {
        return BigDecimalMath.pow(x, y, currentMathContext());
    }

    /**
     * Calculates {@link BigDecimal} x to the power of <code>long</code> y (x<sup>y</sup>) using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to take to the power
     * @param y the <code>long</code> value to serve as exponent
     * @return the calculated x to the power of y with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#pow(BigDecimal, long, MathContext)
     */
    public static BigDecimal pow(BigDecimal x, long y) {
        return BigDecimalMath.pow(x, y, currentMathContext());
    }

    /**
     * Calculates the square root of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to calculate the square root
     * @return the calculated square root of x with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#sqrt(BigDecimal, MathContext)
     */
    public static BigDecimal sqrt(BigDecimal x) {
        return BigDecimalMath.sqrt(x, currentMathContext());
    }

    /**
     * Calculates the n'th root of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} value to calculate the n'th root
     * @param n the {@link BigDecimal} defining the root
     *
     * @return the calculated n'th root of x with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#root(BigDecimal, BigDecimal, MathContext)
     */
    public static BigDecimal root(BigDecimal x, BigDecimal n) {
        return BigDecimalMath.root(x, n, currentMathContext());
    }

    /**
     * Calculates the natural logarithm of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the natural logarithm for
     * @return the calculated natural logarithm {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#log(BigDecimal, MathContext)
     */
    public static BigDecimal log(BigDecimal x) {
        return BigDecimalMath.log(x, currentMathContext());
    }

    /**
     * Calculates the logarithm of {@link BigDecimal} x to the base 2 using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the logarithm base 2 for
     * @return the calculated natural logarithm {@link BigDecimal} to the base 2 with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#log2(BigDecimal, MathContext)
     */
    public static BigDecimal log2(BigDecimal x) {
        return BigDecimalMath.log2(x, currentMathContext());
    }

    /**
     * Calculates the logarithm of {@link BigDecimal} x to the base 10 using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the logarithm base 10 for
     * @return the calculated natural logarithm {@link BigDecimal} to the base 10 with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#log10(BigDecimal, MathContext)
     */
    public static BigDecimal log10(BigDecimal x) {
        return BigDecimalMath.log10(x, currentMathContext());
    }

    /**
     * Returns the number pi using the current {@link MathContext}.
     *
     * @return the number pi with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#pi(MathContext)
     */
    public static BigDecimal pi() {
        return BigDecimalMath.pi(currentMathContext());
    }

    /**
     * Returns the number e using the current {@link MathContext}.
     *
     * @return the number e with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#e(MathContext)
     */
    public static BigDecimal e() {
        return BigDecimalMath.e(currentMathContext());
    }

    /**
     * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>) using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the exponent for
     * @return the calculated exponent {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#exp(BigDecimal, MathContext)
     */
    public static BigDecimal exp(BigDecimal x) {
        return BigDecimalMath.exp(x, currentMathContext());
    }

    /**
     * Calculates the sine (sinus) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the sine for
     * @return the calculated sine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#sin(BigDecimal, MathContext)
     */
    public static BigDecimal sin(BigDecimal x) {
        return BigDecimalMath.sin(x, currentMathContext());
    }

    /**
     * Calculates the arc sine (inverted sine) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc sine for
     * @return the calculated arc sine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#asin(BigDecimal, MathContext)
     */
    public static BigDecimal asin(BigDecimal x) {
        return BigDecimalMath.asin(x, currentMathContext());
    }

    /**
     * Calculates the cosine (cosinus) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the cosine for
     * @return the calculated cosine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     */
    public static BigDecimal cos(BigDecimal x) {
        return BigDecimalMath.cos(x, currentMathContext());
    }

    /**
     * Calculates the arc cosine (inverted cosine) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc cosine for
     * @return the calculated arc sine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#acos(BigDecimal, MathContext)
     */
    public static BigDecimal acos(BigDecimal x) {
        return BigDecimalMath.acos(x, currentMathContext());
    }

    /**
     * Calculates the tangens of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the tangens for
     * @return the calculated tangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#tan(BigDecimal, MathContext)
     */
    public static BigDecimal tan(BigDecimal x) {
        return BigDecimalMath.tan(x, currentMathContext());
    }

    /**
     * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc tangens for
     * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#atan(BigDecimal, MathContext)
     */
    public static BigDecimal atan(BigDecimal x) {
        return BigDecimalMath.atan(x, currentMathContext());
    }

    /**
     * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} y / x in the range -<i>pi</i> to <i>pi</i> using the current {@link MathContext}.
     *
     * @param y the {@link BigDecimal}
     * @param x the {@link BigDecimal}
     * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see #atan2(BigDecimal, BigDecimal)
     */
    public static BigDecimal atan2(BigDecimal y, BigDecimal x) {
        return BigDecimalMath.atan2(y, x, currentMathContext());
    }

    /**
     * Calculates the cotangens of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the cotangens for
     * @return the calculated cotanges {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#cot(BigDecimal, MathContext)
     */
    public static BigDecimal cot(BigDecimal x) {
        return BigDecimalMath.cot(x, currentMathContext());
    }

    /**
     * Calculates the inverse cotangens (arc cotangens) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc cotangens for
     * @return the calculated arc cotangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#acot(BigDecimal, MathContext)
     */
    public static BigDecimal acot(BigDecimal x) {
        return BigDecimalMath.acot(x, currentMathContext());
    }

    /**
     * Calculates the hyperbolic sine of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic sine for
     * @return the calculated hyperbolic sine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#sinh(BigDecimal, MathContext)
     */
    public static BigDecimal sinh(BigDecimal x) {
        return BigDecimalMath.sinh(x, currentMathContext());
    }

    /**
     * Calculates the hyperbolic cosine of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic cosine for
     * @return the calculated hyperbolic cosine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#cosh(BigDecimal, MathContext)
     */
    public static BigDecimal cosh(BigDecimal x) {
        return BigDecimalMath.cosh(x, currentMathContext());
    }

    /**
     * Calculates the hyperbolic tangens of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic tangens for
     * @return the calculated hyperbolic tangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#tanh(BigDecimal, MathContext)
     */
    public static BigDecimal tanh(BigDecimal x) {
        return BigDecimalMath.tanh(x, currentMathContext());
    }

    /**
     * Calculates the hyperbolic cotangens of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the hyperbolic cotangens for
     * @return the calculated hyperbolic cotangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#coth(BigDecimal, MathContext)
     */
    public static BigDecimal coth(BigDecimal x) {
        return BigDecimalMath.coth(x, currentMathContext());
    }

    /**
     * Calculates the arc hyperbolic sine (inverse hyperbolic sine) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic sine for
     * @return the calculated arc hyperbolic sine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#asinh(BigDecimal, MathContext)
     */
    public static BigDecimal asinh(BigDecimal x) {
        return BigDecimalMath.asinh(x, currentMathContext());
    }

    /**
     * Calculates the arc hyperbolic cosine (inverse hyperbolic cosine) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic cosine for
     * @return the calculated arc hyperbolic cosine {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#acosh(BigDecimal, MathContext)
     */
    public static BigDecimal acosh(BigDecimal x) {
        return BigDecimalMath.acosh(x, currentMathContext());
    }

    /**
     * Calculates the arc hyperbolic tangens (inverse hyperbolic tangens) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic tangens for
     * @return the calculated arc hyperbolic tangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#atanh(BigDecimal, MathContext)
     */
    public static BigDecimal atanh(BigDecimal x) {
        return BigDecimalMath.atanh(x, currentMathContext());
    }

    /**
     * Calculates the arc hyperbolic cotangens (inverse hyperbolic cotangens) of {@link BigDecimal} x using the current {@link MathContext}.
     *
     * @param x the {@link BigDecimal} to calculate the arc hyperbolic cotangens for
     * @return the calculated arc hyperbolic cotangens {@link BigDecimal} with the precision specified in the current {@link MathContext}
     * @see #currentMathContext()
     * @see BigDecimalMath#acoth(BigDecimal, MathContext)
     */
    public static BigDecimal acoth(BigDecimal x) {
        return BigDecimalMath.acoth(x, currentMathContext());
    }

    /**
     * The local context used to push and pop a {@link MathContext} on the stack.
     *
     * <p>The recommended way to use this class is to use the try-with-resources.</p>
     */
    public static class LocalMathContext implements AutoCloseable {
        public final MathContext mathContext;

        LocalMathContext(MathContext mathContext) {
            this.mathContext = mathContext;
            pushMathContext(mathContext);
        }

        @Override
        public void close() {
            popMathContext();
        }
    }
}
