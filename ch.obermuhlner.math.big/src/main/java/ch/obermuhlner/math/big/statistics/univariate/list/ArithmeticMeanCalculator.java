package ch.obermuhlner.math.big.statistics.univariate.list;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class ArithmeticMeanCalculator implements UnivariateListCalculator<BigDecimal> {

    private final MathContext mathContext;

    public ArithmeticMeanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getResult(List<BigDecimal> values) {
        int count = values.size();

        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal value : values) {
            sum = sum.add(value, mathContext);
        }

        return sum.divide(BigDecimal.valueOf(count), mathContext);
    }
}
