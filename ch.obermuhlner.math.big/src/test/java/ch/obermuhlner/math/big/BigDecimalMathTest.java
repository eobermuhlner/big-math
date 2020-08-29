package ch.obermuhlner.math.big;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

import ch.obermuhlner.math.big.stream.BigDecimalStream;
import static ch.obermuhlner.util.ThreadUtil.runMultiThreaded;
import org.junit.Test;

public class BigDecimalMathTest {

    public enum TestLevel {
        Fast,
        Medium,
        Slow
    }

	private static final TestLevel TEST_LEVEL = getTestLevel();

	private static final MathContext MC = MathContext.DECIMAL128;

	private static final MathContext MC_CHECK_DOUBLE = new MathContext(10);

	private static final int AUTO_TEST_MAX_PRECISION = getMaxPrecision();
	private static final int RANDOM_MAX_PRECISION = getMaxPrecision();

	@Test
	public void testInternals() {
		assertEquals(null, toCheck(Double.NaN));
		assertEquals(null, toCheck(Double.NEGATIVE_INFINITY));
		assertEquals(null, toCheck(Double.POSITIVE_INFINITY));

        assertBigDecimal(new BigDecimal("1.23"), new BigDecimal("1.23"), new MathContext(3));
        assertBigDecimal(new BigDecimal("1.23"), new BigDecimal("1.23"), new MathContext(2));
        assertBigDecimal(new BigDecimal("1.23"), new BigDecimal("1.23"), new MathContext(1));

        assertBigDecimal(new BigDecimal("1.24"), new BigDecimal("1.23"), new MathContext(3));
        assertBigDecimal(new BigDecimal("1.23"), new BigDecimal("1.24"), new MathContext(3));

        assertThrows(IllegalArgumentException.class, () -> { throw new IllegalArgumentException(); });
        assertThrows(IllegalArgumentException.class, "blabla", () -> { throw new IllegalArgumentException("blabla"); });
	}

