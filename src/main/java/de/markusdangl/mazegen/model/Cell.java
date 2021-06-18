package de.markusdangl.mazegen.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cell {

    private final int x, y;
    private final Map<Direction, Cell> neighbours;
    private final Map<Direction, Boolean> walls;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbours = new HashMap<>();

        // Walls all exist by default
        this.walls = Direction.streamAll()
            .collect(Collectors.toMap(Function.identity(), d -> true));
    }

    @Override
    public String toString() {
        return "Cell{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<Direction, Cell> getNeighbours() {
        return neighbours;
    }

    public Map<Direction, Boolean> getWalls() {
        return walls;
    }

    public boolean hasWall(Direction direction) {
        Boolean wall = walls.get(direction);
        return (wall != null && wall == Boolean.TRUE);
    }

    public void breakWall(Direction direction) {
        walls.put(direction, false);

        // Break the wall from the other direction, too
        Cell neighbour = neighbours.get(direction);
        if (neighbour != null) {
            // FIXME: This assertion is an invariant of maze and should be unit-tested.
            assert neighbour.neighbours.get(direction.opposite()) == this;
            neighbour.walls.put(direction.opposite(), false);
        }
    }

    public Cell getNeighbour(Direction direction) {
        return neighbours.get(direction);
    }

    public Stream<Direction> streamNeighbourDirections() {
        return Direction.streamAll()
            .filter(direction -> neighbours.get(direction) != null);
    }

    // FIXME doc
    public static Cell align(
        @NotNull Dimension dimension,
        @NotNull Cell lower,
        @NotNull Cell higher
    ) {
        switch (dimension) {
            case HORIZONTAL:
                lower.neighbours.put(Direction.RIGHT, higher);
                higher.neighbours.put(Direction.LEFT, lower);
                break;
            case VERTICAL:
                lower.neighbours.put(Direction.DOWN, higher);
                higher.neighbours.put(Direction.UP, lower);
                break;
        }
        return higher;
    }
}
