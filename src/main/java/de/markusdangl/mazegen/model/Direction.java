package de.markusdangl.mazegen.model;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * The four possible directions in the 2D maze.
 */
public enum Direction {

    UP(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    DOWN(0, 1),

    ;

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * @return The x-coordinate delta corresponding to this direction.
     */
    public int getDx() {
        return dx;
    }

    /**
     * @return The y-coordinate delta corresponding to this direction.
     */
    public int getDy() {
        return dy;
    }

    /**
     * @return The opposite (inverse) direction.
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case DOWN -> UP;
        };
    }

    /**
     * @return A stream of all four possible directions.
     */
    public static Stream<Direction> streamAll() {
        return Arrays.stream(Direction.values());
    }
}
