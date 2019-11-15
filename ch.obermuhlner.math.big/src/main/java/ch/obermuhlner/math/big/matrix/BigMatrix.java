package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.matrix.Coord.coord;
import static ch.obermuhlner.math.big.matrix.CoordValue.coordValue;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

public interface BigMatrix {
    int rows();
    int columns();

    BigDecimal get(int row, int column);

    default int size() {
        return rows() * columns();
    }

    default ImmutableBigMatrix round(MathContext mathContext) {
        return ImmutableBigMatrix.matrix(rows(), columns(), (row, column) -> get(row, column).round(mathContext).stripTrailingZeros());
    }

    default ImmutableBigMatrix add(BigMatrix other) {
        return add(other, null);
    }
    default ImmutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        return ImmutableBigMatrix.matrix(lazyAdd(other, mathContext));
    }

    default ImmutableBigMatrix subtract(BigMatrix other) {
        return subtract(other, null);
    }
    default ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        return ImmutableBigMatrix.matrix(lazySubtract(other, mathContext));
    }

    default ImmutableBigMatrix multiply(BigDecimal value) {
        return multiply(value, null);
    }
    default ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        return ImmutableBigMatrix.matrix(lazyMultiply(value, mathContext));
    }

    ImmutableBigMatrix multiply(BigMatrix other);
    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    default ImmutableBigMatrix transpose() {
        return ImmutableBigMatrix.matrix(lazyTranspose());
    }

    default ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.matrix(lazySubMatrix(startRow, startColumn, rows, columns));
    }

    default ImmutableBigMatrix minor(int skipRow, int skipColumn) {
        return ImmutableBigMatrix.matrix(lazyMinor(skipRow, skipColumn));
    }

    default ImmutableBigMatrix invert(MathContext mathContext) {
        MatrixUtils.checkSquare(this);

        MutableBigMatrix work = MutableBigMatrix.sparseMatrix(rows(), columns() * 2);
        work.set(0, 0, this);
        work.set(0, columns(), ImmutableBigMatrix.identityMatrix(rows()));

        work.gaussianElimination(true, mathContext);

        return work.subMatrix(0, columns(), rows(), columns());
    }

    default BigDecimal sum() {
        return sum(null);
    }

    default BigDecimal sum(MathContext mathContext) {
        BigDecimal result = ZERO;
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                result = MatrixUtils.add(result, get(row, col), mathContext);
            }
        }
        return result;
    }

    default BigDecimal product() {
        return product(null);
    }


    default BigDecimal product(MathContext mathContext) {
        BigDecimal result = ONE;
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                result = MatrixUtils.multiply(result, get(row, col), mathContext);
            }
        }
        return result;
    }

    default BigDecimal determinant() {
        MatrixUtils.checkSquare(this);

        int n = columns();
        switch (n) {
            case 0:
                return ONE;
            case 1:
                return get(0, 0);
            case 2:
                return get(0, 0).multiply(get(1, 1))
                        .subtract(get(0, 1).multiply(get(1, 0))).stripTrailingZeros();
            case 3:
                return get(0, 0).multiply(get(1, 1)).multiply(get(2, 2))
                        .add(get(0, 1).multiply(get(1, 2)).multiply(get(2, 0)))
                        .add(get(0, 2).multiply(get(1, 0)).multiply(get(2, 1)))
                        .subtract(get(0, 2).multiply(get(1, 1)).multiply(get(2, 0)))
                        .subtract(get(0, 1).multiply(get(1, 0)).multiply(get(2, 2)))
                        .subtract(get(0, 0).multiply(get(1, 2)).multiply(get(2, 1))).stripTrailingZeros();
        }

        BigDecimal result = ZERO;
        boolean sign = true;
        for (int i = 0; i < columns(); i++) {
            BigDecimal term = get(0, i).multiply(lazyMinor(0, i).determinant());
            if (sign) {
                result = result.add(term);
            } else {
                result = result.subtract(term);
            }
            sign = !sign;
        }
        return result.stripTrailingZeros();
    }

    default ImmutableBigMatrix lazyAdd(BigMatrix other) {
        return lazyAdd(other, null);
    }
    default ImmutableBigMatrix lazyAdd(BigMatrix other, MathContext mathContext) {
        MatrixUtils.checkSameSize(this, other);
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> MatrixUtils.add(get(row, column), other.get(row, column), mathContext).stripTrailingZeros());
    }

    default ImmutableBigMatrix lazySubtract(BigMatrix other) {
        return lazySubtract(other, null);
    }
    default ImmutableBigMatrix lazySubtract(BigMatrix other, MathContext mathContext) {
        MatrixUtils.checkSameSize(this, other);
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> MatrixUtils.subtract(get(row, column), other.get(row, column), mathContext).stripTrailingZeros());
    }

    default ImmutableBigMatrix lazyMultiply(BigDecimal value) {
        return lazyMultiply(value, null);
    }
    default ImmutableBigMatrix lazyMultiply(BigDecimal value, MathContext mathContext) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> MatrixUtils.multiply(get(row, column), value, mathContext).stripTrailingZeros());
    }

    default ImmutableBigMatrix lazyTranspose() {
        return ImmutableBigMatrix.lambdaMatrix(columns(), rows(),
                (row, column) -> get(column, row));
    }

    default ImmutableBigMatrix lazySubMatrix(int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.lambdaMatrix(rows, columns,
                (row, column) -> get(row + startRow, column + startColumn));
    }

    default ImmutableBigMatrix lazyMinor(int skipRow, int skipColumn) {
        return ImmutableBigMatrix.lambdaMatrix(rows() - 1, columns() - 1,
                (row, column) ->
                        get(row < skipRow ? row : row + 1,
                                column < skipColumn ? column : column + 1));
    }

    default Stream<Coord> getCoords() {
        return IntStream.range(0, rows() * columns())
                .mapToObj(i -> coord(i / columns(), i % columns()))
                .filter(c -> get(c.row, c.column).signum() != 0);
    }

    default Stream<CoordValue> getCoordValues() {
        return getCoords()
                .map(c -> coordValue(c, get(c.row, c.column)));
    }

    default ImmutableBigMatrix asImmutableMatrix() {
        if (this instanceof ImmutableBigMatrix) {
            return (ImmutableBigMatrix) this;
        }
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default BigDecimal[] toBigDecimalArray() {
        BigDecimal[] result = new BigDecimal[rows() * columns()];
        int i = 0;
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                result[i++] = get(row, column);
            }
        }
        return result;
    }

    default BigDecimal[][] toBigDecimalNestedArray() {
        BigDecimal[][] result = new BigDecimal[rows()][];
        for (int row = 0; row < rows(); row++) {
            BigDecimal[] currentRow = new BigDecimal[columns()];
            result[row] = currentRow;
            for (int column = 0; column < columns(); column++) {
                currentRow[column] = get(row, column);
            }
        }
        return result;
    }

    default double[] toDoubleArray() {
        double[] result = new double[rows() * columns()];
        int i = 0;
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                result[i++] = get(row, column).doubleValue();
            }
        }
        return result;
    }

    default double[][] toDoubleNestedArray() {
        double[][] result = new double[rows()][];
        for (int row = 0; row < rows(); row++) {
            double[] currentRow = new double[columns()];
            result[row] = currentRow;
            for (int column = 0; column < columns(); column++) {
                currentRow[column] = get(row, column).doubleValue();
            }
        }
        return result;
    }

    default Map<Integer, Map<Integer, BigDecimal>> toSparseNestedMap() {
        Map<Integer, Map<Integer, BigDecimal>> result = new HashMap<>();
        getCoordValues().forEach(cv -> {
            result.computeIfAbsent(cv.coord.row, HashMap::new).put(cv.coord.column, cv.value);
        });
        return result;
    }

    default Map<Integer, Map<Integer, BigDecimal>> toTransposedSparseNestedMap() {
        Map<Integer, Map<Integer, BigDecimal>> result = new HashMap<>();
        getCoordValues().forEach(cv -> {
            result.computeIfAbsent(cv.coord.column, HashMap::new).put(cv.coord.row, cv.value);
        });
        return result;
    }
}
