package ch.obermuhlner.math.big.example;

import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.MathContext;

public class MatrixExamples {
    private static final MathContext MC = MathContext.DECIMAL128;

    public static void main(String[] args) {
        runBasicImmutableExample();
    }

    private static void runBasicImmutableExample() {
        ImmutableBigMatrix m1 = ImmutableBigMatrix.matrix(3, 3);
        ImmutableBigMatrix m2 = ImmutableBigMatrix.matrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        ImmutableBigMatrix m3 = m1.add(m2).multiply(m2);
        System.out.println(m3);
    }
}
