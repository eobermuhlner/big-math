package ch.obermuhlner.math.big;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import ch.obermuhlner.math.big.internal.AsinCalculator;
import ch.obermuhlner.math.big.internal.CosCalculator;
import ch.obermuhlner.math.big.internal.CoshCalculator;
import ch.obermuhlner.math.big.internal.ExpCalculator;
import ch.obermuhlner.math.big.internal.SinCalculator;
import ch.obermuhlner.math.big.internal.SinhCalculator;

/**
 * Provides advanced functions operating on {@link BigDecimal}s.
 */
public class BigDecimalMath {

	public static Context DECIMAL128 = new Context(MathContext.DECIMAL128);
	public static Context DECIMAL64 = new Context(MathContext.DECIMAL64);
	public static Context DECIMAL32 = new Context(MathContext.DECIMAL32);
	public static Context UNLIMITED = new Context(MathContext.UNLIMITED);
	
	private static final BigDecimal TWO = valueOf(2);
	private static final BigDecimal THREE = valueOf(3);
	private static final BigDecimal MINUS_ONE = valueOf(-1);

	private static final BigDecimal LOG_TWO = new BigDecimal("0.69314718055994530941723212145817656807550013436025525412068000949339362196969471560586332699641868754200148102057068573368552023575813055703267075163507596193072757082837143519030703862389167347112335011536449795523912047517268157493206515552473413952588295045300709532636664265410423915781495204374043038550080194417064167151864471283996817178454695702627163106454615025720740248163777338963855069526066834113727387372292895649354702576265209885969320196505855476470330679365443254763274495125040606943814710468994650622016772042452452961268794654619316517468139267250410380254625965686914419287160829380317271436778265487756648508567407764845146443994046142260319309673540257444607030809608504748663852313818167675143866747664789088143714198549423151997354880375165861275352916610007105355824987941472950929311389715599820565439287170007218085761025236889213244971389320378439353088774825970171559107088236836275898425891853530243634214367061189236789192372314672321720534016492568727477823445353476481149418642386776774406069562657379600867076257199184734022651462837904883062033061144630073719489");
	private static final BigDecimal LOG_THREE = new BigDecimal("1.0986122886681096913952452369225257046474905578227494517346943336374942932186089668736157548137320887879700290659578657423680042259305198210528018707672774106031627691833813671793736988443609599037425703167959115211455919177506713470549401667755802222031702529468975606901065215056428681380363173732985777823669916547921318181490200301038236301222486527481982259910974524908964580534670088459650857484441190188570876474948670796130858294116021661211840014098255143919487688936798494302255731535329685345295251459213876494685932562794416556941578272310355168866102118469890439943063138255285736466882824988136822800634143910786893251456437510204451627561934973982116941585740535361758900975122233797736969687754354795135712982177017581242122351405810163272465588937249564919185242960796684234647069377237252655082032078333928055892853146873095132606458309184397496822230325765467533311823019649275257599132217851353390237482964339502546074245824934666866121881436526565429542767610505477795422933973323401173743193974579847018559548494059478353943841010602930762292228131207489306344534025277732685627");
	private static final BigDecimal LOG_TEN = new BigDecimal("2.3025850929940456840179914546843642076011014886287729760333279009675726096773524802359972050895982983419677840422862486334095254650828067566662873690987816894829072083255546808437998948262331985283935053089653777326288461633662222876982198867465436674744042432743651550489343149393914796194044002221051017141748003688084012647080685567743216228355220114804663715659121373450747856947683463616792101806445070648000277502684916746550586856935673420670581136429224554405758925724208241314695689016758940256776311356919292033376587141660230105703089634572075440370847469940168269282808481184289314848524948644871927809676271275775397027668605952496716674183485704422507197965004714951050492214776567636938662976979522110718264549734772662425709429322582798502585509785265383207606726317164309505995087807523710333101197857547331541421808427543863591778117054309827482385045648019095610299291824318237525357709750539565187697510374970888692180205189339507238539205144634197265287286965110862571492198849978748873771345686209167058498078280597511938544450099781311469159346662410718466923101075984383191913");

	private static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989380952572010654858632788659361533818279682303019520353018529689957736225994138912497217752834791315");                               
	
