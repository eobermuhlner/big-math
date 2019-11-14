package ch.obermuhlner.math.big.matrix;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;

public final class CoordValue {
    public final Coord coord;
    public final BigDecimal value;

    private CoordValue(Coord coord, BigDecimal value) {
        this.coord = coord;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordValue that = (CoordValue) o;
        return Objects.equals(coord, that.coord) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, value);
    }

    @Override
    public String toString() {
        return coord + ": " + value;
    }

    public static CoordValue coordValue(Coord coord, double value) {
        return coordValue(coord, valueOf(value));
    }

    public static CoordValue coordValue(int row, int column, double value) {
        return coordValue(row, column, valueOf(value));
    }

    public static CoordValue coordValue(Coord coord, BigDecimal value) {
        return new CoordValue(coord, value);
    }

    public static CoordValue coordValue(int row, int column, BigDecimal value) {
        return new CoordValue(Coord.coord(row, column), value);
    }
}
