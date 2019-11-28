package ch.obermuhlner.math.big.vector.internal;

import ch.obermuhlner.math.big.vector.BigVector;

public class VectorUtils {

    public static void checkSize(BigVector vector, int size) {
        if (vector.size() != size) {
            throw new IllegalArgumentException("size != " + size + " : " + vector.size());
        }
    }

    public static void checkSameSize(BigVector vector, BigVector other) {
        if (vector.size() != other.size()) {
            throw new IllegalArgumentException("size != other.size : " + vector.size() + " != " + other.size());
        }
    }
}
