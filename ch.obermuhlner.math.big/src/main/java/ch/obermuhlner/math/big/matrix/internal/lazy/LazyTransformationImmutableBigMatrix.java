package ch.obermuhlner.math.big.matrix.internal.lazy;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.Coord;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.matrix.Coord.coord;

public class LazyTransformationImmutableBigMatrix extends AbstractBigMatrix implements ImmutableBigMatrix {
    private int rows;
    private int columns;
    private final Function<Coord, Coord> coordFunction;
    private BigMatrix matrix;

    public LazyTransformationImmutableBigMatrix(BigMatrix matrix, int rows, int columns, Function<Coord, Coord> coordFunction) {
        this.matrix = matrix;
        this.rows = rows;
        this.columns = columns;
        this.coordFunction = coordFunction;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public BigDecimal get(int row, int column) {
        Coord transformedCoord = coordFunction.apply(coord(row, column));
        return matrix.get(transformedCoord.row, transformedCoord.column);
    }

    @Override
    public Stream<Coord> getSparseCoords() {
        return matrix.getSparseCoords()
                .map(c -> coordFunction.apply(c));
    }

    @Override
    public int sparseEmptyElementCount() {
        return matrix.sparseEmptyElementCount();
    }

    @Override
    public void internalSet(int row, int column, BigDecimal value) {
        throw new IllegalStateException();
    }
}