    @Test(expected = java.lang.AssertionError.class)
    public void testInternalsAssertBigDecimalFail1() {
        assertBigDecimal(new BigDecimal("1.25"), new BigDecimal("1.23"), new MathContext(3));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testInternalsBigDecimalFail2() {
        assertBigDecimal(new BigDecimal("1.23"), new BigDecimal("1.25"), new MathContext(3));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testInternalsAssertThrowsFail1() {
        assertThrows(IllegalArgumentException.class, () -> { });
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testInternalsAssertThrowsFail2() {
        assertThrows(IllegalArgumentException.class, () -> { throw new RuntimeException(); });
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testInternalsAssertThrowsFail3() {
        assertThrows(IllegalArgumentException.class, "blabla", () -> { throw new IllegalArgumentException("another message"); });
    }

    @Test
    public void testToBigDecimalFails() {
        assertToBigDecimalThrows("");
        assertToBigDecimalThrows("x");
        assertToBigDecimalThrows(" ");
        assertToBigDecimalThrows("1x");
        assertToBigDecimalThrows("1.2x");
        assertToBigDecimalThrows("1.2E3x");

        assertToBigDecimalThrows("1..2");
        assertToBigDecimalThrows("1.2.3");

        assertToBigDecimalThrows("++1");
        assertToBigDecimalThrows("+1+2");
        assertToBigDecimalThrows("--1");
        assertToBigDecimalThrows("-1-2");
        assertToBigDecimalThrows("+-1");
        assertToBigDecimalThrows("+1-2");
        assertToBigDecimalThrows("-+1");
        assertToBigDecimalThrows("-1+2");

        assertToBigDecimalThrows("1EE2");

        assertToBigDecimalThrows("1E2.3");
        assertToBigDecimalThrows("1E++2");
        assertToBigDecimalThrows("1E+2+3");
        assertToBigDecimalThrows("1E--2");
        assertToBigDecimalThrows("1E-2-3");
        assertToBigDecimalThrows("1E+-2");
        assertToBigDecimalThrows("1E+2-3");
        assertToBigDecimalThrows("1E-+2");
        assertToBigDecimalThrows("1E-2+3");

        assertToBigDecimalThrows("1E9999999999");
        assertToBigDecimalThrows("1E999999999999999");
    }

    @Test
    public void testToBigDecimal() {
        assertToBigDecimal("0");
        assertToBigDecimal("00");
        assertToBigDecimal("0.0");
        assertToBigDecimal("0.00");
        assertToBigDecimal("00.00");
        assertToBigDecimal("+0");
        assertToBigDecimal("-0");
        assertToBigDecimal("+0E123");
        assertToBigDecimal("-0E-123");

        assertToBigDecimal(".123");
        assertToBigDecimal("123.");

        assertToBigDecimal("1");
        assertToBigDecimal("1.0");
        assertToBigDecimal("1.23");
        assertToBigDecimal("1.2300");
        assertToBigDecimal("1234567890");
        assertToBigDecimal("1234567890.1234567890123456789");

        assertToBigDecimal("001");
        assertToBigDecimal("001.23");
        assertToBigDecimal("001.2300");
        assertToBigDecimal("001234567890");
        assertToBigDecimal("001234567890.1234567890123456789");

        assertToBigDecimal("+1");
        assertToBigDecimal("+1.23");
        assertToBigDecimal("+1.2300");
        assertToBigDecimal("+1234567890");
        assertToBigDecimal("+1234567890.1234567890123456789");

        assertToBigDecimal("-1");
        assertToBigDecimal("-1.23");
        assertToBigDecimal("-1.2300");
        assertToBigDecimal("-1234567890");
        assertToBigDecimal("-1234567890.1234567890123456789");

        assertToBigDecimal("1.23E123");
        assertToBigDecimal("1.2300E123");
        assertToBigDecimal("1.23E+123");
        assertToBigDecimal("1.23E-123");
    }

    private static void assertToBigDecimal(String string) {
        BigDecimal expected = new BigDecimal(string);

        for (int i = 2; i < 10; i++) {
            BigDecimal actual = BigDecimalMath.toBigDecimal(string, MathContext.UNLIMITED, i);

            assertTrue("toBigDecimal(_,_," + i + ") " + expected + " compareTo " + actual, expected.compareTo(actual) == 0);
            assertEquals(expected, actual);
        }
    }

    private static void assertToBigDecimalThrows(String string) {
        assertThrows(NumberFormatException.class, () -> new BigDecimal(string));

        assertThrows(NumberFormatException.class, () -> BigDecimalMath.toBigDecimal(string, MathContext.UNLIMITED, 1));
    }

    @Test
	public void testIsIntValue() {
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MIN_VALUE)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MAX_VALUE)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(0)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(-55)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(33)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(-55.0)));
		assertEquals(true, BigDecimalMath.isIntValue(BigDecimal.valueOf(33.0)));

		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MIN_VALUE).subtract(BigDecimal.ONE)));
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(Integer.MAX_VALUE ).add(BigDecimal.ONE)));
		
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(3.333)));
		assertEquals(false, BigDecimalMath.isIntValue(BigDecimal.valueOf(-5.555)));
	}

	@Test
	public void testIsLongValue() {
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(Long.MIN_VALUE)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(Long.MAX_VALUE)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(0)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(-55)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(33)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(-55.0)));
		assertEquals(true, BigDecimalMath.isLongValue(BigDecimal.valueOf(33.0)));

		assertEquals(false, BigDecimalMath.isLongValue(BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.ONE)));
		assertEquals(false, BigDecimalMath.isLongValue(BigDecimal.valueOf(Long.MAX_VALUE ).add(BigDecimal.ONE)));

		assertEquals(false, BigDecimalMath.isLongValue(BigDecimal.valueOf(3.333)));
		assertEquals(false, BigDecimalMath.isLongValue(BigDecimal.valueOf(-5.555)));
	}

	@Test
	public void testIsDoubleValue() {
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(Double.MIN_VALUE)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(Double.MAX_VALUE)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(-Double.MAX_VALUE)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(-Double.MIN_VALUE)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(0)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(-55)));
		assertEquals(true, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(33)));
		assertEquals(true, BigDecimalMath.isDoubleValue(new BigDecimal("1E-325")));
		assertEquals(true, BigDecimalMath.isDoubleValue(new BigDecimal("-1E-325")));
		assertEquals(true, BigDecimalMath.isDoubleValue(new BigDecimal("1E-325")));
		assertEquals(true, BigDecimalMath.isDoubleValue(new BigDecimal("-1E-325")));

		assertEquals(false, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(Double.MAX_VALUE).add(BigDecimal.valueOf(1))));
		assertEquals(false, BigDecimalMath.isDoubleValue(BigDecimal.valueOf(-Double.MAX_VALUE).subtract(BigDecimal.valueOf(1))));
		assertEquals(false, BigDecimalMath.isDoubleValue(new BigDecimal("1E309")));
		assertEquals(false, BigDecimalMath.isDoubleValue(new BigDecimal("-1E309")));
	}
	
	@Test
	public void testMantissa() {
		assertEquals(0, BigDecimal.ZERO.compareTo(BigDecimalMath.mantissa(BigDecimal.ZERO)));

		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("123.45"))));
		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("0.00012345"))));

		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("123.45"))));
		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("0.00012345"))));

		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("123.45000"))));
		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("0.00012345000"))));

		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("12.345E1"))));
		assertEquals(0, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("0.0012345E-1"))));

		assertEquals(-1, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("123.459999"))));
		assertEquals(1, new BigDecimal("1.2345").compareTo(BigDecimalMath.mantissa(new BigDecimal("0.000123"))));
	}
	
	@Test
	public void testExponent() {
		assertEquals(0, BigDecimalMath.exponent(BigDecimal.ZERO));

		assertEquals(0, BigDecimalMath.exponent(new BigDecimal("1.2345")));
		assertEquals(2, BigDecimalMath.exponent(new BigDecimal("123.45")));
		assertEquals(-2, BigDecimalMath.exponent(new BigDecimal("0.012345")));
		
		assertEquals(0, BigDecimalMath.exponent(new BigDecimal("123.45E-2")));
		assertEquals(0, BigDecimalMath.exponent(new BigDecimal("0.012345E2")));

		assertEquals(3, BigDecimalMath.exponent(new BigDecimal("1.2345E3")));
		assertEquals(5, BigDecimalMath.exponent(new BigDecimal("123.45E3")));
		assertEquals(1, BigDecimalMath.exponent(new BigDecimal("0.012345E3")));
	}

	@Test
	public void testSignificantDigits() {
		assertEquals(1, BigDecimalMath.significantDigits(BigDecimal.ZERO));

		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("12.3")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("1.23")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("0.123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("0.0123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("0.00123")));

		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("12.300")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("1.2300")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("0.12300")));

		assertEquals(6, BigDecimalMath.significantDigits(new BigDecimal("123000")));
		assertEquals(6, BigDecimalMath.significantDigits(new BigDecimal("123000.00")));

		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-12.3")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-1.23")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-0.123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-0.0123")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-0.00123")));

		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-12.300")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-1.2300")));
		assertEquals(3, BigDecimalMath.significantDigits(new BigDecimal("-0.12300")));

		assertEquals(6, BigDecimalMath.significantDigits(new BigDecimal("-123000")));
		assertEquals(6, BigDecimalMath.significantDigits(new BigDecimal("-123000.00")));
	}

	@Test
	public void testIntegralPart() {
		assertEquals(0, BigDecimal.ZERO.compareTo(BigDecimalMath.integralPart(BigDecimal.ZERO)));

		assertEquals(0, new BigDecimal("1").compareTo(BigDecimalMath.integralPart(new BigDecimal("1.2345"))));
		assertEquals(0, new BigDecimal("123").compareTo(BigDecimalMath.integralPart(new BigDecimal("123.45"))));
		assertEquals(0, new BigDecimal("0").compareTo(BigDecimalMath.integralPart(new BigDecimal("0.12345"))));

		assertEquals(0, new BigDecimal("-1").compareTo(BigDecimalMath.integralPart(new BigDecimal("-1.2345"))));
		assertEquals(0, new BigDecimal("-123").compareTo(BigDecimalMath.integralPart(new BigDecimal("-123.45"))));
		assertEquals(0, new BigDecimal("-0").compareTo(BigDecimalMath.integralPart(new BigDecimal("-0.12345"))));

		assertEquals(0, new BigDecimal("123E987").compareTo(BigDecimalMath.integralPart(new BigDecimal("123E987"))));
		assertEquals(0, new BigDecimal("0").compareTo(BigDecimalMath.integralPart(new BigDecimal("123E-987"))));
	}
	
	@Test
	public void testFractionalPart() {
		assertEquals(0, BigDecimal.ZERO.compareTo(BigDecimalMath.fractionalPart(BigDecimal.ZERO)));

		assertEquals(0, new BigDecimal("0.2345").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("1.2345"))));
		assertEquals(0, new BigDecimal("0.45").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("123.45"))));
		assertEquals(0, new BigDecimal("0.12345").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("0.12345"))));

		assertEquals(0, new BigDecimal("-0.2345").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("-1.2345"))));
		assertEquals(0, new BigDecimal("-0.45").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("-123.45"))));
		assertEquals(0, new BigDecimal("-0.12345").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("-0.12345"))));

		assertEquals(0, new BigDecimal("0").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("123E987"))));
		assertEquals(0, new BigDecimal("123E-987").compareTo(BigDecimalMath.fractionalPart(new BigDecimal("123E-987"))));
	}


    @Test
    public void testRound() {
        MathContext mc = new MathContext(5);

        for (String s : Arrays.asList("1.234567", "1.23", "12.34567", "0.001234567")) {
            BigDecimal value = new BigDecimal(s);
            assertEquals(value.round(mc), BigDecimalMath.round(value, mc));
        }
    }

    @Test
    public void testRoundWithTrailingZeroes() {
        MathContext mc = new MathContext(5);

        assertEquals("0.0000", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0"), mc).toString());
        assertEquals("0.0000", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00000000000"), mc).toString());

        assertEquals("1.2345", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.2345"), mc).toString());
        assertEquals("123.45", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123.45"), mc).toString());
        assertEquals("0.0012345", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.0012345"), mc).toString());

        assertEquals("1.2346", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567"), mc).toString());
        assertEquals("1.2300", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23"), mc).toString());
        assertEquals("1.2300", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.230000"), mc).toString());
        assertEquals("123.46", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123.4567"), mc).toString());
        assertEquals("0.0012346", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.001234567"), mc).toString());
        assertEquals("0.0012300", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00123"), mc).toString());

        assertEquals("1.2346E+100", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E+100"), mc).toString());
        assertEquals("1.2346E-100", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E-100"), mc).toString());
        assertEquals("1.2300E+100", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E+100"), mc).toString());
        assertEquals("1.2300E-100", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E-100"), mc).toString());
        assertEquals("1.2346E+999999999", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E+999999999"), mc).toString());
        assertEquals("1.2346E-999999999", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E-999999999"), mc).toString());
        assertEquals("1.2300E+999999999", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E+999999999"), mc).toString());
        assertEquals("1.2300E-999999999", BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E-999999999"), mc).toString());
    }

	@Test
	public void testE() {
		// Result from wolframalpha.com: exp(1)
		BigDecimal expected = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350354021234078498193343210681701210056278802351930332247450158539047304199577770935036604169973297250886876966403555707162268447162560798826517871341951246652010305921236677194325278675398558944896970964097545918569563802363701621120477427228364896134225164450781824423529486363721417402388934412479635743702637552944483379980161254922785092577825620926226483262779333865664816277251640191059004916449982893150566047258027786318641551956532442586982946959308019152987211725563475463964479101459040905862984967912874068705048958586717479854667757573205681288459205413340539220001137863009455606881667400169842055804033637953764520304024322566135278369511778838638744396625322498506549958862342818997077332761717839280349465014345588970719425863987727547109629537415211151368350627526023264847287039207643100595841166120545297030");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.e(mathContext),
				10);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testEUnlimitedFail() {
		BigDecimalMath.e(MathContext.UNLIMITED);
	}

	@Test
	public void testPi() {
		// Result from wolframalpha.com: pi
		BigDecimal expected = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989380952572010654858632788659361533818279682303019520353018529689957736225994138912497217752834791315155748572424541506959508295331168617278558890750983817546374649393192550604009277016711390098488240128583616035637076601047101819429555961989467678374494482553797747268471040475346462080466842590694912933136770289891521047521620569660240580381501935112533824300355876402474964732639141992726042699227967823547816360093417216412199245863150302861829745557067498385054945885869269956909272107975093029553211653449872027559602364806654991198818347977535663698074265425278625518184175746728909777727938000816470600161452491921732172147723501414419735685481613611573525521334757418494684385233239073941433345477624168625189835694855620992192221842725502542568876717904946016534668049886272327917860857843838279679766814541009538");
		assertPrecisionCalculation(
			expected,
			mathContext -> BigDecimalMath.pi(mathContext),
			10);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPiUnlimitedFail() {
		BigDecimalMath.pi(MathContext.UNLIMITED);
	}

	@Test
	public void testBernoulli() {
        assertBigDecimal(toCheck(1),			BigDecimalMath.bernoulli(0, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(-1.0/2),		BigDecimalMath.bernoulli(1, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(1.0/6),		BigDecimalMath.bernoulli(2, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(3, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(-1.0/30),	BigDecimalMath.bernoulli(4, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(5, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(1.0/42),		BigDecimalMath.bernoulli(6, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(7, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(-1.0/30),	BigDecimalMath.bernoulli(8, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(9, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(5.0/66),		BigDecimalMath.bernoulli(10, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(11, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(-691.0/2730),BigDecimalMath.bernoulli(12, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(13, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(7.0/6),		BigDecimalMath.bernoulli(14, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(15, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(-3617.0/510),BigDecimalMath.bernoulli(16, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(0),			BigDecimalMath.bernoulli(17, MC), MC_CHECK_DOUBLE);
        assertBigDecimal(toCheck(43867.0/798),BigDecimalMath.bernoulli(18, MC), MC_CHECK_DOUBLE);
	}

	@Test(expected = ArithmeticException.class)
	public void testBernoulliNegative() {
		BigDecimalMath.bernoulli(-1, MC);
	}

	@Test
	public void testBernoulliUnlimited() {
		assertBigDecimal(toCheck(1), BigDecimalMath.bernoulli(0, MathContext.UNLIMITED), MC_CHECK_DOUBLE);
		assertBigDecimal(toCheck(-1.0/2), BigDecimalMath.bernoulli(1, MathContext.UNLIMITED), MC_CHECK_DOUBLE);
		assertBigDecimal(toCheck(1), BigDecimalMath.bernoulli(3, MathContext.UNLIMITED), MC_CHECK_DOUBLE);
	}

	@Test(expected = ArithmeticException.class)
	public void testBernoulliUnlimitedFail() {
		BigDecimalMath.bernoulli(2, MathContext.UNLIMITED);
	}

	@Test
	public void testReciprocal() {
		assertEquals(BigDecimal.ONE, BigDecimalMath.reciprocal(BigDecimal.valueOf(1), MC));
		assertEquals(BigDecimal.valueOf(0.5), BigDecimalMath.reciprocal(BigDecimal.valueOf(2), MC));
	}

	@Test
	public void testReciprocalUnlimited() {
		assertEquals(BigDecimal.ONE, BigDecimalMath.reciprocal(BigDecimal.valueOf(1), MathContext.UNLIMITED));
		assertEquals(BigDecimal.valueOf(0.5), BigDecimalMath.reciprocal(BigDecimal.valueOf(2), MathContext.UNLIMITED));
	}

	@Test(expected = ArithmeticException.class)
	public void testReciprocalUnlimitedFail() {
		BigDecimalMath.reciprocal(BigDecimal.valueOf(3), MathContext.UNLIMITED);
	}

	@Test(expected = ArithmeticException.class)
	public void testReciprocalFail() {
		assertEquals(BigDecimal.valueOf(123), BigDecimalMath.reciprocal(BigDecimal.ZERO, MC));
	}

	@Test
	public void testFactorialInt() {
		assertEquals(new BigDecimal(1), BigDecimalMath.factorial(0));
		assertEquals(new BigDecimal(1), BigDecimalMath.factorial(1));
		assertEquals(new BigDecimal(2), BigDecimalMath.factorial(2));
		assertEquals(new BigDecimal(6), BigDecimalMath.factorial(3));
		assertEquals(new BigDecimal(24), BigDecimalMath.factorial(4));
		assertEquals(new BigDecimal(120), BigDecimalMath.factorial(5));
		
		assertEquals(
				BigDecimalMath.toBigDecimal("9425947759838359420851623124482936749562312794702543768327889353416977599316221476503087861591808346911623490003549599583369706302603264000000000000000000000000"),
				BigDecimalMath.factorial(101));

        BigDecimal expected = BigDecimal.ONE;
        for (int n = 1; n < 1000; n++) {
            expected = expected.multiply(BigDecimal.valueOf(n));
            assertEquals(expected, BigDecimalMath.factorial(n));
        }
	}

	@Test(expected = ArithmeticException.class)
	public void testFactorialIntNegative() {
		BigDecimalMath.factorial(-1);
	}

	@Test
	public void testFactorial() {
		// Result from wolframalpha.com: 1.5!
		BigDecimal expected = new BigDecimal("1.3293403881791370204736256125058588870981620920917903461603558423896834634432741360312129925539084990621701");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.factorial(new BigDecimal("1.5"), mathContext),
		   10);
	}

	@Test
	public void testFactorialNegative() {
		// Result from wolframalpha.com: (-1.5)!
		BigDecimal expected = new BigDecimal("-3.544907701811032054596334966682290365595098912244774256427615579705822569182064362749901313477089330832453647248");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.factorial(new BigDecimal("-1.5"), mathContext),
		   10);
	}

	@Test
	public void testFactorialIntegerValues() {
		assertEquals(BigDecimalMath.round(new BigDecimal(1), MC), BigDecimalMath.factorial(BigDecimal.valueOf(0), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(1), MC), BigDecimalMath.factorial(BigDecimal.valueOf(1), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(2), MC), BigDecimalMath.factorial(BigDecimal.valueOf(2), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(6), MC), BigDecimalMath.factorial(BigDecimal.valueOf(3), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(24), MC), BigDecimalMath.factorial(BigDecimal.valueOf(4), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(120), MC), BigDecimalMath.factorial(BigDecimal.valueOf(5), MC));
	}

	@Test(expected = ArithmeticException.class)
	public void testFactorialNegative1() {
		BigDecimalMath.factorial(BigDecimal.valueOf(-1), MC);
	}

	@Test(expected = ArithmeticException.class)
	public void testFactorialNegative2() {
		BigDecimalMath.factorial(BigDecimal.valueOf(-2), MC);
	}

	@Test
	public void testGamma() {
		// Result from wolframalpha.com: gamma(1.5)
		BigDecimal expected = new BigDecimal("0.886226925452758013649083741670572591398774728061193564106903894926455642295516090687475328369272332708113411812");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.gamma(new BigDecimal("1.5"), mathContext),
		   10);
	}

	@Test
	public void testGammaSlightlyPositive() {
		// Result from wolframalpha.com: gamma(0.5)
		BigDecimal expected = new BigDecimal("1.77245385090551602729816748334114518279754945612238712821380778985291128459103218137495065673854466541622682362428");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.gamma(new BigDecimal("0.5"), mathContext),
		   10);
	}

	@Test
	public void testGammaNegative() {
		// Result from wolframalpha.com: gamma(-1.5)
		BigDecimal expected = new BigDecimal("2.36327180120735470306422331112152691039673260816318283761841038647054837945470957516660087565139288722163576483237676");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.gamma(new BigDecimal("-1.5"), mathContext),
		   10);
	}

	@Test
	public void testGammaIntegerValues() {
		assertEquals(BigDecimalMath.round(new BigDecimal(1), MC), BigDecimalMath.gamma(BigDecimal.valueOf(1), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(1), MC), BigDecimalMath.gamma(BigDecimal.valueOf(2), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(2), MC), BigDecimalMath.gamma(BigDecimal.valueOf(3), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(6), MC), BigDecimalMath.gamma(BigDecimal.valueOf(4), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(24), MC), BigDecimalMath.gamma(BigDecimal.valueOf(5), MC));
		assertEquals(BigDecimalMath.round(new BigDecimal(120), MC), BigDecimalMath.gamma(BigDecimal.valueOf(6), MC));
	}

	@Test(expected = ArithmeticException.class)
	public void testPowIntZeroPowerNegative() {
		BigDecimalMath.pow(BigDecimal.valueOf(0), -5, MC);
	}

	@Test
	public void testPowIntPositiveY() {
		// positive exponents
		for(int x : new int[] { -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5 }) {
			for(int y : new int[] { 0, 1, 2, 3, 4, 5 }) {
				assertEquals(
						x + "^" + y,
                        BigDecimalMath.round(BigDecimal.valueOf((int) Math.pow(x, y)), MC),
						BigDecimalMath.pow(BigDecimal.valueOf(x), y, MC));
			}
		}
	}

	@Test
	public void testPowIntUnlimited() {
		assertEquals(BigDecimal.valueOf(1.44), BigDecimalMath.pow(BigDecimal.valueOf(1.2), 2, MathContext.UNLIMITED));
		assertEquals(BigDecimal.valueOf(0.25), BigDecimalMath.pow(BigDecimal.valueOf(2), -2, MathContext.UNLIMITED));
	}

	@Test
	public void testPowIntUnnecessary() {
        MathContext mathContext = new MathContext(20, RoundingMode.UNNECESSARY);
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(2.0736), mathContext), BigDecimalMath.pow(BigDecimal.valueOf(1.2), 4, mathContext));
	}

	@Test(expected = ArithmeticException.class)
	public void testPowIntUnnecessaryFail() {
		BigDecimalMath.pow(BigDecimal.valueOf(1.2), 4, new MathContext(3, RoundingMode.UNNECESSARY));
	}

	@Test(expected = ArithmeticException.class)
	public void testPowIntUnlimitedFail() {
		BigDecimalMath.pow(BigDecimal.valueOf(1.2), -2, MathContext.UNLIMITED);
	}

	@Test
	public void testPowIntHighAccuracy() {
		// Result from wolframalpha.com: 1.000000000000001 ^ 1234567
		BigDecimal expected = BigDecimalMath.toBigDecimal("1.0000000012345670007620772217746112884011264566574371750661936042203432730421791357400340579375261062151425984605455718643834831212687809215508627027381366482513893346638309647254328483125554030430209837119592796226273439855097892690164822394282109582106572606688508863981956571098445811521589634730079294115917257238821829137340388818182807197358582081813107978164190701238742379894183398009280170118101371420721038965387736053980576803168658232943601622524279972909569009054951992769572674935063940581972099846878996147233580891866876374475623810230198932136306920161303356757346458080393981632574418878114647836311205301451612892591304592483387202671500569971713254903439669992702668656944996771767101889159835990797016804271347502053715595561455746434955874842970156476866700704289785119355240166844949092583102028847019848438487206052262820785557574627974128427022802453099783875466674774383283633178630613523399806185908766812896743349684394795523513553454443796268439405430281375480024234032172402840564635266057234920659063946839453576870882295214918516855889289061559150620879201634277096704728220897344041618549870872138380388841708643468696894694958739051584506837702527545643699395947205334800543370866515060967536750156194684592206567524739086165295878406662557303580256110172236670067327095217480071365601062314705686844139397480994156197621687313833641789783258629317024951883457084359977886729599488232112988200551717307628303748345907910029990065217835915703110440740246602046742181454674636608252499671425052811702208797030657332754492225689850123854291480472732132658657813229027494239083970478001231283002517914471878332200542180147054941938310139493813524503325181756491235593304058711999763930240249546122995086505989026270701355781888675020326791938289147344430814703304780863155994800418441632244536");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.pow(new BigDecimal("1.000000000000001"), 1234567, mathContext),
				10);
	}
	
	@Test
	public void testPowIntNegativeY() {
		// positive exponents
		for(int x : new int[] { -5, -4, -3, -2, -1, 1, 2, 3, 4, 5 }) { // no x=0 !
			for(int y : new int[] { -5, -4, -3, -2, -1}) {
				assertEquals(
						x + "^" + y,
						BigDecimalMath.round(BigDecimal.ONE.divide(BigDecimal.valueOf((int) Math.pow(x, -y)), MC), MC),
						BigDecimalMath.pow(BigDecimal.valueOf(x), y, MC));
			}
		}
	}

	@Test
	public void testPowIntSpecialCases() {
		// 0^0 = 1
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(0), 0, MC));
		// 0^x = 0 for x > 0
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(0), MC), BigDecimalMath.pow(BigDecimal.valueOf(0), +5, MC));

		// x^0 = 1 for all x
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(-5), 0, MC));
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(+5), 0, MC));
	}

	@Test(expected = ArithmeticException.class)
	public void testPowInt0NegativeY() {
		// 0^x for x < 0 is undefined
		System.out.println(BigDecimalMath.pow(BigDecimal.valueOf(0), -5, MC));
	}

	@Test
	public void testPowPositiveX() {
		for(double x : new double[] { 1, 1.5, 2, 2.5, 3, 4, 5 }) {
			for(double y : new double[] { -5, -4, -3, -2.5, -2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2, 2.5, 3, 4, 5 }) {
				assertBigDecimal(
						x + "^" + y,
						toCheck(Math.pow(x, y)),
						BigDecimalMath.pow(BigDecimal.valueOf(x), BigDecimal.valueOf(y), MC),
                        MC_CHECK_DOUBLE);
			}
		}
		for(double x : new double[] { 0 }) {
			for(double y : new double[] { 0, 0.5, 1, 1.5, 2, 2.5, 3, 4, 5 }) {
				assertBigDecimal(
						x + "^" + y,
						toCheck(Math.pow(x, y)),
						BigDecimalMath.pow(BigDecimal.valueOf(x), BigDecimal.valueOf(y), MC),
                        MC_CHECK_DOUBLE);
			}
		}
	}

	@Test
	public void testPowNegativeX() {
		for(double x : new double[] { -2, -1 }) {
			for(double y : new double[] { -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5 }) {
				assertBigDecimal(
						x + "^" + y,
						toCheck(Math.pow(x, y)),
						BigDecimalMath.pow(BigDecimal.valueOf(x), BigDecimal.valueOf(y), MC),
                        MC);
			}
		}
	}

	@Test
	public void testPowSpecialCases() {
		// 0^0 = 1
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(0), BigDecimal.valueOf(0), MC));
		// 0^x = 0 for x > 0
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(0), MC), BigDecimalMath.pow(BigDecimal.valueOf(0), BigDecimal.valueOf(+5), MC));

		// x^0 = 1 for all x
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(-5), BigDecimal.valueOf(0), MC));
		assertEquals(BigDecimalMath.round(BigDecimal.valueOf(1), MC), BigDecimalMath.pow(BigDecimal.valueOf(+5), BigDecimal.valueOf(0), MC));
	}

	@Test(expected = ArithmeticException.class)
	public void testPow0NegativeY() {
		// 0^x for x < 0 is undefined
		System.out.println(BigDecimalMath.pow(BigDecimal.valueOf(0), BigDecimal.valueOf(-5), MC));
	}

	@Test
	public void testPowHighAccuracy1() {
		// Result from wolframalpha.com: 0.12345 ^ 0.54321
		BigDecimal expected = BigDecimalMath.toBigDecimal("0.3209880595151945185125730942395290036641685516401211365668021036227236806558712414817507777010529315619538091221044550517779379562785777203521073317310721887789752732383195992338046561142233197839101366627988301068817528932856364705673996626318789438689474137773276533959617159796843289130492749319006030362443626367021658149242426847020379714749221060925227256780407031977051743109767225075035162749746755475404882675969237304723283707838724317900591364308593663647305926456586738661094577874745954912201392504732008960366344473904725152289010662196139662871362863747003357119301290791005303042638323919552042428899542474653695157843324029537490471818904797202183382709740019779991866183409872343305557416160635632389025962773383948534706993646814493361946320537133866646649868386696744314086907873844459873522561100570574729858449637845765912377361924716997579241434414109143219005616107946583880474580592369219885446517321145488945984700859989002667482906803702408431898991426975130215742273501237614632961770832706470833822137675136844301417148974010849402947454745491575337007331634736828408418815679059906104486027992986268232803807301917429934411578887225359031451001114134791114208050651494053415141140416237540583107162910153240598400275170478935634433997238593229553374738812677055332589568742194164880936765282391919077003882108791507606561409745897362292129423109172883116578263204383034775181118065757584408324046421493189442843977781400819942671602106042861790274528866034496106158048150133736995335643971391805690440083096190217018526827375909068556103532422317360304116327640562774302558829111893179295765516557567645385660500282213611503701490309520842280796017787286271212920387358249026225529459857528177686345102946488625734747525296711978764741913791309106485960272693462458335037929582834061232406160");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.pow(new BigDecimal("0.12345"), new BigDecimal("0.54321"), mathContext),
				10);
	}
	
	//@Test
	public void testPowHighAccuracy2() {
		// Result from wolframalpha.com: 1234.5 ^ 5.4321
		BigDecimal expected = BigDecimalMath.toBigDecimal("62128200273178468.6677398330313037781753494560130835832101960387223758707669665725754895879107246310011029364211118269934534848627597104718365299707675269883473866053798863560099145230081124493870576780612499275723252481988188990085485417903685910250385975275407201318962063571641788853056193956632922391172489400257505790978314596080576631215805090936935676836971091464254857748180262699112027530753684170510323511798980747639116410705861310591624568227525136728034348718513230067867653958961909807085463366698897670703033966902227226026963721428348393842605660315775615215897171041744502317760375398468093874441545987214768846585209830041286071364933140664316884545264314137705612948991849327809564207354415319908754752255701802039139765434084951567836148382259822205056903343078315714330953561297888049627241752521508353126178543435267324563502039726903979264593590549404498146175495384414213014048644769191478319546475736458067346291095970042183567796890291583916374248166579807593334209446774446615766870268699990517113368293867016985423417705611330741518898131591089047503977721006889839010831321890964560951517989774344229913647667605138595803854678957098670003929907267918591145790413480904188741307063239101475728087298405926679231349800701106750462465201862628618772432920720630962325975002703818993580555861946571650399329644600854846155487513507946368829475408071100475344884929346742632438630083062705384305478596166582416332328006339035640924818942503261178020860473649223332292597947133883640686283632593820956826840942563265332271497540069352040396588314197259366049553760360493773149812879272759032356567261509967695159889106382819692093987902453799750689562469611095996225341555322139606462193260609916132372239906927497183040765412767764999503366952191218000245749101208123555266177028678838265168229");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.pow(new BigDecimal("1234.5"), new BigDecimal("5.4321"), mathContext),
				10);
	}

	@Test
	public void testPowLargeInt() {
		BigDecimal x = new BigDecimal("1.5");
		BigDecimal y = new BigDecimal("1E10"); // still possible with longValueExact()
		// Result from wolframalpha.com: 1.5 ^ 1E10
		BigDecimal expected = new BigDecimal("3.60422936590014149127041615892759056162677175765E1760912590");
		assertEquals(10_000_000_000L, y.longValueExact());
		assertPrecisionCalculation(
				expected,
				mathContext -> {
					return BigDecimalMath.pow(x, y, mathContext);
				},
				20);
	}
	
	@Test
	public void testPowVeryLargeInt() {
		BigDecimal x = new BigDecimal("1.00000000000001");
		BigDecimal y = new BigDecimal("1E20"); // not possible with than longValueExact()
		// Result from wolframalpha.com: 1.00000001 ^ 1E20
		BigDecimal expected = new BigDecimal("3.03321538163601059899125791999959844544825181205425E434294");
		assertPrecisionCalculation(
				expected,
				mathContext -> {
					return BigDecimalMath.pow(x, y, mathContext);
				},
				20);
	}
	
	@Test
	public void testPowRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"pow",
				random -> random.nextDouble() * 100 + 0.000001,
				random -> random.nextDouble() * 100 - 50,
				Math::pow,
				(x, y, mathContext) -> BigDecimalMath.pow(x, y, mathContext));
	}

    @Test(expected = ArithmeticException.class)
	public void testPowOverflow() {
		BigDecimalMath.pow(new BigDecimal("123"), new BigDecimal("1E20"), MC);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPowUnlimitedFail() {
		BigDecimalMath.pow(BigDecimal.valueOf(1.2), BigDecimal.valueOf(1.1), MathContext.UNLIMITED);
	}

	@Test
	public void testSqrt() {
		for(double value : new double[] { 0, 0.1, 2, 4, 10, 16, 33.3333 }) {
			assertBigDecimal(
					"sqrt(" + value + ")",
					toCheck(Math.sqrt(value)),
					BigDecimalMath.sqrt(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSqrtUnlimitedFail() {
		BigDecimalMath.sqrt(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testSqrtHighAccuracy() {
		// Result from wolframalpha.com: sqrt(2)
		BigDecimal expected = BigDecimalMath.toBigDecimal("1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214970999358314132226659275055927557999505011527820605714701095599716059702745345968620147285174186408891986095523292304843087143214508397626036279952514079896872533965463318088296406206152583523950547457502877599617298355752203375318570113543746034084988471603868999706990048150305440277903164542478230684929369186215805784631115966687130130156185689872372352885092648612494977154218334204285686060146824720771435854874155657069677653720226485447015858801620758474922657226002085584466521458398893944370926591800311388246468157082630100594858704003186480342194897278290641045072636881313739855256117322040245091227700226941127573627280495738108967504018369868368450725799364729060762996941380475654823728997180326802474420629269124859052181004459842150591120249441341728531478105803603371077309182869314710171111683916581726889419758716582152128229518488472089694633862891562882765952635140542267653239694617511291602408715510135150455381287560052631468017127402653969470240300517495318862925631385188163478001569369176881852378684052287837629389214300655869568685964595155501644724509836896036887323114389415576651040883914292338113206052433629485317049915771756228549741438999188021762430965206564211827316726257539594717255934637238632261482742622208671155839599926521176252698917540988159348640083457085181472231814204070426509056532333398436457865796796519267292399875366617215982578860263363617827495994219403777753681426217738799194551397231274066898329989895386728822856378697749662519966583525776198939322845344735694794962952168891485492538904755828834526096524096542889394538646625744927556381964410316979833061852019379384940057156333720548068540575867999670121372239");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.sqrt(new BigDecimal("2"), mathContext),
				10);
	}

	@Test
	public void testSqrtHuge() {
		// Result from wolframalpha.com: sqrt(1e399)
		BigDecimal expected = new BigDecimal("3.1622776601683793319988935444327185337195551393252168E199");
		assertEquals(expected.round(MC), BigDecimalMath.sqrt(new BigDecimal("1E399"), MC));
	}

	@Test
	public void testSqrtRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"sqrt",
				random -> random.nextDouble() * 100 + 0.000001,
				Math::sqrt,
				(x, mathContext) -> BigDecimalMath.sqrt(x, mathContext));
	}

	/*
    @Test
    public void testSqrtJava9Random() {
        assertRandomCalculation(
                adaptCount(1000),
                "sqrt(x)",
                "java9 sqrt(x)",
                (random, mathContext) -> randomBigDecimal(random, mathContext),
                (x, mathContext) -> BigDecimalMath.sqrt(x, mathContext),
                (x, mathContext) -> x.sqrt(mathContext));
    }
    */

	@Test(expected = ArithmeticException.class)
	public void testSqrtNegative() {
		BigDecimalMath.sqrt(new BigDecimal(-1), MC);
	}

	@Test
	public void testRootSqrtCbrt() {
		for(double x : new double[] { 0, 0.1, 1, 2, 10, 33.3333 }) {
			assertBigDecimal(
					"root(2," + x + ")",
					toCheck(Math.sqrt(x)),
					BigDecimalMath.root(BigDecimal.valueOf(x), BigDecimal.valueOf(2), MC),
					MC_CHECK_DOUBLE);
			assertBigDecimal(
					"root(3," + x + ")",
					toCheck(Math.cbrt(x)),
					BigDecimalMath.root(BigDecimal.valueOf(x), BigDecimal.valueOf(3), MC),
					MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testRoot() {
		for(double n : new double[] { 0.1, 0.9, 1, 1.1, 2, 10, 33.3333, 1234.5678 }) {
			for (double x : new double[]{ 0, 0.1, 0.9, 1, 1.1, 2, 10, 33.3333, 1234.5678 }) {
				System.out.println("ROOT x=" + x + " n=" + n);
				assertBigDecimal(
						"root(2," + x + ")",
						toCheck(Math.pow(x, 1.0 / n)),
						BigDecimalMath.root(BigDecimal.valueOf(x), BigDecimal.valueOf(n), MC),
						MC_CHECK_DOUBLE);
			}
		}
	}

	@Test(expected = ArithmeticException.class)
	public void testRootZeroN() {
		BigDecimalMath.root(BigDecimal.ONE, BigDecimal.ZERO, MC);
	}

	@Test(expected = ArithmeticException.class)
	public void testRootNegativeN() {
		BigDecimalMath.root(BigDecimal.ONE, new BigDecimal(-1), MC);
	}

	@Test(expected = ArithmeticException.class)
	public void testRootNegativeX() {
		BigDecimalMath.root(new BigDecimal(-1), BigDecimal.ONE, MC);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRootUnlimitedFail() {
		BigDecimalMath.root(BigDecimal.valueOf(1.2), BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testRootHighAccuracy1() {
		// Result from wolframalpha.com: root(1.23, 123)
		BigDecimal expected = BigDecimalMath.toBigDecimal("50.016102539344819307741514415079435545110277821887074630242881493528776023690905378058352283823814945584087486290764920313665152884137840533937075179853255596515758851877960056849468879933122908090021571162427934915567330612627267701300492535817858361072169790783434196345863626810981153268939825893279523570322533446766188724600595265286542918045850353371520018451295635609248478721067200812355632099802713302132804777044107393832707173313768807959788098545050700242134577863569636367439867566923334792774940569273585734964008310245010584348384920574103306733020525390136397928777667088202296433541706175886006626333525007680397351405390927420825851036548474519239425298649420795296781692303253055152441850691276044546565109657012938963181532017974206315159305959543881191233733179735321461579808278383770345759408145745617032705494900390986476773247981270283533959979287340513398944113566999839889290733896874439682249327621463735375868408190435590094166575473967368412983975580104741004390308453023021214626015068027388545767003666342291064051883531202983476423138817666738346033272948508395214246047027012105246939488877506475824651688812245962816086719050192476878886543996441778751825677213412487177484703116405390741627076678284295993334231429145515176165808842776515287299275536932744066126348489439143701880784521312311735178716650919024092723485314329094064704170548551468318250179561508293077056611877488417962195965319219352314664764649802231780262169742484818333055713291103286608643184332535729978330383356321740509817475633105247757622805298711765784874873240679024286215940395303989612556865748135450980540945799394622053158729350598632915060818702520420240989908678141379300904169936776618861221839938283876222332124814830207073816864076428273177778788053613345444299361357958409716099682468768353446625063");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.root(BigDecimal.valueOf(123), BigDecimal.valueOf(1.23), mathContext),
				10);
	}

	@Test
	public void testRootHighAccuracy2() {
		// Result from wolframalpha.com: root(7.5, 123)
		BigDecimal expected = BigDecimalMath.toBigDecimal("1.8995643695815870676539369434054361726808105217886880103090875996194822396396255621113000403452538887419132729641364085738725440707944858433996644867599831080192362123855812595483776922496542428049642916664676504355648001147425299497362249152998433619265150901899608932149147324281944326398659053901429881376755786331063699786297852504541315337453993167176639520666006383001509553952974478682921524643975384790223822148525159295285828652242201443762216662072731709846657895992750535254286493842754491094463672629441270037173501058364079340866564365554529160216015597086145980711187711119750807640654996392084846441696711420521658760165363535215241687408369549643269709297427044177507157609035697648282875422321141920576120188389383509318979064825824777240151847818551071255436323480281154877997743553609520167536258202911691329853232693386770937694807506144279660147324316659333074620896627829029651910783066736606497262785345465872401993026696735802446138584306213230373571409591420951964537136053258998945471633936332983896917810023265095766395377592848121611444196796785031727740335105553348270077424620974061727975050161324060753928284759055040064976732991126510635738927993365006832681484889202649313814280125684525505938973967575274196130269615461251746873419445856759329916403947432038902141704646304799083820073914767560878449162496519826664715572693747490088659968040153989493366037393989012508491856761986732685422561958101646754270192269505879594808800416777471196270722586367363680538183391904535845392721112874375802640395545739073303112631715831096156004422381940090623765493332249827278090443678800852264922795299927727708248191560574252923342860845325222035245426918719153132138325983001330317244830727602810422542012322698940744820925849667642343510406965273569391887099540050259962759858771196756422007171");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.root(BigDecimal.valueOf(123), BigDecimal.valueOf(7.5), mathContext),
				10);
	}

	@Test
	public void testRootRandom() {
		assertRandomCalculation(
                adaptCount(100),
				"root",
				random -> random.nextDouble() * 10 + 0.000001,
				random -> random.nextDouble() * 5,
				null,
				(x, y, mathContext) -> BigDecimalMath.root(x, y, mathContext));
	}

	@Test(expected = ArithmeticException.class)
	public void testRootNegative() {
		BigDecimalMath.root(new BigDecimal(-1), BigDecimal.ONE, MC);
	}

	@Test
	public void testLogRange10() {
		double step = getRangeStep(0.1);
		BigDecimalStream.range(step, 10.0, step, MC).forEach(x -> {
			System.out.println("Testing log(" + x + ")");
			assertBigDecimal("log(" + x + ")",
                    toCheck(Math.log(x.doubleValue())),
                    BigDecimalMath.log(x, MC),
                    MC_CHECK_DOUBLE);

			BigDecimal finalX = x;
			assertPrecisionCalculation(
			        mathContext -> BigDecimalMath.log(finalX, mathContext),
                    10, AUTO_TEST_MAX_PRECISION);
		});
	}

	@Test
	public void testLogRange100() {
		double step = getRangeStep(1.0);
		BigDecimalStream.range(step, 100.0, step, MC).forEach(x -> {
			System.out.println("Testing log(" + x + ")");
			assertBigDecimal("log(" + x + ")",
                    toCheck(Math.log(x.doubleValue())),
                    BigDecimalMath.log(x, MC),
                    MC_CHECK_DOUBLE);

			BigDecimal finalX = x;
			assertPrecisionCalculation(
			   mathContext -> BigDecimalMath.log(finalX, mathContext),
			   10, AUTO_TEST_MAX_PRECISION);
		});
	}

	@Test
	public void testLogHighAccuracy1() {
		// Result from wolframalpha.com: ln(0.1)
		BigDecimal expected = BigDecimalMath.toBigDecimal("-2.30258509299404568401799145468436420760110148862877297603332790096757260967735248023599720508959829834196778404228624863340952546508280675666628736909878168948290720832555468084379989482623319852839350530896537773262884616336622228769821988674654366747440424327436515504893431493939147961940440022210510171417480036880840126470806855677432162283552201148046637156591213734507478569476834636167921018064450706480002775026849167465505868569356734206705811364292245544057589257242082413146956890167589402567763113569192920333765871416602301057030896345720754403708474699401682692828084811842893148485249486448719278096762712757753970276686059524967166741834857044225071979650047149510504922147765676369386629769795221107182645497347726624257094293225827985025855097852653832076067263171643095059950878075237103331011978575473315414218084275438635917781170543098274823850456480190956102992918243182375253577097505395651876975103749708886921802051893395072385392051446341972652872869651108625714921988499787488737713456862091670584980782805975119385444500997813114691593466624107184669231010759843831919129223079250374729865092900988039194170265441681633572755570315159611356484654619089704281976336583698371632898217440736600916217785054177927636773114504178213766011101073104239783252189489881759792179866639431952393685591644711824675324563091252877833096360426298215304087456092776072664135478757661626292656829870495795491395491804920906943858079003276301794150311786686209240853794986126493347935487173745167580953708828106745244010589244497647968607512027572418187498939597164310551884819528833074669931781463493000032120032776565413047262188397059679445794346834321839530441484480370130575367426215367557981477045803141363779323629156012818533649846694226146520645994207291711937060244492");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.log(new BigDecimal("0.1"), mathContext),
				10);
	}

	@Test
	public void testLogHighAccuracy2() {
		// Result from wolframalpha.com: ln(1.1)
		BigDecimal expected = BigDecimalMath.toBigDecimal("0.0953101798043248600439521232807650922206053653086441991852398081630010142358842328390575029130364930727479418458517498888460436935129806386890150217023263755687346983551204157456607731117050481406611584967219092627683199972666804124629171163211396201386277872575289851216418802049468841988934550053918259553296705084248072320206243393647990631942365020716424972582488628309770740635849277971589257686851592941134955982468458204470563781108676951416362518738052421687452698243540081779470585025890580291528650263570516836272082869034439007178525831485094480503205465208833580782304569935437696233763597527612962802332419887793490159262767738202097437296124304231269978317763387834500850947983607954894765663306829441000443449252110585597386446423305000249520642003351749383035733163887183863658864095987980592896922224719866617664086469438599082172014984648661016553883267832731905893594398418365160836037053676940083743785539126726302367554039807719021730407981203469520199824994506211545156995496539456365581027383589659382402015390419603824664083368873307873019384357785045824504691072378535575392646883979065139246126662251603763318447377681731632334250380687464278805888614468777887659631017437620270326399552535490068490417697909725326896790239468286121676873104226385183016443903673794887669845552057786043820598162664741719835262749471347084606772426040314789592161567246837020619602671610506695926435445325463039957620861253293473952704732964930764736250291219054949541518603372096218858644670199237818738241646938837142992083372427353696766016209216197009652464144415416340684821107427035544058078681627922043963452271529803892396332155037590445683916173953295983049207965617834301297873495901595044766960173144576650851894013006899406665176310040752323677741807454239794575425116685728529323731335086049670268306");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.log(new BigDecimal("1.1"), mathContext),
				10);
	}

	@Test
	public void testLogHighAccuracy3() {
		// Result from wolframalpha.com: ln(12345.6)
		BigDecimal expected = BigDecimalMath.toBigDecimal("9.42105500327135525114521501453525399237436111276300326386323534432727151942992511520562558738175320737219392933678106934681377640298797158131139323906361961480381516008415766949640011144295651380957422777114172279167654006534622812747920122075143832000303491928864417567534602811492095685408856581074035803357797631847469251006466446952382984400769172787795491275890878474305023861509824367243299385769279771744041937866552134975148449991501344008449686333627176197439283560717007769286520651804657135365525410547797134491863813264296599988480767570621877413992243488449252058389112464675521921368744908030643106093708139694498213865760209374231089223703469389057990578641477811580679006647361045368883126313166757159295044784734054746026667561208850147352459931288221690064827656007945926558137817955314752299200021125335319543610643148781413031739368946686197126231424703883123190210238015791369611214420726133482521541649129324232190740641049135517129893844376556993789191631768552752257796461172834352906322971133196717292014063557464657868471260257837864581817895933554699436597231519928906186824100551929174973211768975723220457184410041128885431823059460270296159512608527194960997843854276107619358871611335110158160499192067423059751567986373407423489599586293284362977309927604782683386482396609096117347165767675657470578510018397575185923185572052807175571518796143517238193372303027925460053807069802388627060672427272087223286476333683468229892546440731981947511457788744089944064466689422654892614083398427300212135529866471079161390374604296893598724751037581346990096479637907462110313260901383748633868418336284029147686046156013978973990920093756659785588328734878986910751799701679853456356654554727303139653731884939067754728654663370026652097310980166441905496504187282659704649813546716585697691");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.log(new BigDecimal("12345.6"), mathContext),
				10);
	}

	@Test
	public void testLogNotorious1() {
		// Result from wolframalpha.com: ln(3.627)
		// result contains digit sequence 249999790 which is tricky
		BigDecimal expected = new BigDecimal("1.28840586030076531271916787589963460174688352499997906354516854751071684132712190465267938913524540189227183498685955203091012976295191938570887410037177795976156712449887694786077349630415919676796628382920641191635039097198525638716788242413712812154035694161623839896238526801424419472197899141291552341986057193552767761847325665588799624460996389716450246797586099819246857022106263044473561032621692801928684892761931286774706996443604259279886700716");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.log(new BigDecimal("3.627"), mathContext),
		   10);
	}

	@Test
	public void testLogSmall() {
		// Result from wolframalpha.com: log(1e-399)
		BigDecimal expected = new BigDecimal("-918.731452104624227923178590419061318832839493962880417437");
		assertEquals(expected.round(MC), BigDecimalMath.log(new BigDecimal("1E-399"), MC));
	}

	@Test
	public void testLogHuge() {
		// Result from wolframalpha.com: log(1e399)
		BigDecimal expected = new BigDecimal("918.7314521046242279231785904190613188328394939628804174372");
		assertEquals(expected.round(MC), BigDecimalMath.log(new BigDecimal("1E399"), MC));
	}

	@Test
	public void testLog10WithPositivePowersOfTen() {
		MathContext mathContext = new MathContext(50);		
		BigDecimal x = new BigDecimal("1");
		BigDecimal expectedLog10 = new BigDecimal(0);
		for (int i = 0; i < 20; i++) {
			BigDecimal actualLog10 = BigDecimalMath.log10(x, mathContext);
			assertEquals(true, expectedLog10.compareTo(actualLog10) == 0);

			x = x.multiply(BigDecimal.TEN, mathContext);
			expectedLog10 = expectedLog10.add(BigDecimal.ONE, mathContext);
		}
	}
	
	@Test
	public void testLog10WithNegativePowersOfTen() {
		MathContext mathContext = new MathContext(50);		
		BigDecimal x = new BigDecimal("1");
		BigDecimal expectedLog10 = new BigDecimal(0);
		for (int i = 0; i < 20; i++) {
			BigDecimal actualLog10 = BigDecimalMath.log10(x, mathContext);
			assertEquals(true, expectedLog10.compareTo(actualLog10) == 0);

			x = x.divide(BigDecimal.TEN, mathContext);
			expectedLog10 = expectedLog10.subtract(BigDecimal.ONE, mathContext);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLog10UnlimitedFail() {
		BigDecimalMath.log10(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testLogRandom() {
		assertRandomCalculation(
                adaptCount(100),
				"log",
				random -> random.nextDouble() * 100 + 0.00001,
				Math::log,
				(x, mathContext) -> BigDecimalMath.log(x, mathContext));
	}
	
	@Test
	public void testLog2Random() {
		assertRandomCalculation(
                adaptCount(100),
				"log",
				random -> random.nextDouble() * 100 + 0.00001,
				(x) -> Math.log(x) / Math.log(2),
				(x, mathContext) -> BigDecimalMath.log2(x, mathContext));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLog2UnlimitedFail() {
		BigDecimalMath.log2(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test(expected = ArithmeticException.class)
	public void testLogNegative() {
		BigDecimalMath.log(BigDecimal.valueOf(-1), MC);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLogUnlimitedFail() {
		BigDecimalMath.log(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testExp() {
		for(double value : new double[] { -5, -1, 0.1, 2, 10 }) {
			assertBigDecimal("exp(" + value + ")",
					toCheck(Math.exp(value)),
					BigDecimalMath.exp(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testExpHuge() {
		// Largest exp(10^x) that still gives a result on Wolfram Alpha 
		// exp(1000000000) = 8.00298177066097253304190937436500068878231499717... * 10^434294481
		BigDecimal expected = new BigDecimal("8.00298177066E434294481");
		BigDecimal actual = BigDecimalMath.exp(BigDecimal.valueOf(1000000000), new MathContext(12));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExp1E() {
		for (int precision = 1; precision <= 2001; precision+=100) {
			MathContext mathContext = new MathContext(precision);
			assertEquals("exp(1)",
					toCheck(BigDecimalMath.e(mathContext)),
					toCheck(BigDecimalMath.exp(BigDecimal.ONE, mathContext)));
		}
	}

	@Test
	public void testExpHighAccuracy1() {
		// Result from wolframalpha.com: exp(0.1)
		BigDecimal expected = BigDecimalMath.toBigDecimal("1.1051709180756476248117078264902466682245471947375187187928632894409679667476543029891433189707486536329171204854012445361537347145315787020068902997574505197515004866018321613310249357028047934586850494525645057122112661163770326284627042965573236001851138977093600284769443372730658853053002811154007820888910705403712481387499832879763074670691187054786420033729321209162792986139109713136202181843612999064371057442214441509033603625128922139492683515203569550353743656144372757405378395318324008280741587539066613515113982139135726893022699091000215648706791206777090283207508625041582515035160384730085864811589785637025471895631826720701700554046867490844416060621933317666818019314469778173494549497985045303406629427511807573756398858555866448811811806333247210364950515781422279735945226411105718464916466588898895425154437563356326922423993425668055030150187978568089290481077628854935380963680803086975643392286380110893491216896970405186147072881173903395370306903756052863966751655566156177044091023716763999613715961429909147602055822171056918247483370329310652377494326018131931115202583455695740577117305727325929270892586003078380276849851024733440526333630939768046873818746897979176031710638428538365444373036344477660068827517905394205724765809719068497652979331103372768988364106139063845834332444587680278142035133567220351279735997089196132184270510670193246409032174006524564495804123904224547124821906736781803247534842994079537510834190198353331683651574603364551464993636940684957076677363104098202444018343049556576017452467191522001230198866508508728780804296630956390659819928014152407848066718063601429519635764058390569704470217925967541099757148635387989599481795155282833193600584112822014656645896726556449347326910544815360769564296952628696236865028848565540573895707695598984577773238");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.exp(new BigDecimal("0.1"), mathContext),
				10);
	}

	@Test
	public void testExpHighAccuracy2() {
		// Result from wolframalpha.com: exp(123.4)
		BigDecimal expected = BigDecimalMath.toBigDecimal("390786063200889154948155379410925404241701821048363382.932350954191939407875540538095053725850542917235991826991631549658381619846119064767940229694652504799690942074237719293556052198585602941442651814977379463173507703540164446248233994372649675083170661574855926134163163649067886904058135980414181563116455815478263535970747684634869846370078756117785925810367190913580101129012440848783613501818345221727921636036313301394206941005430607708535856550269918711349535144151578877168501672228271098301349906118292542981905746359989070403424693487891904874342086983801039403550584241691952868285098443443717286891245248589074794703961309335661643261592184482383775612097087066220605742406426487994296854782150419816494210555905079335674950579368212272414401633950719948812364415716009625682245889528799300726848267101515893741151833582331196187096157250772884710690932741239776061706938673734755604112474396879294621933875319320351790004373826158307559047749814106033538586272336832756712454484917457975827690460377128324949811226379546825509424852035625713328557508831082726245169380827972015037825516930075858966627188583168270036404466677106038985975116257215788600328710224325128935656214272530307376436037654248341541925033083450953659434992320670198187236379508778799056681080864264023524043718014105080505710276107680142887912693096434707224622405921182458722451547247803222237498053677146957211048297712875730899381841541047254172958887430559055751735318481711132216206915942752379320012433097749980476094039036829992786175018479140791048841069099146681433638254527364565199203683980587269493176948487694117499339581660653106481583097500412636413209554147009042448657752082659511080673300924304690964484273924800648584285968546527296722686071417123776801220060226116144242129928933422759721847194902947144831258");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.exp(new BigDecimal("123.4"), mathContext),
				60);
	}

	@Test
	public void testExpRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"exp",
				random -> random.nextDouble() * 100 - 50,
				Math::exp,
				(x, mathContext) -> BigDecimalMath.exp(x, mathContext));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testExpUnlimitedFail() {
		BigDecimalMath.exp(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testSin() {
		for(double value : new double[] { -10, -5, -1, -0.3, 0, 0.1, 2, 10, 20, 222 }) {
			assertBigDecimal("sin(" + value + ")",
					toCheck(Math.sin(value)),
					BigDecimalMath.sin(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testSinHighAccuracy() {
		// Result from wolframalpha.com: sin(1.234)
		BigDecimal expected = BigDecimalMath.toBigDecimal("0.9438182093746337048617510061568275895172142720765760747220911781871399906968099483012598865055627858443507995518738766093869470509555068501582327052306784505752678705592908705201008148688700290760777223780263846758767378849305659165171458418076473553139600704400668632728702595059340199442411041490960324146869032516728992265808389968786198384238945833333329583982909393226122072922972072082343881982280834707504367506003311264818344731205557095928837491316071651630909050078777342482603092413467227932481298625668189293277970973821823536368859836352290171029827678389361668326651223313262181049179177713541062354198699357532113523026870736528786100665809233401695953717292150408826019906221690064294418649612406003915087946369501457359604343584263199153607653049282756925573849745513783165941970858623580447565222079996405576286670288022685431434886874295950242554364666123772837748084818582410730641892357161908769689946576427006541439717287833624991188137124554987468952436155712514180011917087180464841510692660163853984256220178122573051503993728719511214066957647751102014250171535662112264708511179562539851056691807479887430154563476132015884380272176766265870281843666030351481875369524292556759059067229573601315888931475939650530190997869732280644783380897437687282157862590038715019700476516674872568434184233136320198348795549241647388226943616471234865808472025746819601113742677172125085499919170003010129528504832452877371569832101275092363746925641703736428733071588960741542241552270894271703880793621738884941850045978201484407786879714905305225922874339567944723190660416232538921600185338494145628390029969393239498992087392435528382285271962107670662847438424222622822172719234821254495443425396088216409484488852445333426778397941937246299790022429378799080231482904310254381416336471042617299708975");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.sin(new BigDecimal("1.234"), mathContext),
				10);
	}

	@Test
	public void testSinRandom() {
		testSinRandom(100);
	}
	public void testSinRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"sin",
				random -> random.nextDouble() * 100 - 50,
				Math::sin,
				(x, mathContext) -> BigDecimalMath.sin(x, mathContext));
	}

	@Test
	public void testSinRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testSinRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSinUnlimitedFail() {
		BigDecimalMath.sin(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testAsin() {
		for(double value : new double[] { -1, -0.999, -0.9, -0.1, 0, 0.1, 0.9, 0.999, 1.0 }) {
			assertBigDecimal("asin(" + value + ")",
					toCheck(Math.asin(value)),
					BigDecimalMath.asin(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testAsinRandom() {
		testAsinRandom(100);
	}

	public void testAsinRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"asin",
				random -> random.nextDouble() * 2 - 1,
				Math::asin,
				(x, mathContext) -> BigDecimalMath.asin(x, mathContext));
	}

	@Test
	public void testAsinRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAsinRandom(10));
	}


	@Test(expected = ArithmeticException.class)
	public void testAsinGreaterOne() {
		BigDecimalMath.asin(new BigDecimal("1.00001"), MC);
	}

	@Test(expected = ArithmeticException.class)
	public void testAsinSmallerMinusOne() {
		BigDecimalMath.asin(new BigDecimal("-1.00001"), MC);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAsinUnlimitedFail() {
		BigDecimalMath.asin(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testCos() {
		for(double value : new double[] { -5, -1, -0.3, 0, 0.1, 2, 10 }) {
			assertBigDecimal("cos(" + value + ")",
					toCheck(Math.cos(value)),
					BigDecimalMath.cos(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testCosHighAccuracy() {
		// Result from wolframalpha.com: cos(1.234)
		BigDecimal expected = BigDecimalMath.toBigDecimal("0.3304651080717298574032807727899271437216920101969540348605304415152510377850481654650247150200780863415535299490857568279769354541967379397431278152484662377393883768410533419456683721348368071060447119629226464127475191769149818089642412919990646712138828462407239787011203786844401859479654861215480468553428321928822608813865008312100125205763217809424012405019490461648738007730900576327363563072819683608077467442286094847912950576189413624713414163958384339772584148744200648200688260933678578647517949013249027860144759454924413798901254668352778102301380649346953594529136819938821616590614874123930824463095104424946532966863750206459438812141713997562660701774968530149079881716322567945593156313333714539747617833144412172753445042952390635799639722239182963246046253903297563427741240081854182759746064810195237864060495745282046388159544259160022883886283097655348787382625328541498058884531961700370121969709480517496749271767735566816249479148488140162802977360971480510530896749944967304972380342831111213248738743617588927820627474733980422901948506009170945896565358929343777077336070289567245971065005860921723126096986632224093068775586235017140132374230378564807873973345322857782900999655081761884197357196908109838154083921138904571471346009606070648486103795109388774364448499820533743041120697352743044140279966823607345221684081898024173036376672034911709557102798619864101440725109041264516550229345850413762376113868869256025801898710854538411051622029568572639882301754336762028948110406127835411158515890274188501674397646117070538768699719967119559314804437052735458481025364866752041137855637961697664203246176781407193905595472755222134533679020285886126388322265972029035063590381025908006103799793443322205833892605275969980406879438015951448721792889383476504454337544038606643477976186");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.cos(new BigDecimal("1.234"), mathContext),
		   10);
	}

	@Test
	public void testCosRandom() {
		testCoshRandom(100);
	}

	public void testCosRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"cos",
				random -> random.nextDouble() * 100 - 50,
				Math::cos,
				(x, mathContext) -> BigDecimalMath.cos(x, mathContext));
	}

	@Test
	public void testCosRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testCosRandom(10));
	}


	@Test(expected = UnsupportedOperationException.class)
	public void testCosUnlimitedFail() {
		BigDecimalMath.cos(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testAcosRandom() {
		testAcosRandom(100);
	}

	public void testAcosRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"acos",
				random -> random.nextDouble() * 2 - 1,
				Math::acos,
				(x, mathContext) -> BigDecimalMath.acos(x, mathContext));
	}

	@Test
	public void testAcosRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAcosRandom(10));
	}

	@Test
	public void testAcosMinusOne() {
		for (int precision = 1; precision <= 2001; precision+=100) {
			MathContext mathContext = new MathContext(precision);
			BigDecimal pi = BigDecimalMath.pi(mathContext);
			BigDecimal acosMinusOne = BigDecimalMath.acos(BigDecimal.ONE.negate(), mathContext);
			assertEquals(true, pi.compareTo(acosMinusOne) == 0);
		}
	}
	
	@Test(expected = ArithmeticException.class)
	public void testAcosGreaterOne() {
		BigDecimalMath.acos(new BigDecimal("1.00001"), MC);
	}

	@Test(expected = ArithmeticException.class)
	public void testAcosSmallerMinusOne() {
		BigDecimalMath.acos(new BigDecimal("-1.00001"), MC);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAcosUnlimitedFail() {
		BigDecimalMath.acos(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testTan() {
		for(double value : new double[] { 1.1, -10, -5, -1, -0.3, 0, 0.1, 2, 10, 20, 222 }) {
			assertBigDecimal("tan(" + value + ")",
					toCheck(Math.tan(value)),
					BigDecimalMath.tan(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	@Test
	public void testTanRandom() {
		testTanRandom(100);
	}

	public void testTanRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"tan",
				random -> random.nextDouble() * 100 - 50,
				Math::tan,
				(x, mathContext) -> BigDecimalMath.tan(x, mathContext));
	}

	@Test
	public void testTanRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testTanRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTanUnlimitedFail() {
		BigDecimalMath.tan(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testAtanRandom() {
		testAtanRandom(100);
	}

	public void testAtanRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"atan",
				random -> random.nextDouble() * 100 - 50,
				Math::atan,
				(x, mathContext) -> BigDecimalMath.atan(x, mathContext));
	}

	@Test
	public void testAtanRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAtanRandom(10));
	}

	@Test(expected = ArithmeticException.class)
	public void testAtan2ZeroZero() {
		BigDecimalMath.atan2(BigDecimal.ZERO, BigDecimal.ZERO, MC);
	}
	
	@Test
	public void testAtan2() {
		BigDecimal pi = BigDecimalMath.pi(MC);
		BigDecimal piHalf = BigDecimalMath.pi(new MathContext(MC.getPrecision() + 10)).divide(BigDecimal.valueOf(2), MC);

		assertEquals(piHalf, BigDecimalMath.atan2(BigDecimal.TEN, BigDecimal.ZERO, MC));
		assertEquals(piHalf.negate(), BigDecimalMath.atan2(BigDecimal.TEN.negate(), BigDecimal.ZERO, MC));
		assertEquals(pi, BigDecimalMath.atan2(BigDecimal.ZERO, BigDecimal.TEN.negate(), MC));
	}

	@Test
	public void testAtan2HighAccuracy() {
		// Result from wolframalpha.com: atan2(123456789, 987654321)
		BigDecimal expected = BigDecimalMath.toBigDecimal("0.12435499342522297334968147683476071896899844881294839643180323485370657121551589550118807775010954424614161504017100766102274760347773144234717264025098792427062054083201522193127589446759654134052154287720616792464947608894974472784377275294647356650781819500878799427824049369273085965502705708393722924586235499530689424922197361614748436463923316792256542944548723768578591781632376382102987565485177288344132765819127765508975729310469873258293578995");
		assertPrecisionCalculation(
		   expected,
		   mathContext -> BigDecimalMath.atan2(new BigDecimal("123456789"), new BigDecimal("987654321"), mathContext),
		   10);
	}

	@Test
	public void testAtan2Random() {
		testAtan2Random(100);
	}

	public void testAtan2Random(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"atan2",
				random -> random.nextDouble() * 100 - 50,
				random -> random.nextDouble() * 100 - 50,
				Math::atan2,
		   		(y, x, mathContext) -> {
					BigDecimal pi = BigDecimalMath.pi(mathContext);
					BigDecimal result = BigDecimalMath.atan2(y, x, mathContext);
					if (result.compareTo(pi.negate()) < 0 || result.compareTo(pi) > 0) {
					   fail("outside allowed range: " + result + " for y=" + y + ", x=" + x);
					}
					return result;
			   	});
	}

	@Test
	public void testAtan2RandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAtan2Random(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAtanUnlimitedFail() {
		BigDecimalMath.atan(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testCot() {
		for(double value : new double[] { 0.5, -0.5 }) {
			assertBigDecimal("cot(" + value + ")",
					toCheck(cot(value)),
					BigDecimalMath.cot(BigDecimal.valueOf(value), MC),
                    MC_CHECK_DOUBLE);
		}
	}

	private double cot(double x) {
		return Math.cos(x) / Math.sin(x);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCotUnlimitedFail() {
		BigDecimalMath.cot(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testSinhRandom() {
		testSinhRandom(100);
	}

	public void testSinhRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"sinh",
				random -> random.nextDouble() * 100 - 50,
				Math::sinh,
				(x, mathContext) -> BigDecimalMath.sinh(x, mathContext));
	}

	@Test
	public void testSinhRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testSinhRandom(10));
	}

	@Test
	public void testAsinhRandom() {
		testAsinhRandom(100);
	}

	public void testAsinhRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"asinh",
				random -> random.nextDouble() * 100 - 50,
				BigDecimalMathTest::asinh,
				(x, mathContext) -> BigDecimalMath.asinh(x, mathContext));
	}

	@Test
	public void testAsinhRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAsinhRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAsinhUnlimitedFail() {
		BigDecimalMath.asinh(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	public static double asinh(double x) {
		return Math.log(x + Math.sqrt(x*x + 1));
	}
	
	@Test
	public void testAcoshRandom() {
		testAcoshRandom(100);
	}

	public void testAcoshRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"acosh",
				random -> random.nextDouble() * 100 + 1,
				BigDecimalMathTest::acosh,
				(x, mathContext) -> BigDecimalMath.acosh(x, mathContext));
	}

	@Test
	public void testAcoshRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAcoshRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAcoshUnlimitedFail() {
		BigDecimalMath.acosh(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	public static double acosh(double x) {
		return Math.log(x + Math.sqrt(x*x - 1));
	}
	
	@Test
	public void testAtanhRandom() {
		testAtanhRandom(100);
	}

	public void testAtanhRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"atanh",
				random -> random.nextDouble() * 1.9999 - 1,
				BigDecimalMathTest::atanh,
				(x, mathContext) -> BigDecimalMath.atanh(x, mathContext));
	}

	@Test
	public void testAtanhRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAtanhRandom(10));
	}

    @Test(expected = ArithmeticException.class)
    public void testAtanhFailOne() {
        BigDecimalMath.atanh(BigDecimal.ONE, MC);
    }

    @Test(expected = ArithmeticException.class)
    public void testAtanhFailMinusOne() {
        BigDecimalMath.atanh(BigDecimal.ONE.negate(), MC);
    }

    @Test(expected = UnsupportedOperationException.class)
	public void testAtanhUnlimitedFail() {
		BigDecimalMath.atanh(BigDecimal.valueOf(0), MathContext.UNLIMITED);
	}

	public static double atanh(double x) {
		return Math.log((1+x)/(1-x))/2;
	}
	
	@Test
	public void testAcothRandom() {
		testAcothRandom(100);
	}

	public void testAcothRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"acoth",
				random -> random.nextDouble() * 100 + 1,
				BigDecimalMathTest::acoth,
				(x, mathContext) -> BigDecimalMath.acoth(x, mathContext));

		assertRandomCalculation(
				adaptCount(count),
				"acoth",
				random -> -(random.nextDouble() * 100 + 1),
				BigDecimalMathTest::acoth,
				(x, mathContext) -> BigDecimalMath.acoth(x, mathContext));
	}

	@Test
	public void testAcothRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testAcothRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAcothUnlimitedFail() {
		BigDecimalMath.acoth(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	public static double acoth(double x) {
		return Math.log((x+1)/(x-1))/2;
	}
	
	@Test
	public void testCoshRandom() {
		testCoshRandom(1000);
	}

	public void testCoshRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"cosh",
				random -> random.nextDouble() * 100 - 50,
				Math::cosh,
				(x, mathContext) -> BigDecimalMath.cosh(x, mathContext));
	}

	@Test
	public void testCoshRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testCoshRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCoshUnlimitedFail() {
		BigDecimalMath.cosh(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testTanhRandom() {
		testTanhRandom(100);
	}

	public void testTanhRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"tanh",
				random -> random.nextDouble() * 100 - 50,
				Math::tanh,
				(x, mathContext) -> BigDecimalMath.tanh(x, mathContext));
	}

	@Test
	public void testTanhRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testTanhRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTanhUnlimitedFail() {
		BigDecimalMath.tanh(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	@Test
	public void testCothHighAccuracy() {
		// Result from wolframalpha.com: coth(1.234)
		BigDecimal expected = BigDecimalMath.toBigDecimal("1.185205324770926556048860223430792270329901540411285137873448359282652953527094889287726814715510048521019659319860676563297837519357735755222676888688329329110303974145134088876752919477109732168023596610148428505480869241099661164811509156002985143871723093089577983606565248689782796839398517745077805743517260787039640386735301278276086876372931112481082107564483405956328739694693730574938024717490194723853249114925227131778575014948071122840812952034872863104176945075471984392801178291333351353562325025835067985454487645827393165057275970686744920047997309198618696444874486121281651600092381786769980527201043162650354454070144174248097841732569004869651674629227052611951971499852906803162445370338547925467890388150946743902496473437710179269365962240697297233777642354604121931412626467105497562707506260133068364009228804583415867970900958121367348411835937666408175088231826400822030426266552");
		assertPrecisionCalculation(
				expected,
				mathContext -> BigDecimalMath.coth(new BigDecimal("1.234"), mathContext),
				10);
	}

	@Test
	public void testCothRandom() {
		testCothRandom(100);
	}

	public void testCothRandom(int count) {
		assertRandomCalculation(
                adaptCount(count),
				"tanh",
				random -> random.nextDouble() * 100 - 50,
				BigDecimalMathTest::coth,
				(x, mathContext) -> BigDecimalMath.coth(x, mathContext));
	}

	@Test
	public void testCothRandomMultiThreaded() throws Throwable {
		runMultiThreaded(() -> testCothRandom(10));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCothUnlimitedFail() {
		BigDecimalMath.coth(BigDecimal.valueOf(2), MathContext.UNLIMITED);
	}

	private static double coth(double x) {
		return Math.cosh(x) / Math.sinh(x);
	}

	@Test
	public void testSinAsinRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"asin(sin(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.asin(BigDecimalMath.sin(x, mathContext), mathContext));
	}

	@Test
	public void testCosAcosRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"acos(cos(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.acos(BigDecimalMath.cos(x, mathContext), mathContext));
	}

	@Test
	public void testTanAtanRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"atan(tan(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.atan(BigDecimalMath.tan(x, mathContext), mathContext));
	}

	@Test
	public void testCotAcotRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"acot(cot(x))",
				(random, mathContext) -> {
					BigDecimal r;
					do {
						r = randomBigDecimal(random, mathContext);
					} while(r.compareTo(BigDecimal.ZERO) == 0);
					return r;
				},
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.acot(BigDecimalMath.cot(x, mathContext), mathContext));
	}

	@Test(expected = ArithmeticException.class)
	public void testCotEqualZero() {
		BigDecimalMath.cot(BigDecimal.ZERO, MC);
	}

	@Test
	public void testSinhAsinhRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"asinh(sinh(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.asinh(BigDecimalMath.sinh(x, mathContext), mathContext));
	}

	@Test
	public void testCoshAcoshRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"acosh(cosh(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.acosh(BigDecimalMath.cosh(x, mathContext), mathContext));
	}

	@Test
	public void testTanhAtanhRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"atan(tan(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.atanh(BigDecimalMath.tanh(x, mathContext), mathContext));
	}

	@Test
	public void testCothAcothRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"acoth(coth(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext).add(BigDecimal.valueOf(0.0000001)),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.acoth(BigDecimalMath.coth(x, mathContext), mathContext));
	}

	@Test
	public void testPow2Random() {
		assertRandomCalculation(
                adaptCount(1000),
				"x*x",
				"pow(x,2)",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x.multiply(x, mathContext),
				(x, mathContext) -> BigDecimalMath.pow(x, 2, mathContext));
	}

	@Test
	public void testSqrtPow2Random() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"pow(sqrt(x),2)",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.pow(BigDecimalMath.sqrt(x, mathContext), 2, mathContext));
	}

	@Test
	public void testSqrtRootRandom() {
		BigDecimal value2 = new BigDecimal("2");
		assertRandomCalculation(
                adaptCount(1000),
				"sqrt(x)",
				"root(x, 2)",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> BigDecimalMath.sqrt(x, mathContext),
				(x, mathContext) -> BigDecimalMath.root(x, value2, mathContext));
	}

	@Test
	public void testRootPowRandom() {
		for (BigDecimal n : Arrays.asList(new BigDecimal("0.1"), new BigDecimal("1.0"), new BigDecimal("2.1"), new BigDecimal("1234.5678"))) {
			assertRandomCalculation(
                    adaptCount(1000),
					"x",
					"pow(root(x, " + n + ")," + n + ")",
					(random, mathContext) -> randomBigDecimal(random, mathContext),
					(x, mathContext) -> x,
					(x, mathContext) -> BigDecimalMath.pow(BigDecimalMath.root(x, n, mathContext), n, mathContext));
		}
	}

	@Test
	public void testLogExpRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"log(exp(x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.log(BigDecimalMath.exp(x, mathContext), mathContext));
	}

/*
This test fails for some reason only on travis but not on any other machine I have tested it:
ch.obermuhlner.math.big.BigDecimalMathTest > testLog2PowRandom FAILED
    java.lang.AssertionError: x=0.87344468893732422696209498372540635905450777208072365658944016386845896582721178218599 x=0.87344468893732422696209498372540635905450777208072365658944016386845896582721178218599 log2(pow(2,x))=0.873444688937324226962094983725406359054507772080723656589618519811102588824061807610765143887160 expected=0.87344468893732422696209498372540635905450777208072365658944016386845896582721178218599 actual=0.873444688937324226962094983725406359054507772080723656589618519811102588824061807610765143887160 precision=86 error=1.78355942643622996850025424775143887160E-58 acceptableError=1E-86
        at org.junit.Assert.fail(Assert.java:88)
        at org.junit.Assert.assertTrue(Assert.java:41)
        at ch.obermuhlner.math.big.BigDecimalMathTest.assertBigDecimal(BigDecimalMathTest.java:1678)
        at ch.obermuhlner.math.big.BigDecimalMathTest.assertRandomCalculation(BigDecimalMathTest.java:1664)
        at ch.obermuhlner.math.big.BigDecimalMathTest.testLog2PowRandom(BigDecimalMathTest.java:1542)

	@Test
	public void testLog2PowRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"log2(pow(2,x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.log2(BigDecimalMath.pow(new BigDecimal(2), x, mathContext), mathContext));
	}
*/
	@Test
	public void testLog10PowRandom() {
		assertRandomCalculation(
                adaptCount(1000),
				"x",
				"log10(pow(10,x))",
				(random, mathContext) -> randomBigDecimal(random, mathContext),
				(x, mathContext) -> x,
				(x, mathContext) -> BigDecimalMath.log10(BigDecimalMath.pow(BigDecimal.TEN, x, mathContext), mathContext));
	}

	private void assertPrecisionCalculation(Function<MathContext, BigDecimal> precisionCalculation, int startPrecision, int endPrecision) {
		BigDecimal expected = precisionCalculation.apply(new MathContext(endPrecision * 2));
		//System.out.println("reference expected:      " + expected);
		assertPrecisionCalculation(expected, precisionCalculation, startPrecision, endPrecision);
	}

	private void assertPrecisionCalculation(BigDecimal expected, Function<MathContext, BigDecimal> precisionCalculation, int startPrecision) {
		assertPrecisionCalculation(expected, precisionCalculation, startPrecision, expected.precision()-20);
	}
	
	private void assertPrecisionCalculation(BigDecimal expected, Function<MathContext, BigDecimal> precisionCalculation, int startPrecision, int endPrecision) {
		int precision = startPrecision;
		while (precision <= endPrecision) {
			MathContext mathContext = new MathContext(precision);
			System.out.println("Testing precision=" + precision);
			assertBigDecimal(
					"precision=" + precision, 
					expected.round(mathContext),
					precisionCalculation.apply(mathContext),
                    mathContext);
			precision += getPrecisionStep();
		}
	}
	
	private static interface Function3<T1, T2, T3, R> {
		R apply(T1 t1, T2 t2, T3 t3);
	}

	void assertRandomCalculation(int count, String functionName, Function<Random, Double> xFunction, Function<Double, Double> doubleFunction, BiFunction<BigDecimal, MathContext, BigDecimal> calculation) {
		Random random = new Random(1);

		for (int i = 0; i < count; i++) {
			int precision = random.nextInt(RANDOM_MAX_PRECISION) + 1;
			Double xDouble = xFunction.apply(random);
			BigDecimal x = BigDecimal.valueOf(xDouble);
			
			String description = functionName + "(" + x + ")";

			System.out.println("Testing " + description + " precision=" + precision);
			MathContext mathContext = new MathContext(precision);
			BigDecimal result = calculation.apply(x, mathContext);

			if (doubleFunction != null && precision > MC_CHECK_DOUBLE.getPrecision() + 4) {
                BigDecimal doubleResult = toCheck(doubleFunction.apply(xDouble));
                if (doubleResult != null) {
                    String doubleDescription = description + " vs. double function ";
                    assertBigDecimal(doubleDescription, doubleResult, result, MC_CHECK_DOUBLE);
                }
			}

			MathContext referenceMathContext = new MathContext(precision * 2 + 20);
			BigDecimal referenceResult = calculation.apply(x, referenceMathContext);
			BigDecimal expected = referenceResult.round(mathContext);
            assertBigDecimal(description, expected, result, mathContext);
		}
	}

	private static void assertRandomCalculation(int count, String functionName, Function<Random, Double> xFunction, Function<Random, Double> yFunction, BiFunction<Double, Double, Double> doubleFunction, Function3<BigDecimal, BigDecimal, MathContext, BigDecimal> calculation) {
		Random random = new Random(1);
		
		for (int i = 0; i < count; i++) {
			int precision = random.nextInt(100) + 1;
			Double xDouble = xFunction.apply(random);
			Double yDouble = yFunction.apply(random);
			
			BigDecimal x = BigDecimal.valueOf(xDouble);
			BigDecimal y = BigDecimal.valueOf(yDouble);
			
			String description = functionName + "(" + x + "," + y + ")";
			System.out.println("Testing " + description + " precision=" + precision);

			MathContext mathContext = new MathContext(precision);
			BigDecimal result = calculation.apply(x, y, mathContext);
			
			if (doubleFunction != null && precision > MC_CHECK_DOUBLE.getPrecision() + 4) {
                BigDecimal doubleResult = toCheck(doubleFunction.apply(xDouble, yDouble));
                String doubleDescription = description + " vs. double function : " + result;
                assertBigDecimal(doubleDescription, doubleResult, result, MC_CHECK_DOUBLE);
			}

            BigDecimal expected = calculation.apply(x, y, new MathContext(precision + 20, mathContext.getRoundingMode()));
            assertBigDecimal(description, expected, result, mathContext);
		}
	}

	private void assertRandomCalculation(int count, String function1Name, String function2Name, BiFunction<Random, MathContext, BigDecimal> xFunction, BiFunction<BigDecimal, MathContext, BigDecimal> calculation1, BiFunction<BigDecimal, MathContext, BigDecimal> calculation2) {
		Random random = new Random(1);
		
		for (int i = 0; i < count; i++) {
			int numberPrecision = random.nextInt(100) + 1;
			int calculationPrecision = numberPrecision + 10;
			
			MathContext numberMathContext = new MathContext(numberPrecision);
			BigDecimal x = xFunction.apply(random, numberMathContext);

			MathContext calculationMathContext = new MathContext(calculationPrecision);
			BigDecimal y1 = calculation1.apply(x, calculationMathContext);
			BigDecimal y2 = calculation2.apply(x, calculationMathContext);

            String description = "x=" + x + " " + function1Name + "=" + y1 + " " + function2Name + "=" + y2;
			System.out.println("Testing " + description + " precision=" + numberPrecision);

			assertBigDecimal(description, y1, y2, numberMathContext);
		}
	}

    private static boolean assertBigDecimal(BigDecimal expected, BigDecimal actual, MathContext mathContext) {
	    return assertBigDecimal("", expected, actual, mathContext);
    }

    private static boolean assertBigDecimal(String description, BigDecimal expected, BigDecimal actual, MathContext mathContext) {
        MathContext calculationMathContext = new MathContext(mathContext.getPrecision() + 10);
        BigDecimal error = expected.subtract(actual, calculationMathContext).abs();
        BigDecimal acceptableError = actual.round(mathContext).ulp();

        String fullDescription = description + " expected=" + expected + " actual=" + actual + " precision=" + mathContext.getPrecision() + " error=" + error + " acceptableError=" + acceptableError;
        assertTrue(fullDescription, error.compareTo(acceptableError) <= 0);
        return error.signum() == 0;
    }

    private static BigDecimal randomBigDecimal(Random random, MathContext mathContext) {
		char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		StringBuilder stringNumber = new StringBuilder();
		stringNumber.append("0.");
		
		for (int i = 0; i < mathContext.getPrecision(); i++) {
			stringNumber.append(digits[random.nextInt(digits.length)]);
		}
		
		return new BigDecimal(stringNumber.toString(), mathContext);
	}

    private static BigDecimal randomBigDecimalWithExponent(Random random, MathContext mathContext) {
        int exponent = random.nextInt(200) - 100;
        return randomBigDecimal(random, mathContext).multiply(new BigDecimal("1E" + exponent, mathContext), mathContext);
    }

    public static Exception assertThrows(Class<? extends Exception> exceptionClass, Runnable runnable) {
	    return assertThrows(exceptionClass, null, runnable);
    }

    public static Exception assertThrows(Class<? extends Exception> exceptionClass, String message, Runnable runnable) {
	    Exception result = null;
        try {
            runnable.run();
            fail("Expected: " + exceptionClass.getName());
        } catch (Exception exception) {
            if (!exceptionClass.isAssignableFrom(exception.getClass())) {
                fail("Expected: " + exceptionClass.getName());
            }
            if (message != null && !message.equals(exception.getMessage())) {
                fail("Expected: " + exceptionClass.getName() + " with message: \"" + message + "\" but received message: \"" + exception.getMessage() + "\"");
            }
            result = exception;
        }
        return result;
    }

	private static BigDecimal toCheck(double value) {
		long longValue = (long) value;
		if (value == (double)longValue) {
			return BigDecimal.valueOf(longValue);
		}
		
		if (Double.isFinite(value)) {
			return BigDecimal.valueOf(value);
		}
		
		return null;
	}

	private static BigDecimal toCheck(BigDecimal value) {
		return BigDecimal.valueOf(value.round(MC_CHECK_DOUBLE).doubleValue());
	}

    private int adaptCount(int count) {
	    switch(TEST_LEVEL) {
            case Fast:
                return count;
            case Medium:
                return count * 10;
            case Slow:
                return count * 100;
        }
        return count;
    }

    private static TestLevel getTestLevel() {
	    TestLevel level = TestLevel.Fast;

	    String envTestLevel = System.getenv("BIGDECIMALTEST_LEVEL");
	    if (envTestLevel != null) {
            try {
                level = TestLevel.valueOf(envTestLevel);
            } catch (IllegalArgumentException ex) {
                System.err.println("Illegal env var TEST_LEVEL: " + envTestLevel);
            }
        }

        return level;
    }

    private static int getMaxPrecision() {
        switch(TEST_LEVEL) {
            case Fast:
                return 100;
            case Medium:
                return 200;
            case Slow:
                return 1000;
        }
        return 100;
    }

    private static int getPrecisionStep() {
        switch(TEST_LEVEL) {
            case Fast:
                return 50;
            case Medium:
                return 20;
            case Slow:
                return 5;
        }
        return 30;
    }

    private static double getRangeStep(double step) {
        switch(TEST_LEVEL) {
            case Fast:
                return step;
            case Medium:
                return step / 2;
            case Slow:
                return step / 10;
        }
        return step;
    }
}
