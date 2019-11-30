package ch.obermuhlner.math.big.vector.internal;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.BigVector;
import ch.obermuhlner.math.big.vector.ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.matrix.MatrixImmutableBigVector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

import static java.math.BigDecimal.ZERO;

public abstract class AbstractBigVectorImpl implements BigVector {

    @Override
    public BigDecimal get(int index) {
        return asBigMatrix().get(index, 0);
    }

    @Override
    public int size() {
        return asBigMatrix().rows();
    }

    @Override
    public ImmutableBigVector round(MathContext mathContext) {
        return new MatrixImmutableBigVector(asBigMatrix().round(mathContext));
    }

    @Override
    public ImmutableBigVector add(BigVector other, MathContext mathContext) {
        return new MatrixImmutableBigVector(asBigMatrix().add(other.asBigMatrix(), mathContext));
    }

    @Override
    public ImmutableBigVector subtract(BigVector other, MathContext mathContext) {
        return new MatrixImmutableBigVector(asBigMatrix().subtract(other.asBigMatrix(), mathContext));
    }

    @Override
    public ImmutableBigVector multiply(BigDecimal value, MathContext mathContext) {
        return new MatrixImmutableBigVector(asBigMatrix().multiply(value, mathContext));
    }

    @Override
    public ImmutableBigVector divide(BigDecimal value, MathContext mathContext) {
        return new MatrixImmutableBigVector(asBigMatrix().divide(value, mathContext));
    }

    @Override
    public ImmutableBigVector elementOperation(Function<BigDecimal, BigDecimal> operation) {
        return new MatrixImmutableBigVector(asBigMatrix().elementOperation(operation));
    }

    @Override
    public BigDecimal dot(BigVector other, MathContext mathContext) {
        VectorUtils.checkSameSize(this, other);

        BigDecimal result = ZERO;
        for (int i = 0; i < size(); i++) {
            result = MatrixUtils.add(result, MatrixUtils.multiply(get(i), other.get(i), mathContext), mathContext);
        }
        return result;
    }

    @Override
    public BigDecimal magnitude(MathContext mathContext) {
        BigDecimal sumSquares = ZERO;

        for (int i = 0; i < size(); i++) {
            BigDecimal v = get(i);
            sumSquares = MatrixUtils.add(sumSquares, MatrixUtils.multiply(v, v, mathContext), mathContext);
        }

        return BigDecimalMath.sqrt(sumSquares, mathContext);
    }

    @Override
    public ImmutableBigVector cross(BigVector other, MathContext mathContext) {
        VectorUtils.checkSize(this, 3);
        VectorUtils.checkSameSize(this, other);

        return new MatrixImmutableBigVector(ImmutableBigMatrix.denseMatrix(3, 1,
                MatrixUtils.subtract(MatrixUtils.multiply(get(1), other.get(2), mathContext), MatrixUtils.multiply(get(2), other.get(1), mathContext), mathContext),
                MatrixUtils.subtract(MatrixUtils.multiply(get(2), other.get(0), mathContext), MatrixUtils.multiply(get(0), other.get(2), mathContext), mathContext),
                MatrixUtils.subtract(MatrixUtils.multiply(get(0), other.get(1), mathContext), MatrixUtils.multiply(get(1), other.get(0), mathContext), mathContext)));
    }

    @Override
    public ImmutableBigVector normalize(MathContext mathContext) {
        if (isZero()) {
            return ImmutableBigVector.zeroVector(size());
        }

        BigDecimal m = magnitude(mathContext);
        return divide(m, mathContext);
    }

    @Override
    public ImmutableBigVector abs() {
        return elementOperation(b -> b.abs());
    }

    @Override
    public boolean isZero() {
        // TODO sparse impl
        for (int i = 0; i < size(); i++) {
            if (get(i).signum() != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof BigVector)) return false;
        BigVector other = (BigVector) obj;

        if (size() != other.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            if (get(i).compareTo(other.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        int n = size();

        result = 31 * result + n;

        int index = 1;
        int lastIndex = 1;

        while (index < n) {
            int elementHash = get(index).stripTrailingZeros().hashCode();
            result = 31 * result + elementHash;

            int tmp = index + lastIndex;
            lastIndex = index;
            index = tmp;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[");
        int maxIndex = Math.min(size(), 10);
        for (int i = 0; i < maxIndex; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(get(i));
        }
        if (size() != maxIndex) {
            result.append(", ...");
        }
        result.append("]");

        return result.toString();
    }
}
