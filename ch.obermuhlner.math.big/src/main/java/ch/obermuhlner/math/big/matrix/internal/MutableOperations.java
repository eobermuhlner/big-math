package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public class MutableOperations {

    public static void add(MutableBigMatrix mutableMatrix, BigMatrix matrix, MathContext mathContext) {
        if (mutableMatrix.rows() != matrix.rows()) {
            throw new ArithmeticException("rows: " + mutableMatrix.rows() + " != " + matrix.rows());
        }
        if (mutableMatrix.columns() != matrix.columns()) {
            throw new ArithmeticException("columns: " + mutableMatrix.columns() + " != " + matrix.columns());
        }

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).add(matrix.get(row, column), mathContext));
            }
        }
    }

    public static void add(MutableBigMatrix mutableMatrix, BigMatrix matrix) {
        if (mutableMatrix.rows() != matrix.rows()) {
            throw new ArithmeticException("rows: " + mutableMatrix.rows() + " != " + matrix.rows());
        }
        if (mutableMatrix.columns() != matrix.columns()) {
            throw new ArithmeticException("columns: " + mutableMatrix.columns() + " != " + matrix.columns());
        }

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).add(matrix.get(row, column)));
            }
        }
    }

    public static void subtract(MutableBigMatrix mutableMatrix, BigMatrix matrix, MathContext mathContext) {
        if (mutableMatrix.rows() != matrix.rows()) {
            throw new ArithmeticException("rows: " + mutableMatrix.rows() + " != " + matrix.rows());
        }
        if (mutableMatrix.columns() != matrix.columns()) {
            throw new ArithmeticException("columns: " + mutableMatrix.columns() + " != " + matrix.columns());
        }

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).subtract(matrix.get(row, column), mathContext));
            }
        }
    }

    public static void subtract(MutableBigMatrix mutableMatrix, BigMatrix matrix) {
        if (mutableMatrix.rows() != matrix.rows()) {
            throw new ArithmeticException("rows: " + mutableMatrix.rows() + " != " + matrix.rows());
        }
        if (mutableMatrix.columns() != matrix.columns()) {
            throw new ArithmeticException("columns: " + mutableMatrix.columns() + " != " + matrix.columns());
        }

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).subtract(matrix.get(row, column)));
            }
        }
    }

    public static void multiply(MutableBigMatrix mutableMatrix, BigDecimal value, MathContext mathContext) {
        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).multiply(value, mathContext));
            }
        }
    }

    public static void multiply(MutableBigMatrix mutableMatrix, BigDecimal value) {
        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, mutableMatrix.get(row, column).multiply(value));
            }
        }
    }
}
