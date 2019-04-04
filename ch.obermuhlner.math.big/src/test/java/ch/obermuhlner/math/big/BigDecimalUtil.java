package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class BigDecimalUtil {
    public static List<BigDecimal> toBigDecimals(double... values) {
        return Arrays.stream(values)
                .mapToObj(v -> BigDecimal.valueOf(v))
                .collect(Collectors.toList());
    }

    public static void assertBigDecimal(BigDecimal expected, BigDecimal actual) {
        if (expected.compareTo(actual) != 0) {
            fail("expected=" + expected + " != actual=" + actual);
        }
    }

    public static BigDecimal[] tuple(BigDecimal... tuple) {
        return tuple;
    }

    public static BigDecimal[] tuple(double... tuple) {
        return Arrays.stream(tuple)
                .mapToObj(v -> BigDecimal.valueOf(v))
                .collect(Collectors.toList()).toArray(new BigDecimal[tuple.length]);
    }
}
