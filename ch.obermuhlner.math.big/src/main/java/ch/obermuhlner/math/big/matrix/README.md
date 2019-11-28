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
- `lazyMatrix()` - does not store the matrix elements but
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
    static ImmutableBigMatrix lazyMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction);
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
    ImmutableBigMatrix asImmutableMatrix();
    BigDecimal[] toBigDecimalArray();
    BigDecimal[][] toBigDecimalNestedArray();
    double[] toDoubleArray();
    double[][] toDoubleNestedArray();
```

The mathematical operations in `BigMatrix` have heuristics to call the optimal operation in `ImmutableOperations`.
 

## `MutableBigMatrix` operations

Operations in `MutableBigMatrix`:
```
    void set(int row, int column, BigDecimal value);
    void set(int row, int column, double value);
    void set(int row, int column, BigMatrix matrix);
    void fill(BigDecimal value);
    void clear();
    void insertRow(int row, double... values);
    void insertRow(int row, BigDecimal... values);
    void appendRow(double... values);
    void appendRow(BigDecimal... values);
    void removeRow(int row);
    void insertColumn(int column, double... values);
    void insertColumn(int column, BigDecimal... values);
    void appendColumn(double... values);
    void appendColumn(BigDecimal... values);
    void removeColumn(int column);
    void swapRows(int row1, int row2);
    void swapColumns(int column1, int column2);
    void gaussianElimination(boolean reducedEchelonForm, MathContext mathContext);
```  

## External immutable operations

The class `ImmutableOperations` provides external specialized operations:
```
    public static ImmutableBigMatrix denseAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix denseSubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix denseMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
    public static ImmutableBigMatrix denseMultiply(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix denseElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
    public static ImmutableBigMatrix denseTranspose(BigMatrix matrix) {
    public static ImmutableBigMatrix denseRound(BigMatrix matrix, MathContext mathContext) {
    public static BigDecimal denseSum(BigMatrix matrix, MathContext mathContext) {
    public static BigDecimal denseProduct(BigMatrix matrix, MathContext mathContext) {
    public static boolean denseEquals(BigMatrix left, BigMatrix right) {

    public static ImmutableBigMatrix sparseAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix sparseSubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix sparseMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
    public static ImmutableBigMatrix sparseMultiply(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix sparseElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
    public static ImmutableBigMatrix sparseTranspose(BigMatrix matrix) {
    public static ImmutableBigMatrix sparseRound(BigMatrix matrix, MathContext mathContext) {
    public static BigDecimal sparseSum(BigMatrix matrix, MathContext mathContext) {
    public static BigDecimal sparseProduct(BigMatrix matrix, MathContext mathContext) {
    public static boolean sparseEquals(BigMatrix left, BigMatrix right) {

    public static ImmutableBigMatrix lazyAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix lazySubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
    public static ImmutableBigMatrix lazyMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
    public static ImmutableBigMatrix lazyElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
    public static ImmutableBigMatrix lazyTranspose(BigMatrix matrix) {
    public static ImmutableBigMatrix lazySubMatrix(BigMatrix matrix, int startRow, int startColumn, int rows, int columns) {
    public static ImmutableBigMatrix lazyMinor(BigMatrix matrix, int skipRow, int skipColumn) {
    public static ImmutableBigMatrix lazyRound(BigMatrix matrix, MathContext mathContext) {
```

The `dense` operations are straightforward implementations optimized for dense matrices where all elements in the matrix have values.

The `sparse` operations are optimized for sparse matrices where many elements have the same value (typically 0).

The `lazy` operations defer the actual calculation of the operation to later.
This has the advantage of reducing the memory consumption by not creating concrete matrices for every operationn.

Beware that lazy operations can become inefficient when the last operation after a chain of lazy operations is not O(n).
For example the matrix multiplication is worst case O(n^3) which will lead to the same lazy element being recalculated multiple times.
In such a case it is recommended to copy the result of a chain of lazy operations into a new matrix instance by using the 
appropriate `ImmutableBigMatrix` constructor method, for example `ImmutableBigMatrix.matrix()`. 
 

