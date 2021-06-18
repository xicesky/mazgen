package de.markusdangl.mazegen.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A single cell of the Maze.
 *
 * Each cell contains information about its own coordinates and neighbours
 * (akin to a linked list), and which of the four walls exist.
 */
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

    /**
     * Checks whether a wall in a certain direction exists.
     *
     * @param direction The direction of the wall to check.
     * @return true if the wall exists, false otherwise.
     */
    public boolean hasWall(Direction direction) {
        Boolean wall = walls.get(direction);
        return wall == Boolean.TRUE;
    }

    /**
     * Breaks the wall in a certain direction.
     *
     * @param direction The direction of the wall to break.
     */
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

    /**
     * Streams all directions which have neighbouring cells.
     * @return a Stream of directions to neighbouring cells.
     */
    public Stream<Direction> streamNeighbourDirections() {
        return Direction.streamAll()
            .filter(direction -> neighbours.get(direction) != null);
    }

    /**
     * Aligns two cells by setting the appropriate neighbour properties.
     *
     * @param dimension The dimension in which to align the cells (HORIZONTAL, VERTICAL).
     * @param lower The cell with the lower index (i.e. left or top cell).
     * @param higher The cell with the higher index (i.e. right or bottom cell).
     * @return The cell passed as the higher parameter.
     */
    public static Cell align(
        @NotNull Dimension dimension,
        @NotNull Cell lower,
        @NotNull Cell higher
    ) {
        switch (dimension) {
            case HORIZONTAL -> {
                lower.neighbours.put(Direction.RIGHT, higher);
                higher.neighbours.put(Direction.LEFT, lower);
            }
            case VERTICAL -> {
                lower.neighbours.put(Direction.DOWN, higher);
                higher.neighbours.put(Direction.UP, lower);
            }
        }
        return higher;
    }
}
