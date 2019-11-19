package ch.obermuhlner.benchmark;

import java.util.function.Consumer;

public class Benchmark<T> {
    public final String name;
    private Consumer<T> benchmarkFunction;

    public Benchmark(String name, Consumer<T> benchmarkFunction) {
        this.name = name;
        this.benchmarkFunction = benchmarkFunction;
    }

    public void run(T data) {
        benchmarkFunction.accept(data);
    }
}
