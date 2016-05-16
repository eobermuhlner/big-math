package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PerformanceBigDecimalMath {

	public static void main(String[] args) {
		MathContext mathContext = new MathContext(200);
		
		performanceReportOverPrecision(
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				BigDecimal.valueOf(3.1),
				10,
				1000,
				10);

//		performanceReportOverValue(
//				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
//				mathContext,
//				+0.01,
//				+2.0,
//				+0.01);
		
//		performanceReportOverValue(
//				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
//				mathContext,
//				+0.5,
//				+100.0,
//				+0.5);
	}

	private static void performanceReportOverValue(BiFunction<BigDecimal, MathContext, BigDecimal> calculation, MathContext mathContext, double xStart, double xEnd, double xStep) {
		performanceReportOverValue(calculation, mathContext, BigDecimal.valueOf(xStart), BigDecimal.valueOf(xEnd), BigDecimal.valueOf(xStep));
	}

	private static void performanceReportOverValue(BiFunction<BigDecimal, MathContext, BigDecimal> calculation, MathContext mathContext, BigDecimal xStart, BigDecimal xEnd, BigDecimal xStep) {
		// warmup
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			calculation.apply(x, MathContext.DECIMAL32);
		}
		
		// real measurement
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			StopWatch stopWatch = new StopWatch();
			
			calculation.apply(x, mathContext);
			long elapsedMillis = stopWatch.getElapsedMillis();
			
			System.out.println(x + "," + elapsedMillis);
		}
	}

	private static void performanceReportOverPrecision(BiFunction<BigDecimal, MathContext, BigDecimal> calculation, BigDecimal value, int precisionStart, int precisionEnd, int precisionStep) {
		// warmup
		for (int i = 0; i < 1000; i++) {
			calculation.apply(value, MathContext.DECIMAL32);
		}
		
		// real measurement
		for (int precision = precisionStart; precision < precisionEnd; precision+=precisionStep) {
			MathContext mathContext = new MathContext(precision);
			
			StopWatch stopWatch = new StopWatch();
			
			calculation.apply(value, mathContext);
			long elapsedMillis = stopWatch.getElapsedMillis();
			
			System.out.println(precision + "," + elapsedMillis);
		}
	}
}