	private static final BigDecimal ROUGHLY_TWO_PI = new BigDecimal("3.141592653589793").multiply(TWO);
	
	private static final BigDecimal MAX_INT = valueOf(Integer.MAX_VALUE);
	
	private static final int EXPECTED_INITIAL_PRECISION = 17;
	
	private static BigDecimal[] factorialCache = new BigDecimal[100];
	static {
		BigDecimal result = ONE;
		factorialCache[0] = result;
		for (int i = 1; i < factorialCache.length; i++) {
			result = result.multiply(valueOf(i));
			factorialCache[i] = result;
		}
	}

	private BigDecimalMath() {
		// prevent instances
	}

	/**
	 * Returns whether the specified {@link BigDecimal} value can be represented as <code>int</code> without loss of precision.
	 * 
	 * <p>If this returns <code>true</code> you can call {@link BigDecimal#intValueExact()} without fear of an {@link ArithmeticException}.</p>
	 * 
	 * @param value the {@link BigDecimal} to check 
	 * @return <code>true</code> if the value can be represented as <code>int</code> without loss of precision
	 */
	public static boolean isIntValue(BigDecimal value) {
		// TODO impl isIntValue() without exceptions
		try {
			value.intValueExact();
			return true;
		} catch (ArithmeticException ex) {
			// ignored
		}
		return false;
	}

	/**
	 * Returns the mantissa of the specified {@link BigDecimal} written as <em>mantissa * 10<sup>exponent</sup></em>.
	 * 
	 * <p>The mantissa is defined as having exactly 1 digit before the decimal point.</p>
	 * 
	 * @param value the {@link BigDecimal}
	 * @return the mantissa
	 * @see #exponent(BigDecimal)
	 */
	public static BigDecimal mantissa(BigDecimal value) {
		int exponent = exponent(value);
		if (exponent == 0) {
			return value;
		}
		
		return value.movePointLeft(exponent);
	}
	
	/**
	 * Returns the exponent of the specified {@link BigDecimal} written as <em>mantissa * 10<sup>exponent</sup></em>.
	 * 
	 * <p>The mantissa is defined as having exactly 1 digit before the decimal point.</p>
	 * 
	 * @param value the {@link BigDecimal}
	 * @return the exponent
	 * @see #mantissa(BigDecimal)
	 */
	public static int exponent(BigDecimal value) {
		return value.precision() - value.scale() - 1;
	}

