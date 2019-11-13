package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.MathContext;

import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static ch.obermuhlner.util.AssertUtil.assertBigMatrix;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static java.math.BigDecimal.*;

public abstract class AbstractBigMatrixTest {
    protected abstract BigMatrix createBigMatrix(int rows, int columns, double... values);

    @Test
    public void testEmpty() {
        BigMatrix m = createBigMatrix(0, 0);

        assertEquals(0, m.rows());
        assertEquals(0, m.columns());
    }

    @Test
    public void testRowsColumnsGet() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertEquals(2, m.rows());
        assertEquals(3, m.columns());

        assertEquals(valueOf(1.0), m.get(0, 0));
        assertEquals(valueOf(2.0), m.get(0, 1));
        assertEquals(valueOf(3.0), m.get(0, 2));
        assertEquals(valueOf(4.0), m.get(1, 0));
        assertEquals(valueOf(5.0), m.get(1, 1));
        assertEquals(valueOf(6.0), m.get(1, 2));
    }

    @Test
    public void testSum() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertBigDecimal(valueOf(1 + 2 + 3 + 4 + 5 + 6), m.sum());
    }

    @Test
    public void testProduct() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertBigDecimal(valueOf(1 * 2 * 3 * 4 * 5 * 6), m.product());
    }

    @Test
    public void testTranspose() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);
        BigMatrix r = m.transpose();

        assertEquals(
                ImmutableBigMatrix.denseMatrix(3, 2,
                        1, 4,
                        2, 5,
                        3, 6),
                r);
    }

    @Test
    public void testSubMatrix() {
        BigMatrix m = createBigMatrix(6, 6,
                1, 2, 3, 4, 5, 6,
                7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18,
                19, 20, 21, 22, 23, 24,
                25, 26, 27, 28, 29, 30,
                31, 32, 33, 34, 35, 36);
        BigMatrix r = m.subMatrix(1, 2, 3, 4);

        assertEquals(
                ImmutableBigMatrix.denseMatrix(3, 4,
                        9, 10, 11, 12,
                        15, 16, 17, 18,
                        21, 22, 23, 24),
                r);
    }

    @Test
    public void testMinor() {
        BigMatrix m = createBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);
        BigMatrix r = m.minor(0, 1);

        assertEquals(
                ImmutableBigMatrix.denseMatrix(2, 2,
                        4, 6,
                        7, 9),
                r);
    }

    @Test
    public void testAddMathContext() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 0, 0);
        BigMatrix m2 = createBigMatrix(2, 3,
                10, 20, 30,
                0, 50, 0);

        BigMatrix r = m1.add(m2, MathContext.DECIMAL128);

        assertEquals(
                createBigMatrix(2, 3,
                        11, 22, 33,
                        4, 50, 0),
                r);
    }

    @Test
    public void testAdd() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 0, 0);
        BigMatrix m2 = createBigMatrix(2, 3,
                10, 20, 30,
                0, 50, 0);

        BigMatrix r = m1.add(m2);

        assertEquals(
                createBigMatrix(2, 3,
                        11, 22, 33,
                        4, 50, 0),
                r);
    }

    @Test
    public void testSubtractMathContext() {
        BigMatrix m1 = createBigMatrix(2, 3,
                10, 20, 30,
                40, 0, 0);
        BigMatrix m2 = createBigMatrix(2, 3,
                1, 2, 3,
                0, 5, 0);

        BigMatrix r = m1.subtract(m2, MathContext.DECIMAL128);

        assertEquals(
                createBigMatrix(2, 3,
                        9, 18, 27,
                        40, -5, 0),
                r);
    }

    @Test
    public void testSubtract() {
        BigMatrix m1 = createBigMatrix(2, 3,
                10, 20, 30,
                40, 0, 0);
        BigMatrix m2 = createBigMatrix(2, 3,
                1, 2, 3,
                0, 5, 0);

        BigMatrix r = m1.subtract(m2);

        assertEquals(
                createBigMatrix(2, 3,
                        9, 18, 27,
                        40, -5, 0),
                r);
    }

    @Test
    public void testMultiply() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 0);
        BigMatrix m2 = createBigMatrix(3, 2,
                1, 2,
                3, 4,
                0, 6);

        BigMatrix r = m1.multiply(m2, MathContext.DECIMAL128);
        System.out.println(r);

        assertEquals(
                createBigMatrix(2, 2,
                        7, 28,
                        19, 28),
                r);
    }

    @Test
    public void testMultiplyScalarMathContext() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 0);

        BigMatrix r = m1.multiply(valueOf(2), MathContext.DECIMAL128);

        assertEquals(createBigMatrix(2, 3,
                2, 4, 6,
                8, 10, 0),
                r);
    }

    @Test
    public void testMultiplyScalar() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 0);

        BigMatrix r = m1.multiply(valueOf(2));

        assertEquals(createBigMatrix(2, 3,
                2, 4, 6,
                8, 10, 0),
                r);
    }

    @Test
    public void testDeterminant() {
        assertBigDecimal(
                valueOf(1),
                createBigMatrix(0, 0).determinant());
        assertBigDecimal(
                valueOf(99),
                createBigMatrix(1, 1, 99).determinant());
        assertBigDecimal(
                valueOf(-2),
                createBigMatrix(2, 2,
                        1, 2,
                        3, 4).determinant());
        assertBigDecimal(
                valueOf(-2),
                createBigMatrix(2, 2,
                        1, 2,
                        3, 4).determinant());
        assertBigDecimal(
                valueOf(21),
                createBigMatrix(3, 3,
                        1, 2, 3,
                        6, 5, 4,
                        1, 3, 2).determinant());
        assertBigDecimal(
                valueOf(-24),
                createBigMatrix(4, 4,
                        1, 2, 3, 4,
                        6, 5, 4, 1,
                        1, 3, 2, 4,
                        3, 4, 2, 1).determinant());
    }

    @Test
    public void testInvert() {
        BigMatrix m = createBigMatrix(3, 3,
                2, -1, 0,
                -1, 2, -1,
                0, -1, 2);
        BigMatrix r = m.invert(MathContext.DECIMAL64);

        assertBigMatrix(
                createBigMatrix(3, 3,
                        0.75, 0.5, 0.25,
                        0.5, 1.0, 0.5,
                        0.25, 0.5, 0.75),
                r,
                valueOf(0.0000001));
    }

    @Test
    public void testEquals() {
        BigMatrix m1 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);
        BigMatrix m2 = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertTrue(m1.equals(m2));

        assertTrue(m1.equals(ImmutableBigMatrix.denseMatrix(2, 3, 1, 2, 3, 4, 5, 6)));
        assertTrue(m1.equals(ImmutableBigMatrix.sparseMatrix(2, 3, 1, 2, 3, 4, 5, 6)));
        assertTrue(m1.equals(ImmutableBigMatrix.lambdaMatrix(2, 3, (row, column) -> valueOf(row * 3 + column + 1))));
        assertTrue(m1.equals(MutableBigMatrix.denseMatrix(2, 3, 1, 2, 3, 4, 5, 6)));
        assertTrue(m1.equals(MutableBigMatrix.sparseMatrix(2, 3, 1, 2, 3, 4, 5, 6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetRowLower() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.get(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetRowUpper() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.get(2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetColumnLower() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.get(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failGetColumnUpper() {
        BigMatrix m = createBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.get(0, 3);
    }
}
