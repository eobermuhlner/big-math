package parser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.obermuhlner.math.big.viewer.BigDecimalFunction0;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction1;
import ch.obermuhlner.math.big.viewer.BigDecimalFunction2;

public class ExpressionFunctionParser extends AbstractFunctionParser {

	@Override
	public BigDecimalFunction1 compile(String expression) {
		BigDecimalFunction1 function = new BigDecimalFunction1() {
			List<String> tokens = tokenize(expression);
			
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				Map<String, BigDecimal> variables = new HashMap<>();
				variables.put("x", value);
				return executeExpression(tokens, variables, mathContext);
			};
		};
		return function;
	}

	private enum Token {
		WHITESPACE("^\\s+"),
		NUMBER("^\\d+(\\.\\d*)?(eE[+-]?\\d)?"),
		IDENTIFIER("^[a-zA-Z]+"),
		OPERATOR("^.");
		
		private Pattern pattern;

		Token(String regex) {
			pattern = Pattern.compile(regex);
		}
	}
	
	private List<String> tokenize(String expression) {
		List<String> tokens = new ArrayList<>();
		
		while (!expression.isEmpty()) {
			for (Token tokenType : Token.values()) {
				Matcher matcher = tokenType.pattern.matcher(expression);
				if (matcher.find()) {
					String token = matcher.group(0);
					if (tokenType != Token.WHITESPACE) {
						tokens.add(token);
					}
					expression = expression.substring(token.length());
					break;
				}
			}
		}
		
		return tokens;
	}

	private BigDecimal executeExpression(List<String> tokens, Map<String, BigDecimal> variables, MathContext mathContext) {
		try {
			Deque<String> tokenStack = new ArrayDeque<>(tokens);
			Deque<BigDecimal> valueStack = new ArrayDeque<>();
			
			while (!tokenStack.isEmpty()) {
				executeExpression(tokenStack, valueStack, variables, mathContext);
			}
			
			return valueStack.pop();
		} catch (Exception ex) {
			//System.out.println(ex.getMessage());
			return BigDecimal.ZERO;
		}
	}
		
	private void executeExpression(Deque<String> tokenStack, Deque<BigDecimal> valueStack, Map<String, BigDecimal> variables, MathContext mathContext) {
		String token = tokenStack.pop();
		
		BigDecimalFunction0 function0 = function0Map.get(token);
		BigDecimalFunction1 function1 = function1Map.get(token);
		BigDecimalFunction2 function2 = function2Map.get(token);
		BigDecimalFunction2 function2Infix = function2InfixMap.get(token);

		if (function0 != null) {
			valueStack.push(function0.apply(mathContext));
		} else if (function1 != null) {
			executeExpression(tokenStack, valueStack, variables, mathContext);
			BigDecimal argument1 = valueStack.pop();
			
			valueStack.push(function1.apply(argument1, mathContext));
		} else if (function2 != null) {
			executeExpression(tokenStack, valueStack, variables, mathContext);
			BigDecimal argument1 = valueStack.pop();
			BigDecimal argument2 = valueStack.pop();
			
			valueStack.push(function2.apply(argument1, argument2, mathContext));
		} else if (function2Infix != null) {
			BigDecimal argument1 = valueStack.pop();
			executeExpression(tokenStack, valueStack, variables, mathContext);
			BigDecimal argument2 = valueStack.pop();
			
			valueStack.push(function2Infix.apply(argument1, argument2, mathContext));
		} else if (token.equals("(")) {
			String endToken = null;
			do {
				executeExpression(tokenStack, valueStack, variables, mathContext);
				endToken = tokenStack.peek();
				if (endToken.equals(",") || endToken.equals(")")) {
					tokenStack.pop();
				}
			} while (!endToken.equals(")"));
		} else {
			BigDecimal value = variables.get(token);
			if (value == null) {
				value = new BigDecimal(token);
			}
			valueStack.push(value);
		}
	}

}
