package ch.obermuhlner.math.big.example;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.internal.AsinCalculator;
import ch.obermuhlner.math.big.internal.ExpCalculator;

public class BigDecimalMathExperimental {

	private static final BigDecimal TWO = valueOf(2);
	private static final BigDecimal THREE = valueOf(3);
	private static final BigDecimal FOUR = BigDecimal.valueOf(4);

	private static final BigDecimal LOG_TWO = new BigDecimal("0.69314718055994530941723212145817656807550013436025525412068000949339362196969471560586332699641868754200148102057068573368552023575813055703267075163507596193072757082837143519030703862389167347112335011536449795523912047517268157493206515552473413952588295045300709532636664265410423915781495204374043038550080194417064167151864471283996817178454695702627163106454615025720740248163777338963855069526066834113727387372292895649354702576265209885969320196505855476470330679365443254763274495125040606943814710468994650622016772042452452961268794654619316517468139267250410380254625965686914419287160829380317271436778265487756648508567407764845146443994046142260319309673540257444607030809608504748663852313818167675143866747664789088143714198549423151997354880375165861275352916610007105355824987941472950929311389715599820565439287170007218085761025236889213244971389320378439353088774825970171559107088236836275898425891853530243634214367061189236789192372314672321720534016492568727477823445353476481149418642386776774406069562657379600867076257199184734022651462837904883062033061144630073719489");
	private static final BigDecimal LOG_THREE = new BigDecimal("1.0986122886681096913952452369225257046474905578227494517346943336374942932186089668736157548137320887879700290659578657423680042259305198210528018707672774106031627691833813671793736988443609599037425703167959115211455919177506713470549401667755802222031702529468975606901065215056428681380363173732985777823669916547921318181490200301038236301222486527481982259910974524908964580534670088459650857484441190188570876474948670796130858294116021661211840014098255143919487688936798494302255731535329685345295251459213876494685932562794416556941578272310355168866102118469890439943063138255285736466882824988136822800634143910786893251456437510204451627561934973982116941585740535361758900975122233797736969687754354795135712982177017581242122351405810163272465588937249564919185242960796684234647069377237252655082032078333928055892853146873095132606458309184397496822230325765467533311823019649275257599132217851353390237482964339502546074245824934666866121881436526565429542767610505477795422933973323401173743193974579847018559548494059478353943841010602930762292228131207489306344534025277732685627");
	private static final BigDecimal LOG_TEN = new BigDecimal("2.3025850929940456840179914546843642076011014886287729760333279009675726096773524802359972050895982983419677840422862486334095254650828067566662873690987816894829072083255546808437998948262331985283935053089653777326288461633662222876982198867465436674744042432743651550489343149393914796194044002221051017141748003688084012647080685567743216228355220114804663715659121373450747856947683463616792101806445070648000277502684916746550586856935673420670581136429224554405758925724208241314695689016758940256776311356919292033376587141660230105703089634572075440370847469940168269282808481184289314848524948644871927809676271275775397027668605952496716674183485704422507197965004714951050492214776567636938662976979522110718264549734772662425709429322582798502585509785265383207606726317164309505995087807523710333101197857547331541421808427543863591778117054309827482385045648019095610299291824318237525357709750539565187697510374970888692180205189339507238539205144634197265287286965110862571492198849978748873771345686209167058498078280597511938544450099781311469159346662410718466923101075984383191913");

	private static final BigDecimal MINUS_ONE = valueOf(-1);

	private BigDecimalMathExperimental() {
		// prevent instances
	}

	// variations on exp()
	
