package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
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

    ImmutableBigMatrix round(MathContext mathContext);

    default ImmutableBigMatrix add(BigMatrix other) {
        return add(other, null);
    }
    ImmutableBigMatrix add(BigMatrix other, MathContext mathContext);

    default ImmutableBigMatrix subtract(BigMatrix other) {
        return subtract(other, null);
    }
    ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext);

    default ImmutableBigMatrix multiply(BigDecimal value) {
        return multiply(value, null);
    }
    ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext);

    default ImmutableBigMatrix multiply(BigMatrix other) {
        return multiply(other, null);
    }
    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    ImmutableBigMatrix elementOperation(Function<BigDecimal, BigDecimal> operation);

    ImmutableBigMatrix transpose();

    default ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.matrix(ImmutableOperations.lazySubMatrix(this, startRow, startColumn, rows, columns));
    }

    default ImmutableBigMatrix minor(int skipRow, int skipColumn) {
        return ImmutableBigMatrix.matrix(ImmutableOperations.lazyMinor(this, skipRow, skipColumn));
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
    BigDecimal sum(MathContext mathContext);

    default BigDecimal product() {
        return product(null);
    }
    BigDecimal product(MathContext mathContext);

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
            BigDecimal term = get(0, i).multiply(ImmutableOperations.lazyMinor(this, 0, i).determinant());
            if (sign) {
                result = result.add(term);
            } else {
                result = result.subtract(term);
            }
            sign = !sign;
        }
        return result.stripTrailingZeros();
    }

    default Stream<Coord> getSparseCoords() {
        return IntStream.range(0, rows() * columns())
                .mapToObj(i -> coord(i / columns(), i % columns()))
                .filter(c -> get(c.row, c.column).signum() != 0);
    }

    default Stream<CoordValue> getSparseCoordValues() {
        return getSparseCoords()
                .map(c -> coordValue(c, get(c.row, c.column)));
    }

    default boolean isSparse() {
        return false;
    }

    default BigDecimal getSparseDefaultValue() {
        return ZERO;
    }

    default int sparseFilledSize() {
        return size();
    }

    default int sparseEmptySize() {
        return size() - sparseFilledSize();
    }

    default double sparseEmptyRatio() {
        if (size() == 0) {
            return 0.0;
        }

        return (double) sparseEmptySize() / size();
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
        getSparseCoordValues().forEach(cv -> {
            result.computeIfAbsent(cv.coord.row, HashMap::new).put(cv.coord.column, cv.value);
        });
        return result;
    }

    default Map<Integer, Map<Integer, BigDecimal>> toTransposedSparseNestedMap() {
        Map<Integer, Map<Integer, BigDecimal>> result = new HashMap<>();
        getSparseCoordValues().forEach(cv -> {
            result.computeIfAbsent(cv.coord.column, HashMap::new).put(cv.coord.row, cv.value);
        });
        return result;
    }
}
