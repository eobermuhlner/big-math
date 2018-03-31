# API changes

No API changes.


# Bugfixes

## Fixed `BigDecimalMath.log(BigDecimal, MathContext)` argument `x` very close to 0

`BigDecimalMath.log(BigDecimal, MathContext)` failed with arguments very close to 0 due to
`BigDecimal.doubleValue()` rounding down to 0.


# Enhancements

## Added `Example.method(y)`

Added `Example.method(y)` 


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
