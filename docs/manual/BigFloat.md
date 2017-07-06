`BigFloat` is a wrapper around `BigDecimal` which simplifies the consistent usage of the MathContext and provides a simpler API for calculations. 

# Overview

Every BigFloat instance has a reference to a Context that specifies the MathContext to be used for all calculations and values.

The API for calculations is simplified and more consistent with the typical mathematical usage.
* Factory methods for values:
	* `valueOf(BigFloat)`
	* `valueOf(BigDecimal)`
	* `valueOf(int)`
	* `valueOf(long)`
	* `valueOf(double)`
	* `valueOf(String)`
	* `pi()`
	* `e()`

* All standard operators:
	* `add(x)`
	* `subtract(x)`
	* `multiply(x)`
	* `remainder(x)`
	* `pow(y)`
	* `root(y)`

* Calculation methods are overloaded for different value types:
 	* `add(BigFloat)`
	* `add(BigDecimal)`
	* `add(int)`
	* `add(long)`
	* `add(double)`
	* ...

* Mathematical functions are written as they are traditionally are written:
 	* `abs(x)`
	* `log(x)`
	* `sin(x)`
	* `min(x1, x2, ...)`
	* `max(x1, x2, ...)`
	* ...

* Support for advanced mathematical functions:
 	* `sqrt(x)`
	* `log(x)`
	* `exp(x)`
	* `sin(x)`
	* `cos(x)`
	* `tan(x)`
	* ...

* Methods to access parts of a value:
 	* `getMantissa()`
	* `getExponent()`
	* `getIntegralPart()`
	* `getFractionalPart()`

* Comparison methods:
 	* `isEqual(BigFloat)`
	* `isLessThan(BigFloat)`
	* `isLessThanOrEqual(BigFloat)`
	* `isGreaterThan(BigFloat)`
	* `isGreaterThanOrEqual(BigFloat)`


# Usage

Before doing any calculations you need to create a `Context` specifying the precision used for all calculations.
```java
Context context = BigFloat.context(100); // precision of 100 digits
Context anotherContext = BigFloat.context(new MathContext(10, RoundingMode.HALF_UP); // precision of 10 digits, rounding half up
```

The `Context` can then be used to create the first value of the calculation:
```java
BigFloat value1 = context.valueOf(640320);
```

The `BigFloat` instance holds a reference to the `Context`. This context is then passed from calculation to calculation.
```java
BigFloat value2 = context.valueOf(640320).pow(3).divide(24);
BigFloat value3 = BigFloat.sin(value2);
```


The `BigFloat` result can be converted to other numerical types:
```java
BigDecimal bigDecimalValue = value3.toBigDecimal();
double doubleValue = value3.toDouble();
long longValue = value3.toLong();
int intValue = value3.toInt();
```

