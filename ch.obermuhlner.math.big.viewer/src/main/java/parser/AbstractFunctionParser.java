package parser;

import java.util.HashMap;
import java.util.Map;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction0;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction1;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction2;

public abstract class AbstractFunctionParser {
	
	protected static final Map<String, BigDecimalFunction0> function0Map = new HashMap<>();
	protected static final Map<String, BigDecimalFunction1> function1Map = new HashMap<>();
	protected static final Map<String, BigDecimalFunction2> function2Map = new HashMap<>();
	protected static final Map<String, BigDecimalFunction2> function2InfixMap = new HashMap<>();

	static {
		function0Map.put("pi", (mathContext) -> BigDecimalMath.pi(mathContext));
		function0Map.put("e", (mathContext) -> BigDecimalMath.e(mathContext));

		function1Map.put("abs", (x, mathContext) -> x.abs());
		function1Map.put("neg", (x, mathContext) -> x.negate());
		function1Map.put("log", (x, mathContext) -> BigDecimalMath.log(x, mathContext));
		function1Map.put("log2", (x, mathContext) -> BigDecimalMath.log2(x, mathContext));
		function1Map.put("log10", (x, mathContext) -> BigDecimalMath.log10(x, mathContext));
		function1Map.put("exp", (x, mathContext) -> BigDecimalMath.exp(x, mathContext));
		function1Map.put("sqrt", (x, mathContext) -> BigDecimalMath.sqrt(x, mathContext));
		function1Map.put("sin", (x, mathContext) -> BigDecimalMath.sin(x, mathContext));
		function1Map.put("cos", (x, mathContext) -> BigDecimalMath.cos(x, mathContext));
		function1Map.put("tan", (x, mathContext) -> BigDecimalMath.tan(x, mathContext));
		function1Map.put("cot", (x, mathContext) -> BigDecimalMath.cot(x, mathContext));
		function1Map.put("asin", (x, mathContext) -> BigDecimalMath.asin(x, mathContext));
		function1Map.put("acos", (x, mathContext) -> BigDecimalMath.acos(x, mathContext));
		function1Map.put("atan", (x, mathContext) -> BigDecimalMath.atan(x, mathContext));
		function1Map.put("acot", (x, mathContext) -> BigDecimalMath.acot(x, mathContext));
		function1Map.put("sinh", (x, mathContext) -> BigDecimalMath.sinh(x, mathContext));
		function1Map.put("cosh", (x, mathContext) -> BigDecimalMath.cosh(x, mathContext));
		function1Map.put("tanh", (x, mathContext) -> BigDecimalMath.tanh(x, mathContext));
		function1Map.put("coth", (x, mathContext) -> BigDecimalMath.coth(x, mathContext));
		function1Map.put("asinh", (x, mathContext) -> BigDecimalMath.asinh(x, mathContext));
		function1Map.put("acosh", (x, mathContext) -> BigDecimalMath.acosh(x, mathContext));
		function1Map.put("atanh", (x, mathContext) -> BigDecimalMath.atanh(x, mathContext));
		function1Map.put("acoth", (x, mathContext) -> BigDecimalMath.acoth(x, mathContext));
		
		function2Map.put("pow", (x, y, mathContext) -> BigDecimalMath.pow(x, y, mathContext));
		function2Map.put("root", (x, y, mathContext) -> BigDecimalMath.root(x, y, mathContext));
		function2Map.put("min", (x, y, mathContext) -> x.min(y));
		function2Map.put("max", (x, y, mathContext) -> x.max(y));
		
		function2InfixMap.put("+", (x, y, mathContext) -> x.add(y, mathContext));
		function2InfixMap.put("-", (x, y, mathContext) -> x.subtract(y, mathContext));
		function2InfixMap.put("*", (x, y, mathContext) -> x.multiply(y, mathContext));
		function2InfixMap.put("/", (x, y, mathContext) -> x.divide(y, mathContext));
		function2InfixMap.put("%", (x, y, mathContext) -> x.remainder(y, mathContext));
		function2InfixMap.put("^", (x, y, mathContext) -> BigDecimalMath.pow(x, y, mathContext));
	}

	public abstract BigDecimalFunction1 compile(String expression);
}
