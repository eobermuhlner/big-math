package ch.obermuhlner.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;

public class BenchmarkReport {

    private final BenchmarkRunner benchmarkRunner;

    public BenchmarkReport() {
        this(new BenchmarkRunner());
    }

    public BenchmarkReport(BenchmarkRunner benchmarkRunner) {
        this.benchmarkRunner = benchmarkRunner;
    }

    public <T> void report(String name, T data, Benchmark<T>... benchmarks) {
        System.out.println(name);
        try (PrintWriter out = new PrintWriter(new FileWriter(name + ".csv"))) {
            for (int i = 0; i < benchmarks.length; i++) {
                Benchmark benchmark = benchmarks[i];
                if (i > 0) {
                    out.print(", ");
                }
                out.print(benchmark.name);
            }
            out.println();

            for (int i = 0; i < benchmarks.length; i++) {
                Benchmark benchmark = benchmarks[i];
                if (i > 0) {
                    out.print(", ");
                }

                double elapsedSeconds = benchmarkRunner.measure(benchmark, data);
                double elapsedMillis = elapsedSeconds * 1000;
                out.print(elapsedMillis);
                System.out.print(String.format("%-30s : %15.5f ms\n", benchmark.name, elapsedMillis));
            }
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <V, T> void report(String name, String inputName, List<V> inputSequence, Function<V, T> converter, Benchmark<T>... benchmarks) {
        System.out.println(name);
        try (PrintWriter out = new PrintWriter(new FileWriter(name + ".csv"))) {
            out.print(inputName);
            for (int i = 0; i < benchmarks.length; i++) {
                Benchmark benchmark = benchmarks[i];
                out.print(", ");
                out.print(benchmark.name);
            }
            out.println();

            for (V input : inputSequence) {
                System.out.println(name + " [" + input + "]");
                out.print(input);

                T data = converter.apply(input);

                for (Benchmark benchmark : benchmarks) {
                    out.print(", ");
                    double elapsedSeconds = benchmarkRunner.measure(benchmark, data);
                    double elapsedMillis = elapsedSeconds * 1000;
                    out.print(elapsedMillis);

                    System.out.print(String.format("%-30s : %15.5f ms\n", benchmark.name, elapsedMillis));
                }
                out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
