package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class SparseImmutableBigMatrixTest extends AbstractImmutableBigMatrixTest {

    private static final int TIMEOUT = 50;
    private static final int ROWS = 10000;
    private static final int COLUMNS = 10000;

    @Override
    protected ImmutableBigMatrix createImmutableBigMatrix(int rows, int columns, double... values) {
        return ImmutableBigMatrix.sparseMatrix(rows, columns, values);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseHashCode() {
        ImmutableBigMatrix m = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        System.out.println("hashCode = " + m.hashCode());
    }

    @Test(timeout = TIMEOUT)
    public void testSparseEquals() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertEquals(m1, m2);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseAdd() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(ROWS, COLUMNS, 10, 20, 30);

        BigMatrix r = m1.add(m2);

        assertEquals(createImmutableBigMatrix(ROWS, COLUMNS, 11, 22, 33), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseSubtract() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(ROWS, COLUMNS, 11, 22, 33);
        ImmutableBigMatrix m2 = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        BigMatrix r = m1.subtract(m2);

        assertEquals(createImmutableBigMatrix(ROWS, COLUMNS, 10, 20, 30), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseMultiply() {
        ImmutableBigMatrix m1 = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        BigMatrix r = m1.multiply(valueOf(2));

        assertEquals(createImmutableBigMatrix(ROWS, COLUMNS, 2, 4, 6), r);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseSum() {
        ImmutableBigMatrix m = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertBigDecimal(valueOf(1+2+3), m.sum());
    }

    @Test(timeout = TIMEOUT)
    public void testSparseProduct() {
        ImmutableBigMatrix m = createImmutableBigMatrix(ROWS, COLUMNS, 1, 2, 3);

        assertBigDecimal(valueOf(0), m.product());
    }
}
