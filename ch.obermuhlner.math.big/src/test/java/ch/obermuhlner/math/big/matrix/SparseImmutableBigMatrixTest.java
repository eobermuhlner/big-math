package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static java.math.BigDecimal.valueOf;

public class SparseImmutableBigMatrixTest extends AbstractImmutableBigMatrixTest {

    @Override
    protected ImmutableBigMatrix createImmutableBigMatrix(int rows, int columns, double... values) {
        return ImmutableBigMatrix.sparseMatrix(rows, columns, values);
    }

    @Test(timeout = 10)
    public void testSparseSum() {
        ImmutableBigMatrix m = createImmutableBigMatrix(10000, 20000, 1, 2, 3);

        assertBigDecimal(valueOf(1+2+3), m.sum());
    }
}
