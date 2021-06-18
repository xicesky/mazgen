package de.markusdangl.mazegen;

import de.markusdangl.mazegen.generator.Generator;
import de.markusdangl.mazegen.model.Cell;
import de.markusdangl.mazegen.model.Direction;
import de.markusdangl.mazegen.model.Maze;

public class Main {

    public static void main(String[] args) {
//        Maze maze = new Maze(5, 5);
//        System.out.println(maze);
//        System.out.println();

//        maze.get(0, 0).breakWall(Direction.UP);
//        maze.get(0, 0).breakWall(Direction.DOWN);

        Maze maze = Generator.generateMaze(3,3);

//        printCellProperties(maze.get(0,0));
//        printCellProperties(maze.get(1,0));
//        printCellProperties(maze.get(0,1));
//        printCellProperties(maze.get(1,2));
//        printCellProperties(maze.get(4,4));

        maze.printAsciiMazeTo(System.out);
    }

    public static void printCellProperties(Cell cell) {
        System.out.println(cell);
        System.out.print("    ");
        System.out.println(cell.getWalls());
        System.out.print("    ");
        System.out.println(cell.getNeighbours());
        System.out.println();
    }
}
