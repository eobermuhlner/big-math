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

    public static void checkIndex(BigVector matrix, int row) {
        checkIndex(matrix, "index", row);
    }

    public static void checkIndex(BigVector matrix, String name, int row) {
        checkIndex(matrix.size(), name, row);
    }

    public static void checkIndex(int rows, int index) {
        checkIndex(rows, "index", index);
    }

    public static void checkIndex(int size, String name, int index) {
        if (index < 0 ) {
            throw new IllegalArgumentException(name + " < 0 : " + index);
        }
        if (index >= size) {
            throw new IllegalArgumentException(name + " >= " + size + " : " + index);
        }
    }

}