	public static BigDecimal exp(BigDecimal x, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal result = ExpCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);

	}
	
	public static BigDecimal expReducing(BigDecimal x, MathContext mathContext, int reduce) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		x = x.divide(valueOf(reduce), mc);
		BigDecimal result = ExpCalculator.INSTANCE.calculate(x, mc);
		result = BigDecimalMath.pow(result, reduce, mc);
		return result.round(mathContext);

	}
	
	// variations on asin()
	
	public static BigDecimal asinUsingNewton(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal asin(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal asin(x) for x < -1: x = " + x);
		}
		
		if (x.signum() == -1) {
			return asinUsingNewton(x.negate(), mathContext).negate();
		}

		int maxPrecision = mathContext.getPrecision() + 4;

		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		MathContext maxMathContext = new MathContext(maxPrecision, mathContext.getRoundingMode());
		
		if (x.compareTo(BigDecimal.valueOf(0.707107)) >= 0) {
			BigDecimal xTransformed = BigDecimalMath.sqrt(ONE.subtract(x.multiply(x, maxMathContext), maxMathContext), maxMathContext);
			return acosUsingNewton(xTransformed, mathContext);
		}

		BigDecimal denominator = BigDecimalMath.cos(x, maxMathContext);

		BigDecimal result = BigDecimal.valueOf(Math.asin(x.doubleValue()));
		int adaptivePrecision = 17;
		BigDecimal step;
		
		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());

			BigDecimal nominator = BigDecimalMath.sin(result, mc).subtract(x, mc); 
			step = nominator.divide(denominator, mc);
			result = result.subtract(step, mc);
