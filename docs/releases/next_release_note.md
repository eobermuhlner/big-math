# API changes

No API changes.


# Bugfixes

No Bugfix changes.


# Enhancements

## Added `DefaultBigDecimalMath.withPrecision()`

The new method `DefaultBigDecimalMath.withPrecision()` in several overloaded variants
allows to manage temporary `MathContext` on top of the default `MathContext`.

This allows to specify a precision that will be used only in specific calculation.

```java
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
DefaultBigDecimalMath.withPrecision(5, () -> {
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
    DefaultBigDecimalMath.withPrecision(10, () -> {
        System.out.println("Pi[10]: " + DefaultBigDecimalMath.pi());
    });
    System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
});
System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
``` 

The new method `DefaultBigDecimalMath.currentMathContext()` can be
used to access the current `MathContext`.
Usually there is no need to do this, since the current `MathContext` is
passed to the `BigDecimalMath` functions automatically.


# Examples

Note: The example code is available on github, but not part of the big-math library.

## Examples for `withPrecision()` in `DefaultBigDecimalMathExample`

Example code was added to the class `DefaultBigDecimalMathExample`
to demonstrate the usage of `DefaultBigDecimalMath.withPrecision()`.
