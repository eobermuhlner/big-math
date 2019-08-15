# API changes

## Support for `NaN`, `POSITIVE_INFINITY`, `NEGATIVE_INFINITY` in `BigFloat`

The `BigFloat` class supports now the same special values as `double`:
* `NaN` for values that cannot be represented a number
* `POSITIVE_INFINITY` to represent positive infinite values
* `NEGATIVE_INFINITY` to represent negative infinite values


# Bugfixes

No Bugfix changes.


# Enhancements

## Performance optimizations

Performance analysis of the basic operations on `BigDecimal` has shown
that the `add()` and `subtract()` operation are faster without
specifying a `MathContext` argument. The `multiply()` operation has a
more complex performance behaviour. A single multiplication tends to be
faster without `MathContext` argument but the following operations are
at risk to be slower since it accumulates precision.

Many operations where manually fine tuned to find the optimum
performance.

The following chart shows performance improvements for the involved
operations:

![Performance improvements MathContext optimization](https://raw.githubusercontent.com/eobermuhlner/big-math/master/ch.obermuhlner.math.big.example/docu/benchmarks/regression/before%20after%20fewer%20mc%20optimization.png)



# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
