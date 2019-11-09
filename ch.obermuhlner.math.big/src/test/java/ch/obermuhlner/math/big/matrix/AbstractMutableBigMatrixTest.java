package ch.obermuhlner.math.big.matrix;

import org.junit.Test;

import java.math.BigDecimal;

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
}
