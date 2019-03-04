# API changes

## Use `DefaultBigDecimalMath` to avoid passing `MathContext`

Due to popular demand a convenience class `DefaultBigDecimalMath` was added that provides mathematical function
where the `MathContext` must not be passed every time.

Instead it uses a default `MathContext` that will be used by every method.

The default `MathContext` can be defined in three ways:
* the initial default is equivalent to `MathContext.DECIMAL128`
* specifying system properties during the start of the application
* programmatically using `DefaultBigDecimalMath.setDefaultMathContext()`

### System properties for default `MathContext`

`ch.obermuhlner.math.big.default.precision`
`ch.obermuhlner.math.big.default.rounding`

### Using `DefaultBigDecimalMath.setDefaultMathContext()`

The static method `DefaultBigDecimalMath.setDefaultMathContext()` can be used to set the new default `MathContext`.

This call should be done as early as possible during application start, so that all calculations use the same precision.

*Important*: Avoid the pitfall of setting the precision temporarily for a calculation.
This can lead to race conditions and calculations with the wrong precision
if other threads in your application do the same thing.

# Bugfixes

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
