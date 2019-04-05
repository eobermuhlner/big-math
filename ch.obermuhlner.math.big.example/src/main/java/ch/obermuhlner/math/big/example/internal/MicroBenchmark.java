package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.example.StopWatch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MicroBenchmark {
    private static final double AVERAGE_PERCENTILE = 0.50;

    public static <T, U> void performanceReportOverLambda(String outputDirectoy, String name, int start, int end, int step, int warmupRepeats, int repeats, Function<Integer, T> convert, List<String> functionNames, Function<T, U>... functions) {
        StopWatch stopWatch = new StopWatch();
        System.out.println("Writing  " + name);

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputDirectoy + name))) {
            performanceReportOverLambdaToWriter(writer, start, end, step, warmupRepeats, repeats, convert, functionNames, functions);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Finished in " + stopWatch);
    }

    private static <T, U> void performanceReportOverLambdaToWriter(PrintWriter writer, int start, int end, int step, int warmupRepeats, int repeats, Function<Integer, T> convert, List<String> functionNames, Function<T, U>... functions) {
        int innerRepeats = 10;

        int warmupEnd = Math.min(start+step*5, end);

        // warmup
        for (int warmupCount = 0; warmupCount < warmupRepeats; warmupCount++) {
            for (int valueIndex = start; valueIndex <= warmupEnd; valueIndex+=step) {
                for (Function<T, U> function : functions) {
                    T value = convert.apply(valueIndex);
                    function.apply(value);
                }
            }
        }

        // print headers
        writer.printf("%8s", "value");
        for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
            writer.print(",");
            writer.printf("%8s", functionNames.get(fIndex));
        }
        writer.println();

        // print types
        writer.printf("%8s", "number");
        for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
            writer.print(",");
            writer.printf("%8s", "number");
        }
        writer.println();

        // prepare data storage
        int pCount = 0;
        for (int valueIndex = start; valueIndex <= end; valueIndex += step) {
            pCount++;
        }

        long[][][] nanosFunctionValueRepeat = new long[functions.length][][];
        for (int fIndex = 0; fIndex < functions.length; fIndex++) {
            nanosFunctionValueRepeat[fIndex] = new long[pCount][];

            for (int pIndex = 0; pIndex < pCount; pIndex++) {
                nanosFunctionValueRepeat[fIndex][pIndex] = new long[repeats];
            }
        }

        // real measurement
        for (int rIndex = 0; rIndex < repeats; rIndex++) {
            for (int fIndex = 0; fIndex < functions.length; fIndex++) {
                int pIndex = 0;
                for (int valueIndex = start; valueIndex <= end; valueIndex += step) {
                    Function<T, U> calculation = functions[fIndex];
                    T value = convert.apply(valueIndex);

                    try {
                        StopWatch stopWatch = new StopWatch();
                        for (int innerIndex = 0; innerIndex < innerRepeats; innerIndex++) {
                            calculation.apply(value);
                        }
                        nanosFunctionValueRepeat[fIndex][pIndex][rIndex] = stopWatch.getElapsedNanos();
                    } catch (Exception ex) {
                        // ignore
                    }
                    pIndex++;
                }

            }
            System.out.print(".");
        }
        System.out.println();

        // write report
        {
            int pIndex = 0;
            for (int valueIndex = start; valueIndex <= end; valueIndex += step) {
                writer.printf("%8d", valueIndex);
                for (int fIndex = 0; fIndex < functions.length; fIndex++) {
                    writer.print(",");
                    double elapsedNanos = averagePercentile(AVERAGE_PERCENTILE, nanosFunctionValueRepeat[fIndex][pIndex]) / innerRepeats;
                    writer.printf("%8.3f", elapsedNanos);
                }
                pIndex++;
                writer.println();
            }
        }
    }

    private static double averagePercentile(double percentile, long[] values) {
        Arrays.sort(values);

        int startIndex = (int) (values.length / 2 - values.length * percentile / 2);
        int endIndex = (int) (values.length / 2 + values.length * percentile / 2);

        double sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += values[i];
        }

        return (sum / (endIndex - startIndex));
    }
}
