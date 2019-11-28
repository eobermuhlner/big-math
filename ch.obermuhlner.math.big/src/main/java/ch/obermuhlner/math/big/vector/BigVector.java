package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.BigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

public interface BigVector {
    BigDecimal get(int index);
    int size();

    ImmutableBigVector round(MathContext mathContext);

    default ImmutableBigVector add(BigVector other) {
        return add(other, null);
    }
    ImmutableBigVector add(BigVector other, MathContext mathContext);

    default ImmutableBigVector subtract(BigVector other) {
        return subtract(other, null);
    }
    ImmutableBigVector subtract(BigVector other, MathContext mathContext);

    default ImmutableBigVector multiply(BigDecimal value) {
        return multiply(value, null);
    }
    ImmutableBigVector multiply(BigDecimal value, MathContext mathContext);

    ImmutableBigVector divide(BigDecimal value, MathContext mathContext);

    default ImmutableBigVector cross(BigVector other) {
        return cross(other, null);
    }
    ImmutableBigVector cross(BigVector other, MathContext mathContext);

    default BigDecimal dot(BigVector other) {
        return dot(other, null);
    }
    BigDecimal dot(BigVector other, MathContext mathContext);

    BigDecimal magnitude(MathContext mathContext);

    ImmutableBigVector normalize(MathContext mathContext);

    ImmutableBigVector abs();

    ImmutableBigVector elementOperation(Function<BigDecimal, BigDecimal> operation);

    boolean isZero();

    BigMatrix asBigMatrix();

    default BigDecimal[] toBigDecimalArray() {
        return asBigMatrix().toBigDecimalArray();
    }

    default double[] toDoubleArray() {
        return asBigMatrix().toDoubleArray();
    }

}