	/**
	 * Returns the integral part of the specified {@link BigDecimal} (left of the decimal point).
	 * 
	 * @param value the {@link BigDecimal}
	 * @return the integral part
	 * @see #fractionalPart(BigDecimal)
	 */
	public static BigDecimal integralPart(BigDecimal value) {
		return value.setScale(0, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * Returns the integral part of the specified {@link BigDecimal} (right of the decimal point).
	 * 
	 * @param value the {@link BigDecimal}
	 * @return the integral part
	 * @see #integralPart(BigDecimal)
	 */
	public static BigDecimal fractionalPart(BigDecimal value) {
		return value.subtract(integralPart(value));
	}
	
	/**
	 * Calculates the factorial of the specified {@link BigDecimal}.
	 * 
	 * <p>factorial = 1 * 2 * 3 * ... n</p>
	 * 
	 * @param n the {@link BigDecimal}
	 * @return the factorial {@link BigDecimal}
	 */
	public static BigDecimal factorial(int n) {
		if (n < 0) {
			throw new ArithmeticException("Illegal factorial(n) for n < 0: n = " + n);
		}
		if (n < factorialCache.length) {
			return factorialCache[n];
		}

		BigDecimal result = factorialCache[factorialCache.length - 1];
		for (int i = factorialCache.length; i <= n; i++) {
			result = result.multiply(valueOf(i));
		}
		return result;
	}

	public static BigDecimal bernoulli(int n, MathContext mathContext) {
		if (n < 0) {
			throw new ArithmeticException("Illegal bernoulli(n) for n < 0: n = " + n);
		}
		
		BigRational b = BigRational.bernoulli(n);
		return b.toBigDecimal(mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of {@link BigDecimal} y (x<sup>y</sup>).
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the {@link BigDecimal} value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal pow(BigDecimal x, BigDecimal y, MathContext mathContext) {
		// x^y = exp(y*log(x))

		if (x.signum() == 0) {
			switch (y.signum()) {
				case 0 : return ONE;
				case 1 : return ZERO;
			}
		}

		try {
			int intValue = y.intValueExact();
			return pow(x, intValue, mathContext);
		} catch (ArithmeticException ex) {
			// ignored
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = exp(y.multiply(log(x, mc), mc), mc);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates {@link BigDecimal} x to the power of <code>int</code> y (x<sup>y</sup>).
	 * 
	 * <p>The implementation tries to minimize the number of multiplications of {@link BigDecimal x} (using squares whenever possible).</p>
	 * 
	 * <p>See: <a href="https://en.wikipedia.org/wiki/Exponentiation#Efficient_computation_with_integer_exponents">Wikipedia: Exponentiation - efficient computation</a></p>
	 * 
	 * @param x the {@link BigDecimal} value to take to the power
	 * @param y the <code>int</code> value to serve as exponent
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated x to the power of y with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal pow(BigDecimal x, int y, MathContext mathContext) {
		if (y < 0) {
			return ONE.divide(pow(x, -y, mathContext), mathContext);
		}
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = ONE;
		while (y > 0) {
			if ((y & 1) == 1) {
				// odd exponent -> multiply result with x
				result = result.multiply(x, mc);
				y -= 1;
			}
			
			if (y > 0) {
				// even exponent -> square x
				x = x.multiply(x, mc);
			}
			
			y >>= 1;
		}

		return result.round(mathContext);
	}
	
	/**
	 * Calculates the square root of {@link BigDecimal} x.
	 * 
	 * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * 
	 * @param x the {@link BigDecimal} value to calculate the square root
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated square root of x with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal sqrt(BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
		}

		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal result = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
		int adaptivePrecision = EXPECTED_INITIAL_PRECISION;
		BigDecimal last;

		do {
			last = result;
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
		} while (adaptivePrecision < maxPrecision || result.subtract(last).abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the n'th root of {@link BigDecimal} x.
	 * 
	 * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
	 * 
	 * @param n the {@link BigDecimal} defining the root
	 * @param x the {@link BigDecimal} value to calculate the n'th root
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated n'th root of x with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal root(BigDecimal n, BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal root(x) for x < 0: x = " + x);
		}

		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal nMinus1 = n.subtract(ONE);
		BigDecimal result = x.divide(TWO, MathContext.DECIMAL32);
		int adaptivePrecision = 2; // first approximation has really bad precision
		BigDecimal step;

		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			
			step = x.divide(pow(result, nMinus1, mc), mc).subtract(result, mc).divide(n, mc);
			result = result.add(step, mc);
		} while (adaptivePrecision < maxPrecision || step.abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}

	/**
	 * Calculates the natural logarithm of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the natural logarithm for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException for 0 or negative numbers
	 */
	public static BigDecimal log(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Natural_logarithm
		
		if (x.signum() <= 0) {
			throw new ArithmeticException("Illegal log(x) for x <= 0: x = " + x);
		}
		if (x.compareTo(ONE) == 0) {
			return ZERO;
		}
		
		BigDecimal result;
		switch (x.compareTo(TEN)) {
		case 0:
			result = logTen(mathContext);
			break;
		case 1:
			result = logUsingExponent(x, mathContext);
			break;
		default :
			result = logUsingTwoThree(x, mathContext);
		}

		return result.round(mathContext);
	}

	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 2.
	 * 
	 * @param x the {@link BigDecimal} to calculate the logarithm base 2 for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 2 with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException for 0 or negative numbers
	 */
	public static BigDecimal log2(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 2, mathContext.getRoundingMode());

		BigDecimal result = log(x, mc).divide(logTwo(mc), mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the logarithm of {@link BigDecimal} x to the base 10.
	 * 
	 * @param x the {@link BigDecimal} to calculate the logarithm base 10 for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated natural logarithm {@link BigDecimal} to the base 10 with the precision specified in the <code>mathContext</code>
	 * @throws ArithmeticException for 0 or negative numbers
	 */
	public static BigDecimal log10(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 2, mathContext.getRoundingMode());

		BigDecimal result = log(x, mc).divide(logTen(mc), mc);
		return result.round(mathContext);
	}
	
	private static BigDecimal logUsingNewton(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Natural_logarithm in chapter 'High Precision'
		// y = y + 2 * (x-exp(y)) / (x+exp(y))

		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		
		BigDecimal result = BigDecimal.valueOf(Math.log(x.doubleValue()));
		int adaptivePrecision = EXPECTED_INITIAL_PRECISION;
		BigDecimal step;
		
		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
			
			BigDecimal expY = BigDecimalMath.exp(result, mc);
			step = TWO.multiply(x.subtract(expY, mc), mc).divide(x.add(expY, mc), mc);
			result = result.add(step);
		} while (adaptivePrecision < maxPrecision || step.abs().compareTo(acceptableError) > 0);

		return result;
	}

	private static BigDecimal logUsingTwoThree(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		int factorOfTwo = 0;
		int powerOfTwo = 1;
		int factorOfThree = 0;
		int powerOfThree = 1;

		double value = x.doubleValue();
		if (value < 0.01) {
			// do nothing
		} else if (value < 0.1) { // never happens when called by logUsingExponent()
			while (value < 0.6) {
				value *= 2;
				factorOfTwo--;
				powerOfTwo *= 2;
			}
		}
		else if (value < 0.115) { // (0.1 - 0.11111 - 0.115) -> (0.9 - 1.0 - 1.035)
			factorOfThree = -2;
			powerOfThree = 9;
		}
		else if (value < 0.14) { // (0.115 - 0.125 - 0.14) -> (0.92 - 1.0 - 1.12)
			factorOfTwo = -3;
			powerOfTwo = 8;
		}
		else if (value < 0.2) { // (0.14 - 0.16667 - 0.2) - (0.84 - 1.0 - 1.2)
			factorOfTwo = -1;
			powerOfTwo = 2;
			factorOfThree = -1;
			powerOfThree = 3;
		}
		else if (value < 0.3) { // (0.2 - 0.25 - 0.3) -> (0.8 - 1.0 - 1.2)
			factorOfTwo = -2;
			powerOfTwo = 4;
		}
		else if (value < 0.42) { // (0.3 - 0.33333 - 0.42) -> (0.9 - 1.0 - 1.26)
			factorOfThree = -1;
			powerOfThree = 3;
		}
		else if (value < 0.7) { // (0.42 - 0.5 - 0.7) -> (0.84 - 1.0 - 1.4)
			factorOfTwo = -1;
			powerOfTwo = 2;
		}
		else if (value < 1.4) { // (0.7 - 1.0 - 1.4) -> (0.7 - 1.0 - 1.4)
			// do nothing
		}
		else if (value < 2.5) { // (1.4 - 2.0 - 2.5) -> (0.7 - 1.0 - 1.25)
			factorOfTwo = 1;
			powerOfTwo = 2;
		} 
		else if (value < 3.5) { // (2.5 - 3.0 - 3.5) -> (0.833333 - 1.0 - 1.166667)
			factorOfThree = 1;
			powerOfThree = 3;
		}
		else if (value < 5.0) { // (3.5 - 4.0 - 5.0) -> (0.875 - 1.0 - 1.25)
			factorOfTwo = 2;
			powerOfTwo = 4;
		}
		else if (value < 7.0) { // (5.0 - 6.0 - 7.0) -> (0.833333 - 1.0 - 1.166667)
			factorOfThree = 1;
			powerOfThree = 3;
			factorOfTwo = 1;
			powerOfTwo = 2;
		}
		else if (value < 8.5) { // (7.0 - 8.0 - 8.5) -> (0.875 - 1.0 - 1.0625)
			factorOfTwo = 3;
			powerOfTwo = 8;
		}
		else if (value < 10.0) { // (8.5 - 9.0 - 10.0) -> (0.94444 - 1.0 - 1.11111)
			factorOfThree = 2;
			powerOfThree = 9;
		}
		else {
			while (value > 1.4) { // never happens when called by logUsingExponent()
				value /= 2;
				factorOfTwo++;
				powerOfTwo *= 2;
			}
		}

		BigDecimal correctedX = x;
		BigDecimal result = ZERO;

		if (factorOfTwo > 0) {
			correctedX = correctedX.divide(valueOf(powerOfTwo), mc);
			result = result.add(logTwo(mc).multiply(valueOf(factorOfTwo), mc), mc);
		}
		else if (factorOfTwo < 0) {
			correctedX = correctedX.multiply(valueOf(powerOfTwo), mc);
			result = result.subtract(logTwo(mc).multiply(valueOf(-factorOfTwo), mc), mc);
		}

		if (factorOfThree > 0) {
			correctedX = correctedX.divide(valueOf(powerOfThree), mc);
			result = result.add(logThree(mc).multiply(valueOf(factorOfThree), mc), mc);
		}
		else if (factorOfThree < 0) {
			correctedX = correctedX.multiply(valueOf(powerOfThree), mc);
			result = result.subtract(logThree(mc).multiply(valueOf(-factorOfThree), mc), mc);
		}

		result = result.add(logUsingNewton(correctedX, mc));

		return result;
	}

	private static BigDecimal logUsingExponent(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		int exponent = exponent(x);
		BigDecimal mantissa = mantissa(x);
		
		BigDecimal result = logUsingTwoThree(mantissa, mc);
		if (exponent != 0) {
			result = result.add(valueOf(exponent).multiply(logTen(mc), mc), mc);
		}
		return result;
	}
	
	public static BigDecimal pi(MathContext mathContext) {
		if (mathContext.getPrecision() < PI.precision()) {
			return PI.round(mathContext);
		}
		return piChudnovski(mathContext); 
	}
	
	private static BigDecimal piChudnovski(MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 10, mathContext.getRoundingMode());

		final BigDecimal value24 = BigDecimal.valueOf(24);
		final BigDecimal value640320 = BigDecimal.valueOf(640320);
		final BigDecimal value13591409 = BigDecimal.valueOf(13591409);
		final BigDecimal value545140134 = BigDecimal.valueOf(545140134);
		final BigDecimal valueDivisor = value640320.pow(3).divide(value24, mc);

		BigDecimal sumA = BigDecimal.ONE;
		BigDecimal sumB = BigDecimal.ZERO;

		BigDecimal a = BigDecimal.ONE;
		long dividendTerm1 = 5; // -(6*k - 5)
		long dividendTerm2 = -1; // 2*k - 1
		long dividendTerm3 = -1; // 6*k - 1
		BigDecimal kPower3 = BigDecimal.ZERO;
		
		long iterationCount = (mc.getPrecision()+13) / 14;
		for (long k = 1; k <= iterationCount; k++) {
			BigDecimal valueK = BigDecimal.valueOf(k);
			dividendTerm1 += -6;
			dividendTerm2 += 2;
			dividendTerm3 += 6;
			BigDecimal dividend = BigDecimal.valueOf(dividendTerm1).multiply(BigDecimal.valueOf(dividendTerm2)).multiply(BigDecimal.valueOf(dividendTerm3));
			kPower3 = valueK.pow(3);
			BigDecimal divisor = kPower3.multiply(valueDivisor, mc);
			a = a.multiply(dividend).divide(divisor, mc);
			BigDecimal b = valueK.multiply(a, mc);
			
			sumA = sumA.add(a);
			sumB = sumB.add(b);
		}
		
		final BigDecimal value426880 = BigDecimal.valueOf(426880);
		final BigDecimal value10005 = BigDecimal.valueOf(10005);
		final BigDecimal factor = value426880.multiply(sqrt(value10005, mc));
		BigDecimal pi = factor.divide(value13591409.multiply(sumA, mc).add(value545140134.multiply(sumB, mc)), mc);
		
		return pi.round(mathContext);
	}

	
	public static BigDecimal e(MathContext mathContext) {
		return exp(ONE, mathContext);
	}
	
	private static BigDecimal logTen(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_TEN.precision()) {
			return LOG_TEN;
		}
		return logUsingNewton(BigDecimal.TEN, mathContext);
	}
	
	private static BigDecimal logTwo(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_TWO.precision()) {
			return LOG_TWO;
		}
		return logUsingNewton(TWO, mathContext);
	}

	private static BigDecimal logThree(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_THREE.precision()) {
			return LOG_THREE;
		}
		return logUsingNewton(THREE, mathContext);
	}

	/**
	 * Calculates the natural exponent of {@link BigDecimal} x (e<sup>x</sup>).
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Exponent">Wikipedia: Exponent</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the exponent for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated exponent {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal exp(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ONE;
		}

		return expIntegralFractional(x, mathContext);
	}

	private static BigDecimal expIntegralFractional(BigDecimal x, MathContext mathContext) {
		BigDecimal integralPart = integralPart(x);
		
		if (integralPart.signum() == 0) {
			return expTaylor(x, mathContext);
		}
		
		BigDecimal fractionalPart = x.subtract(integralPart);

		MathContext mc = new MathContext(mathContext.getPrecision() + 5, mathContext.getRoundingMode());

        BigDecimal z = ONE.add(fractionalPart.divide(integralPart, mc));
        BigDecimal t = expTaylor(z, mc);

        BigDecimal result = ONE;
        while (integralPart.compareTo(MAX_INT) >= 0) {
        	result = result.multiply(pow(t, Integer.MAX_VALUE, mc), mc);
        	integralPart = integralPart.subtract(MAX_INT);
        }
        
        result = result.multiply(pow(t, integralPart.intValue(), mc));
        
		return result.round(mathContext);
	}
	
	private static BigDecimal expTaylor(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		x = x.divide(valueOf(256), mc);
		
		BigDecimal result = ExpCalculator.INSTANCE.calculate(x, mc);
		result = BigDecimalMath.pow(result, 256, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the sine (sinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Sine">Wikipedia: Sine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal sin(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.abs().compareTo(ROUGHLY_TWO_PI) > 0) {
			BigDecimal twoPi = TWO.multiply(pi(mc), mc);
			x = x.remainder(twoPi, mc);
		}

		BigDecimal result = SinCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the arc sine (inverted sine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arcsine">Wikipedia: Arcsine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc sine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal asin(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal asin(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal asin(x) for x < -1: x = " + x);
		}
		
		if (x.signum() == -1) {
			return asin(x.negate(), mathContext).negate();
		}
		
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.compareTo(BigDecimal.valueOf(0.707107)) >= 0) {
			BigDecimal xTransformed = sqrt(ONE.subtract(x.multiply(x, mc), mc), mc);
			return acos(xTransformed, mathContext);
		}

		BigDecimal result = AsinCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}
	
	/**
	 * Calculates the cosine (cosinus) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Cosine">Wikipedia: Cosine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated cosine {@link BigDecimal}
	 */
	public static BigDecimal cos(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		if (x.abs().compareTo(ROUGHLY_TWO_PI) > 0) {
			BigDecimal twoPi = TWO.multiply(pi(mc), mc);
			x = x.remainder(twoPi, mc);
		}
		
		BigDecimal result = CosCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the arc cosine (inverted cosine) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arccosine">Wikipedia: Arccosine</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc cosine for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc sine {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal acos(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal acos(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal acos(x) for x < -1: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = pi(mc).divide(TWO, mc).subtract(asin(x, mc), mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the tangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Tangens">Wikipedia: Tangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the tangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal tan(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			return ZERO;
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		return sin(x, mc).divide(cos(x, mc), mc).round(mathContext);
	}
	
	/**
	 * Calculates the arc tangens (inverted tangens) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arctangens">Wikipedia: Arctangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc tangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc tangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal atan(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		x = x.divide(sqrt(ONE.add(x.multiply(x, mc), mc), mc), mc);

		BigDecimal result = asin(x, mc);
		return result.round(mathContext);
	}

	/**
	 * Calculates the cotangens of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Cotangens">Wikipedia: Cotangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated cotanges {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal cot(BigDecimal x, MathContext mathContext) {
		if (x.signum() == 0) {
			throw new ArithmeticException("Illegal cot(x) for x = 0");
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = cos(x, mc).divide(sin(x, mc), mc).round(mathContext);
		return result.round(mathContext);
	}

	/**
	 * Calculates the inverse cotangens (arc cotangens) of {@link BigDecimal} x.
	 * 
	 * <p>See: <a href="http://en.wikipedia.org/wiki/Arccotangens">Wikipedia: Arccotangens</a></p>
	 * 
	 * @param x the {@link BigDecimal} to calculate the arc cotangens for
	 * @param mathContext the {@link MathContext} used for the result
	 * @return the calculated arc cotangens {@link BigDecimal} with the precision specified in the <code>mathContext</code>
	 */
	public static BigDecimal acot(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = pi(mc).divide(TWO, mc).subtract(atan(x, mc), mc);
		return result.round(mathContext);
	}

	public static BigDecimal sinh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = SinhCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	public static BigDecimal cosh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal result = CoshCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	public static BigDecimal tanh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = sinh(x, mc).divide(cosh(x, mc), mc);
		return result.round(mathContext);
	}
	
	public static BigDecimal asinh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(sqrt(x.multiply(x, mc).add(ONE, mc), mc), mc), mc);
		return result.round(mathContext);
	}	
	
	public static BigDecimal acosh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(sqrt(x.multiply(x, mc).subtract(ONE, mc), mc), mc), mc);
		return result.round(mathContext);
	}	

	public static BigDecimal atanh(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(ONE.add(x, mc).divide(ONE.subtract(x, mc), mc), mc).divide(TWO, mc);
		return result.round(mathContext);
	}	

	public static BigDecimal acoth(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());
		BigDecimal result = log(x.add(ONE, mc).divide(x.subtract(ONE, mc), mc), mc).divide(TWO, mc);
		return result.round(mathContext);
	}	

	/**
	 * A context for {@link BigDecimal} calculations with a specific {@link MathContext}.
	 */
	public static class Context {
		private MathContext mathContext;

		/**
		 * Creates a context with the specified {@link MathContext}.
		 * 
		 * @param mathContext the {@link MathContext} to use
		 */
		public Context(MathContext mathContext) {
			this.mathContext = mathContext;
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>int</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the <code>int</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(int, MathContext)
		 */
		public BigDecimal valueOf(int value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>long</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the <code>long</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(long, MathContext)
		 */
		public BigDecimal valueOf(long value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified <code>double</code> value using the {@link MathContext} from this {#link Context}.
		 * 
		 * <p>To avoid unexpected results this method uses <br>
		 * {@link BigDecimal#valueOf(double)} and {@link BigDecimal#round(MathContext)}<br>
		 * and <strong>not</strong> {@link BigDecimal#BigDecimal(double, MathContext)}.</p>
		 * 
		 * @param value the <code>double</code> value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#valueOf(double)
		 * @see BigDecimal#round(MathContext)
		 */
		public BigDecimal valueOf(double value) {
			return BigDecimal.valueOf(value).round(mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified {@link BigInteger} value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the {@link BigInteger} value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(BigInteger, MathContext)
		 */
		public BigDecimal valueOf(BigInteger value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Creates a {@link BigDecimal} from the specified {@link String} value using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param value the {@link String} value
		 * @return the created {@link BigDecimal}
		 * @see BigDecimal#BigDecimal(String, MathContext)
		 */
		public BigDecimal valueOf(String value) {
			return new BigDecimal(value, mathContext);
		}

		/**
		 * Adds two {@link BigDecimal} values using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#add(BigDecimal, MathContext)
		 */
		public BigDecimal add(BigDecimal x, BigDecimal y) {
			return x.add(y, mathContext);
		}
		
		/**
		 * Subtracts {@link BigDecimal} y from {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#subtract(BigDecimal, MathContext)
		 */
		public BigDecimal subtract(BigDecimal x, BigDecimal y) {
			return x.subtract(y, mathContext);
		}
		
		/**
		 * Multiples two {@link BigDecimal} values using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#multiply(BigDecimal, MathContext)
		 */
		public BigDecimal multiply(BigDecimal x, BigDecimal y) {
			return x.multiply(y, mathContext);
		}
		
		/**
		 * Divides {@link BigDecimal} y from {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#divide(BigDecimal, MathContext)
		 */
		public BigDecimal divide(BigDecimal x, BigDecimal y) {
			return x.divide(y, mathContext);
		}

		/**
		 * Calculates the division to integral value of {@link BigDecimal} y divided by {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#divideToIntegralValue(BigDecimal, MathContext)
		 */
		public BigDecimal divideToIntegralValue(BigDecimal x, BigDecimal y) {
			return x.divideToIntegralValue(y, mathContext);
		}

		/**
		 * Calculated the remainder of {@link BigDecimal} y divided by {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#remainder(BigDecimal, MathContext)
		 */
		public BigDecimal remainder(BigDecimal x, BigDecimal y) {
			return x.remainder(y, mathContext);
		}

		public BigDecimal[] divideAndRemainder(BigDecimal x, BigDecimal y) {
			return x.divideAndRemainder(y, mathContext);
		}

		/**
		 * Returns the absolute value of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#abs(MathContext)
		 */
		public BigDecimal abs(BigDecimal x) {
			return x.abs(mathContext);
		}

		/**
		 * Returns the negated value of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimal#negate(MathContext)
		 */
		public BigDecimal negate(BigDecimal x) {
			return x.negate(mathContext);
		}

		/**
		 * Returns the natural logarithm of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#log(BigDecimal, MathContext)
		 */
		public BigDecimal log(BigDecimal x) {
			return BigDecimalMath.log(x, mathContext);
		}
		
		/**
		 * Returns the {@link BigDecimal} x to the power of {@link BigDecimal} y using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @param y the {@link BigDecimal} y value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)
		 */
		public BigDecimal pow(BigDecimal x, BigDecimal y) {
			return BigDecimalMath.pow(x, y, mathContext);
		}
		
		/**
		 * Returns the natural exponent of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#exp(BigDecimal, MathContext)
		 */
		public BigDecimal exp(BigDecimal x) {
			return BigDecimalMath.exp(x, mathContext);
		}
		
		/**
		 * Returns the square root of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#sqrt(BigDecimal, MathContext)
		 */
		public BigDecimal sqrt(BigDecimal x) {
			return BigDecimalMath.sqrt(x, mathContext);
		}
		
		/**
		 * Returns the n'th root of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param n the {@link BigDecimal} n value
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#root(BigDecimal, BigDecimal, MathContext)
		 */
		public BigDecimal root(BigDecimal n, BigDecimal x) {
			return BigDecimalMath.root(n, x, mathContext);
		}
		
		/**
		 * Returns the sine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#sin(BigDecimal, MathContext)
		 */
		public BigDecimal sin(BigDecimal x) {
			return BigDecimalMath.sin(x, mathContext);
		}
		
		/**
		 * Returns the cosine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#cos(BigDecimal, MathContext)
		 */
		public BigDecimal cos(BigDecimal x) {
			return BigDecimalMath.cos(x, mathContext);
		}

		/**
		 * Returns the tangens of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#tan(BigDecimal, MathContext)
		 */
		public BigDecimal tan(BigDecimal x) {
			return BigDecimalMath.cos(x, mathContext);
		}

		/**
		 * Returns the cotangens of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#cot(BigDecimal, MathContext)
		 */
		public BigDecimal cot(BigDecimal x) {
			return BigDecimalMath.cos(x, mathContext);
		}

		/**
		 * Returns the arc sine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#asin(BigDecimal, MathContext)
		 */
		public BigDecimal asin(BigDecimal x) {
			return BigDecimalMath.asin(x, mathContext);
		}
		
		/**
		 * Returns the arc cosine of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#acos(BigDecimal, MathContext)
		 */
		public BigDecimal acos(BigDecimal x) {
			return BigDecimalMath.acos(x, mathContext);
		}

		/**
		 * Returns the arc tangens of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#atan(BigDecimal, MathContext)
		 */
		public BigDecimal atan(BigDecimal x) {
			return BigDecimalMath.atan(x, mathContext);
		}

		/**
		 * Returns the arc cotangens of {@link BigDecimal} x using the {@link MathContext} from this {#link Context}.
		 * 
		 * @param x the {@link BigDecimal} x value
		 * @return the resulting {@link BigDecimal}
		 * @see BigDecimalMath#acot(BigDecimal, MathContext)
		 */
		public BigDecimal acot(BigDecimal x) {
			return BigDecimalMath.cos(x, mathContext);
		}
	}}
