package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
public class PopulationSkewnessCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private PopulationStandardDeviationCalculator standardDeviationCalculator;

    private int count = 0;
    private BigDecimal mean = BigDecimal.ZERO;
    private BigDecimal m2 = BigDecimal.ZERO;
    private BigDecimal m3 = BigDecimal.ZERO;

    public PopulationSkewnessCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.standardDeviationCalculator = new PopulationStandardDeviationCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        count++;
        BigDecimal n = BigDecimal.valueOf(count);
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);

        BigDecimal delta = value.subtract(mean, mathContext);

        mean = mean.add(delta.divide(n, mathContext), mathContext);

        BigDecimal deltaPow2 = delta.multiply(delta, mathContext);
        BigDecimal deltaPow3 = deltaPow2.multiply(delta, mathContext);
        BigDecimal nPow2 = n.multiply(n, mathContext);

        BigDecimal m3Step = deltaPow3.multiply(nMinus1, mathContext).multiply(nMinus2, mathContext).divide(nPow2, mathContext);
        m3Step = m3Step.subtract(BigDecimal.valueOf(3).multiply(delta, mathContext).multiply(m2, mathContext).divide(n, mathContext), mathContext);
        m3 = m3.add(m3Step, mathContext);

        BigDecimal m2Step = deltaPow2.multiply(nMinus1, mathContext).divide(n, mathContext);
        m2 = m2.add(m2Step, mathContext);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal n = BigDecimal.valueOf(count);
        BigDecimal nom = BigDecimalMath.sqrt(n, mathContext).multiply(m3, mathContext);
        BigDecimal denom = BigDecimalMath.pow(m2, BigDecimal.valueOf(1.5), mathContext);
        return nom.divide(denom, mathContext);
    }
}
