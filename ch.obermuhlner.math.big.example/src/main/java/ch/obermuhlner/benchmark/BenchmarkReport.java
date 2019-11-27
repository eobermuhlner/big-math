package ch.obermuhlner.benchmark;

import ch.obermuhlner.benchmark.annotation.Before;
import ch.obermuhlner.benchmark.annotation.DoubleParameter;
import ch.obermuhlner.benchmark.annotation.IntParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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

    public void report(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        List<ParameterSequence<?>> parameterSequences = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            IntParameter[] intParameters = field.getAnnotationsByType(IntParameter.class);
            for (IntParameter intParameter : intParameters) {
                int[] intValues = intParameter.value();
                List<Integer> intList = IntStream.of(intValues).boxed().collect(Collectors.toList());
                parameterSequences.add(new ParameterSequence<Integer>(field, instance, intList));
            }

            DoubleParameter[] doubleParameters = field.getAnnotationsByType(DoubleParameter.class);
            for (DoubleParameter doubleParameter : doubleParameters) {
                double[] doubleValues = doubleParameter.value();
                List<Double> doubleList = DoubleStream.of(doubleValues).boxed().collect(Collectors.toList());
                parameterSequences.add(new ParameterSequence<Double>(field, instance, doubleList));
            }
        }

        NestedParameters nestedParameters = new NestedParameters(parameterSequences);
        while (nestedParameters.hasNext()) {
            nestedParameters.next();

            runBeforeMethods(clazz, instance);
            runBenchmarkMethods(clazz, instance);
        }
    }

    private void runBeforeMethods(Class<?> clazz, Object instance) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Before beforeAnnotation = method.getDeclaredAnnotation(Before.class);
            if (beforeAnnotation != null) {
                method.setAccessible(true);

                try {
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void runBenchmarkMethods(Class<?> clazz, Object instance) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ch.obermuhlner.benchmark.annotation.Benchmark benchmarkAnnotation = method.getDeclaredAnnotation(ch.obermuhlner.benchmark.annotation.Benchmark.class);
            if (benchmarkAnnotation != null) {
                method.setAccessible(true);

                Benchmark<Void> benchmark = new Benchmark<>(method.getName(), (data) -> {
                    try {
                        method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

                double elapsedSeconds = benchmarkRunner.measure(benchmark, null);
                double elapsedMillis = elapsedSeconds * 1000;
                System.out.print(String.format("%-30s : %15.5f ms\n", benchmark.name, elapsedMillis));
            }
        }
    }

    private static class NestedParameters {
        private final List<ParameterSequence<?>> parameterSequences;
        private boolean initial;

        public NestedParameters(List<ParameterSequence<?>> parameterSequences) {
            this.parameterSequences = parameterSequences;
            initial = true;
        }

        public boolean hasNext() {
            if (parameterSequences.size() == 0) {
                return false;
            }

            for (ParameterSequence<?> parameterSequence : parameterSequences) {
                if (parameterSequence.hasNext()) {
                    return true;
                }
            }

            return false;
        }

        public void next() {
            if (initial) {
                for (int i = 0; i < parameterSequences.size(); i++) {
                    ParameterSequence<?> parameterSequence = parameterSequences.get(i);
                    if (parameterSequence.hasNext()) {
                        parameterSequence.next();
                    }
                }
                initial = false;
                return;
            }

            for (int i = 0; i < parameterSequences.size(); i++) {
                ParameterSequence<?> parameterSequence = parameterSequences.get(i);
                if (parameterSequence.hasNext()) {
                    parameterSequence.next();
                    return;
                } else {
                    parameterSequence.reset();
                    parameterSequence.next();
                }
            }
        }
    }

    private static class ParameterSequence<T> {
        private final Field field;
        private final Object instance;
        private final List<T> values;
        private int index = 0;

        ParameterSequence(Field field, Object instance, List<T> values) {
            this.field = field;
            this.instance = instance;
            this.values = values;

            this.field.setAccessible(true);
        }

        public void reset() {
            index = 0;
        }

        public boolean hasNext() {
            return index < values.size();
        }

        public void next() {
            try {
                System.out.println("Set " + field + " : " + values.get(index));
                field.set(instance, values.get(index++));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
