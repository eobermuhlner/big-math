package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ch.obermuhlner.math.big.BigDecimalMath;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GraphApp extends Application {
	private static final double GRAPH_WIDTH = 800;
	private static final double GRAPH_HEIGHT = 800;

	private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("##0");
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("##0.000");

	private static final StringConverter<BigDecimal> BIGDECIMAL_STRING_CONVERTER = new StringConverter<BigDecimal>() {
		@Override
		public String toString(BigDecimal object) {
			return object.toString();
		}

		@Override
		public BigDecimal fromString(String string) {
			try {
				return new BigDecimal(string);
			} catch (NumberFormatException ex) {
				return BigDecimal.ZERO;
			}
		}
	};

	ObjectProperty<BigDecimal> xStartProperty = new SimpleObjectProperty<>(new BigDecimal(-2));
	ObjectProperty<BigDecimal> xEndProperty = new SimpleObjectProperty<>(new BigDecimal(2));
	ObjectProperty<BigDecimal> yStartProperty = new SimpleObjectProperty<>(new BigDecimal(-2));
	ObjectProperty<BigDecimal> yEndProperty = new SimpleObjectProperty<>(new BigDecimal(2));
	IntegerProperty precisionProperty = new SimpleIntegerProperty(20);

	List<FunctionInfo> functionInfos = new ArrayList<>();
	
	private Canvas graphCanvas;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		
		BorderPane borderPane = new BorderPane();
		root.getChildren().add(borderPane);
		
		Node toolbar = createToolbar();
		borderPane.setTop(toolbar);

		Node editor = createEditor();
		borderPane.setRight(editor);

		graphCanvas = createGraphCanvas();
		borderPane.setCenter(graphCanvas);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		functionInfos.add(new FunctionInfo("sin(x)", Color.RED, new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return BigDecimalMath.sin(value, mathContext);
			}
		}));
		functionInfos.add(new FunctionInfo("sin(1/x)", Color.BLUE, new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return BigDecimalMath.sin(BigDecimal.ONE.divide(value, mathContext), mathContext);
			}
		}));
		functionInfos.add(new FunctionInfo("sin(x)/x", Color.DARKGREEN, new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return BigDecimalMath.sin(value, mathContext).divide(value, mathContext);
			}
		}));
		functionInfos.add(new FunctionInfo("sin(x)+sin(x*10)*0.1", Color.MAGENTA, new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return BigDecimalMath.sin(value, mathContext).add(BigDecimalMath.sin(value.multiply(new BigDecimal("100"), mathContext), mathContext).multiply(new BigDecimal("0.1"), mathContext), mathContext);
			}
		}));
		functionInfos.add(new FunctionInfo("log(x)", Color.YELLOW, new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return BigDecimalMath.log(value, mathContext);
			}
		}));
		functionInfos.add(new FunctionInfo("f(x)", Color.PURPLE, new FunctionParser("sin 1 swap /")));
		
		ChangeListener<? super Number> graphDrawingListener = (observable, oldValue, newValue) -> drawGraph(graphCanvas);
		xStartProperty.addListener(graphDrawingListener);
		xEndProperty.addListener(graphDrawingListener);
		yStartProperty.addListener(graphDrawingListener);
		yEndProperty.addListener(graphDrawingListener);
		precisionProperty.addListener(graphDrawingListener);
		
		drawGraph(graphCanvas);
	}

	private Node createToolbar() {
		HBox box = new HBox(4);
		
		return box;
	}

	private Node createEditor() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);

		int rowIndex = 0;
		
		gridPane.add(new Label("X Start:"), 0, rowIndex);
		TextField xStartTextField = new TextField();
		gridPane.add(xStartTextField, 1, rowIndex);
		Bindings.bindBidirectional(xStartTextField.textProperty(), xStartProperty, BIGDECIMAL_STRING_CONVERTER);
		rowIndex++;

		gridPane.add(new Label("X End:"), 0, rowIndex);
		TextField xEndTextField = new TextField();
		gridPane.add(xEndTextField, 1, rowIndex);
		Bindings.bindBidirectional(xEndTextField.textProperty(), xEndProperty, BIGDECIMAL_STRING_CONVERTER);
		rowIndex++;

		gridPane.add(new Label("Y Start:"), 0, rowIndex);
		TextField yStartTextField = new TextField();
		gridPane.add(yStartTextField, 1, rowIndex);
		Bindings.bindBidirectional(yStartTextField.textProperty(), yStartProperty, BIGDECIMAL_STRING_CONVERTER);
		rowIndex++;

		gridPane.add(new Label("Y End:"), 0, rowIndex);
		TextField yEndTextField = new TextField();
		gridPane.add(yEndTextField, 1, rowIndex);
		Bindings.bindBidirectional(yEndTextField.textProperty(), yEndProperty, BIGDECIMAL_STRING_CONVERTER);
		rowIndex++;

		gridPane.add(new Label("Precision:"), 0, rowIndex);
		TextField precisionTextField = new TextField();
		gridPane.add(precisionTextField, 1, rowIndex);
		Bindings.bindBidirectional(precisionTextField.textProperty(), precisionProperty, INTEGER_FORMAT);
		rowIndex++;

		return gridPane;
	}
	
	private Canvas createGraphCanvas() {
		Canvas canvas = new Canvas(GRAPH_WIDTH, GRAPH_HEIGHT);

		return canvas;
	}
	
	private void drawGraph(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		BigDecimal bigDecimalWidth = BigDecimal.valueOf(width);
		BigDecimal bigDecimalHeight = BigDecimal.valueOf(height);
		
		BigDecimal xStart = xStartProperty.get();
		BigDecimal xEnd = xEndProperty.get();
		BigDecimal yStart = yStartProperty.get();
		BigDecimal yEnd = yEndProperty.get();

		MathContext graphMathContext = new MathContext(precisionProperty.get() + 10);
		
		BigDecimalFunction1 toX = new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return toPixel(value, xStart, xEnd, bigDecimalWidth, mathContext);
			}
		};
		BigDecimalFunction1 toY = new BigDecimalFunction1() {
			@Override
			public BigDecimal apply(BigDecimal value, MathContext mathContext) {
				return bigDecimalHeight.subtract(toPixel(value, yStart, yEnd, bigDecimalHeight, mathContext), mathContext);
			}
		};
		
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		
		gc.setStroke(Color.BLACK);
		gc.strokeLine(0, toY.apply(BigDecimal.ZERO, graphMathContext).doubleValue(), width, toY.apply(BigDecimal.ZERO, graphMathContext).doubleValue());
		gc.strokeLine(toX.apply(BigDecimal.ZERO, graphMathContext).doubleValue(), 0, toX.apply(BigDecimal.ZERO, graphMathContext).doubleValue(), height);

		MathContext mathContext = new MathContext(precisionProperty.get());
		for (FunctionInfo functionInfo : functionInfos) {
			gc.setStroke(functionInfo.color);
			
			BigDecimal xStep = xEnd.subtract(xStart, graphMathContext).divide(bigDecimalWidth, graphMathContext);
			BigDecimal lastPixelX = BigDecimal.ZERO;
			BigDecimal lastPixelY = null;
			for(BigDecimal x = xStart ; x.compareTo(xEnd) < 0 ; x = x.add(xStep, graphMathContext)) {
				BigDecimal pixelX = null;
				BigDecimal pixelY = null;
				try {
					BigDecimal y = functionInfo.function.apply(x, mathContext);
					pixelX = toX.apply(x, graphMathContext);
					pixelY = toY.apply(y, graphMathContext);
				} catch (ArithmeticException ex) {
					// TODO better handling
				}

				if (pixelX != null && pixelY != null && lastPixelX != null && lastPixelY != null) {
					gc.strokeLine(lastPixelX.doubleValue(), lastPixelY.doubleValue(), pixelX.doubleValue(), pixelY.doubleValue());
				}
				
				lastPixelX = pixelX;
				lastPixelY = pixelY;
			}
		}
	}
	
	private static BigDecimal toPixel(BigDecimal value, BigDecimal start, BigDecimal end, BigDecimal pixels, MathContext mathContext) {
		return value.subtract(start, mathContext).divide(end.subtract(start, mathContext), mathContext).multiply(pixels, mathContext);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
