package de.markusdangl.mazegen.generator;

import de.markusdangl.mazegen.model.Cell;
import de.markusdangl.mazegen.model.Direction;
import de.markusdangl.mazegen.model.Maze;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static de.markusdangl.mazegen.util.RandomUtils.selectRandomFrom;

public class Generator {

    // Current stage of the generator
    private GeneratorStage stage = GeneratorStage.ENTRY;

    // The maze being currently generated
    private final Maze maze;

    // The current DFS stack, i.e. "dug path" of cells.
    private final Deque<Cell> stack = new ArrayDeque<>();

    // The set of visited cells
    private final Set<Cell> visited = new HashSet<>();

    public Generator(int width, int height) {
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

    public boolean step() {
        return switch (stage) {
            case ENTRY -> stepGenerateEntry();
            case MAZE -> stepGenerateMaze();
            case EXIT -> stepGenerateExit();
            case DONE -> false;
        };
    }

    public boolean stepGenerateEntry() {
        // Break the wall on the top left as an entry
        maze.get(0, 0).breakWall(Direction.LEFT);
        stage = GeneratorStage.MAZE;
        return true;
    }

    /**
     * Perform a single step of the maze DFS algorithm.
     *
     * @return true if further steps may be required, false if finished.
     */
    public boolean stepGenerateMaze() {
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
                } else {
                    // Current cell has unvisited neighbours
                    break;
                }
            }

            if (currentCell == null) {
                // We didn't find a cell with unvisited neighbours, done
                stage = GeneratorStage.EXIT;
                return true;
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

    public boolean stepGenerateExit() {
        // Break the wall on the bottom right as an exit
        maze.get(maze.getWidth() - 1, maze.getHeight() - 1)
            .breakWall(Direction.RIGHT);
        stage = GeneratorStage.DONE;
        return false;
    }

    public List<Direction> unvisitedNeighbours(@NotNull Cell cell) {
        return cell.streamNeighbourDirections()
            .filter(direction -> !visited.contains(cell.getNeighbour(direction)))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static Maze generateMaze(int width, int height) {
        Generator generator = new Generator(width, height);

        do {
            // Do nothing
        } while (generator.step());

        return generator.getMaze();
    }

}
