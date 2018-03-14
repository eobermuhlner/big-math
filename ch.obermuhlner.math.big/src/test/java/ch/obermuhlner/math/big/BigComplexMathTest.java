package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigComplexMathTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	@Test
	public void testExp() {
		assertEquals(
				BigComplex.valueOf("-1.1312043837568136384312552555107947106288679958265257502", "2.4717266720048189276169308935516645327361903692410081842", MC), 
				BigComplexMath.exp(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testSin() {
		assertEquals(
				BigComplex.valueOf("3.16577851321616814674073461719190553837911076789146893228", "1.95960104142160589707035204998935827843632016018455965880", MC), 
				BigComplexMath.sin(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testCos() {
		assertEquals(
				BigComplex.valueOf("2.03272300701966552943634344849951426373199040663875238194", "-3.05189779915180005751211568689510545288843761773331964466", MC), 
				BigComplexMath.cos(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testTan() {
		assertEquals(
				BigComplex.valueOf("0.03381282607989669028437055972528730164018766933075587436", "1.014793616146633568117054175417967614163217471123533242", MC), 
				BigComplexMath.tan(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testLog() {
		assertEquals(
				BigComplex.valueOf("0.80471895621705018730037966661309381976280067713425886095", "1.1071487177940905030170654601785370400700476454014326466", MC), 
				BigComplexMath.log(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testSqrt() {
		assertEquals(
				BigComplex.valueOf("1.2720196495140689642524224617374914917156080418400962486", "0.78615137775742328606955858584295892952312205783772323766", MC), 
				BigComplexMath.sqrt(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testPowInt() {
		assertEquals(
				BigComplex.valueOf("18456.81197182559", "-7537.93433754262", MC), 
				BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), 11, MC));

		assertEquals(
				BigComplex.valueOf("0.00004643522633206022281216609142474444925257050125807831", "0.00001896457999216310228315702790913198870386323455307511", MC), 
				BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), -11, MC));
	}
	
	@Test
	public void testPow() {
		assertEquals(
				BigComplex.valueOf("0.93025840620844374948965939784803335557080436887094399412", "2.5254359132812654282958795629575897052282541443174302618", MC), 
				BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), BigDecimal.valueOf(1.1), MC));
	}

	@Test
	public void testPowComplex() {
		assertEquals(
				BigComplex.valueOf("0.84004892508974359637454837539731545717714689412422564692", "0.25134081792349062667714080016033852435880339768328815847", MC), 
				BigComplexMath.pow(BigComplex.valueOf(1.1, 2.2), BigComplex.valueOf(0.1, 0.2), MC));
	}

	@Test
	public void testRoot() {
		assertEquals(
				BigComplex.valueOf("1.2589858696615772964532950314498058030426856758251923734", "0.48693816197569980888611771300161883360684382303096712193", MC), 
				BigComplexMath.root(BigComplex.valueOf(1.1, 2.2), BigDecimal.valueOf(3), MC));
	}

	@Test
	public void testRootComplex() {
		assertEquals(
				BigComplex.valueOf("1.4494235461237517229913668192323085954480223365111721042", "0.17040446483770403353362509072059809846680452097000085527", MC), 
				BigComplexMath.root(BigComplex.valueOf(1.1, 2.2), BigComplex.valueOf(3, 2), MC));
	}

	@Test
	public void testAsin() {
		assertEquals(
				BigComplex.valueOf("0.42707858639247612548064688331895685930333615088099242008", "1.5285709194809981612724561847936733932886832962358541247", MC), 
				BigComplexMath.asin(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testAcos() {
		assertEquals(
				BigComplex.valueOf("1.14371774040242049375067480832079458279524854880656049040", "-1.52857091948099816127245618479367339328868329623585412477", MC), 
				BigComplexMath.acos(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testAtan() {
		assertEquals(
				BigComplex.valueOf("1.3389725222944935611241935759091442410843161725444927785", "0.40235947810852509365018983330654690988140033856712943047", MC), 
				BigComplexMath.atan(BigComplex.valueOf(1, 2), MC));
	}

	@Test
	public void testAcot() {
		assertEquals(
				BigComplex.valueOf("0.23182380450040305810712811573060720101426852714306013190", "-0.40235947810852509365018983330654690988140033856712943047", MC), 
				BigComplexMath.acot(BigComplex.valueOf(1, 2), MC));
	}

}
