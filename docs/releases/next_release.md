# API changes



# Bugfixes

## `isDoubleValue()` to return `true` for values abs(x) < `Double.MIN_VALUE` 

Fixed `BigDecimalMath.isDoubleValue()` to return `true` for values abs(x) < `Double.MIN_VALUE`.

For example `BigDecimalMath.isDoubleValue(new BigDecimal("1E-325"))` will return `true`
although this value is smaller than `Double.MIN_VALUE` (and therefore outside the range of values that can be represented as `double`)
because `new BigDecimal("1E-325").doubleValue()` returns `0` which is a legal value with loss of precision.


# Enhancements



# Examples

Note: The example code is available on github, but not part of the big-math library.

