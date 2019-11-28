package ch.obermuhlner.util;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.vector.BigVector;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class AssertUtil {
    public static void assertBigDecimal(BigDecimal expected, BigDecimal actual) {
        assertTrue("Failed expected " + expected + " = actual " + actual, expected.compareTo(actual) == 0);
    }

    public static void assertBigDecimal(BigDecimal expected, BigDecimal actual, BigDecimal delta) {
        BigDecimal expectedLower = expected.subtract(delta);
        if (actual.compareTo(expectedLower) < 0) {
            fail("Failed actual " + actual + " below expected " + expected + " - delta " + delta + " : " + actual + " < " + expectedLower);
        }

        BigDecimal expectedUpper = expected.add(delta);
        if (actual.compareTo(expectedUpper) > 0) {
            fail("Failed actual " + actual + " above expected " + expected + " + delta " + delta + " : " + actual + " > " + expectedUpper);
        }
    }

    public static void assertBigMatrix(BigMatrix expected, BigMatrix actual, BigDecimal epsilon) {
        assertEquals("rows", expected.rows(), actual.rows());
        assertEquals("columns", expected.columns(), actual.columns());

        for (int row = 0; row < expected.rows(); row++) {
            for (int column = 0; column < expected.columns(); column++) {
                assertBigDecimal(expected.get(row, column), actual.get(row, column), epsilon);
            }
        }
    }


    public static void assertBigVector(BigVector expected, BigVector actual, BigDecimal epsilon) {
        assertEquals("size", expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertBigDecimal(expected.get(i), actual.get(i), epsilon);
        }
    }
}
