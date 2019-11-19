package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.benchmark.Benchmark;
import ch.obermuhlner.benchmark.BenchmarkReport;
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

    private static final MathContext MC = MathContext.DECIMAL64;

    public static void main(String[] args) {

        BenchmarkReport report = new BenchmarkReport();
        for (int fillPercent = 0; fillPercent <= 100; fillPercent+=10) {
            double fillRatio = fillPercent * 0.01;
            report.report(
                    "perf_matrix_add_" + fillPercent + "%",
                    "Size",
                    Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 150, 200),
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseAdd(sparse;sparse)", (d) -> {
                        ImmutableOperations.sparseAdd(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseAdd(sparse;sparse)", (d) -> {
                        ImmutableOperations.denseAdd(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseAdd(dense;dense)", (d) -> {
                        ImmutableOperations.denseAdd(d[2], d[3], MC);
                    })
            );
            report.report(
                    "perf_matrix_multiply_" + fillPercent + "%",
                    "Size",
                    Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 150, 200),
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseMultiply(sparse;sparse)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseMultiply(sparse;sparse)", (d) -> {
                        ImmutableOperations.denseMultiply(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseMultiply(dense;dense)", (d) -> {
                        ImmutableOperations.denseMultiply(d[2], d[3], MC);
                    })
            );
            BigDecimal pi = valueOf(Math.PI);
            report.report(
                    "perf_matrix_multiplyScalar_" + fillPercent + "%",
                    "Size",
                    Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 150, 200),
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseMultiply(sparse;value)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[0], pi, MC);
                    }),
                    new Benchmark<>("denseMultiply(sparse;value)", (d) -> {
                        ImmutableOperations.denseMultiply(d[0], pi, MC);
                    }),
                    new Benchmark<>("denseMultiply(dense;value)", (d) -> {
                        ImmutableOperations.denseMultiply(d[2], pi, MC);
                    })
            );
        }
    }

    private static BigMatrix[] createSizedBigMatrices(int size, double fillRatio) {
        Random random = new Random(1);

        ImmutableBigMatrix sparse1 = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(random, size, size, fillRatio));
        ImmutableBigMatrix sparse2 = ImmutableBigMatrix.sparseMatrix(randomBigMatrix(random, size, size, fillRatio));

        return new BigMatrix[] {
                sparse1,
                sparse2,
                ImmutableBigMatrix.denseMatrix(sparse1),
                ImmutableBigMatrix.denseMatrix(sparse2),
        };
    }

    private static BigMatrix randomBigMatrix(Random random, int rows, int columns, double fillRatio) {
        return MutableBigMatrix.sparseMatrix(rows, columns, (row, column) -> {
            return random.nextDouble() < fillRatio ? valueOf(random.nextDouble()) : ZERO;
        });
    }
}
