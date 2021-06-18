package de.markusdangl.mazegen.generator;

import de.markusdangl.mazegen.model.Cell;
import de.markusdangl.mazegen.model.Direction;
import de.markusdangl.mazegen.model.Maze;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Generator {

    // The maze being currently generated
    private final Maze maze;

    // The current DFS stack, i.e. "dug path" of cells.
    private final Deque<Cell> stack = new ArrayDeque<>();

    // The set of visited cells
    private final Set<Cell> visited = new HashSet<>();

    public Generator(int width, int height) {
        // FIXME throw proper exception
        assert width > 0;
        assert height > 0;

        this.maze = new Maze(width, height);

        // Always start at the upper left corner
        goToCell(maze.get(0,0));
    }

    protected void goToCell(Cell cell) {
        stack.push(cell);
        visited.add(cell);
    }

    public Maze getMaze() {
        return maze;
    }

    public Cell getCurrentCell() {
        return stack.peek();
    }

    public static Maze generateMaze(int width, int height) {
        Generator generator = new Generator(width, height);
        while (generator.step()) {
            // Do nothing
        }
        return generator.getMaze();
    }

    /**
     * Perform a single step of the maze DFS algorithm.
     *
     * @return true if further steps may be required, false if finished.
     */
    public boolean step() {
        if (stack.isEmpty()) {
            return false;
        } else {
            // Drop all cells from the stack that have no unvisited neighbours
            Cell currentCell = stack.peek();
            List<Direction> unvisitedDirections = null;
            while (currentCell != null) {
                unvisitedDirections = unvisitedNeighbours(currentCell);
                if (unvisitedDirections.size() == 0) {
                    stack.pop();
                    currentCell = stack.peek();
                    continue;
                } else {
                    // Current cell has unvisited neighbours
                    break;
                }
            }

            if (currentCell == null) {
                // We didn't find a cell with unvisited neighbours, done
                return false;
            } else {

                // Select a random unvisited neighbour
                Direction direction = selectRandomFrom(unvisitedDirections);

                // Break the wall and continue with the new cell
                Cell newCell = currentCell.getNeighbour(direction);
                currentCell.breakWall(direction);
                goToCell(newCell);
            }
        }
        return true;
    }

    public List<Direction> unvisitedNeighbours(@NotNull Cell cell) {
        return cell.streamNeighbourDirections()
            .filter(direction -> !visited.contains(cell.getNeighbour(direction)))
            .collect(Collectors.toList());
    }

    // FIXME Utility method, should be moved to own class
    public static <T> T selectRandomFrom(@NotNull List<T> list) {
        assert list.size() > 0;
        if (list.size() == 1)
            return list.get(0);

        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }

}
