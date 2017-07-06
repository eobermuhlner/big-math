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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import parser.AbstractFunctionParser;
import parser.ExpressionFunctionParser;

public class GraphApp extends Application {
	private static final double GRAPH_WIDTH = 800;
	private static final double GRAPH_HEIGHT = 800;

	private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("##0");

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
	StringProperty function1Property = new SimpleStringProperty("sin(x)");
	StringProperty function2Property = new SimpleStringProperty("");
	StringProperty function3Property = new SimpleStringProperty("");

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
		
		graphCanvas.requestFocus();

		primaryStage.setScene(scene);
		primaryStage.show();
				
		function1Property.addListener((observable, oldValue, newValue) -> {
			updateFunctions();
		});
		function2Property.addListener((observable, oldValue, newValue) -> {
			updateFunctions();
		});
		function3Property.addListener((observable, oldValue, newValue) -> {
			updateFunctions();
		});
		
		ChangeListener<? super Number> graphDrawingListener = (observable, oldValue, newValue) -> drawGraph(graphCanvas);
		xStartProperty.addListener(graphDrawingListener);
		xEndProperty.addListener(graphDrawingListener);
		yStartProperty.addListener(graphDrawingListener);
		yEndProperty.addListener(graphDrawingListener);
		precisionProperty.addListener(graphDrawingListener);
		
