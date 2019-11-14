package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.AbstractSparseBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractBigMatrix implements BigMatrix {

    protected abstract void internalSet(int row, int column, BigDecimal value);

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other) {
        return multiply(other, null);
    }

    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        MatrixUtils.checkColumnsOtherRows(this, other);

        AbstractBigMatrix result;
        if (isSparseWithLotsOfZeroes(this) && isSparseWithLotsOfZeroes(other)) {
            result = new SparseImmutableBigMatrix(rows(), other.columns(), new BigDecimal[0]);
        } else {
            result = new DenseImmutableBigMatrix(rows(), other.columns(), new BigDecimal[0]);
        }

        for (int row = 0; row < result.rows(); row++) {
            for (int column = 0; column < result.columns(); column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < columns(); index++) {
                    sum = MatrixUtils.add(sum, MatrixUtils.multiply(get(row, index), other.get(index, column), mathContext), mathContext);
                }
                result.internalSet(row, column, sum.stripTrailingZeros());
            }
        }

        return ImmutableBigMatrix.matrix(result);
    }

    private boolean isSparseWithLotsOfZeroes(BigMatrix matrix) {
        if (matrix instanceof AbstractSparseBigMatrix) {
            AbstractSparseBigMatrix sparseMatrix = (AbstractSparseBigMatrix) matrix;
            return sparseMatrix.getDefaultValue().signum() == 0 && sparseMatrix.sparseEmptyRatio() > 0.5;
        }
        return false;
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
