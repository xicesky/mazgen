package de.markusdangl.mazegen;

import de.markusdangl.mazegen.generator.Generator;
import de.markusdangl.mazegen.model.Cell;
import de.markusdangl.mazegen.model.Direction;
import de.markusdangl.mazegen.model.Maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
//        Maze maze = new Maze(5, 5);
//        System.out.println(maze);
//        System.out.println();

//        maze.get(0, 0).breakWall(Direction.UP);
//        maze.get(0, 0).breakWall(Direction.DOWN);

//        Maze maze = Generator.generateMaze(3,3);

//        printCellProperties(maze.get(0,0));
//        printCellProperties(maze.get(1,0));
//        printCellProperties(maze.get(0,1));
//        printCellProperties(maze.get(1,2));
//        printCellProperties(maze.get(4,4));

//        maze.printAsciiMazeTo(System.out);

        try {
            mazeDemo(5, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mazeDemo(int width, int height) throws IOException {
        Generator generator = new Generator(width, height);

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

        // Print initial maze
        generator.getMaze().printAsciiMazeTo(System.out);
        reader.readLine();

        // Execute step-by-step and show maze
        while (generator.step()) {

            System.out.println();
            System.out.println();
            generator.getMaze().printAsciiMazeTo(System.out);
            // reader.readLine();

            // Wait 500 ms
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
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
