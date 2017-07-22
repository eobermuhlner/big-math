# API changes

No API changes.


# Bugfixes

No Bugfixes changes.


# Enhancements

## Factory methods for `BigDecimal` and `BigFloat` streams

The classes `BigDecimalStream` and `BigFloatStream` provide factory methods for streams of `BigDecimal` respectively `BigFloat` elements.

Overloaded variants of `range(start, end, step)` provide sequential elements equivalent to `IntStream.range(start, end)` but with configurable step.

Similar methods for the `rangeClosed()` (inclusive end) are available.

The streams are well behaved when used in parallel mode. 


## Added `BigFloat.signum()` and convenience `BigFloat.isNegative()`, `BigFloat.isZero()`, `BigFloat.isPositive()`  

The signum function and its convenience variants where added to the `BigFloat` class:
* `BigFloat.signum()` returns -1, 0, or 1 as the value is negative, zero, or positive
* `BigFloat.isNegative()` returns `true` if negative
* `BigFloat.isZero()` returns `true` if 0
* `BigFloat.isPositive()` returns `true` if positive


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