//			System.out.println(BigDecimalMath.exponent(step) + " : " + result);
		} while (step.abs().compareTo(acceptableError) > 0);

		return result.round(mathContext);
	}
	
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
			BigDecimal xTransformed = BigDecimalMath.sqrt(ONE.subtract(x.multiply(x, mc), mc), mc);
			return acos(xTransformed, mathContext);
		}

		BigDecimal result = AsinCalculator.INSTANCE.calculate(x, mc);
		return result.round(mathContext);
	}

	public static BigDecimal acos(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal acos(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal acos(x) for x < -1: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = BigDecimalMath.pi(mc).divide(TWO, mc).subtract(asin(x, mc), mc);
		return result.round(mathContext);
	}	
	
	public static BigDecimal acosUsingNewton(BigDecimal x, MathContext mathContext) {
		if (x.compareTo(ONE) > 0) {
			throw new ArithmeticException("Illegal acos(x) for x > 1: x = " + x);
		}
		if (x.compareTo(MINUS_ONE) < 0) {
			throw new ArithmeticException("Illegal acos(x) for x < -1: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 6, mathContext.getRoundingMode());

		BigDecimal result = BigDecimalMath.pi(mc).divide(TWO, mc).subtract(asinUsingNewton(x, mc), mc);
		return result.round(mathContext);
	}	
	
	// variations on sqrt()
	
	public static BigDecimal sqrtUsingNewton(BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal result = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
		BigDecimal last;

		do {
			last = result;
			result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
		} while (result.subtract(last).abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}
	
	public static BigDecimal sqrtUsingNewtonAdaptivePrecision(BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
		}

		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal result = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
		int adaptivePrecision = 17;
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

	// variations on root()
	
	public static BigDecimal rootFixPrecision(BigDecimal n, BigDecimal x, MathContext mathContext) {
		switch (x.signum()) {
		case 0:
			return ZERO;
		case -1:
			throw new ArithmeticException("Illegal root(x) for x < 0: x = " + x);
		}

		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);

		BigDecimal factor = ONE.divide(n, mc);
		BigDecimal nMinus1 = n.subtract(ONE);
		BigDecimal result = x.divide(TWO, mc);
		BigDecimal step;

		do {
			step = factor.multiply(x.divide(BigDecimalMath.pow(result, nMinus1, mc), mc).subtract(result, mc), mc);
					
			result = result.add(step, mc);
		} while (step.abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}

	public static BigDecimal rootAdaptivePrecision(BigDecimal n, BigDecimal x, MathContext mathContext) {
		return rootAdaptivePrecision(n, x, mathContext, 4);
	}
	
	public static BigDecimal rootAdaptivePrecision(BigDecimal n, BigDecimal x, MathContext mathContext, int initialPrecision) {
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
		int adaptivePrecision = initialPrecision;
		BigDecimal step;

		do {
			adaptivePrecision = adaptivePrecision * 3;
			if (adaptivePrecision > maxPrecision) {
				adaptivePrecision = maxPrecision;
			}
			MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());

			step = x.divide(BigDecimalMath.pow(result, nMinus1, mc), mc).subtract(result, mc).divide(n, mc);
			result = result.add(step, mc);
		} while (adaptivePrecision < maxPrecision || step.abs().compareTo(acceptableError) > 0);
		
		return result.round(mathContext);
	}

	// variations on log()

	public static BigDecimal logRangeTen(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		// http://en.wikipedia.org/wiki/Natural_logarithm

		if (x.signum() <= 0) {
			throw new ArithmeticException("Illegal log(x) for x <= 0: x = " + x);
		}
		if (x.compareTo(ONE) == 0) {
			return ZERO;
		}

		switch (x.compareTo(TEN)) {
			case 0:
				return logTen(mathContext);
			case 1:
				return logUsingExponent(x, mathContext, logFunction);
		}

		return logFunction.apply(x, mathContext);
	}

	public static BigDecimal logUsingRoot(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		// log(x) = log(root(r, x)^r) = r * log(root(r, x))
		BigDecimal r = valueOf(Math.max(2, (int) (Math.log(x.doubleValue()) * 5)));

		BigDecimal result = BigDecimalMath.root(r, x, mc);
		result = logFunction.apply(result, mc).multiply(r, mc);

		return result.round(mathContext);
	}

	public static BigDecimal logUsingSqrt(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		BigDecimal sqrtX = BigDecimalMath.sqrt(x, mc);
		BigDecimal result = logFunction.apply(sqrtX, mc).multiply(TWO, mc);

		return result.round(mathContext);
	}

	public static BigDecimal logUsingPowerTwo(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		int factorOfTwo = 0;
		int powerOfTwo = 1;
		double value = x.doubleValue();
		if (value > 1) {
			while (value > 1.4) {
				value /= 2;
				factorOfTwo++;
				powerOfTwo *= 2;
			}
		}
		else {
			while (value < 0.6) {
				value *= 2;
				factorOfTwo--;
				powerOfTwo *= 2;
			}
		}

		BigDecimal result = null;
		if (factorOfTwo != 0) {
			if (factorOfTwo > 0) {
				BigDecimal correctedX = x.divide(valueOf(powerOfTwo), mc);
				result = logFunction.apply(correctedX, mc).add(logTwo(mc).multiply(valueOf(factorOfTwo), mc), mc);
			}
			else if (factorOfTwo < 0) {
				BigDecimal correctedX = x.multiply(valueOf(powerOfTwo), mc);
				result = logFunction.apply(correctedX, mc).subtract(logTwo(mc).multiply(valueOf(-factorOfTwo), mc), mc);
			}
		}

		if (result == null) {
			result = logFunction.apply(x, mc);
		}

		return result.round(mathContext);
	}


	public static BigDecimal logUsingPrimes(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		int factorOfTwo = 0;
		int powerOfTwo = 1;
		int factorOfThree = 0;
		int powerOfThree = 1;

		double value = x.doubleValue();
		if (value < 0.1) {
			while (value < 0.6) {
				value *= 2;
				factorOfTwo--;
				powerOfTwo *= 2;
			}
		}
		else if (value < 0.115) { // 0.11111 * 9 = 1
			factorOfThree = -2;
			powerOfThree = 9;
		}
		else if (value < 0.14) { // 0.125 * 8 = 1
			factorOfTwo = -3;
			powerOfTwo = 8;
		}
		else if (value < 0.20) { // 0.16667 * 6 = 1
			factorOfTwo = -1;
			powerOfTwo = 2;
			factorOfThree = -1;
			powerOfThree = 3;
		}
		else if (value < 0.3) { // 0.25 * 4 = 1
			factorOfTwo = -2;
			powerOfTwo = 4;
		}
		else if (value < 0.42) { // 0.33333 * 3 = 1
			factorOfThree = -1;
			powerOfThree = 3;
		}
		else if (value < 0.7) { // 0.5 * 2 = 1
			factorOfTwo = -1;
			powerOfTwo = 2;
		}
		else if (value < 1.4) {
			// do nothing
		}
		else if (value < 2.5) {
			factorOfTwo = 1;
			powerOfTwo = 2;
		}
		else if (value < 3.5) {
			factorOfThree = 1;
			powerOfThree = 3;
		}
		else if (value < 5.0) {
			factorOfTwo = 2;
			powerOfTwo = 4;
		}
		else if (value < 7.0) {
			factorOfThree = 1;
			powerOfThree = 3;
			factorOfTwo = 1;
			powerOfTwo = 2;
		}
		else if (value < 8.5) {
			factorOfTwo = 3;
			powerOfTwo = 8;
		}
		else if (value < 12.0) {
			factorOfThree = 2;
			powerOfThree = 9;
		}
		else {
			while (value > 1.4) {
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

		result = result.add(logFunction.apply(correctedX, mc));

		return result.round(mathContext);
	}
	public static BigDecimal logUsingExponent(BigDecimal x, MathContext mathContext, BiFunction<BigDecimal, MathContext, BigDecimal> logFunction) {
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());

		int exponent = BigDecimalMath.exponent(x);
		BigDecimal mantissa = BigDecimalMath.mantissa(x);

		BigDecimal result = logFunction.apply(mantissa, mc);
		if (exponent != 0) {
			result = result.add(valueOf(exponent).multiply(logTen(mc), mc), mc);
		}
		return result.round(mathContext);
	}

	private static BigDecimal logTen(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_TEN.precision()) {
			return LOG_TEN;
		}
		return logAreaHyperbolicTangent(BigDecimal.TEN, mathContext);
	}

	private static BigDecimal logTwo(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_TWO.precision()) {
			return LOG_TWO;
		}
		return logAreaHyperbolicTangent(TWO, mathContext);
	}

	private static BigDecimal logThree(MathContext mathContext) {
		if (mathContext.getPrecision() < LOG_THREE.precision()) {
			return LOG_THREE;
		}
		return logAreaHyperbolicTangent(THREE, mathContext);
	}

	public static BigDecimal logUsingNewtonFixPrecision(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Natural_logarithm in chapter 'High Precision'
		// y = y + 2 * (x-exp(y)) / (x+exp(y))
	
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		
		BigDecimal result = BigDecimal.valueOf(Math.log(x.doubleValue()));
		BigDecimal step;
		
		do {
			BigDecimal expY = BigDecimalMath.exp(result, mc);
			step = TWO.multiply(x.subtract(expY, mc), mc).divide(x.add(expY, mc), mc);
			result = result.add(step);
		} while (step.abs().compareTo(acceptableError) > 0);
	
		return result.round(mathContext);
	}

	public static BigDecimal logUsingNewtonAdaptivePrecision(BigDecimal x, MathContext mathContext) {
		return logUsingNewtonAdaptivePrecision(x, mathContext, 17);
	}
	
	public static BigDecimal logUsingNewtonAdaptivePrecision(BigDecimal x, MathContext mathContext, int initialPrecision) {
		// https://en.wikipedia.org/wiki/Natural_logarithm in chapter 'High Precision'
		// y = y + 2 * (x-exp(y)) / (x+exp(y))
	
		int maxPrecision = mathContext.getPrecision() + 4;
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		
		BigDecimal result = BigDecimal.valueOf(Math.log(x.doubleValue()));
		int adaptivePrecision = initialPrecision;
		BigDecimal step = null;
		
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
	
		return result.round(mathContext);
	}

	public static BigDecimal logAreaHyperbolicTangent(BigDecimal x, MathContext mathContext) {
		// http://en.wikipedia.org/wiki/Logarithm#Calculation
		MathContext mc = new MathContext(mathContext.getPrecision() + 4, mathContext.getRoundingMode());
		BigDecimal acceptableError = ONE.movePointLeft(mathContext.getPrecision() + 1);
		
		BigDecimal magic = x.subtract(ONE, mc).divide(x.add(ONE), mc);
		
		BigDecimal result = ZERO;
		BigDecimal step;
		int i = 0;
		do {
			int doubleIndexPlusOne = i * 2 + 1; 
			step = BigDecimalMath.pow(magic, doubleIndexPlusOne, mc).divide(valueOf(doubleIndexPlusOne), mc);

			result = result.add(step, mc);
			
			i++;
		} while (step.abs().compareTo(acceptableError) > 0);
		
		result = result.multiply(TWO, mc);
		
		return result.round(mathContext);
	}

}
