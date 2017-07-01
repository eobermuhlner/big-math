package ch.obermuhlner.math.big;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public class BigFloat implements Comparable<BigFloat> {

	public static class Context {
		private final MathContext mathContext;
		
		private Context(MathContext mathContext) {
			this.mathContext = mathContext;
		}
		
		public MathContext getMathContext() {
			return mathContext;
		}
		
		public int getPrecision() {
			return mathContext.getPrecision();
		}

		public BigFloat valueOf(BigFloat value) {
			return new BigFloat(value.value, max(this, value.context)); 
		}

		public BigFloat valueOf(BigDecimal value) {
			return new BigFloat(value, this); 
		}

		public BigFloat valueOf(int value) {
			return valueOf(new BigDecimal(value, mathContext)); 
		}

		public BigFloat valueOf(long value) {
			return valueOf(new BigDecimal(value, mathContext)); 
		}

		public BigFloat valueOf(double value) {
			return valueOf(new BigDecimal(String.valueOf(value), mathContext)); 
		}

		public BigFloat pi() {
			return valueOf(BigDecimalMath.pi(mathContext)); 
		}

		public BigFloat e() {
			return valueOf(BigDecimalMath.e(mathContext)); 
		}
	}
	
	private final BigDecimal value;
	private final Context context;

	private BigFloat(BigDecimal value, Context context) {
		this.value = value;
		this.context = context;
	}
	
	public BigFloat add(BigFloat x) {
		Context c = max(context, x.context);
		return new BigFloat(value.add(x.value, c.mathContext), c);
	}

	public BigFloat add(BigDecimal x) {
		return add(context.valueOf(x));
	}

	public BigFloat add(int x) {
		return add(context.valueOf(x));
	}

	public BigFloat add(long x) {
		return add(context.valueOf(x));
	}

	public BigFloat add(double x) {
		return add(context.valueOf(x));
	}

	public BigFloat subtract(BigFloat x) {
		Context c = max(context, x.context);
		return new BigFloat(value.subtract(x.value, c.mathContext), c);
	}

	public BigFloat subtract(BigDecimal x) {
		return subtract(context.valueOf(x));
	}

	public BigFloat subtract(int x) {
		return subtract(context.valueOf(x));
	}

	public BigFloat subtract(long x) {
		return subtract(context.valueOf(x));
	}

	public BigFloat subtract(double x) {
		return subtract(context.valueOf(x));
	}

	public BigFloat multiply(BigFloat x) {
		Context c = max(context, x.context);
		return new BigFloat(value.multiply(x.value, c.mathContext), c);
	}

	public BigFloat multiply(BigDecimal x) {
		return multiply(context.valueOf(x));
	}

	public BigFloat multiply(int x) {
		return multiply(context.valueOf(x));
	}

	public BigFloat multiply(long x) {
		return multiply(context.valueOf(x));
	}

	public BigFloat multiply(double x) {
		return multiply(context.valueOf(x));
	}

	public BigFloat divide(BigFloat x) {
		Context c = max(context, x.context);
		return new BigFloat(value.divide(x.value, c.mathContext), c);
	}

	public BigFloat divide(BigDecimal x) {
		return divide(context.valueOf(x));
	}

	public BigFloat divide(int x) {
		return divide(context.valueOf(x));
	}

	public BigFloat divide(long x) {
		return divide(context.valueOf(x));
	}

	public BigFloat divide(double x) {
		return divide(context.valueOf(x));
	}

	public BigFloat remainder(BigFloat x) {
		Context c = max(context, x.context);
		return new BigFloat(value.remainder(x.value, c.mathContext), c);
	}

	public BigFloat remainder(BigDecimal x) {
		return remainder(context.valueOf(x));
	}

	public BigFloat remainder(int x) {
		return remainder(context.valueOf(x));
	}

	public BigFloat remainder(long x) {
		return remainder(context.valueOf(x));
	}

	public BigFloat remainder(double x) {
		return remainder(context.valueOf(x));
	}

	public BigFloat pow(BigFloat y) {
		Context c = max(context, y.context);
		return c.valueOf(BigDecimalMath.pow(this.value, y.value, c.mathContext));
	}

	public BigFloat pow(BigDecimal y) {
		return pow(context.valueOf(y));
	}

	public BigFloat pow(int y) {
		return pow(context.valueOf(y));
	}

	public BigFloat pow(long y) {
		return pow(context.valueOf(y));
	}

	public BigFloat pow(double y) {
		return pow(context.valueOf(y));
	}

	public BigFloat root(BigFloat y) {
		Context c = max(context, y.context);
		return c.valueOf(BigDecimalMath.root(this.value, y.value, c.mathContext));
	}

	public BigFloat root(BigDecimal y) {
		return root(context.valueOf(y));
	}

	public BigFloat root(int y) {
		return root(context.valueOf(y));
	}

	public BigFloat root(long y) {
		return root(context.valueOf(y));
	}

	public BigFloat root(double y) {
		return root(context.valueOf(y));
	}

	public BigFloat factorial(int n) {
		return context.valueOf(factorial(n));
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, context);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigFloat other = (BigFloat) obj;
		
		return Objects.equals(value, other.value) && Objects.equals(context, other.context); 
	}

	@Override
	public int compareTo(BigFloat other) {
		return value.compareTo(other.value);
	}
	
	public boolean isEqual(BigFloat other) {
		return compareTo(other) == 0;
	}

	public boolean isLessThan(BigFloat other) {
		return compareTo(other) < 0;
	}

	public boolean isGreaterThan(BigFloat other) {
		return compareTo(other) > 0;
	}

	public boolean isLessThanOrEqual(BigFloat other) {
		return compareTo(other) <= 0;
	}

	public boolean isGreaterThanOrEqual(BigFloat other) {
		return compareTo(other) >= 0;
	}
	
	public boolean isIntValue() {
		return BigDecimalMath.isIntValue(value);
	}

	public BigFloat getMantissa() {
		return context.valueOf(BigDecimalMath.mantissa(value));
	}

	public BigFloat getExponent() {
		return context.valueOf(BigDecimalMath.exponent(value));
	}

	public BigFloat getIntegralPart() {
		return context.valueOf(BigDecimalMath.integralPart(value));
	}

	public BigFloat getFractionalPart() {
		return context.valueOf(BigDecimalMath.integralPart(value));
	}

	public Context getContext() {
		return context;
	}
	
	public BigFloat withContext(Context otherContext) {
		return new BigFloat(value.round(otherContext.mathContext), otherContext);
	}
	
	public BigDecimal toBigDecimal() {
		return value;
	}

	public double toDouble() {
		return value.doubleValue();
	}

	public long toLong() {
		return value.longValue();
	}

	public int toInt() {
		return value.intValue();
	}

	@Override
	public String toString() {
		return value.toString();
	}
	
	public static Context context(int precision) {
		return new Context(new MathContext(precision));
	}

	public static Context context(MathContext mathContext) {
		return new Context(mathContext);
	}

	public static BigFloat negate(BigFloat x) {
		return x.context.valueOf(x.value.negate());
	}

	public static BigFloat abs(BigFloat x) {
		return x.context.valueOf(x.value.abs());
	}

	public static BigFloat max(BigFloat value1, BigFloat value2) {
		return value1.compareTo(value2) >= 0 ? value1 : value2; 
	}

	public static BigFloat max(BigFloat value1, BigFloat... values) {
		BigFloat result = value1;
		
		for (BigFloat other : values) {
			result = max(result, other);
		}
		
		return result;
	}

	public static BigFloat min(BigFloat value1, BigFloat value2) {
		return value1.compareTo(value2) < 0 ? value1 : value2; 
	}

	public static BigFloat min(BigFloat value1, BigFloat... values) {
		BigFloat result = value1;
		
		for (BigFloat other : values) {
			result = min(result, other);
		}
		
		return result;
	}

	public static BigFloat log(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.log(x.value, x.context.mathContext));
	}

	public static BigFloat log2(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.log2(x.value, x.context.mathContext));
	}

	public static BigFloat log10(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.log10(x.value, x.context.mathContext));
	}

	public static BigFloat exp(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.exp(x.value, x.context.mathContext));
	}

	public static BigFloat sqrt(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.sqrt(x.value, x.context.mathContext));
	}

	public static BigFloat sin(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.sin(x.value, x.context.mathContext));
	}

	public static BigFloat cos(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.cos(x.value, x.context.mathContext));
	}

	public static BigFloat tan(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.tan(x.value, x.context.mathContext));
	}

	public static BigFloat cot(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.cot(x.value, x.context.mathContext));
	}

	public static BigFloat asin(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.asin(x.value, x.context.mathContext));
	}

	public static BigFloat acos(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.acos(x.value, x.context.mathContext));
	}

	public static BigFloat atan(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.atan(x.value, x.context.mathContext));
	}

	public static BigFloat acot(BigFloat x) {
		return x.context.valueOf(BigDecimalMath.acot(x.value, x.context.mathContext));
	}

	private static Context max(Context left, Context right) {
		return left.mathContext.getPrecision() > right.mathContext.getPrecision() ? left : right;
	}
}
