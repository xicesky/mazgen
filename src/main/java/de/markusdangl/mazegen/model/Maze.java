package de.markusdangl.mazegen.model;

import de.markusdangl.mazegen.model.util.CellRun;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Maze {

    private final int width;
    private final int height;

    private final List<List<Cell>> field;

    public Maze(int width, int height) {
        // FIXME throw proper exception
        assert width > 0;
        assert height > 0;

        this.width = width;
        this.height = height;

        /* Downcasting list elements:
            A classic flaw of the java generics type system
        */
        this.field = new ArrayList<List<Cell>>(generateField(width, height));
    }

    @Override
    public String toString() {
        return "Maze{" +
            "width=" + width +
            ", height=" + height +
            ", cells=" + field +
            '}';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell get(int x, int y) {
        if (x < 0 || x >= width) {
            throw new ArrayIndexOutOfBoundsException("Maze x coordinate out of bounds.");
        } else if (y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Maze y coordinate out of bounds.");
        }
        return field.get(y).get(x);
    }

    public Stream<Cell> streamCells() {
        return field.stream().flatMap(List::stream);
    }

    public Stream<Cell> streamRow(int y) {
        if (y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Maze y coordinate out of bounds.");
        }
        return field.get(y).stream();
    }

    public Stream<Cell> streamColumn(int x) {
        if (x < 0 || x >= width) {
            throw new ArrayIndexOutOfBoundsException("Maze x coordinate out of bounds.");
        }
        return field.stream()
            .map(row -> row.get(x));
    }

    public void printAsciiMazeTo(PrintStream out) {
        // Print upper and left wall of each cell
        field.stream()
            .forEach(row -> {
                row.forEach(cell -> {
                    out.printf("+%2s", cell.hasWall(Direction.UP) ? "--" : "  ");
                });
                out.println("+");
                row.forEach(cell -> {
                    out.printf("%s  ", cell.hasWall(Direction.LEFT) ? "|" : " ");
                });
                // Right wall of the last cell in the row
                out.printf("%s\n", row.get(row.size() - 1).hasWall(Direction.RIGHT) ? "|" : " ");
            });

        // Print lower walls of last row
        field.get(height-1)
            .forEach(cell -> {
                out.printf("+%2s", cell.hasWall(Direction.DOWN) ? "--" : "  ");
            });
        out.println("+");

    }

    private static ArrayList<ArrayList<Cell>> generateField(int width, int height) {
        ArrayList<ArrayList<Cell>> field = IntStream.range(0, height)
            .mapToObj(y -> generateRow(y, width))
            .collect(Collectors.toCollection(ArrayList::new));

        // Connect cells top-to-bottom
        /*
            Note: ideally we would use grouping here, but we could potentially use
            the order of the cells.
            Instead, we stream the whole field multiple times, once for each column index.
         */
        IntStream.range(0, width)
            .forEach(x -> {
                // Connect the cells in column x vertically
                field.stream()
                    .map(row -> new CellRun(row.get(x)))
                    .reduce(
                        new CellRun(),
                        (low, high) -> CellRun.align(Dimension.VERTICAL, low, high)
                    );
            });

        return field;
    }

    private static ArrayList<Cell> generateRow(int y, int width) {
        ArrayList<Cell> row = IntStream.range(0, width)
            .mapToObj(x -> new Cell(x, y))
            .collect(Collectors.toCollection(ArrayList::new));

        // Connect cells left-to-right
        row.stream()
            .map(CellRun::new)
            .reduce(
                new CellRun(),
                (low, high) -> CellRun.align(Dimension.HORIZONTAL, low, high)
            );

        return row;
    }

}
