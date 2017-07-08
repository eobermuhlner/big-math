# API changes

No API changes.


# Bugfixes

No bugfixes.


# Enhancements

## Constants `pi`, `log2`, `log3`, `log10` with improved caching

The constants `pi`, `log2`, `log3`, `log10` are now cached with a different strategy.

Previously the constants where stored with a precalculated precision of slightly over 1000 digits and rounded down to the desired precision.
This increases the size of the jar library and uses unnecessary memory if the full precision is never needed.
If more precision is needed the constants where calculated again and a again with the high precision.

The new caching strategy keeps now precalculated values (making the library smaller).
Whenever a constant with a higher precision than cached is calculated it will replace the previously cached value.
This uses only the minimum memory for the cached values and works well even if very high precision is needed.

Tip: If your application needs the constant with many different precisions it might be more efficient to calculate the necessary constants with the maximum precision in the initialization phase of your application.
 
## Constant e is cached

The constant `e` is now cached with the same strategy as pi and the other mathematical constants. 

## Peformance improvements in `sqrt()`

The `sqrt()` was optimized so that the square root of square numbers is calculated much faster.

The adaptive precision calculation was slightly optimized, assuming that the precision increases every iteration by a factor of ~ 1.8.


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
