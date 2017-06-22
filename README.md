# big-math

Java math functions for `BigDecimal`.

## Overview BigDecimalMath

This implementation provides efficient and accurate implementations for:

*   `log(BigDecimal, MathContext)`
*   `exp(BigDecimal, MathContext)`
*   `pow(BigDecimal, BigDecimal, MathContext)` calculates x^y
*   `sqrt(BigDecimal, BigDecimal, MathContext)`
*   `root(BigDecimal, BigDecimal, MathContext)` calculates the n'th root of x

*   `sin(BigDecimal, MathContext)`
*   `cos(BigDecimal, MathContext)`
*   `tan(BigDecimal, MathContext)`
*   `asin(BigDecimal, MathContext)`
*   `acos(BigDecimal, MathContext)`
*   `atan(BigDecimal, MathContext)`

*   `sinh(BigDecimal, MathContext)`
*   `cosh(BigDecimal, MathContext)`
*   `tanh(BigDecimal, MathContext)`
*   `asinh(BigDecimal, MathContext)`
*   `acosh(BigDecimal, MathContext)`
*   `atanh(BigDecimal, MathContext)`

*   `pow(BigDecimal, int, MathContext)` calculates x^y for `int` y
*   `factorial(int, MathContext)` calculates n!
*   `bernoulli(int)` calculates Bernoulli numbers

*   `pi(MathContext)` calculates pi to an arbitrary precision
*   `e(MathContext)` calculates e to an arbitrary precision

*   `mantissa(BigDecimal)` extracts the mantissa from a `BigDecimal` (mantissa * 10^exponent)
*   `exponent(BigDecimal)` extracts the exponent from a `BigDecimal` (mantissa * 10^exponent)
*   `integralPart(BigDecimal)` extract the integral part from a `BigDecimal` (everything before the decimal point) 
*   `fractionalPart(BigDecimal)` extract the fractional part from a `BigDecimal` (everything after the decimal point)

## Documentation

For the mathematical background and performance analysis please refer to this article:
*	[BigDecimalMath](http://obermuhlner.ch/wordpress/2016/06/02/bigdecimalmath/)

Some of the implementation details are explained here: 
*	[Adaptive precision in Newton’s Method](http://obermuhlner.ch/wordpress/2016/06/07/adaptive-precision-in-newtons-method/)

## Performance

The following charts show the time needed to calculate the functions over a range of values with a precision of 300 digits.

![sqrt(), root(), exp(), sin(), cos() 0 to 10](https://raw.githubusercontent.com/eobermuhlner/big-math/master/ch.obermuhlner.math.big.example/docu/benchmarks/images/perf_fast_funcs_from_0_to_10.png)
![sqrt(), root(), exp(), sin(), cos() 0 to 100](https://raw.githubusercontent.com/eobermuhlner/big-math/master/ch.obermuhlner.math.big.example/docu/benchmarks/images/perf_fast_funcs_from_0_to_100.png)

![exp(), log(), pow() 0 to 10](https://raw.githubusercontent.com/eobermuhlner/big-math/master/ch.obermuhlner.math.big.example/docu/benchmarks/images/perf_slow_funcs_from_0_to_10.png)
![exp(), log(), pow() 0 to 100](https://raw.githubusercontent.com/eobermuhlner/big-math/master/ch.obermuhlner.math.big.example/docu/benchmarks/images/perf_slow_funcs_from_0_to_100.png)
![]()



