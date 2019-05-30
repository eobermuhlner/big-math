package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;

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
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns " + columns() + " != rows " + other.rows());
        }

        AbstractBigMatrix result = createBigMatrix(rows(), other.columns());

        for (int row = 0; row < result.rows(); row++) {
            for (int column = 0; column < result.columns(); column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < columns(); index++) {
                    sum = sum.add(get(row, index).multiply(get(index, column), mathContext), mathContext);
                }
                result.internalSet(row, column, sum);
            }
        }

        return result;
    }

    protected BigMatrix transpose() {
        AbstractBigMatrix result = createBigMatrix(columns(), rows());

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                int targetRow = column;
                int targetColumn = row;
                result.internalSet(targetRow, targetColumn, get(row, column));
            }
        }

        return result;
    }
}
