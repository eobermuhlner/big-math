# Non-integer Factorial and Gamma function

## Introduction

Standard introduction:
https://en.wikipedia.org/wiki/Gamma_function

## Attempt to use Euler's definition as an infinite product

Euler's infinite product definition
https://en.wikipedia.org/wiki/Gamma_function#Euler's_definition_as_an_infinite_product
is easy to implement but after a 1000 multiplications achieves only 3 digits precision for factorial(3.0).
This will not work.


![Factorial - Euler's definition as infinite product](http://mathurl.com/ycpugx97.png)


## Using Spouge's Approximation

Probably most useful approximation for arbitrary precision:
https://en.wikipedia.org/wiki/Spouge%27s_approximation


![Factorial - Spouge's approximation](http://mathurl.com/yath3d2p.png)

where `a` is an arbitrary positive integer that can be used to control the precision and the coefficients are given by

![Factorial - Spouge's approximation - c0](http://mathurl.com/ybd4oev2.png)

![Factorial - Spouge's approximation - ck](http://mathurl.com/y7c3or6k.png)

The relative error when omitting the epsilon part is bound to

![Factorial - Spouge's approximation - error](http://mathurl.com/ybsk79h7.png)


### Expected error of Spouge's Approximation

Using the relative error formula of Spouge's approximation we see that the expected precision
is roughly linear to the chosen value of `a` for the values `[1..1000]`.
`precision ~= 0.8 * a`

```java
	public static BigDecimal errorOfFactorialUsingSpouge(int a, MathContext mc) {
		return pow(BigDecimal.valueOf(a), BigDecimal.valueOf(-0.5), mc).multiply(pow(TWO.multiply(pi(mc), mc), BigDecimal.valueOf(-a-0.5), mc), mc);
	}
```

![Precision of Spouge's approximation](factorial_spouge_precision.png)

While testing this I found a bug in 
`log(new BigDecimal("6.8085176335035800378E-325"))`.
Fixed it before it could run away.


### Caching Spouge's coefficients (depending on precision)

The coefficients depend only on the value of `a` and therefore from the desired precision.
  
We can cache the coefficients for every precision:
 
```java
	private static Map<Integer, List<BigDecimal>> spougeFactorialConstantsCache = new ConcurrentHashMap<>();

	private static List<BigDecimal> getSpougeFactorialConstants(int a) {
		return spougeFactorialConstantsCache.computeIfAbsent(a, key -> {
			List<BigDecimal> constants = new ArrayList<>(a);
			MathContext mc = new MathContext(a * 15/10);

			BigDecimal c0 = sqrt(pi(mc).multiply(TWO, mc), mc);
			constants.add(c0);

			boolean negative = false;
			BigDecimal factor = c0;
			for (int k = 1; k < a; k++) {
				BigDecimal bigK = BigDecimal.valueOf(k);
				BigDecimal ck = pow(BigDecimal.valueOf(a-k), bigK.subtract(BigDecimal.valueOf(0.5), mc), mc);
				ck = ck.multiply(exp(BigDecimal.valueOf(a-k), mc), mc);
				ck = ck.divide(factorial(k - 1), mc);
				if (negative) {
					ck = ck.negate();
				}
				constants.add(ck);

				negative = !negative;
			}

			return constants;
		});
	}
```

Calculating the coefficients becomes quite expensive with higher precision.

![Time calculating Spouge's coefficients](factorial_calculating_spouge_constants.png)

### Spouge's approximation with pre-calculated constants

Now that we have the coefficients for a specific value of `a` we can implement the factorial method:
 
```java
	public static BigDecimal factorialUsingSpougeCached(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Spouge%27s_approximation
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());

		int a = mathContext.getPrecision() * 13 / 10;
		List<BigDecimal> constants = getSpougeFactorialConstants(a);

		BigDecimal bigA = BigDecimal.valueOf(a);

		boolean negative = false;
		BigDecimal factor = constants.get(0);
		for (int k = 1; k < a; k++) {
			BigDecimal bigK = BigDecimal.valueOf(k);
			factor = factor.add(constants.get(k).divide(x.add(bigK), mc), mc);
			negative = !negative;
		}

		BigDecimal result = pow(x.add(bigA, mc), x.add(BigDecimal.valueOf(0.5), mc), mc);
		result = result.multiply(exp(x.negate().subtract(bigA, mc), mc), mc);
		result = result.multiply(factor, mc);

		return result.round(mathContext);
	}
```

![Time calculating Spouge's approximation with precalculated coefficients to various precisions](factorial_spouge_cached_precisions.png)


![Time calculating Spouge's approximation with precalculated coefficients to precision of 200 digits](factorial_spouge_prec200.png)
