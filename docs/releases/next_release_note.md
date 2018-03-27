# API changes

## Added `BigComplex` and `BigComplexMath`

### Class `BigComplex`

The class `BigComplex` represents complex numbers in the form `(a + bi)`.
It follows the design of `BigDecimal` with some convenience improvements like overloaded operator methods.

A big difference to `BigDecimal` is that `BigComplex.equals()` implements the *mathematical* equality
and *not* the strict technical equality.
This was a difficult decision because it means that `BigComplex` behaves slightly different than `BigDecimal`
but considering that the strange equality of `BigDecimal` is a major source of bugs we
decided it was worth the slight inconsistency.

If you need the strict equality use `BigComplex.strictEquals()`.

* `re`
* `im`


* `add(BigComplex)`
* `add(BigComplex, MathContext)`
* `add(BigDecimal)`
* `add(BigDecimal, MathContext)`
* `add(double)`
* `subtract(BigComplex)`
* `subtract(BigComplex, MathContext)`
* `subtract(BigDecimal)`
* `subtract(BigDecimal, MathContext)`
* `subtract(double)`
* `multiply(BigComplex)`
* `multiply(BigComplex, MathContext)`
* `multiply(BigDecimal)`
* `multiply(BigDecimal, MathContext)`
* `multiply(double)`
* `divide(BigComplex)`
* `divide(BigComplex, MathContext)`
* `divide(BigDecimal)`
* `divide(BigDecimal, MathContext)`
* `divide(double)`


* `reciprocal(MathContext)`
* `conjugate()`
* `negate()`
* `abs(MathContext)`
* `angle(MathContext)`
* `absSquare(MathContext)`


* `isReal()`
* `re()`
* `im()`
* `round(MathContext)`


* `hashCode()`
* `equals(Object)`
* `strictEquals(Object)`
* `toString()`


* `valueOf(BigDecimal)`
* `valueOf(double)`
* `valueOf(BigDecimal, BigDecimal)`
* `valueOf(double, double)`
* `valueOfPolar(BigDecimal, BigDecimal, MathContext)`
* `valueOfPolar(double, double, MathContext)`

### Class `BigComplexMath`

The class `BigComplexMath` is the equivalent of `BigDecimalMath` and contains mathematical functions in the complex domain.

* `sin(BigComplex, MathContext)` 
* `cos(BigComplex, MathContext)` 
* `tan(BigComplex, MathContext)` 
* `asin(BigComplex, MathContext)`
* `acos(BigComplex, MathContext)`
* `atan(BigComplex, MathContext)`
* `acot(BigComplex, MathContext)`
* `exp(BigComplex, MathContext)`
* `log(BigComplex, MathContext)`
* `pow(BigComplex, long, MathContext)` 
* `pow(BigComplex, BigDecimal, MathContext)` 
* `pow(BigComplex, BigComplex, MathContext)` 
* `sqrt(BigComplex, MathContext)` 
* `root(BigComplex, BigDecimal, MathContext)` 
* `root(BigComplex, BigComplex, MathContext)` 


## Changed `pow(BigDecimal, int)` to `pow(BigDecimal, long)` 

The signature of `BigDecimalMath.pow(BigDecimal, int)` to `BigDecimalMath.pow(BigDecimal, long)`
in order to improve the possible range of the `y` argument. 


# Bugfixes

## Fix `pow(BigDecimal, int)` with large integer `y` argument

The `BigDecimalMath.pow(BigDecimal, int)` would give wrong results for large `y` values, because
internally `BigDecimal.intValue()` was used instead of `BigDecimal.intValueExact()`.

This has been fixed and improved so that calculations with very large `y` values are still possible. 


## Precision improvements in `log()`, `sqrt()`

Strict unit testing has shown that the `log()` function would not calculate
with the maximum precision, especially if the `x` argument was transformed into
another value range (for example if `x > 10`).

This has been fixed by calculating the internal transformation calculations with a higher precision.


# Enhancements

## Added `Example.method(y)`

Added `Example.method(y)` 


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
