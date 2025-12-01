package sk.uniba.fmph.dcs.terra_futura.red_classes;

public class GridPosition {
    private final int x;
    private final int y;

    public GridPosition(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new IllegalArgumentException(
                    "Invalid coordinates"
            );
        }

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridPosition tmp = (GridPosition) o;
        return x == tmp.x && y == tmp.y;
    }

    @Override
    public int hashCode() {
        return 31 * (x + y);
    }

    @Override
    public String toString() {
        return "Grid position: (" + x + ", " + y + ")";
    }
}
