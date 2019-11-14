package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.math.MathContext;

public class MutableOperations {

    public static void add(MutableBigMatrix mutableMatrix, BigMatrix matrix, MathContext mathContext) {
        MatrixUtils.checkSameSize(mutableMatrix, matrix);

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, MatrixUtils.add(mutableMatrix.get(row, column), matrix.get(row, column), mathContext));
            }
        }
    }

    public static void add(MutableBigMatrix mutableMatrix, BigMatrix matrix) {
        add(mutableMatrix, matrix, null);
    }

    public static void subtract(MutableBigMatrix mutableMatrix, BigMatrix matrix, MathContext mathContext) {
        MatrixUtils.checkSameSize(mutableMatrix, matrix);

        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, MatrixUtils.subtract(mutableMatrix.get(row, column), matrix.get(row, column), mathContext));
            }
        }
    }

    public static void subtract(MutableBigMatrix mutableMatrix, BigMatrix matrix) {
        subtract(mutableMatrix, matrix, null);
    }

    public static void multiply(MutableBigMatrix mutableMatrix, BigDecimal value, MathContext mathContext) {
        for (int row = 0; row < mutableMatrix.rows(); row++) {
            for (int column = 0; column < mutableMatrix.columns(); column++) {
                mutableMatrix.set(row, column, MatrixUtils.multiply(mutableMatrix.get(row, column), value, mathContext));
            }
        }
    }

    public static void multiply(MutableBigMatrix mutableMatrix, BigDecimal value) {
        multiply(mutableMatrix, value, null);
    }
}
