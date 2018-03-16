package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

import org.junit.Test;

public class BigComplexMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	@Test
	public void testReciprocal() {
		assertEquals(BigComplex.valueOf(1.2, 2.3).reciprocal(MC), BigComplexMath.reciprocal(BigComplex.valueOf(1.2, 2.3), MC));
	}

	@Test
	public void testConjugate() {
		assertEquals(BigComplex.valueOf(1.2, 2.3).conjugate(), BigComplexMath.conjugate(BigComplex.valueOf(1.2, 2.3)));
	}

	@Test
	public void testAngle() {
		assertEquals(BigComplex.valueOf(1.2, 2.3).angle(MC), BigComplexMath.angle(BigComplex.valueOf(1.2, 2.3), MC));
	}

	@Test
	public void testAbs() {
		assertEquals(BigComplex.valueOf(1.2, 2.3).abs(MC), BigComplexMath.abs(BigComplex.valueOf(1.2, 2.3), MC));
	}

	@Test
	public void testAbsSquare() {
		assertEquals(BigComplex.valueOf(1.2, 2.3).absSquare(MC), BigComplexMath.absSquare(BigComplex.valueOf(1.2, 2.3), MC));
	}

	@Test
	public void testExp() {
		assertPrecisionCalculation(
				BigComplex.valueOf("-1.1312043837568136384312552555107947106288679958265257502", "2.4717266720048189276169308935516645327361903692410081842", MC),
		   		(mc) -> BigComplexMath.exp(BigComplex.valueOf(1, 2), mc),
		   	5);
	}

	@Test
	public void testSin() {
		assertPrecisionCalculation(
				BigComplex.valueOf("3.16577851321616814674073461719190553837911076789146893228", "1.95960104142160589707035204998935827843632016018455965880", MC),
		  		(mc) -> BigComplexMath.sin(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testCos() {
		assertPrecisionCalculation(
				BigComplex.valueOf("2.03272300701966552943634344849951426373199040663875238194", "-3.05189779915180005751211568689510545288843761773331964466", MC),
		   		(mc) -> BigComplexMath.cos(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testTan() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.03381282607989669028437055972528730164018766933075587436", "1.014793616146633568117054175417967614163217471123533242", MC),
		   		(mc) -> BigComplexMath.tan(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testLog() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.80471895621705018730037966661309381976280067713425886095", "1.1071487177940905030170654601785370400700476454014326466", MC),
		   		(mc) -> BigComplexMath.log(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testSqrt() {
		assertPrecisionCalculation(
				BigComplex.valueOf("1.2720196495140689642524224617374914917156080418400962486", "0.78615137775742328606955858584295892952312205783772323766", MC),
				(mc) -> BigComplexMath.sqrt(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testPowInt() {
		assertPrecisionCalculation(
				BigComplex.valueOf("18456.81197182559", "-7537.93433754262", MC),
		   		(mc) -> BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), 11, mc),
		   5);

		assertPrecisionCalculation(
				BigComplex.valueOf("0.00004643522633206022281216609142474444925257050125807831", "0.00001896457999216310228315702790913198870386323455307511", MC),
		   		(mc) -> BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), -11, mc),
		   		5);
	}
	
	@Test
	public void testPow() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.93025840620844374948965939784803335557080436887094399412", "2.5254359132812654282958795629575897052282541443174302618", MC),
		   		(mc) -> BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), BigDecimal.valueOf(1.1), mc),
		   		5);
	}

	@Test
	public void testPowComplex() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.84004892508974359637454837539731545717714689412422564692", "0.25134081792349062667714080016033852435880339768328815847", MC),
		   		(mc) -> BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), BigComplex.valueOf(0.1, 0.2), mc),
		   		5);
	}

	@Test
	public void testRoot() {
		assertPrecisionCalculation(
				BigComplex.valueOf("1.2589858696615772964532950314498058030426856758251923734", "0.48693816197569980888611771300161883360684382303096712193", MC),
		   		(mc) -> BigComplexMath.root(BigComplex.valueOf(1.1, 2.2), BigDecimal.valueOf(3), mc),
		   		5);
	}

	@Test
	public void testRootComplex() {
		assertPrecisionCalculation(
				BigComplex.valueOf("1.4494235461237517229913668192323085954480223365111721042", "0.17040446483770403353362509072059809846680452097000085527", MC),
		   		(mc) -> BigComplexMath.root(BigComplex.valueOf(1.1, 2.2), BigComplex.valueOf(3, 2), mc),
		   		5);
	}

	@Test
	public void testAsin() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.42707858639247612548064688331895685930333615088099242008", "1.5285709194809981612724561847936733932886832962358541247", MC),
		   		(mc) -> BigComplexMath.asin(BigComplex.valueOf(1, 2), mc),
		   		5);
	}

	@Test
	public void testAcos() {
		assertPrecisionCalculation(
				BigComplex.valueOf("1.14371774040242049375067480832079458279524854880656049040", "-1.52857091948099816127245618479367339328868329623585412477", MC),
		   		(mc) -> BigComplexMath.acos(BigComplex.valueOf(1, 2), mc),
		   		5);
	}

	@Test
	public void testAtan() {
		assertPrecisionCalculation(
				BigComplex.valueOf("1.3389725222944935611241935759091442410843161725444927785", "0.40235947810852509365018983330654690988140033856712943047", MC), 
		   		(mc) -> BigComplexMath.atan(BigComplex.valueOf(1, 2), mc),
		   5);
	}

	@Test
	public void testAcot() {
		assertPrecisionCalculation(
				BigComplex.valueOf("0.23182380450040305810712811573060720101426852714306013190", "-0.40235947810852509365018983330654690988140033856712943047", MC), 
		   		(mc) -> BigComplexMath.acot(BigComplex.valueOf(1, 2), mc),
		   		5);
	}

	private void assertPrecisionCalculation(BigComplex expected, Function<MathContext, BigComplex> precisionCalculation, int startPrecision) {
		assertPrecisionCalculation(expected, precisionCalculation, startPrecision, expected.re.precision());
	}

	private void assertPrecisionCalculation(BigComplex expected, Function<MathContext, BigComplex> precisionCalculation, int startPrecision, int endPrecision) {
		int precision = startPrecision;
		while (precision <= endPrecision) {
			MathContext mathContext = new MathContext(precision);
			System.out.println("precision=" + precision + " " + expected.round(mathContext));
			assertEquals(
			   "precision=" + precision,
			   expected.round(mathContext).toString(),
			   precisionCalculation.apply(mathContext).toString());
			precision += 5;
		}
	}

}
