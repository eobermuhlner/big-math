# big-math

Java math functions for `BigDecimal`.

This implementation provides efficient and accurate implementations for:

*   `pow(BigDecimal, BigDecimal, MathContext)`
*   `sqrt(BigDecimal, BigDecimal, MathContext)`
*   `log(BigDecimal, MathContext)`
*   `exp(BigDecimal, MathContext)`
*   `sin(BigDecimal, MathContext)`
*   `cos(BigDecimal, MathContext)`

*   `pow(BigDecimal, int, MathContext)` calculates x^y for `int` y
*   `factorial(int, MathContext)` calculates n!

*   `pi(MathContext)` calculates pi to an arbitrary precision
*   `e(MathContext)` calculates e to an arbitrary precision

*   `mantissa(BigDecimal)` extracts the mantissa from a `BigDecimal` (mantissa * 10^exponent)
*   `exponent(BigDecimal)` extracts the exponent from a `BigDecimal` (mantissa * 10^exponent)
*   `integralPart(BigDecimal)` extract the integral part from a `BigDecimal` (everything before the decimal point) 
*   `fractionalPart(BigDecimal)` extract the fractional part from a `BigDecimal` (everything after the decimal point)


