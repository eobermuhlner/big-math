# API changes

## Module name for Java 9

Not really an API change but rather an API specification.

The deployed big-math Jar file contains now a module name for the Java 9 JigSaw module system.

This allows it to be used as automatic module with a well defined module name instead
of deriving the name magically from the Jar file name.

The module name follows the reverse domain convention and is: `ch.obermuhlner.math.big`


## OSGi support

The big-math Jar file is OSGi compatible.

The `MANIFEST.MF` contains all the necessary headers and exports the public packages
- ``ch.obermuhlner.math.big`
- ``ch.obermuhlner.math.big.stream`


# Bugfixes

## Fixed `BigDecimalMath.log(BigDecimal, MathContext)` argument `x` very close to 0

`BigDecimalMath.log(BigDecimal, MathContext)` failed with arguments very close to 0 due to
`BigDecimal.doubleValue()` rounding down to 0.


## Fixed `BigDecimalMath.atan2(BigDecimal, BigDecimal, MathContext)` with positive `x`
 
A stupid typo in a division would give results with wrong precision results with positive `x` arguments. 


# Enhancements

## Added `Example.method(y)`

Added `Example.method(y)` 


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
