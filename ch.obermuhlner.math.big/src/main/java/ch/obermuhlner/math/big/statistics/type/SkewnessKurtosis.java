package ch.obermuhlner.math.big.statistics.type;

import java.math.BigDecimal;

public class SkewnessKurtosis {
    public final BigDecimal skewness;
    public final BigDecimal kurtosis;

    public SkewnessKurtosis(BigDecimal skewness, BigDecimal kurtosis) {
        this.skewness = skewness;
        this.kurtosis = kurtosis;
    }

    @Override
    public String toString() {
        return "(skewness=" + skewness + ", kurtosis=" + kurtosis + ")";
    }
}
