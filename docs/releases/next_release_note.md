# API changes

## Use `DefaultBigDecimalMath` to avoid passing `MathContext`

Due to popular demand a convenience class `DefaultBigDecimalMath` was added that provides mathematical functions
where the `MathContext` must not be passed every time.

The class `DefaultBigDecimalMath` is a wrapper around `BigDecimalMath` that passes always the same default `MathContext` to the
functions that need a `MathContext` argument.

This class is designed for applications that will always need the same precision in all calculations.

The initial default `MathContext` is equivalent to `MathContext.DECIMAL128`
but this can be overridden by setting the following system properties:
* `ch.obermuhlner.math.big.default.precision` to a positive integer precision (default=34)
* `ch.obermuhlner.math.big.default.rounding` to a `RoundingMode` name (default=HALF_UP)

It is also possible to set the default `MathContext` using `DefaultBigDecimalMath.setDefaultMathContext(MathContext)`.
It is recommended to set the desired precision in the `MathContext` early in the startup of the application.

*Important*: Avoid the pitfall of setting the precision temporarily for a calculation.
This can lead to race conditions and calculations with the wrong precision
if other threads in your application do the same thing.

## Improved rounding with `BigDecimalMath.roundWithTrailingZeroes()`

The new function `BigDecimalMath.roundWithTrailingZeroes()` rounds the specified `BigDecimal` to the precision of the specified `MathContext` including trailing zeroes.

Example:
```
MathContext mc = new MathContext(5);
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567"), mc));    // 1.2346
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123.4567"), mc));    // 123.46
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.001234567"), mc)); // 0.0012346
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23"), mc));        // 1.2300
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.230000"), mc));    // 1.2300
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00123"), mc));     // 0.0012300
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0"), mc));           // 0.0000
System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00000000"), mc));  // 0.0000
```

For consistency the normal `BigDecimal.round()` function has also been provided in `BigDecimalMath`. 


# Bugfixes

## Fix `sqrt()` sometimes wrong in last digit with low precision.

Calculation of square root with low precision (16 digits and less) could sometimes
calculate the last digit wrong.

For example:
```java
BigDecimalMath.sqrt(new BigDecimal("43.79427225801566", new MathContext(16)));
```
returns `6.617724099568950` instead of `6.617724099568949`

This has been fixed.

## Fix `BigDecimalMath` functions with `MathContext.UNLIMITED`

The behaviour of the `BigDecimalMath` functions when being used with the `MathContext.UNLIMITED` was badly defined.
In many cases the calculation was done with a low precision like 10.

The functions that calculate the result with an algorithm using well defined and obvious operations
may throw the same `ArithmeticException` as the underlying `BigDecimal` operations
 if the specified `MathContext` has a precision of 0 (for example `MathContext.UNLIMITED`):
- `BigDecimalMath.reciprocal(BigDecimal)` 
- `BigDecimalMath.bernoulli(int, MathContext)` 
- `BigDecimalMath.factorial(BigDecimal, MathContext)` with integer argument
- `BigDecimalMath.gamma(BigDecimal, MathContext)` with integer argument
- `BigDecimalMath.pow(BigDecimal, long, MathContext)` 
 
 All other functions that use approximations to calculate the result will throw a `UnsupportedOperationException`
 if the specified `MathContext` has a precision of 0 (for example `MathContext.UNLIMITED`):
- `BigDecimalMath.factorial(BigDecimal, MathContext)` with non-integer argument
- `BigDecimalMath.gamma(BigDecimal, MathContext)` with non-integer argument
- `BigDecimalMath.pow(BigDecimal, BigDecimal, MathContext)`
- `BigDecimalMath.sqrt(BigDecimal, MathContext)`
- `BigDecimalMath.root(BigDecimal, BigDecimal, MathContext)`
- `BigDecimalMath.log(BigDecimal, MathContext)`
- `BigDecimalMath.log2(BigDecimal, MathContext)`
- `BigDecimalMath.log10(BigDecimal, MathContext)`
- `BigDecimalMath.pi(MathContext)`
- `BigDecimalMath.e(MathContext)`
- `BigDecimalMath.exp(BigDecimal, MathContext)`
- `BigDecimalMath.sin(BigDecimal, MathContext)`
- `BigDecimalMath.asin(BigDecimal, MathContext)`
- `BigDecimalMath.cos(BigDecimal, MathContext)`
- `BigDecimalMath.acos(BigDecimal, MathContext)`
- `BigDecimalMath.tan(BigDecimal, MathContext)`
- `BigDecimalMath.atan(BigDecimal, MathContext)`
- `BigDecimalMath.atan2(BigDecimal, BigDecimal MathContext)`
- `BigDecimalMath.cot(BigDecimal, MathContext)`
- `BigDecimalMath.acot(BigDecimal, MathContext)`
- `BigDecimalMath.sinh(BigDecimal, MathContext)`
- `BigDecimalMath.cosh(BigDecimal, MathContext)`
- `BigDecimalMath.tanh(BigDecimal, MathContext)`
- `BigDecimalMath.coth(BigDecimal, MathContext)`
- `BigDecimalMath.asinh(BigDecimal, MathContext)`
- `BigDecimalMath.acosh(BigDecimal, MathContext)`
- `BigDecimalMath.atanh(BigDecimal, MathContext)`
- `BigDecimalMath.acoth(BigDecimal, MathContext)`

# Enhancements

## Added `BigDecimalMath.reciprocal()`

The convenience method `BigDecimalMath.reciprocal(BigDecimal, MathContext)` was added.


## Added `BigComplexMath.factorial()`

The method `BigComplexMath.factorial(BigComplex x, MathContext mathContext)` was added.

It calculates the factorial in the complex domain and is equivalent to `BigDecimalMath.factorial(BigDecimal x, MathContext mathContext)`.


## Added `BigComplexMath.gamma()`

The method `BigComplexMath.gamma(BigComplex x, MathContext mathContext)` was added.

It calculates the gamma function in the complex domain and is equivalent to `BigDecimalMath.gamma(BigDecimal x, MathContext mathContext)`.


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
