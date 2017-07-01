package ch.obermuhlner.math.big.viewer;

import javafx.scene.paint.Color;

public class FunctionInfo {
	public final String name;
	public final Color color;
	public final BigDecimalFunction function;

	public FunctionInfo(String name, Color color, BigDecimalFunction function) {
		this.name = name;
		this.color = color;
		this.function = function;
	}
}
