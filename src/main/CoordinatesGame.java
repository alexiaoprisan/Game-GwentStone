package main;

/**
 * Represents the coordinates of the game.
 */
public class CoordinatesGame {
    private int x, y;

    /**
     * Default constructor for CoordinatesGame.
     */
    public CoordinatesGame() {
    }

    /**
     * Constructs a CoordinatesGame with specified x and y coordinates.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the x coordinate to set
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the y coordinate to set
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * Returns the string representation of the coordinates.
     *
     * @return the string representation of the coordinates
     */
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
