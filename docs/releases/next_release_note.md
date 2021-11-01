# API changes

## `BigRational` implements now `Serializable`

The `BigRational` class implements now the `Serializable` interface and can be serialized
with the standard Java approach using `ObjectInputStream` and deserialized using `ObjectOutputStream`.

## `BigRational` extends now `Number`

The `BigRational` class extends now from `Number` and provides the following standard methods:
- `int intValue()`
- `long longValue()`
- `float floatValue()`
- `double doubleValue()`


# Bugfixes

## `BigRational.toFloat()` and `BigRational.toDouble()` with large nominators/denominators

The methods `BigRational.toFloat()` and `BigRational.toDouble()` failed to convert large nominators/denominators
into valid `float`, respectively `double` numbers.

For example:
```java
BigRational x = BigRational.valueOf("8.804462619980757911125181749462772084351");
System.out.println("rational : " + x.toRationalString());
System.out.println("float    : " + x.toFloat());
```

would print:
```
rational : 8804462619980757911125181749462772084351/1000000000000000000000000000000000000000
float    : NaN
```

After the fix this example would print:
```
rational : 8804462619980757911125181749462772084351/1000000000000000000000000000000000000000
float    : 8.804462
```


# Enhancements

No enhancements.


# Examples

Note: The example code is available on github, but not part of the big-math library.

No changes in the examples.