		updateFunctions();
	}

	private void updateFunctions() {
		AbstractFunctionParser parser = new ExpressionFunctionParser();

		functionInfos.clear();
		functionInfos.add(new FunctionInfo(function1Property.get(), Color.RED, parser.compile(function1Property.get())));
		functionInfos.add(new FunctionInfo(function2Property.get(), Color.GREEN, parser.compile(function2Property.get())));
		functionInfos.add(new FunctionInfo(function3Property.get(), Color.BLUE, parser.compile(function3Property.get())));

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

		gridPane.add(new Label("Function 1:"), 0, rowIndex);
		TextField function1TextField = new TextField();
		gridPane.add(function1TextField, 1, rowIndex);
		Bindings.bindBidirectional(function1TextField.textProperty(), function1Property);
		rowIndex++;

		gridPane.add(new Label("Function 2:"), 0, rowIndex);
		TextField function2TextField = new TextField();
		gridPane.add(function2TextField, 1, rowIndex);
		Bindings.bindBidirectional(function2TextField.textProperty(), function2Property);
		rowIndex++;

		gridPane.add(new Label("Function 3:"), 0, rowIndex);
		TextField function3TextField = new TextField();
		gridPane.add(function3TextField, 1, rowIndex);
		Bindings.bindBidirectional(function3TextField.textProperty(), function3Property);
		rowIndex++;

		return gridPane;
	}
	
	private Canvas createGraphCanvas() {
		Canvas canvas = new Canvas(GRAPH_WIDTH, GRAPH_HEIGHT);

		canvas.setFocusTraversable(true);
		setupCanvasEventHandlers(canvas);
		
		return canvas;
	}
	
	private void setupCanvasEventHandlers(Canvas canvas) {
		canvas.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case UP: {
				zoom(BigDecimal.valueOf(1 / 1.2));
				break;
			}
			case DOWN: {
				zoom(BigDecimal.valueOf(1.2));
				break;
			}
			case W:
				translate(0, 1);
				break;
			case A:
				translate(-1, 0);
				break;
			case S:
				translate(0, -1);
				break;
			case D:
				translate(1, 0);
				break;
			default:
			}
			event.consume();
		});
	}
	
	private void translate(int deltaX, int deltaY) {
		MathContext mathContext = createGraphMathContext();

		BigDecimal stepFactor = BigDecimal.TEN;

		BigDecimal xRange = xEndProperty.get().subtract(xStartProperty.get());
		BigDecimal xStep = xRange.divide(stepFactor, mathContext);

		BigDecimal yRange = yEndProperty.get().subtract(yStartProperty.get());
		BigDecimal yStep = yRange.divide(stepFactor, mathContext);

		xStartProperty.set(xStartProperty.get().add(xStep.multiply(new BigDecimal(deltaX))));
		xEndProperty.set(xEndProperty.get().add(xStep.multiply(new BigDecimal(deltaX))));

		yStartProperty.set(yStartProperty.get().add(yStep.multiply(new BigDecimal(deltaY))));
		yEndProperty.set(yEndProperty.get().add(yStep.multiply(new BigDecimal(deltaY))));
	}
	
	private void zoom(BigDecimal zoomFactor) {
		MathContext mathContext = createGraphMathContext();

		BigDecimal half = BigDecimal.valueOf(0.5);

		BigDecimal xRange = xEndProperty.get().subtract(xStartProperty.get());
		BigDecimal xCenter =  xStartProperty.get().add(xRange.multiply(half, mathContext));

		BigDecimal yRange = yEndProperty.get().subtract(yStartProperty.get());
		BigDecimal yCenter =  yStartProperty.get().add(yRange.multiply(half, mathContext));
		
		BigDecimal xRangeZoomedHalf = xRange.multiply(zoomFactor, mathContext).multiply(half, mathContext);
		BigDecimal yRangeZoomedHalf = yRange.multiply(zoomFactor, mathContext).multiply(half, mathContext);
		
		xStartProperty.set(xCenter.subtract(xRangeZoomedHalf));
		yStartProperty.set(yCenter.subtract(yRangeZoomedHalf));
		
		xEndProperty.set(xCenter.add(xRangeZoomedHalf));
		yEndProperty.set(yCenter.add(yRangeZoomedHalf));
	}

	private void drawGraph(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		BigDecimal bigDecimalWidth = BigDecimal.valueOf(width);
		BigDecimal bigDecimalHeight = BigDecimal.valueOf(height);
		
		BigDecimal xStart = xStartProperty.get();
		BigDecimal xEnd = xEndProperty.get();
		BigDecimal xRange = xEnd.subtract(xStart);
		BigDecimal yStart = yStartProperty.get();
		BigDecimal yEnd = yEndProperty.get();
		BigDecimal yRange = yEnd.subtract(yStart);

		MathContext graphMathContext = createGraphMathContext();
		
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
		
		// draw background
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		
		// draw axis
		gc.setStroke(Color.BLACK);
		double pixelAxisY = toY.apply(BigDecimal.ZERO, graphMathContext).doubleValue();
		gc.strokeLine(0, pixelAxisY, width, pixelAxisY);
		double pixelAxisX = toX.apply(BigDecimal.ZERO, graphMathContext).doubleValue();
		gc.strokeLine(pixelAxisX, 0, pixelAxisX, height);
		
		// draw grid
		// draw scale
		double tickSize = 5;
		double smallTickSize = 2;
		pixelAxisX = Math.min(Math.max(pixelAxisX, 0), width);
		pixelAxisY = Math.min(Math.max(pixelAxisY, 0), height);
		BigDecimal xScaleStep = scaleStep(xRange, graphMathContext);
		BigDecimal yScaleStep = scaleStep(yRange, graphMathContext);
		BigDecimal xScaleEnd = xEnd.divideToIntegralValue(BigDecimal.TEN, graphMathContext).multiply(BigDecimal.TEN);
		BigDecimal yScaleEnd = yEnd.divideToIntegralValue(BigDecimal.TEN, graphMathContext).multiply(BigDecimal.TEN);
		BigDecimal xScaleStart = xScaleEnd.subtract(xScaleStep.multiply(BigDecimal.TEN));
		BigDecimal yScaleStart = yScaleEnd.subtract(yScaleStep.multiply(BigDecimal.TEN));
		
		for (BigDecimal xScale = xScaleStart; xScale.compareTo(xEnd) <= 0; xScale = xScale.add(xScaleStep)) {
			double pixelScaleX = toX.apply(xScale, graphMathContext).doubleValue();
			gc.strokeLine(pixelScaleX, pixelAxisY-tickSize, pixelScaleX, pixelAxisY+tickSize);
			gc.strokeText(xScale.toString(), pixelScaleX, pixelAxisY-tickSize);
		}
		for (BigDecimal yScale = yScaleStart; yScale.compareTo(yEnd) <= 0; yScale = yScale.add(yScaleStep)) {
			double pixelScaleY = toY.apply(yScale, graphMathContext).doubleValue();
			gc.strokeLine(pixelAxisX-tickSize, pixelScaleY, pixelAxisX+tickSize, pixelScaleY);
			gc.strokeText(yScale.toString(), pixelAxisX+tickSize, pixelScaleY);
		}
		
		xScaleStep = xScaleStep.divide(BigDecimal.TEN, graphMathContext);
		yScaleStep = yScaleStep.divide(BigDecimal.TEN, graphMathContext);
		for (BigDecimal xScale = xScaleStart; xScale.compareTo(xEnd) <= 0; xScale = xScale.add(xScaleStep)) {
			double pixelScaleX = toX.apply(xScale, graphMathContext).doubleValue();
			gc.strokeLine(pixelScaleX, pixelAxisY-smallTickSize, pixelScaleX, pixelAxisY+smallTickSize);
		}
		for (BigDecimal yScale = yScaleStart; yScale.compareTo(yEnd) <= 0; yScale = yScale.add(yScaleStep)) {
			double pixelScaleY = toY.apply(yScale, graphMathContext).doubleValue();
			gc.strokeLine(pixelAxisX-smallTickSize, pixelScaleY, pixelAxisX+smallTickSize, pixelScaleY);
		}
		
		// draw functions
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
				} catch (Exception ex) {
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

	private BigDecimal scaleStep(BigDecimal range, MathContext mathContext) {
		return BigDecimalMath.pow(BigDecimal.TEN, BigDecimalMath.integralPart(BigDecimalMath.log10(range, mathContext)), mathContext);
	}

	private MathContext createGraphMathContext() {
		return new MathContext(precisionProperty.get() + 10);
	}
	
	private static BigDecimal toPixel(BigDecimal value, BigDecimal start, BigDecimal end, BigDecimal pixels, MathContext mathContext) {
		return value.subtract(start, mathContext).divide(end.subtract(start, mathContext), mathContext).multiply(pixels, mathContext);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
