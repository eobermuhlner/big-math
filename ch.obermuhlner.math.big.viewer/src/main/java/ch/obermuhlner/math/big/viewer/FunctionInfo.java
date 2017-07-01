package ch.obermuhlner.math.big.viewer;

import javafx.scene.paint.Color;

public class FunctionInfo {
	public final String name;
	public final Color color;
	public final BigDecimalFunction1 function;

	public FunctionInfo(String name, Color color, BigDecimalFunction1 function) {
		this.name = name;
		this.color = color;
		this.function = function;
	}
}
