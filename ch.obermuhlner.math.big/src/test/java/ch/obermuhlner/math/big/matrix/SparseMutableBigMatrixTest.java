package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.MathContext;

import static ch.obermuhlner.math.big.matrix.CoordValue.coordValue;
import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class SparseMutableBigMatrixTest extends AbstractMutableBigMatrixTest {

    private static final int TIMEOUT = 50;
    private static final int ROWS = 10000;
    private static final int COLUMNS = 10000;

    @Override
    protected MutableBigMatrix createMutableBigMatrix(int rows, int columns, double... values) {
        return MutableBigMatrix.sparseMatrix(rows, columns, values);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseMutableProduct() {
        MutableBigMatrix m = createMutableBigMatrix(ROWS, COLUMNS);
        m.fill(valueOf(1));
        m.set(coordValue(0, 1, 2), coordValue(0, 2, 3));

        assertBigDecimal(valueOf(2*3), m.product(MathContext.DECIMAL64));
    }

    // Common tests with SparseImmutableBigMatrixTest

    @Test(timeout = TIMEOUT)
    public void testSparseHashCode() {
        MutableBigMatrix m = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        System.out.println("hashCode = " + m.hashCode());
    }

    @Test(timeout = TIMEOUT)
    public void testSparseEquals() {
        MutableBigMatrix m1 = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);
        MutableBigMatrix m2 = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertEquals(m1, m2);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseAdd() {
        MutableBigMatrix m1 = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);
        MutableBigMatrix m2 = createMutableBigMatrix(ROWS, COLUMNS, 10, 20, 30);

        BigMatrix r = m1.add(m2);

        assertEquals(createMutableBigMatrix(ROWS, COLUMNS, 11, 22, 33), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseSubtract() {
        MutableBigMatrix m1 = createMutableBigMatrix(ROWS, COLUMNS, 11, 22, 33);
        MutableBigMatrix m2 = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        BigMatrix r = m1.subtract(m2);

        assertEquals(createMutableBigMatrix(ROWS, COLUMNS, 10, 20, 30), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseMultiplyScalar() {
        MutableBigMatrix m1 = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        BigMatrix r = m1.multiply(valueOf(2));

        assertEquals(createMutableBigMatrix(ROWS, COLUMNS, 2, 4, 6), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseMultiply() {
        MutableBigMatrix m1 = createMutableBigMatrix(ROWS, COLUMNS, 11, 22, 33);
        MutableBigMatrix m2 = createMutableBigMatrix(ROWS, COLUMNS, 2, 3, 4);

        BigMatrix r = m1.multiply(m2);

        assertEquals(createMutableBigMatrix(ROWS, COLUMNS, 22, 33, 44), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseSum() {
        MutableBigMatrix m = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertBigDecimal(valueOf(1+2+3), m.sum());
    }

    @Test(timeout = TIMEOUT)
    public void testSparseProduct() {
        MutableBigMatrix m = createMutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertBigDecimal(valueOf(0), m.product(MathContext.DECIMAL64));
    }
}
