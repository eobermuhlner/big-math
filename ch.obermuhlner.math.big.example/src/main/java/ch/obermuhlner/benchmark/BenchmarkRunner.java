package ch.obermuhlner.benchmark;

public class BenchmarkRunner<T> {

    private static final double nanosPerSecond = 1_000_000_000.0;
    private static final int maxInnerCount = 1000;

    private double warmupSeconds = 0.1;
    private double measureSeconds = 1.0;

    public double measure(Benchmark benchmark, T data) {
        double singleRunSeconds = measure(benchmark, data, 1);

        if (singleRunSeconds > measureSeconds) {
            return singleRunSeconds;
        }

        singleRunSeconds = measure(benchmark, data, warmupSeconds, singleRunSeconds, 1);

        return measure(benchmark, data, measureSeconds, singleRunSeconds, 10);
    }

    private double measure(Benchmark benchmark, T data, double limitSeconds, double singleRunSeconds, int runCount) {
        double seconds = limitSeconds;

        double minElapsedSeconds = Double.MAX_VALUE;
        int runIndex = 0;
        while (seconds > 0 && runIndex < runCount) {
            int innerCount = estimateInnerCount(runCount, singleRunSeconds);
            double elapsedSeconds = measure(benchmark, data, innerCount);
            minElapsedSeconds = Math.min(minElapsedSeconds, elapsedSeconds);

            seconds -= elapsedSeconds;

            runIndex++;
        }

        return minElapsedSeconds;
    }

    private int estimateInnerCount(int runCount, double singleRunSeconds) {
        if (singleRunSeconds == 0) {
            return maxInnerCount;
        }

        double roughInnerCount = measureSeconds / runCount / singleRunSeconds;
        if (roughInnerCount > maxInnerCount) {
            return maxInnerCount;
        }

        int innerCount = (int)roughInnerCount;
        if (innerCount <= 0) {
            innerCount = 1;
        }
        return innerCount;
    }

    private void warmup(Benchmark benchmark, T data, double seconds) {
        while (seconds > 0) {
            double elapsedSeconds = measure(benchmark, data, 1);
            seconds -= elapsedSeconds;
        }
    }

    private double measure(Benchmark benchmark, T data, int count) {
        long startNanos = System.nanoTime();
        for (int i = 0; i < count; i++) {
            benchmark.run(data);
        }
        long endNanos = System.nanoTime();
        long deltaNanos = endNanos - startNanos;
        return deltaNanos / nanosPerSecond / count;
    }
}
