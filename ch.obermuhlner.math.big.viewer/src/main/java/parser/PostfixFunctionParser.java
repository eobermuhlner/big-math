package parser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.Deque;

import ch.obermuhlner.math.big.viewer.BigDecimalFunction0;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction1;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction2;

public class PostfixFunctionParser extends AbstractFunctionParser {
	
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
				BigDecimalFunction2 function2Infix = function2Map.get(token);

				if (function0 != null) {
					stack.push(function0.apply(mathContext));
				} else if (function1 != null) {
					stack.push(function1.apply(stack.pop(), mathContext));
				} else if (function2 != null) {
					stack.push(function2.apply(stack.pop(), stack.pop(), mathContext));
				} else if (function2Infix != null) {
					stack.push(function2Infix.apply(stack.pop(), stack.pop(), mathContext));
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
	
	@Override
	public BigDecimalFunction1 compile(String expression) {
		String[] tokens = expression.split(" +");
		return new ScriptFunction(tokens);
	}
}
