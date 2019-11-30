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
    public void testConstructorsSize2() {
        BigVector m = createBigVector(1, 2);

        assertEquals(m, ImmutableBigVector.vector(1, 2));
        assertEquals(m, ImmutableBigVector.denseVector(1, 2));
        assertEquals(m, ImmutableBigVector.sparseVector(1, 2));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(2, 1, 2));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(2, 1, 2));
        assertEquals(m, ImmutableBigVector.vector(valueOf(1), valueOf(2)));
        assertEquals(m, ImmutableBigVector.denseVector(valueOf(1), valueOf(2)));
        assertEquals(m, ImmutableBigVector.sparseVector(valueOf(1), valueOf(2)));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(2, valueOf(1), valueOf(2)));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(2, valueOf(1), valueOf(2)));

        assertEquals(m, MutableBigVector.vector(1, 2));
        assertEquals(m, MutableBigVector.denseVector(1, 2));
        assertEquals(m, MutableBigVector.sparseVector(1, 2));
        assertEquals(m, MutableBigVector.denseVectorOfSize(2, 1, 2));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(2, 1, 2));
        assertEquals(m, MutableBigVector.vector(valueOf(1), valueOf(2)));
        assertEquals(m, MutableBigVector.denseVector(valueOf(1), valueOf(2)));
        assertEquals(m, MutableBigVector.sparseVector(valueOf(1), valueOf(2)));
        assertEquals(m, MutableBigVector.denseVectorOfSize(2, valueOf(1), valueOf(2)));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(2, valueOf(1), valueOf(2)));
    }

    @Test
    public void testConstructorsSize3() {
        BigVector m = createBigVector(1, 2, 3);

        assertEquals(m, ImmutableBigVector.vector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.denseVector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.sparseVector(1, 2, 3));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(3, 1, 2, 3));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(3, 1, 2, 3));
        assertEquals(m, ImmutableBigVector.vector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.denseVector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.sparseVector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(3, valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(3, valueOf(1), valueOf(2), valueOf(3)));

        assertEquals(m, MutableBigVector.vector(1, 2, 3));
        assertEquals(m, MutableBigVector.denseVector(1, 2, 3));
        assertEquals(m, MutableBigVector.sparseVector(1, 2, 3));
        assertEquals(m, MutableBigVector.denseVectorOfSize(3, 1, 2, 3));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(3, 1, 2, 3));
        assertEquals(m, MutableBigVector.vector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, MutableBigVector.denseVector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, MutableBigVector.sparseVector(valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, MutableBigVector.denseVectorOfSize(3, valueOf(1), valueOf(2), valueOf(3)));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(3, valueOf(1), valueOf(2), valueOf(3)));
    }

    @Test
    public void testConstructorsSize4() {
        BigVector m = createBigVector(1, 2, 3, 4);

        assertEquals(m, ImmutableBigVector.vector(1, 2, 3, 4));
        assertEquals(m, ImmutableBigVector.denseVector(1, 2, 3, 4));
        assertEquals(m, ImmutableBigVector.sparseVector(1, 2, 3, 4));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(4, 1, 2, 3, 4));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(4, 1, 2, 3, 4));
        assertEquals(m, ImmutableBigVector.vector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, ImmutableBigVector.denseVector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, ImmutableBigVector.sparseVector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, ImmutableBigVector.denseVectorOfSize(4, valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, ImmutableBigVector.sparseVectorOfSize(4, valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));

        assertEquals(m, MutableBigVector.vector(1, 2, 3, 4));
        assertEquals(m, MutableBigVector.denseVector(1, 2, 3, 4));
        assertEquals(m, MutableBigVector.sparseVector(1, 2, 3, 4));
        assertEquals(m, MutableBigVector.denseVectorOfSize(4, 1, 2, 3, 4));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(4, 1, 2, 3, 4));
        assertEquals(m, MutableBigVector.vector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, MutableBigVector.denseVector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, MutableBigVector.sparseVector(valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, MutableBigVector.denseVectorOfSize(4, valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
        assertEquals(m, MutableBigVector.sparseVectorOfSize(4, valueOf(1), valueOf(2), valueOf(3),  valueOf(4)));
    }

    @Test
    public void testAdd2() {
        BigVector m1 = createBigVector(10, 20);
        BigVector m2 = createBigVector(1, 2);

        ImmutableBigVector r = m1.add(m2);

        assertEquals(ImmutableBigVector.vector(11, 22), r);
    }

    @Test
    public void testAdd3() {
        BigVector m1 = createBigVector(10, 20, 30);
        BigVector m2 = createBigVector(1, 2, 3);

        ImmutableBigVector r = m1.add(m2);

        assertEquals(ImmutableBigVector.vector(11, 22, 33), r);
    }

    @Test
    public void testSubtract2() {
        BigVector m1 = createBigVector(11, 22);
        BigVector m2 = createBigVector(1, 2);

        ImmutableBigVector r = m1.subtract(m2);

        assertEquals(ImmutableBigVector.vector(10, 20), r);
    }

    @Test
    public void testSubtract3() {
        BigVector m1 = createBigVector(11, 22, 33);
        BigVector m2 = createBigVector(1, 2, 3);

        ImmutableBigVector r = m1.subtract(m2);

        assertEquals(ImmutableBigVector.vector(10, 20, 30), r);
    }

    @Test
    public void testMultiplyScalar2() {
        BigVector m = createBigVector(1, 2);

        ImmutableBigVector r = m.multiply(valueOf(2));

        assertEquals(ImmutableBigVector.vector(2, 4), r);
    }

    @Test
    public void testMultiplyScalar3() {
        BigVector m = createBigVector(1, 2, 3);

        ImmutableBigVector r = m.multiply(valueOf(2));

        assertEquals(ImmutableBigVector.vector(2, 4, 6), r);
    }

    @Test
    public void testDivideScalar2() {
        BigVector m = createBigVector(2, 4);

        ImmutableBigVector r = m.divide(valueOf(2), MathContext.DECIMAL128);

        assertEquals(ImmutableBigVector.vector(1, 2), r);
    }

    @Test
    public void testDivideScalar3() {
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
    public void testDot2() {
        // https://www.wolframalpha.com/input/?i=%281%2C+2%29.%282%2C+3%29
        BigVector m1 = createBigVector(1, 2);
        BigVector m2 = createBigVector(2, 3);

        BigDecimal r = m1.dot(m2);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigDecimal(valueOf(8), r, epsilon);
    }

    @Test
    public void testDot3() {
        // https://www.wolframalpha.com/input/?i=%281%2C+2%2C+3%29.%282%2C+3%2C+4%29
        BigVector m1 = createBigVector(1, 2, 3);
        BigVector m2 = createBigVector(2, 3, 4);

        BigDecimal r = m1.dot(m2);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigDecimal(valueOf(20), r, epsilon);
    }

    @Test
    public void testMagnitude2() {
        BigVector m = createBigVector(1, 2);

        BigDecimal r = m.magnitude(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.00001);
        assertBigDecimal(BigDecimalMath.sqrt(valueOf(1 + 2*2), MathContext.DECIMAL128), r, epsilon);
        assertBigDecimal(valueOf(2.23607), r, epsilon);
    }

    @Test
    public void testMagnitude3() {
        BigVector m = createBigVector(1, 2, 3);

        BigDecimal r = m.magnitude(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.00001);
        assertBigDecimal(BigDecimalMath.sqrt(valueOf(1 + 2*2 + 3*3), MathContext.DECIMAL128), r, epsilon);
        assertBigDecimal(valueOf(3.74166), r, epsilon);
    }

    @Test
    public void testNormalize2() {
        // https://www.wolframalpha.com/input/?i=normalize+%281%2C+2%2C+3%29
        BigVector m = createBigVector(1, 2);

        BigVector r = m.normalize(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(
                1.0 / Math.sqrt(5),
                2.0 / Math.sqrt(5)), r, epsilon);
    }

    @Test
    public void testNormalize3() {
        // https://www.wolframalpha.com/input/?i=normalize+%281%2C+2%2C+3%29
        BigVector m = createBigVector(1, 2, 3);

        BigVector r = m.normalize(MathContext.DECIMAL128);

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(
                1.0 / Math.sqrt(14),
                Math.sqrt(2.0 / 7),
                3.0 / Math.sqrt(14)), r, epsilon);
    }

    @Test
    public void testAbs2() {
        BigVector m = createBigVector(1, -2);

        BigVector r = m.abs();

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(1, 2), r, epsilon);
    }

    @Test
    public void testAbs3() {
        BigVector m = createBigVector(1, -2, 0);

        BigVector r = m.abs();

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(1, 2, 0), r, epsilon);
    }

    @Test
    public void testElementOperation2() {
        BigVector m = createBigVector(1, 2);

        BigVector r = m.elementOperation(b -> b.multiply(valueOf(11)));

        BigDecimal epsilon = valueOf(0.000001);
        assertBigVector(ImmutableBigVector.vector(11, 22), r, epsilon);
    }

    @Test
    public void testElementOperation3() {
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

        for (int i = 0; i < 10; i++) {
            assertEquals(true, ImmutableBigVector.zeroVector(i).isZero());
            assertEquals(true, MutableBigVector.zeroVector(i).isZero());
            assertEquals(true, MutableBigVector.denseZeroVector(i).isZero());
            assertEquals(true, MutableBigVector.sparseZeroVector(i).isZero());
        }
    }
}
