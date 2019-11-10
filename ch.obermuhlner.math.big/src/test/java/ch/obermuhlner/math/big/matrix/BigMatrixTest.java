package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.MathContext;

import static java.math.BigDecimal.valueOf;

public class BigMatrixTest {
    @Test
    public void testBasics() {
        System.out.println(ImmutableBigMatrix.identityMatrix(5, 5));

        ImmutableBigMatrix m2 = ImmutableBigMatrix.denseMatrix(2, 2,
                1.1, 2.2,
                3.3, 4.4);
        System.out.println(m2);

        ImmutableBigMatrix m3 = m2.multiply(valueOf(2.0), MathContext.DECIMAL64);
        System.out.println(m3);

        ImmutableBigMatrix m4 = ImmutableBigMatrix.denseMatrix(3, 3,
                (row, column) -> valueOf(row + column));
        System.out.println(m4);
    }

    @Test
    public void testGaussianElimination() {
        MutableBigMatrix m = MutableBigMatrix.denseMatrix(3, 4,
                valueOf(2), valueOf(1), valueOf(-1), valueOf(8),
                valueOf(-3), valueOf(-1), valueOf(2), valueOf(-11),
                valueOf(-2), valueOf(1), valueOf(2), valueOf(-3));
        m.gaussianElimination(false, MathContext.DECIMAL64);
        System.out.println(m);
    }

    @Test
    public void testGaussianEliminationReducedEchelon() {
        MutableBigMatrix m = MutableBigMatrix.denseMatrix(3, 4,
                valueOf(2), valueOf(1), valueOf(-1), valueOf(8),
                valueOf(-3), valueOf(-1), valueOf(2), valueOf(-11),
                valueOf(-2), valueOf(1), valueOf(2), valueOf(-3));
        m.gaussianElimination(true, MathContext.DECIMAL64);
        System.out.println(m);
    }
}
