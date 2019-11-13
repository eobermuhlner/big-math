package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractBigMatrix implements BigMatrix {

    protected abstract AbstractBigMatrix createBigMatrix(int rows, int columns);

    protected abstract void internalSet(int row, int column, BigDecimal value);

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other) {
        return multiply(other, null);
    }

    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        MatrixUtils.checkColumnsOtherRows(this, other);

        AbstractBigMatrix result = createBigMatrix(rows(), other.columns());

        for (int row = 0; row < result.rows(); row++) {
            for (int column = 0; column < result.columns(); column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < columns(); index++) {
                    sum = MatrixUtils.add(sum, MatrixUtils.multiply(get(row, index), other.get(index, column), mathContext), mathContext);
                }
                result.internalSet(row, column, sum);
            }
        }

        return result.toImmutableMatrix();
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
}
