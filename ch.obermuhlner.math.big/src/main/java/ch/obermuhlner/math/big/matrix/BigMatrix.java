package ch.obermuhlner.math.big.matrix;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.*;

import static java.math.BigDecimal.ONE;

public interface BigMatrix {
    int rows();
    int columns();

    BigDecimal get(int row, int column);

    default ImmutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).add(other.get(row, column), mathContext));
    }
    default ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).subtract(other.get(row, column), mathContext));
    }
    default ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).multiply(value, mathContext));
    }

    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    default ImmutableBigMatrix add(BigMatrix other) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).add(other.get(row, column)));
    }
    default BigMatrix subtract(BigMatrix other) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).subtract(other.get(row, column)));
    }
    default BigMatrix multiply(BigDecimal value) {
        return ImmutableBigMatrix.lambdaMatrix(rows(), columns(), (row, column) -> get(row, column).multiply(value));
    }

    ImmutableBigMatrix multiply(BigMatrix other);

    default ImmutableBigMatrix transpose() {
        return ImmutableBigMatrix.lambdaMatrix(columns(), rows(),
                (row, column) -> get(column, row));
    }

    default ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return ImmutableBigMatrix.lambdaMatrix(rows, columns,
                (row, column) -> get(row + startRow, column + startColumn));
    }

    default ImmutableBigMatrix minor(int skipRow, int skipColumn) {
        return ImmutableBigMatrix.lambdaMatrix(rows() - 1, columns() - 1,
                (row, column) ->
                        get(row < skipRow ? row : row + 1,
                                column < skipColumn ? column : column + 1));
    }

    default ImmutableBigMatrix invert(MathContext mathContext) {
        return toMutableMatrix().invert(mathContext);
    }

    default BigDecimal sum() {
        BigDecimal result = ZERO;
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                result = result.add(get(row, col));
            }
        }
        return result;
    }

    default BigDecimal product() {
        BigDecimal result = ONE;
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                result = result.multiply(get(row, col));
            }
        }
        return result;
    }

    default BigDecimal determinant() {
        if (columns() != rows()) {
            throw new ArithmeticException("columns " + columns() + " != rows " + rows());
        }

        int n = columns();
        switch (n) {
            case 0:
                return ONE;
            case 1:
                return get(0, 0);
            case 2:
                return get(0, 0).multiply(get(1, 1))
                        .subtract(get(0, 1).multiply(get(1, 0)));
            case 3:
                return get(0, 0).multiply(get(1, 1)).multiply(get(2, 2))
                        .add(get(0, 1).multiply(get(1, 2)).multiply(get(2, 0)))
                        .add(get(0, 2).multiply(get(1, 0)).multiply(get(2, 1)))
                        .subtract(get(0, 2).multiply(get(1, 1)).multiply(get(2, 0)))
                        .subtract(get(0, 1).multiply(get(1, 0)).multiply(get(2, 2)))
                        .subtract(get(0, 0).multiply(get(1, 2)).multiply(get(2, 1)));
        }

        BigDecimal result = ZERO;
        boolean sign = true;
        for (int i = 0; i < columns(); i++) {
            BigDecimal term = get(0, i).multiply(asImmutableMatrix().minor(0, i).determinant());
            if (sign) {
                result = result.add(term);
            } else {
                result = result.subtract(term);
            }
            sign = !sign;
        }
        return result;
    }

    default ImmutableBigMatrix toImmutableMatrix() {
        return ImmutableBigMatrix.matrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default ImmutableBigMatrix toImmutableDenseMatrix() {
        return ImmutableBigMatrix.denseMatrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default ImmutableBigMatrix toImmutableSparseMatrix() {
        return ImmutableBigMatrix.sparseMatrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default MutableBigMatrix toMutableMatrix() {
        return MutableBigMatrix.matrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default MutableBigMatrix toMutableDenseMatrix() {
        return MutableBigMatrix.denseMatrix(rows(), columns(), (row, column) -> get(row, column));
    }

    default MutableBigMatrix toMutableSparseMatrix() {
        return MutableBigMatrix.sparseMatrix(rows(), columns(), (row, column) -> get(row, column));
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
}
