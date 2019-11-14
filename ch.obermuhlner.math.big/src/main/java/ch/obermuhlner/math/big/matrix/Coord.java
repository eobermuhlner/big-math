package ch.obermuhlner.math.big.matrix;

import java.util.Objects;

public final class Coord {
    public final int row;
    public final int column;

    private Coord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return row == coord.row &&
                column == coord.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ')';
    }

    public static Coord coord(int row, int column) {
        return new Coord(row, column);
    }
}
