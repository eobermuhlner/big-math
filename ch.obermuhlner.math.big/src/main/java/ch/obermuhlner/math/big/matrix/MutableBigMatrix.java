package ch.obermuhlner.math.big.matrix;

import java.math.BigDecimal;
import java.math.MathContext;

public interface MutableBigMatrix extends BigMatrix {
    void set(int row, int column, BigDecimal value);

    default void fill(BigDecimal value) {
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                set(row, col, value);
            }
        }
    }

    default void clear() {
        fill(BigDecimal.ZERO);
    }

    MutableBigMatrix add(BigMatrix other, MathContext mathContext);
    MutableBigMatrix subtract(BigMatrix other, MathContext mathContext);
    MutableBigMatrix multiply(BigDecimal value, MathContext mathContext);
    MutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    MutableBigMatrix transpose();

    default void addToThis(MutableBigMatrix other, MathContext mathContext) {
        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).add(other.get(row, column), mathContext));
            }
        }
    }

    default void subtractFromThis(MutableBigMatrix other, MathContext mathContext) {
        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).subtract(other.get(row, column), mathContext));
            }
        }
    }

    default void multiplyToThis(BigDecimal value, MathContext mathContext) {
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).multiply(value, mathContext));
            }
        }
    }
}
