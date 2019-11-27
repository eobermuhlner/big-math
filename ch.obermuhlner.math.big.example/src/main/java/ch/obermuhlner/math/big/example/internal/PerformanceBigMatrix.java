package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.benchmark.Benchmark;
import ch.obermuhlner.benchmark.BenchmarkReport;
import ch.obermuhlner.benchmark.BenchmarkRunner;
import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableOperations;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.math.BigDecimal.*;

public class PerformanceBigMatrix {

    private static final MathContext MC = MathContext.DECIMAL64;

    public static void main(String[] args) {
        performanceReport_basicOperations();
    }

    private static void performanceReport_basicOperations() {
        //List<Integer> sizes = Arrays.asList(10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000);
        List<Integer> sizes = Arrays.asList(10, 50, 100, 200, 300, 400, 500);

        BenchmarkReport report = new BenchmarkReport(new BenchmarkRunner(0.5));
        //for (int fillPercent : Arrays.asList(0, 1, 2, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)) {
        for (int fillPercent : Arrays.asList(1, 2, 5, 10, 20, 30)) {
            double fillRatio = fillPercent * 0.01;
            /*
            report.report(
                    "perf_matrix_add_" + fillPercent + "%",
                    "Size",
                    sizes,
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseAdd(sparse;sparse)", (d) -> {
                        ImmutableOperations.sparseAdd(d[0], d[1], MC);
                    }),
                    new Benchmark<>("sparseAdd(dense;dense)", (d) -> {
                        ImmutableOperations.sparseAdd(d[2], d[3], MC);
                    }),
                    new Benchmark<>("denseAdd(sparse;sparse)", (d) -> {
                        ImmutableOperations.denseAdd(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseAdd(dense;dense)", (d) -> {
                        ImmutableOperations.denseAdd(d[2], d[3], MC);
                    }),
                    new Benchmark<>("autoAdd(sparse;sparse)", (d) -> {
                        ImmutableOperations.autoAdd(d[0], d[1], MC);
                    }),
                    new Benchmark<>("autoAdd(dense;dense)", (d) -> {
                        ImmutableOperations.autoAdd(d[2], d[3], MC);
                    })
            );
            report.report(
                    "perf_matrix_multiply_" + fillPercent + "%",
                    "Size",
                    sizes,
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseMultiply(sparse;sparse)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[0], d[1], MC);
                    }),
                    new Benchmark<>("sparseMultiply(dense;dense)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[2], d[3], MC);
                    }),
                    new Benchmark<>("denseMultiply(sparse;sparse)", (d) -> {
                        ImmutableOperations.denseMultiply(d[0], d[1], MC);
                    }),
                    new Benchmark<>("denseMultiply(dense;dense)", (d) -> {
                        ImmutableOperations.denseMultiply(d[2], d[3], MC);
                    }),
                    new Benchmark<>("autoMultiply(sparse;sparse)", (d) -> {
                        ImmutableOperations.autoMultiply(d[0], d[1], MC);
                    }),
                    new Benchmark<>("autoMultiply(dense;dense)", (d) -> {
                        ImmutableOperations.autoMultiply(d[2], d[3], MC);
                    })
            );
            BigDecimal pi = valueOf(Math.PI);
            report.report(
                    "perf_matrix_multiplyScalar_" + fillPercent + "%",
                    "Size",
                    sizes,
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseMultiply(sparse;value)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[0], pi, MC);
                    }),
                    new Benchmark<>("sparseMultiply(dense;value)", (d) -> {
                        ImmutableOperations.sparseMultiply(d[2], pi, MC);
                    }),
                    new Benchmark<>("denseMultiply(sparse;value)", (d) -> {
                        ImmutableOperations.denseMultiply(d[0], pi, MC);
                    }),
                    new Benchmark<>("denseMultiply(dense;value)", (d) -> {
                        ImmutableOperations.denseMultiply(d[2], pi, MC);
                    }),
                    new Benchmark<>("autoMultiply(sparse;value)", (d) -> {
                        ImmutableOperations.autoMultiply(d[0], pi, MC);
                    }),
                    new Benchmark<>("autoMultiply(dense;value)", (d) -> {
                        ImmutableOperations.autoMultiply(d[2], pi, MC);
                    })
            );
            report.report(
                    "perf_matrix_sum_" + fillPercent + "%",
                    "Size",
                    sizes,
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseSum(sparse)", (d) -> {
                        ImmutableOperations.sparseSum(d[0], MC);
                    }),
                    new Benchmark<>("sparseSum(dense)", (d) -> {
                        ImmutableOperations.sparseSum(d[2], MC);
                    }),
                    new Benchmark<>("denseSum(sparse)", (d) -> {
                        ImmutableOperations.denseSum(d[0], MC);
                    }),
                    new Benchmark<>("denseSum(dense)", (d) -> {
                        ImmutableOperations.denseSum(d[2], MC);
                    }),
                    new Benchmark<>("autoSum(sparse)", (d) -> {
                        ImmutableOperations.autoSum(d[0], MC);
                    }),
                    new Benchmark<>("autoSum(dense)", (d) -> {
                        ImmutableOperations.autoSum(d[2], MC);
                    })
            );
            */
            report.report(
                    "perf_matrix_product_" + fillPercent + "%",
                    "Size",
                    sizes,
                    size -> createSizedBigMatrices(size, fillRatio),
                    new Benchmark<>("sparseProduct(sparse)", (d) -> {
                        ImmutableOperations.sparseProduct(d[0], MC);
                    }),
                    new Benchmark<>("sparseProduct(dense)", (d) -> {
                        ImmutableOperations.sparseProduct(d[2], MC);
                    }),
                    new Benchmark<>("denseProduct(sparse)", (d) -> {
                        ImmutableOperations.denseProduct(d[0], MC);
                    }),
                    new Benchmark<>("denseProduct(dense)", (d) -> {
                        ImmutableOperations.denseProduct(d[2], MC);
                    }),
                    new Benchmark<>("autoProduct(sparse)", (d) -> {
                        ImmutableOperations.autoProduct(d[0], MC);
                    }),
                    new Benchmark<>("autoSum(dense)", (d) -> {
                        ImmutableOperations.autoProduct(d[2], MC);
                    })
            );
        }
    }

    private static BigMatrix[] createSizedBigMatrices(Integer size, double fillRatio) {
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
