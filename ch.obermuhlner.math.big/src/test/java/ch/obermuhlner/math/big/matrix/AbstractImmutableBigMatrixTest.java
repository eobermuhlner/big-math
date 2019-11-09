package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.MathContext;

import static org.junit.Assert.assertEquals;
import static java.math.BigDecimal.*;

public abstract class AbstractImmutableBigMatrixTest extends AbstractBigMatrixTest {

    @Override
    protected BigMatrix createBigMatrix(int rows, int columns, double... values) {
        return createImmutableBigMatrix(rows, columns, values);
    }

    protected abstract ImmutableBigMatrix createImmutableBigMatrix(int rows, int columns, double... values);

    @Test
    public void testAdd() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(2, 3,
                10, 20, 30,
                40, 50, 60);

        ImmutableBigMatrix r = m1.add(m2, MathContext.DECIMAL128);

        assertEquals(ImmutableBigMatrix.denseMatrix(2, 3,
                11, 22, 33,
                44, 55, 66),
                r);
    }

    @Test
    public void testSubtract() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(2, 3,
                10, 20, 30,
                40, 50, 60);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        ImmutableBigMatrix r = m1.subtract(m2, MathContext.DECIMAL128);

        assertEquals(
                ImmutableBigMatrix.denseMatrix(2, 3,
                        9, 18, 27,
                        36, 45, 54),
                r);
    }

    @Test
    public void testMultiply() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(3, 2,
                1, 2,
                3, 4,
                5, 6);

        ImmutableBigMatrix r = m1.multiply(m2, MathContext.DECIMAL128);
        System.out.println(r);

        assertEquals(
                ImmutableBigMatrix.denseMatrix(2, 2,
                        22, 28,
                        49, 64),
                r);
    }

    @Test
    public void testMultiplyScalar() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        ImmutableBigMatrix r = m1.multiply(valueOf(2), MathContext.DECIMAL128);

        assertEquals(ImmutableBigMatrix.denseMatrix(2, 3,
                2, 4, 6,
                8, 10, 12),
                r);
    }
}
