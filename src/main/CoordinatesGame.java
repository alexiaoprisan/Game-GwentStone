package main;

public class CoordinatesGame {
    private int x, y;

    public CoordinatesGame() {
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{"
                + "x="
                + x
                + ", y="
                + y
                + '}';
    }
}
