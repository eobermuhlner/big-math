package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.lazy.LazyImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.lazy.LazyTransformationImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.matrix.Coord.coord;

public interface ImmutableBigMatrix extends BigMatrix {

    static ImmutableBigMatrix matrix(BigMatrix matrix) {
        if (MatrixUtils.preferSparseMatrix(matrix, 1000, 0.5)) {
            return sparseMatrix(matrix);
        } else {
            return denseMatrix(matrix);
        }
    }

    static ImmutableBigMatrix matrix(int rows, int columns) {
        return matrix(rows, columns, new BigDecimal[0]);
    }

    static ImmutableBigMatrix matrix(int rows, int columns, double... values) {
        return matrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix matrix(int rows, int columns, BigDecimal... values) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, values)) {
            return sparseMatrix(rows, columns, values);
        } else {
            return denseMatrix(rows, columns, values);
        }
    }

    static ImmutableBigMatrix matrix(int rows, int columns, CoordValue... values) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, values)) {
            return sparseMatrix(rows, columns, values);
        } else {
            return denseMatrix(rows, columns, values);
        }
    }

    static ImmutableBigMatrix matrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, valueFunction)) {
            return sparseMatrix(rows, columns, valueFunction);
        } else {
            return denseMatrix(rows, columns, valueFunction);
        }
    }

    static ImmutableBigMatrix denseMatrix(BigMatrix matrix) {
        return denseMatrix(matrix.rows(), matrix.columns(), (row, column) -> matrix.get(row, column));
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns) {
        return denseMatrix(rows, columns, new BigDecimal[0]);
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, double... values) {
        return denseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, BigDecimal... values) {
        return new DenseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, CoordValue... values) {
        return new DenseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new DenseImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix identityMatrix(int size) {
        return new LazyImmutableBigMatrix(size, size, (row, column) -> {
            return row == column ? BigDecimal.ONE : BigDecimal.ZERO;
        }) {
            @Override
            public int sparseFilledElementCount() {
                return rows();
            }

            @Override
            public Stream<Coord> getSparseCoords() {
                return IntStream.range(0, rows())
                        .mapToObj(i -> coord(i, i));
            }
        };
    }

    static ImmutableBigMatrix sparseMatrix(BigMatrix matrix) {
        return sparseMatrix(matrix.rows(), matrix.columns(), (row, column) -> matrix.get(row, column));
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns) {
        return sparseMatrix(rows, columns, new BigDecimal[0]);
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, double... values) {
        return sparseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BigDecimal... values) {
        return new SparseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, CoordValue... values) {
        return new SparseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new SparseImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix lazyMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new LazyImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix lazyMatrix(BigMatrix matrix, int rows, int columns, Function<Coord, Coord> transformationFunction) {
        return new LazyTransformationImmutableBigMatrix(matrix, rows, columns, transformationFunction);
    }
}
