package de.markusdangl.mazegen.model.util;

import de.markusdangl.mazegen.model.Cell;
import de.markusdangl.mazegen.model.Dimension;
import org.jetbrains.annotations.NotNull;

/**
 * A consecutive (horizontally or vertically connected) run of cells.
 *
 * If empty, both least and greatest are null. If non-empty,
 * both least and greatest are non-null.
 */
public class CellRun {

    private final Cell least;
    private final Cell greatest;

    /**
     * Create an empty run of cells.
     */
    public CellRun() {
        this.least = null;
        this.greatest = null;
        checkInvariant();
    }

    /**
     * Create a run from a single cell.
     * @param single
     */
    public CellRun(Cell single) {
        this.least = single;
        this.greatest = single;
        checkInvariant();
    }

    /**
     * Create a run of cells with a set least and upper bound.
     * @param least
     * @param greatest
     */
    protected CellRun(
        @NotNull Cell least,
        @NotNull Cell greatest
    ) {
        this.least = least;
        this.greatest = greatest;
        checkInvariant();
    }

    public boolean checkInvariant() {
        return (least == null) == (greatest == null);
    }

    public boolean isEmpty() {
        return least == null;
    }

    /**
     * Aligns two CellRuns by setting the neighbour properties of the cells.
     *
     * @param dimension whether to align horizontally or vertically.
     * @param lower the lower (left / upper) of both runs
     * @param higher the higher (right / lower) of both runs
     * @return
     */
    public static CellRun align(
        @NotNull Dimension dimension,
        @NotNull CellRun lower,
        @NotNull CellRun higher
    ) {
        if (lower.isEmpty()) {
            return higher;
        } else if (higher.isEmpty()) {
            return lower;
        } else {
            Cell.align(dimension, lower.greatest, higher.least);
            return new CellRun(lower.least, higher.greatest);
        }
    }

    public Cell getLeast() {
        return least;
    }

    public Cell getGreatest() {
        return greatest;
    }
}
