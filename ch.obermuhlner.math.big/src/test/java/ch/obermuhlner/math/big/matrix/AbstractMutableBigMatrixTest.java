package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.MathContext;

import static ch.obermuhlner.math.big.matrix.CoordValue.coordValue;
import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static java.math.BigDecimal.*;
import static org.junit.Assert.assertEquals;

public abstract class AbstractMutableBigMatrixTest extends AbstractBigMatrixTest {

    @Override
    protected BigMatrix createBigMatrix(int rows, int columns, double... values) {
        return createMutableBigMatrix(rows, columns, values);
    }

    protected abstract MutableBigMatrix createMutableBigMatrix(int rows, int columns, double... values);

    @Test
    public void testSet() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        assertEquals(valueOf(6.0), m.get(1, 2));
        m.set(1, 2, ZERO);
        assertEquals(ZERO, m.get(1, 2));
    }

    @Test
    public void testSetCoordValues() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3);
        m.set(
                coordValue(0, 0, 1),
                coordValue(0, 1, 2),
                coordValue(0, 2, 3),
                coordValue(1, 0, 4),
                coordValue(1, 1, 5),
                coordValue(1, 2, 6));

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
    public void testClear() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.clear();

        assertEquals(
                createBigMatrix(2, 3),
                m);
    }

    @Test
    public void testFill() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        m.fill(valueOf(1));

        assertEquals(
                createBigMatrix(2, 3,
                        1, 1, 1,
                        1, 1, 1),
                m);
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

    @Test
    public void testInsertRow() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.insertRow(1, 10, 11, 12);

        assertEquals(
                createBigMatrix(4, 3,
                        1, 2, 3,
                        10, 11, 12,
                        4, 5, 6,
                        7, 8, 9),
                m1);
    }

    @Test
    public void testInsertRowEnd() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.insertRow(3, 10, 11, 12);

        assertEquals(
                createBigMatrix(4, 3,
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9,
                        10, 11, 12),
                m1);
    }

    @Test
    public void testAppendRow() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.appendRow(10, 11, 12);

        assertEquals(
                createBigMatrix(4, 3,
                        1, 2, 3,
                        4, 5, 6,
                        7, 8, 9,
                        10, 11, 12),
                m1);
    }

    @Test
    public void testRemoveRow() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.removeRow(1);

        assertEquals(
                createBigMatrix(2, 3,
                        1, 2, 3,
                        7, 8, 9),
                m1);
    }

    @Test
    public void testInsertColumn() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.insertColumn(1, 10, 11, 12);

        assertEquals(
                createBigMatrix(3, 4,
                        1, 10, 2, 3,
                        4, 11, 5, 6,
                        7, 12, 8, 9),
                m1);
    }

    @Test
    public void testInsertColumnEnd() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.insertColumn(3, 10, 11, 12);

        assertEquals(
                createBigMatrix(3, 4,
                        1, 2, 3, 10,
                        4, 5, 6, 11,
                        7, 8, 9, 12),
                m1);
    }

    @Test
    public void testAppendColumn() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.appendColumn(10, 11, 12);

        assertEquals(
                createBigMatrix(3, 4,
                        1, 2, 3, 10,
                        4, 5, 6, 11,
                        7, 8, 9, 12),
                m1);
    }

    @Test
    public void testRemoveColumn() {
        MutableBigMatrix m1 = createMutableBigMatrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        m1.removeColumn(1);

        assertEquals(
                createBigMatrix(3, 2,
                        1, 3,
                        4, 6,
                        7, 9),
                m1);
    }

    @Test
    public void testMutableSum() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3);
        m.fill(valueOf(2));
        m.set(0, 0,
                ImmutableBigMatrix.matrix(2, 3,
                        1, 2, 3,
                        4, 5, 6));


        assertBigDecimal(valueOf(1 + 2 + 3 + 4 + 5 + 6), m.sum(MathContext.DECIMAL128));
    }

    @Test
    public void testMutableProduct() {
        MutableBigMatrix m = createMutableBigMatrix(2, 3);
        m.fill(valueOf(2));
        m.set(0, 0,
                ImmutableBigMatrix.matrix(2, 3,
                        1, 2, 3,
                        4, 5, 6));

        assertBigDecimal(valueOf(1 * 2 * 3 * 4 * 5 * 6), m.product(MathContext.DECIMAL128));
    }

    @Test
    public void testMutableMultiply() {
        BigMatrix m1 = createBigMatrix(2, 3,
                0, 0, 3,
                0, 5, 0);
        BigMatrix m2 = createBigMatrix(3, 2,
                0, 2,
                3, 0,
                0, 0);

        BigMatrix r = m1.multiply(m2, MathContext.DECIMAL128);
        System.out.println(r);

        assertEquals(
                createBigMatrix(2, 2,
                        0, 0,
                        15, 0),
                r);
    }
}
