package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PostfixFunctionParser {
	
	private static final Map<String, BigDecimalFunction0> function0Map = new HashMap<>();
	private static final Map<String, BigDecimalFunction1> function1Map = new HashMap<>();
	private static final Map<String, BigDecimalFunction2> function2Map = new HashMap<>();
	private String expression;

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
		function1Map.put("asinh", (x, mathContext) -> BigDecimalMath.asinh(x, mathContext));
		function1Map.put("acosh", (x, mathContext) -> BigDecimalMath.acosh(x, mathContext));
		function1Map.put("atanh", (x, mathContext) -> BigDecimalMath.atanh(x, mathContext));
		function1Map.put("acoth", (x, mathContext) -> BigDecimalMath.acoth(x, mathContext));
		
		function2Map.put("pow", (x, y, mathContext) -> BigDecimalMath.pow(x, y, mathContext));
		function2Map.put("root", (x, y, mathContext) -> BigDecimalMath.root(x, y, mathContext));
		function2Map.put("+", (x, y, mathContext) -> x.add(y, mathContext));
		function2Map.put("-", (x, y, mathContext) -> x.subtract(y, mathContext));
		function2Map.put("*", (x, y, mathContext) -> x.multiply(y, mathContext));
		function2Map.put("/", (x, y, mathContext) -> x.divide(y, mathContext));
		function2Map.put("%", (x, y, mathContext) -> x.remainder(y, mathContext));
	}

	private static class ScriptFunction implements BigDecimalFunction1 {
		private String[] script;

		public ScriptFunction(String[] script) {
			this.script = script;
		}
		
		@Override
		public BigDecimal apply(BigDecimal value, MathContext mathContext) {
			Deque<BigDecimal> stack = new ArrayDeque<>();

			stack.push(value);
			
			executeScript(mathContext, stack);

			return stack.pop();
		}

		private void executeScript(MathContext mathContext, Deque<BigDecimal> stack) {
			for (String token : script) {
				BigDecimalFunction0 function0 = function0Map.get(token);
				BigDecimalFunction1 function1 = function1Map.get(token);
				BigDecimalFunction2 function2 = function2Map.get(token);

				if (function0 != null) {
					stack.push(function0.apply(mathContext));
				} else if (function1 != null) {
					stack.push(function1.apply(stack.pop(), mathContext));
				} else if (function2 != null) {
					stack.push(function2.apply(stack.pop(), stack.pop(), mathContext));
				} else if (token.equals("dup")){
					BigDecimal value = stack.pop();
					stack.push(value);
					stack.push(value);
				} else if (token.equals("swap")){
					BigDecimal value1 = stack.pop();
					BigDecimal value2 = stack.pop();
					stack.push(value2);
					stack.push(value1);
				} else if (token.equals("drop")){
					stack.pop();
				} else {
					stack.push(new BigDecimal(token));
				}
			}
		}
	}
	
	public PostfixFunctionParser(String expression) {
		this.expression = expression;
	}
	
	public BigDecimalFunction1 compile() {
		String[] tokens = expression.split(" +");
		return new ScriptFunction(tokens);
	}
}
