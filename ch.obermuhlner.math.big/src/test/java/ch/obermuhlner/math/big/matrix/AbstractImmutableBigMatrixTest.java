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

}
