package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ch.obermuhlner.math.big.matrix.Coord.coord;
import static java.math.BigDecimal.*;

public class ImmutableOperations {

    public static ImmutableBigMatrix autoAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(left, 100, 0.98) || MatrixUtils.preferSparseMatrix(right, 100, 0.98)) {
            return ImmutableOperations.sparseAdd(left, right, mathContext);
        }
        return ImmutableOperations.denseAdd(left, right, mathContext);
    }

    public static ImmutableBigMatrix autoSubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(left, 100, 0.98) || MatrixUtils.preferSparseMatrix(right, 100, 0.98)) {
            return ImmutableOperations.sparseSubtract(left, right, mathContext);
        }
        return ImmutableOperations.denseSubtract(left, right, mathContext);
    }

    public static ImmutableBigMatrix autoMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(left, 10, 0.01)) {
            return ImmutableOperations.sparseMultiply(left, right, mathContext);
        }
        return ImmutableOperations.denseMultiply(left, right, mathContext);
    }

    public static ImmutableBigMatrix autoMultiply(BigMatrix left, BigMatrix right, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(left, 10, 0.01) || MatrixUtils.preferSparseMatrix(right, 10, 0.01)) {
            return ImmutableOperations.sparseMultiply(left, right, mathContext);
        }
        return ImmutableOperations.denseMultiply(left, right, mathContext);
    }

    public static ImmutableBigMatrix autoElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
        if (MatrixUtils.preferSparseMatrix(matrix, 10, 0.01)) {
            return ImmutableOperations.sparseElementOperation(matrix, operation);
        }
        return ImmutableOperations.denseElementOperation(matrix, operation);
    }

    public static ImmutableBigMatrix autoTranspose(BigMatrix matrix) {
        return lazyTranspose(matrix);
    }

    public static ImmutableBigMatrix autoSubMatrix(BigMatrix matrix, int startRow, int startColumn, int rows, int columns) {
        return lazySubMatrix(matrix, startRow, startColumn, rows, columns);
    }

    public static ImmutableBigMatrix autoMinor(BigMatrix matrix, int skipRow, int skipColumn) {
        return lazyMinor(matrix, skipRow, skipColumn);
    }

    public static BigDecimal autoSum(BigMatrix matrix, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(matrix, 10, 0.01)) {
            return ImmutableOperations.sparseSum(matrix, mathContext);
        }
        return ImmutableOperations.denseSum(matrix, mathContext);
    }

    public static BigDecimal autoProduct(BigMatrix matrix, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(matrix, 10, 0.01)) {
            return ImmutableOperations.sparseProduct(matrix, mathContext);
        }
        return ImmutableOperations.denseProduct(matrix, mathContext);
    }

    public static ImmutableBigMatrix autoRound(BigMatrix matrix, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(matrix, 100, 0.01)) {
            return ImmutableOperations.sparseRound(matrix, mathContext);
        }
        return ImmutableOperations.denseRound(matrix, mathContext);
    }

    public static boolean autoEquals(BigMatrix left, BigMatrix right) {
        if (MatrixUtils.preferSparseMatrix(left, 10, 0.01)) {
            return ImmutableOperations.sparseEquals(left, right);
        }
        return ImmutableOperations.denseEquals(left, right);
    }

    public static ImmutableBigMatrix denseAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);

        return ImmutableBigMatrix.matrix(lazyAdd(left, right, mathContext));
    }

    public static ImmutableBigMatrix denseSubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);

        return ImmutableBigMatrix.matrix(lazySubtract(left, right, mathContext));
    }

    public static ImmutableBigMatrix denseMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
        if (right.compareTo(ZERO) == 0) {
            return ImmutableBigMatrix.matrix(left.rows(), left.columns());
        }
        if (right.compareTo(ONE) == 0) {
            if (left instanceof ImmutableBigMatrix) {
                return (ImmutableBigMatrix) left;
            }
            return ImmutableBigMatrix.matrix(left);
        }

        return ImmutableBigMatrix.matrix(lazyMultiply(left, right, mathContext));
    }

    public static ImmutableBigMatrix denseMultiply(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkColumnsOtherRows(left, right);

        int rows = left.rows();
        int columns = right.columns();

        AbstractBigMatrix result;
        if (MatrixUtils.isSparseWithLotsOfZeroes(left) && MatrixUtils.isSparseWithLotsOfZeroes(right)) {
            result = new SparseImmutableBigMatrix(rows, columns);
        } else {
            result = new DenseImmutableBigMatrix(rows, columns);
        }

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < left.columns(); index++) {
                    BigDecimal value = MatrixUtils.multiply(left.get(row, index), right.get(index, column), mathContext);
                    sum = MatrixUtils.add(sum, value, mathContext);
                }
                result.internalSet(row, column, sum.stripTrailingZeros());
            }
        }

        return result.asImmutableMatrix();
    }

    public static ImmutableBigMatrix denseElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
        return ImmutableBigMatrix.matrix(lazyElementOperation(matrix, operation));
    }

    public static ImmutableBigMatrix denseTranspose(BigMatrix matrix) {
        return ImmutableBigMatrix.matrix(lazyTranspose(matrix));
    }

    public static ImmutableBigMatrix denseSubMatrix(BigMatrix matrix, int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.matrix(lazySubMatrix(matrix, startRow, startColumn, rows, columns));
    }

    public static ImmutableBigMatrix denseMinor(BigMatrix matrix, int skipRow, int skipColumn) {
        return ImmutableBigMatrix.matrix(lazyMinor(matrix, skipRow, skipColumn));
    }

    public static BigDecimal denseSum(BigMatrix matrix, MathContext mathContext) {
        BigDecimal result = ZERO;
        for (int row = 0; row < matrix.rows(); row++) {
            for (int col = 0; col < matrix.columns(); col++) {
                result = MatrixUtils.add(result, matrix.get(row, col), mathContext);
            }
        }
        return result;
    }

    public static BigDecimal denseProduct(BigMatrix matrix, MathContext mathContext) {
        BigDecimal result = ONE;
        for (int row = 0; row < matrix.rows(); row++) {
            for (int col = 0; col < matrix.columns(); col++) {
                result = MatrixUtils.multiply(result, matrix.get(row, col), mathContext);
            }
        }
        return result;
    }

    public static ImmutableBigMatrix denseRound(BigMatrix matrix, MathContext mathContext) {
        return ImmutableBigMatrix.matrix(lazyRound(matrix, mathContext));
    }

    public static boolean denseEquals(BigMatrix left, BigMatrix right) {
        if (left == right) return true;

        if (left.rows() != right.rows()) {
            return false;
        }
        if (left.columns() != right.columns()) {
            return false;
        }
        for (int row = 0; row < left.rows(); row++) {
            for (int column = 0; column < left.columns(); column++) {
                if (left.get(row, column).compareTo(right.get(row, column)) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean sparseEquals(BigMatrix left, BigMatrix right) {
        if (left == right) return true;

        if (left.rows() != right.rows()) {
            return false;
        }
        if (left.columns() != right.columns()) {
            return false;
        }

        if (left.sparseEmptyElementCount() != 0 && right.sparseEmptyElementCount() != 0 && left.getSparseDefaultValue().compareTo(right.getSparseDefaultValue()) != 0) {
            return false;
        }

        Set<Coord> mergedCoords = left.getSparseCoords().collect(Collectors.toSet());
        mergedCoords.addAll(right.getSparseCoords().collect(Collectors.toSet()));

        for (Coord coord : mergedCoords) {
            if (left.get(coord.row, coord.column).compareTo(right.get(coord.row, coord.column)) != 0) {
                return false;
            }
        }

        return true;
    }

    public static ImmutableBigMatrix sparseAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);

        BigDecimal defaultValue = MatrixUtils.add(left.getSparseDefaultValue(), right.getSparseDefaultValue(), mathContext);

        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(defaultValue, left.rows(), left.columns());

        Set<Coord> mergedCoords = left.getSparseCoords().collect(Collectors.toSet());
        mergedCoords.addAll(right.getSparseCoords().collect(Collectors.toSet()));

        for (Coord coord : mergedCoords) {
            BigDecimal value = MatrixUtils.add(left.get(coord.row, coord.column), right.get(coord.row, coord.column), mathContext);
            m.internalSet(coord.row, coord.column, value);
        }

        return m;
    }

    public static ImmutableBigMatrix sparseSubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);

        BigDecimal defaultValue = MatrixUtils.subtract(left.getSparseDefaultValue(), right.getSparseDefaultValue(), mathContext);

        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(defaultValue, left.rows(), left.columns());

        Set<Coord> mergedCoords = left.getSparseCoords().collect(Collectors.toSet());
        mergedCoords.addAll(right.getSparseCoords().collect(Collectors.toSet()));

        for (Coord coord : mergedCoords) {
            BigDecimal value = MatrixUtils.subtract(left.get(coord.row, coord.column), right.get(coord.row, coord.column), mathContext);
            m.internalSet(coord.row, coord.column, value);
        }

        return m;
    }

    public static ImmutableBigMatrix sparseMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
        if (right.compareTo(ZERO) == 0) {
            return ImmutableBigMatrix.matrix(left.rows(), left.columns());
        }
        if (right.compareTo(ONE) == 0) {
            if (left instanceof ImmutableBigMatrix) {
                return (ImmutableBigMatrix) left;
            }
            return ImmutableBigMatrix.matrix(left);
        }

        BigDecimal defaultValue = MatrixUtils.multiply(left.getSparseDefaultValue(), right, mathContext);

        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(defaultValue, left.rows(), left.columns());

        left.getSparseCoordValues().forEach(coordValue -> {
            BigDecimal value = MatrixUtils.multiply(left.get(coordValue.coord.row, coordValue.coord.column), right, mathContext);
            m.internalSet(coordValue.coord.row, coordValue.coord.column, value);
        });

        return m;
    }

    public static ImmutableBigMatrix sparseMultiply(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkColumnsOtherRows(left, right);

        Map<Integer, Map<Integer, BigDecimal>> leftByRowColumn = left.toSparseNestedMap();
        Map<Integer, Map<Integer, BigDecimal>> rightByColumnRow = right.toTransposedSparseNestedMap();

        int rows = left.rows();
        int columns = right.columns();

        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(rows, columns);

        for (Map.Entry<Integer, Map<Integer, BigDecimal>> leftRow : leftByRowColumn.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BigDecimal>> rightColumn : rightByColumnRow.entrySet()) {
                BigDecimal sum = BigDecimal.ZERO;
                Set<Integer> commonIndices = new HashSet<>(leftRow.getValue().keySet());
                commonIndices.retainAll(rightColumn.getValue().keySet());
                for (Integer index : commonIndices) {
                    BigDecimal v = MatrixUtils.multiply(leftRow.getValue().get(index), rightColumn.getValue().get(index), mathContext);
                    sum = MatrixUtils.add(sum, v, mathContext);
                }
                result.internalSet(leftRow.getKey(), rightColumn.getKey(), sum);
            }
        }

        return result;
    }

    public static ImmutableBigMatrix sparseElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
        BigDecimal defaultValue = operation.apply(matrix.getSparseDefaultValue());

        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(defaultValue, matrix.rows(), matrix.columns());

        matrix.getSparseCoordValues().forEach(cv -> {
            result.internalSet(cv.coord.row, cv.coord.column, operation.apply(cv.value));
        });

        return result;
    }

    public static ImmutableBigMatrix sparseTranspose(BigMatrix matrix) {
        BigDecimal defaultValue = matrix.getSparseDefaultValue();

        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(defaultValue, matrix.columns(), matrix.rows());

        matrix.getSparseCoordValues().forEach(cv -> {
            result.internalSet(cv.coord.column, cv.coord.row, cv.value);
        });

        return result;
    }

    public static BigDecimal sparseSum(BigMatrix matrix, MathContext mathContext) {
        BigDecimal common = MatrixUtils.multiply(valueOf(matrix.sparseEmptyElementCount()), matrix.getSparseDefaultValue(), mathContext);

        BigDecimal valuesSum = matrix.getSparseCoordValues()
                .map(cv -> cv.value)
                .reduce(ZERO, (b1, b2) -> MatrixUtils.add(b1, b2, mathContext));

        return MatrixUtils.add(common, valuesSum, mathContext);
    }

    public static BigDecimal sparseProduct(BigMatrix matrix, MathContext mathContext) {
        BigDecimal common;
        if (mathContext == null) {
            common = MatrixUtils.pow(matrix.getSparseDefaultValue(), matrix.sparseEmptyElementCount());
        } else {
            common = BigDecimalMath.pow(matrix.getSparseDefaultValue(), matrix.sparseEmptyElementCount(), mathContext);
        }

        if (common.signum() == 0) {
            return common;
        }

        BigDecimal valuesProduct = matrix.getSparseCoordValues()
                .map(cv -> cv.value)
                .reduce(ONE, (b1, b2) -> MatrixUtils.multiply(b1, b2, mathContext));

        return MatrixUtils.multiply(common, valuesProduct, mathContext);
    }

    public static ImmutableBigMatrix sparseRound(BigMatrix matrix, MathContext mathContext) {
        BigDecimal defaultValue = matrix.getSparseDefaultValue().round(mathContext).stripTrailingZeros();

        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(defaultValue, matrix.columns(), matrix.rows());

        matrix.getSparseCoordValues().forEach(cv -> {
            result.internalSet(cv.coord.row, cv.coord.column, cv.value.round(mathContext).stripTrailingZeros());
        });
        return result;
    }

    public static ImmutableBigMatrix lazyRound(BigMatrix matrix, MathContext mathContext) {
        return ImmutableBigMatrix.lazyMatrix(matrix.rows(), matrix.columns(),
                (row, column) -> matrix.get(row, column).round(mathContext).stripTrailingZeros());
    }

    public static ImmutableBigMatrix lazyAdd(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);
        return ImmutableBigMatrix.lazyMatrix(left.rows(), left.columns(), (row, column) -> {
            return MatrixUtils.add(left.get(row, column), right.get(row, column), mathContext).stripTrailingZeros();
        });
    }

    public static ImmutableBigMatrix lazySubtract(BigMatrix left, BigMatrix right, MathContext mathContext) {
        MatrixUtils.checkSameSize(left, right);
        return ImmutableBigMatrix.lazyMatrix(left.rows(), left.columns(), (row, column) -> {
            return MatrixUtils.subtract(left.get(row, column), right.get(row, column), mathContext).stripTrailingZeros();
        });
    }

    public static ImmutableBigMatrix lazyMultiply(BigMatrix left, BigDecimal right, MathContext mathContext) {
        return ImmutableBigMatrix.lazyMatrix(left.rows(), left.columns(), (row, column) -> {
            return MatrixUtils.multiply(left.get(row, column), right, mathContext).stripTrailingZeros();
        });
    }

    public static ImmutableBigMatrix lazyElementOperation(BigMatrix matrix, Function<BigDecimal, BigDecimal> operation) {
        return ImmutableBigMatrix.lazyMatrix(matrix.rows(), matrix.columns(),
                (row, column) -> operation.apply(matrix.get(row, column)));
    }

    public static ImmutableBigMatrix lazyTranspose(BigMatrix matrix) {
        return ImmutableBigMatrix.lazyMatrix(matrix, matrix.columns(), matrix.rows(),
                c -> coord(c.column, c.row));
    }

    public static ImmutableBigMatrix lazySubMatrix(BigMatrix matrix, int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.lazyMatrix(matrix, rows, columns,
                c -> coord(c.row + startRow, c.column + startColumn));
    }

    public static ImmutableBigMatrix lazyMinor(BigMatrix matrix, int skipRow, int skipColumn) {
        return ImmutableBigMatrix.lazyMatrix(matrix,matrix.rows() - 1, matrix.columns() - 1,
                c -> coord(c.row < skipRow ? c.row : c.row + 1,
                        c.column < skipColumn ? c.column : c.column + 1));
    }
}
