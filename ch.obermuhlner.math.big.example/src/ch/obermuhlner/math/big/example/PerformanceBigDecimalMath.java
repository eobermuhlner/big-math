package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PerformanceBigDecimalMath {

	public static void main(String[] args) {
		MathContext mathContext = new MathContext(200);
		
		performanceReportOverX(
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				mathContext,
				+0.01,
				+2.0,
				+0.01);
		
//		performanceReportOverX(
//				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
//				mathContext,
//				+0.5,
//				+100.0,
//				+0.5);
	}

	private static void performanceReportOverX(BiFunction<BigDecimal, MathContext, BigDecimal> calculation, MathContext mathContext, double xStart, double xEnd, double xStep) {
		performanceReport(calculation, mathContext, BigDecimal.valueOf(xStart), BigDecimal.valueOf(xEnd), BigDecimal.valueOf(xStep));
	}

	private static void performanceReport(BiFunction<BigDecimal, MathContext, BigDecimal> calculation, MathContext mathContext, BigDecimal xStart, BigDecimal xEnd, BigDecimal xStep) {
		// warmup
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			calculation.apply(x, MathContext.DECIMAL32);
		}
		
		// real measurement
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			StopWatch stopWatch = new StopWatch();
			
			BigDecimal result = calculation.apply(x, mathContext);
			long elapsedMillis = stopWatch.getElapsedMillis();
			
			System.out.println(x + "," + elapsedMillis);
			//System.out.printf("%20.2f : %10d ms\n", x, elapsedMillis);
		}
	}
}
