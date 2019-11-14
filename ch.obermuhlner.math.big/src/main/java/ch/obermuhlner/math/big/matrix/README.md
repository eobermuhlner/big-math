# Matrix calculations in BigDecimal

## Introduction

The `matrix` package provides `BigDecimal` based operations on matrices.

The central interface is the `ImmutableBigMatrix`.
It follows the same design guidelines as the `BigDecimal` class itself.
Every instance is immutable and operations return a new immutable
instance with the result.

Using `ImmutableBigMatrix` is easy and safe.

The `MutableBigMatrix` on the other hand allows operations
to modify the instance, for example setting values and swapping rows.
This allows to reduce the number of matrix copies.

The `BigMatrix` is the common interface of `ImmutableBigMatrix` and `MutableBigMatrix`.
The operations in this interface accept any `BigMatrix` (mutable or immutable) as argument
but return an `ImmutableBigMatrix`.
This allows to mix mutable and immutable matrices to implement efficient solutions.

The static constructor methods in `ImmutableBigMatrix` and `MutableBigMatrix`
create matrices that store the elements in different ways.
 
- `denseMatrix()` - stores all matrix elements.
- `sparseMatrix()` - optimized for matrices where most elements
  have the same value (default is 0).
  Only elements that are different from the common value are stored.
- `lambdaMatrix()` - does not store the matrix elements but
  recalculates all elements on the fly.

- `matrix()` - automatically picks the best storage type (dense or sparse).

All operations defined in `BigMatrix` work on all storage types of matrices
but some operations might choose optimized implementations for sparse matrices.


## `ImmutableBigMatrix` constructor methods

The constructor methods in `ImmutableBigMatrix`:
```
    static ImmutableBigMatrix matrix(BigMatrix matrix);
    static ImmutableBigMatrix matrix(int rows, int columns);
    static ImmutableBigMatrix matrix(int rows, int columns, double... values);
    static ImmutableBigMatrix matrix(int rows, int columns, BigDecimal... values);
    static ImmutableBigMatrix matrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static ImmutableBigMatrix denseMatrix(BigMatrix matrix);
    static ImmutableBigMatrix denseMatrix(int rows, int columns);
    static ImmutableBigMatrix denseMatrix(int rows, int columns, double... values);
    static ImmutableBigMatrix denseMatrix(int rows, int columns, BigDecimal... values);
    static ImmutableBigMatrix denseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static ImmutableBigMatrix sparseMatrix(BigMatrix matrix);
    static ImmutableBigMatrix sparseMatrix(int rows, int columns);
    static ImmutableBigMatrix sparseMatrix(int rows, int columns, double... values);
    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BigDecimal... values);
    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static ImmutableBigMatrix identityMatrix(int size);
    static ImmutableBigMatrix lambdaMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
```

## `MutableBigMatrix` constructor methods

The constructor methods in `MutableBigMatrix`:
```
    static MutableBigMatrix matrix(BigMatrix matrix);
    static MutableBigMatrix matrix(int rows, int columns);
    static MutableBigMatrix matrix(int rows, int columns, double... values);
    static MutableBigMatrix matrix(int rows, int columns, BigDecimal... values);
    static MutableBigMatrix matrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static MutableBigMatrix denseMatrix(BigMatrix matrix);
    static MutableBigMatrix denseMatrix(int rows, int columns);
    static MutableBigMatrix denseMatrix(int rows, int columns, double... values);
    static MutableBigMatrix denseMatrix(int rows, int columns, BigDecimal... values);
    static MutableBigMatrix denseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static MutableBigMatrix denseIdentityMatrix(int size);
    static MutableBigMatrix sparseMatrix(BigMatrix matrix);
    static MutableBigMatrix sparseMatrix(int rows, int columns);
    static MutableBigMatrix sparseMatrix(int rows, int columns, double... values);
    static MutableBigMatrix sparseMatrix(int rows, int columns, BigDecimal... values);
    static MutableBigMatrix sparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
    static MutableBigMatrix sparseIdentityMatrix(int size);
    static MutableBigMatrix identityMatrix(int size);
```


## `BigMatrix` operations

Operations in `BigMatrix`:
```
    int rows();
    int columns();
    BigDecimal get(int row, int column);
    int size();
    ImmutableBigMatrix round(MathContext mathContext);
    ImmutableBigMatrix add(BigMatrix other);
    ImmutableBigMatrix add(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix subtract(BigMatrix other);
    ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix multiply(BigDecimal value);
    ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext);
    ImmutableBigMatrix multiply(BigMatrix other);
    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix transpose();
    ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns);
    ImmutableBigMatrix minor(int skipRow, int skipColumn);
    ImmutableBigMatrix invert(MathContext mathContext);
    BigDecimal sum();
    BigDecimal sum(MathContext mathContext);
    BigDecimal product();
    BigDecimal product(MathContext mathContext);
    BigDecimal determinant();
    ImmutableBigMatrix lazyAdd(BigMatrix other);
    ImmutableBigMatrix lazyAdd(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix lazySubtract(BigMatrix other);
    ImmutableBigMatrix lazySubtract(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix lazyMultiply(BigDecimal value);
    ImmutableBigMatrix lazyMultiply(BigDecimal value, MathContext mathContext);
    ImmutableBigMatrix lazyTranspose();
    ImmutableBigMatrix lazySubMatrix(int startRow, int startColumn, int rows, int columns);
    ImmutableBigMatrix lazyMinor(int skipRow, int skipColumn);
    ImmutableBigMatrix asImmutableMatrix();
    BigDecimal[] toBigDecimalArray();
    BigDecimal[][] toBigDecimalNestedArray();
    double[] toDoubleArray();
    double[][] toDoubleNestedArray();
```

The methods prefixed `lazy` are special.
They return the result of the calculation as a lambda matrix that will be calculated later on-the-fly.
This saves memory, since the result matrix is not created as an actual instance.


## `MutableBigMatrix` operations

Operations in `MutableBigMatrix`:
```
    void set(int row, int column, BigDecimal value);
    void set(int row, int column, double value);
    void set(int row, int column, BigMatrix matrix);
    void fill(BigDecimal value);
    void clear();
    void gaussianElimination(boolean reducedEchelonForm, MathContext mathContext);
```  
