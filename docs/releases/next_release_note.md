# API changes

## Added `BigComplex` and `BigComplexMath`

### Class `BigComplex`

The class `BigComplex` to represent complex numbers in the form `(a + bi)` has been added.
It follows the design of `BigDecimal` with some convenience improvements like overloaded operator methods.

A big difference to `BigDecimal` is that `BigComplex.equals()` implements the *mathematical* equality
and *not* the strict technical equality.
This was a difficult decision because it means that `BigComplex` behaves slightly different than `BigDecimal`
but considering that the strange equality of `BigDecimal` is a major source of bugs we
decided it was worth the slight inconsistency.
If you need the strict equality use `BigComplex.strictEquals()`.



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




# Bugfixes

## Fix `pow()` with large integer `y` argument

The `pow(BigDecimal, int)` would give wrong results for large `y` value, because
internally `BigDecimal.intValue()` was used instead of `BigDecimal.intValueExact()`.

This has been fixed and improved so that calculations with very large `y` values are still possible. 


## Precision improvements in `log()`, `sqrt()`

Strict unit testing has shown that the `log()` function would not calculate
with the maximum precision, especially if the `x` argument was transformed into
another value range (for example `x` > 10).

This has been fixed by doing the internal transformation calculations with a higher precision.


# Enhancements

## Added `Example.method(y)`

Added `Example.method(y)` 


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
