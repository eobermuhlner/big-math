package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractBigMatrix implements BigMatrix {

    protected abstract AbstractBigMatrix createBigMatrix(int rows, int columns);

    protected abstract void internalSet(int row, int column, BigDecimal value);

    protected BigMatrix add(BigMatrix other, MathContext mathContext) {
        AbstractBigMatrix result = createBigMatrix(rows(), columns());

        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                result.internalSet(row, column, get(row, column).add(other.get(row, column), mathContext));
            }
        }

        return result;
    }

    protected BigMatrix subtract(BigMatrix other, MathContext mathContext) {
        AbstractBigMatrix result = createBigMatrix(rows(), columns());

        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                result.internalSet(row, column, get(row, column).subtract(other.get(row, column), mathContext));
            }
        }

        return result;
    }

    protected BigMatrix multiply(BigDecimal value, MathContext mathContext) {
        AbstractBigMatrix result = createBigMatrix(rows(), columns());

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                result.internalSet(row, column, get(row, column).multiply(value, mathContext));
            }
        }

        return result;
    }

    protected BigMatrix multiply(BigMatrix other, MathContext mathContext) {
        if (columns() != other.rows()) {
            throw new ArithmeticException("columns " + columns() + " != rows " + other.rows());
        }

        AbstractBigMatrix result = createBigMatrix(rows(), other.columns());

        for (int row = 0; row < result.rows(); row++) {
            for (int column = 0; column < result.columns(); column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < columns(); index++) {
                    sum = sum.add(get(row, index).multiply(other.get(index, column), mathContext), mathContext);
                }
                result.internalSet(row, column, sum);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[");
        for (int row = 0; row < rows(); row++) {
            result.append("[");
            for (int column = 0; column < columns(); column++) {
                if (column != 0) {
                    result.append(", ");
                }
                result.append(get(row, column));
            }
            result.append("]");
            if (row == rows() - 1) {
                result.append("]");
            } else {
                result.append(",\n");
            }
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BigMatrix)) {
            return false;
        }
        BigMatrix other = (BigMatrix) obj;

        if (rows() != other.rows()) {
            return false;
        }
        if (columns() != other.columns()) {
            return false;
        }
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                if (get(row, column).compareTo(other.get(row, column)) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void checkRows(int rows) {
        if (rows < 0 ) {
            throw new IllegalArgumentException("rows < 0: " + rows);
        }
    }

    protected void checkColumns(int columns) {
        if (columns < 0 ) {
            throw new IllegalArgumentException("columns < 0: " + columns);
        }
    }

    protected void checkRow(int row) {
        if (row < 0 ) {
            throw new IllegalArgumentException("row < 0: " + row);
        }
        if (row >= rows()) {
            throw new IllegalArgumentException("row >= " + rows() + " : " + row);
        }
    }

    protected void checkColumn(int column) {
        if (column < 0 ) {
            throw new IllegalArgumentException("column < 0: " + column);
        }
        if (column >= columns()) {
            throw new IllegalArgumentException("column >= " + columns() + " : " + column);
        }
    }
}
