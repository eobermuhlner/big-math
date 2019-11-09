package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.*;
import static org.junit.Assert.assertEquals;

public abstract class AbstractMutableBigMatrixTest extends AbstractBigMatrixTest {

    @Override
    protected BigMatrix createBigMatrix(int rows, int columns, double... values) {
        return createMutableBigMatrix(rows, columns, values);
    }

    protected abstract MutableBigMatrix createMutableBigMatrix(int rows, int columns, double... values);

    @Test
    public void testSetRowLower() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertEquals(valueOf(6.0), m.get(1, 2));
        m.set(1, 2, ZERO);
        assertEquals(ZERO, m.get(1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failSetRowLower() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.set(-1, 0, ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failSetRowUpper() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.set(2, 0, ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failSetColumnLower() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.set(0, -1, ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failSetColumnUpper() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.set(0, 3, ZERO);
    }

    @Test
    public void testAddToThis() {
        MutableBigMatrix m1 = createMutableBigMatrix(2, 3,
                10, 20, 30,
                40, 50, 60);
        MutableBigMatrix m2 = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m1.addToThis(m2, MathContext.DECIMAL128);

        assertEquals(
                createBigMatrix(2, 3,
                        11, 22, 33,
                        44, 55, 66),
                m1);
    }

    @Test
    public void testSubtractFromThis() {
        MutableBigMatrix m1 = createMutableBigMatrix(2, 3,
                11, 22, 33,
                44, 55, 66);
        MutableBigMatrix m2 = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m1.subtractFromThis(m2, MathContext.DECIMAL128);

        assertEquals(
                createBigMatrix(2, 3,
                        10, 20, 30,
                        40, 50, 60),
                m1);
    }

    @Test
    public void testMultiplyToThis() {
        MutableBigMatrix m1 = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m1.multiplyToThis(valueOf(2), MathContext.DECIMAL128);

        assertEquals(
                createBigMatrix(2, 3,
                        2, 4, 6,
                        8, 10, 12),
                m1);
    }

    @Test
    public void testSwapRows() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.swapRows(0, 1);

        assertEquals(
                createBigMatrix(3, 3,
                        4, 5, 6,
                        1, 2, 3,
                        7, 8, 9),
                m1);
    }

    @Test
    public void testSwapRowsSame() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.swapRows(1, 1);

        assertEquals(
                createBigMatrix(3, 3,
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9),
                m1);
    }

    @Test
    public void testSwapColumns() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.swapColumns(0, 1);

        assertEquals(
                createBigMatrix(3, 3,
                        2, 1, 3,
                        5, 4, 6,
                        8, 7, 9),
                m1);
    }


    @Test
    public void testSwapColumnsSame() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.swapColumns(1, 1);

        assertEquals(
                createBigMatrix(3, 3,
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9),
                m1);
    }
}
