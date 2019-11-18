package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableOperations;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Random;

import static java.math.BigDecimal.*;

public class PerformanceBigMatrix {

    private static final int FAST_REPEATS = 3;

    private static final MathContext MC = MathContext.DECIMAL64;

    public static void main(String[] args) {
        for (int size : Arrays.asList(/*10, 20, 50, */ 100, 200)) {
            performanceReport_add(size);
            performanceReport_multiplyScalar(size);
            performanceReport_multiply(size);
        }
    }

    private static void performanceReport_add(int size) {
        int sparse1 = 0;
        int sparse2 = 1;
        int dense1 = 2;
        int dense2 = 3;
        PerformanceBigDecimalMath.performanceReportOverLambda(
                "perf_matrix_add_" + size + "x" + size + ".csv",
                10,
                1,
                FAST_REPEATS,
                (i) -> {
                    BigMatrix[] matrices = new BigMatrix[4];
                    double fillRatio = i * 0.1;
                    matrices[sparse1] = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(size, size, fillRatio));
                    matrices[sparse2] = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(size, size, fillRatio));
                    matrices[dense1] = ImmutableBigMatrix.denseMatrix(matrices[sparse1]);
                    matrices[dense2] = ImmutableBigMatrix.denseMatrix(matrices[sparse2]);
                    return matrices;
                },
                Arrays.asList("sparseAdd(sparse;sparse)", "denseAdd(sparse;sparse)", "denseAdd(dense;dense)"),
                (matrices) -> ImmutableOperations.sparseAdd(matrices[sparse1], matrices[sparse2], MC),
                (matrices) -> ImmutableOperations.denseAdd(matrices[sparse1], matrices[sparse2], MC),
                (matrices) -> ImmutableOperations.denseAdd(matrices[dense1], matrices[dense2], MC));
    }

    private static void performanceReport_multiplyScalar(int size) {
        BigDecimal value = valueOf(Math.PI);

        int sparse1 = 0;
        int dense1 = 2;
        PerformanceBigDecimalMath.performanceReportOverLambda(
                "perf_matrix_multiplyScalar_" + size + "x" + size + ".csv",
                10,
                1,
                FAST_REPEATS,
                (i) -> {
                    BigMatrix[] matrices = new BigMatrix[4];
                    double fillRatio = i * 0.1;
                    matrices[sparse1] = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(size, size, fillRatio));
                    matrices[dense1] = ImmutableBigMatrix.denseMatrix(matrices[sparse1]);
                    return matrices;
                },
                Arrays.asList("sparseMultiply(sparse;v)", "denseMultiply(sparse;v)", "denseMultiply(dense;v)"),
                (matrices) -> ImmutableOperations.sparseMultiply(matrices[sparse1], value, MC),
                (matrices) -> ImmutableOperations.denseMultiply(matrices[sparse1], value, MC),
                (matrices) -> ImmutableOperations.denseMultiply(matrices[dense1], value, MC));
    }

    private static void performanceReport_multiply(int size) {
        int sparse1 = 0;
        int sparse2 = 1;
        int dense1 = 2;
        int dense2 = 3;
        PerformanceBigDecimalMath.performanceReportOverLambda(
                "perf_matrix_multiply_" + size + "x" + size + ".csv",
                10,
                1,
                FAST_REPEATS,
                (i) -> {
                    BigMatrix[] matrices = new BigMatrix[4];
                    double fillRatio = i * 0.1;
                    matrices[sparse1] = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(size, size, fillRatio));
                    matrices[sparse2] = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(size, size, fillRatio));
                    matrices[dense1] = ImmutableBigMatrix.denseMatrix(matrices[sparse1]);
                    matrices[dense2] = ImmutableBigMatrix.denseMatrix(matrices[sparse2]);
                    return matrices;
                },
                Arrays.asList("sparseMultiply(sparse;sparse)", "denseMultiply(sparse;sparse)", "denseMultiply(dense;dense)"),
                (matrices) -> ImmutableOperations.sparseMultiply(matrices[sparse1], matrices[sparse2], MC),
                (matrices) -> ImmutableOperations.denseMultiply(matrices[sparse1], matrices[sparse2], MC),
                (matrices) -> ImmutableOperations.denseMultiply(matrices[dense1], matrices[dense2], MC));
    }

    private static BigMatrix randomBigMatrix(int rows, int columns, double fillRatio) {
        Random random = new Random(1);
        return MutableBigMatrix.sparseMatrix(rows, columns, (row, column) -> {
            return random.nextDouble() < fillRatio ? valueOf(random.nextDouble()) : ZERO;
        });
    }
}
