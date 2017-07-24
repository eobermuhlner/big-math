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

The following code snippet:
```java
System.out.println("Range [0, 10) step 1 (using BigDecimal as input parameters)");
BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ONE, mathContext)
	.forEach(System.out::println);

System.out.println("Range [0, 10) step 3 (using long as input parameters)");
BigDecimalStream.range(0, 10, 3, mathContext)
	.forEach(System.out::println);
```

produces this output:
```
Range [0, 10) step 1 (using BigDecimal as input parameters)
0
1
2
3
4
5
6
7
8
9

Range [0, 12] step 3 (using long as input parameters)
0
3
6
9
12
```


## Added `BigFloat.signum()` and convenience `BigFloat.isNegative()`, `BigFloat.isZero()`, `BigFloat.isPositive()`  

The signum function and its convenience variants where added to the `BigFloat` class:
* `BigFloat.signum()` returns -1, 0, or 1 as the value is negative, zero, or positive
* `BigFloat.isNegative()` returns whether the value is negative
* `BigFloat.isZero()` returns whether the value is zero
* `BigFloat.isPositive()` returns whether the value is positive


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
