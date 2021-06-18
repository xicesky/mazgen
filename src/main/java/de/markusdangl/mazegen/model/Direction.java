package de.markusdangl.mazegen.model;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Direction {

    UP(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    DOWN(0, 1)
    ;

    private final int dx;
    private final int dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case DOWN -> UP;
        };
    }

    public static Stream<Direction> streamAll() {
        return Arrays.stream(Direction.values());
    }
}
