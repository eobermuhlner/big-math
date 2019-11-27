package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.benchmark.BenchmarkReport;
import ch.obermuhlner.benchmark.annotation.Before;
import ch.obermuhlner.benchmark.annotation.Benchmark;
import ch.obermuhlner.benchmark.annotation.DoubleParameter;
import ch.obermuhlner.benchmark.annotation.IntParameter;
import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableOperations;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.MathContext;
import java.util.Random;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class BigMatrixBenchmark {
    private static final MathContext MC = MathContext.DECIMAL128;

    @IntParameter({ 1, 2, 5 })
    private int size;

    @DoubleParameter({ 0.1, 0.2, 0.3 })
    private double fillRatio;

    private BigMatrix m1;
    private BigMatrix m2;

    @Before
    private void prepare() {
        Random random = new Random(1);
        m1 = randomBigMatrix(random, size, size, fillRatio);
        m2 = randomBigMatrix(random, size, size, fillRatio);
    }

    @Benchmark
    public void sparseAdd() {
        ImmutableOperations.sparseAdd(m1, m2, MC);
    }

    @Benchmark
    public void sparseMultiply() {
        ImmutableOperations.sparseMultiply(m1, m2, MC);
    }

    private static BigMatrix randomBigMatrix(Random random, int rows, int columns, double fillRatio) {
        return MutableBigMatrix.sparseMatrix(rows, columns, (row, column) -> {
            return random.nextDouble() < fillRatio ? valueOf(random.nextDouble()) : ZERO;
        });
    }

    public static void main(String[] args) {
        BenchmarkReport benchmarkReport = new BenchmarkReport();
        benchmarkReport.report(BigMatrixBenchmark.class);
    }
}
