package ch.obermuhlner.math.big.example;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;
import ch.obermuhlner.math.big.stream.BigDecimalStream;

import static ch.obermuhlner.math.big.DefaultBigDecimalMath.*;

public class DefaultBigDecimalMathExample {
    public static void main(String[] args) {
        runDefaultExample();
        runWithPrecisionExample();
        runWithPrecision2Example();
    }

    private static void runDefaultExample() {
        System.out.println("Pi[default]: " + pi());
    }

    private static void runWithPrecisionExample() {
        System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
        DefaultBigDecimalMath.withPrecision(5, () -> {
            System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
            DefaultBigDecimalMath.withPrecision(10, () -> {
                System.out.println("Pi[10]: " + DefaultBigDecimalMath.pi());
            });
            System.out.println("Pi[5]: " + DefaultBigDecimalMath.pi());
        });
        System.out.println("Pi[default]: " + DefaultBigDecimalMath.pi());
    }

    private static void runWithPrecisionCompactExample() {
        System.out.println("Pi[default]: " + pi());
        withPrecision(5, () -> {
            System.out.println("Pi[5]: " + pi());
            withPrecision(10, () -> {
                System.out.println("Pi[10]: " + pi());
            });
            System.out.println("Pi[5]: " + pi());
        });
        System.out.println("Pi[default]: " + pi());
    }

    private static void runWithPrecision2Example() {
        DefaultBigDecimalMath.withPrecision(5, () -> {
            BigDecimalStream.range(0.0, 1.0, 0.01, getMathContext())
                    .map(b -> cos(b))
                    .map(b -> "sequential " + Thread.currentThread().getName() + " [5]: " + b)
                    .forEach(System.out::println);

            BigDecimalStream.range(0.0, 1.0, 0.01, getMathContext())
                    .parallel()
                    .map(b -> cos(b))
                    .map(b -> "parallel " + Thread.currentThread().getName() + " [?]: " + b)
                    .forEach(System.out::println);
        });
    }
}

