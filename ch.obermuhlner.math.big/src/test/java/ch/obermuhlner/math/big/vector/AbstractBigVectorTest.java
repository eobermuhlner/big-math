package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static ch.obermuhlner.util.AssertUtil.assertBigDecimal;
import static ch.obermuhlner.util.AssertUtil.assertBigVector;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.*;

public abstract class AbstractBigVectorTest {
    protected abstract BigVector createBigVector(double... values);

    @Test
    public void testEmpty() {
        BigVector m = createBigVector();

        assertEquals(0, m.size());
    }

    @Test
    public void testEquals() {
        BigVector m1 = createBigVector(1, 2, 3);
        BigVector m2 = createBigVector(1, 2, 3);

        assertTrue(m1.equals(m2));
    }

    @Test
    public void testToString() {
        BigVector m = createBigVector(1, 2, 3);
        assertNotNull(m.toString());
    }

    @Test
    public void testConstructors() {
        BigVector m = createBigVector(1, 2, 3);

        assertEquals(m, ImmutableBigVector.vector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.denseVector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.sparseVector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.vector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.denseVector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.sparseVector(valueOf(1), valueOf(2), valueOf(3)));
    }

    @Test
    public void testAdd() {
        BigVector m1 = createBigVector(10, 20, 30);
        BigVector m2 = createBigVector(1, 2, 3);

        ImmutableBigVector r = m1.add(m2);

        assertEquals(ImmutableBigVector.vector(11, 22, 33), r);
    }

    @Test
    public void testSubtract() {
        BigVector m1 = createBigVector(11, 22, 33);
        BigVector m2 = createBigVector(1, 2, 3);

        ImmutableBigVector r = m1.subtract(m2);

        assertEquals(ImmutableBigVector.vector(10, 20, 30), r);
    }

    @Test
    public void testMultiplyScalar() {
        BigVector m = createBigVector(1, 2, 3);

        ImmutableBigVector r = m.multiply(valueOf(2));

        assertEquals(ImmutableBigVector.vector(2, 4, 6), r);
    }

    @Test
    public void testDivideScalar() {
        BigVector m = createBigVector(2, 4, 6);

        ImmutableBigVector r = m.divide(valueOf(2), MathContext.DECIMAL128);

        assertEquals(ImmutableBigVector.vector(1, 2, 3), r);
    }

    @Test
    public void testCross() {
        // https://www.wolframalpha.com/input/?i=%281%2C+2%2C+3%29+cross+%282%2C+3%2C+4%29
        BigVector m1 = createBigVector(1, 2, 3);
        BigVector m2 = createBigVector(2, 3, 4);

        ImmutableBigVector r = m1.cross(m2);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(-1, 2, -1), r, epsilon);
    }

    @Test
    public void testDot() {
        // https://www.wolframalpha.com/input/?i=%281%2C+2%2C+3%29+dot+%282%2C+3%2C+4%29
        BigVector m1 = createBigVector(1, 2, 3);
        BigVector m2 = createBigVector(2, 3, 4);

        BigDecimal r = m1.dot(m2);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigDecimal(valueOf(20), r, epsilon);
    }

    @Test
    public void testMagnitude() {
        BigVector m = createBigVector(1, 2, 3);

        BigDecimal r = m.magnitude(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.00001);
        assertBigDecimal(BigDecimalMath.sqrt(valueOf(1 + 2*2 + 3*3), MathContext.DECIMAL128), r, epsilon);
        assertBigDecimal(valueOf(3.74166), r, epsilon);
    }

    @Test
    public void testNormalize() {
        BigVector m = createBigVector(1, 2, 3);

        BigVector r = m.normalize(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(
                1.0 / Math.sqrt(14),
                Math.sqrt(2.0 / 7),
                3.0 / Math.sqrt(14)), r, epsilon);
    }

    @Test
    public void testAbs() {
        BigVector m = createBigVector(1, -2, 0);

        BigVector r = m.abs();

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(1, 2, 0), r, epsilon);
    }

    @Test
    public void testElementOperation() {
        BigVector m = createBigVector(1, 2, 3);

        BigVector r = m.elementOperation(b -> b.multiply(valueOf(11)));

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(11, 22, 33), r, epsilon);
    }

    @Test
    public void testIsZero() {
        assertEquals(false, createBigVector(1, 2, 3).isZero());
        assertEquals(false, createBigVector(0, 0, 3).isZero());

        assertEquals(true, createBigVector(0, 0, 0).isZero());
        assertEquals(true, createBigVector().isZero());
    }
}
