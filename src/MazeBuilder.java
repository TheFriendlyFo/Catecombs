import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MazeBuilder {
    private static final Point[] directions = new Point[]{
            new Point(0, 1),
            new Point(1, 0),
            new Point(0, -1),
            new Point(-1, 0)};
    private static int cellsAccessed = 0;
    private record Vector(int inX, int inY, int wall) {
    }

    public static Tile[][] buildMaze(int cellSize, int mazeSize) {
        Cell[][] cellMaze = initializeCells(cellSize, mazeSize);
        mazeAlgorithm(cellMaze, mazeSize, 0, 0);

        Tile[][] tileMaze = new Tile[1 + cellSize * mazeSize][1 + cellSize * mazeSize];
        Arrays.fill(tileMaze[0], Tile.BARRIER);

        for (int y = 1; y < tileMaze.length;) {
            tileMaze[y][0] = Tile.BARRIER;

            for (int cellRow = 0; cellRow < cellSize; cellRow++, y++) {
                for (int cellNum = 0; cellNum < tileMaze[y].length; cellNum++) {
                    for (int tileNum = 0; tileNum < cellSize; tileNum++)  {
                        tileMaze[y][1 + cellNum * cellSize + tileNum] = cellMaze[(y - 1)/cellSize][cellNum].getRow(cellRow)[tileNum];
                    }
                }

            }
        }

        return tileMaze;
    }

    public static boolean mazeAlgorithm(Cell[][] maze, int mazeSize, int x, int y) {
        if (cellsAccessed > mazeSize * mazeSize) return true;

        ArrayList<Vector> possibilities = new ArrayList<>();
        boolean checkComplete = false;

        while (!checkComplete) {
            possibilities.clear();
            for (int i = 0; i < directions.length; i++) {
                int newX = x + directions[i].x();
                int newY = y + directions[i].y();

                if (validCell(maze, newX, newY)) possibilities.add(new Vector(newX, newY, i));
            }

            if (possibilities.size() > 0) {
                Vector vector = possibilities.get((int) (Math.random() * possibilities.size()));

                int wall = vector.wall;
                if (wall == 1 || wall == 0) {
                    maze[y][x].lowerWall(wall);
                } else {
                    maze[vector.inY][vector.inX].lowerWall(wall - 2);
                }
                cellsAccessed++;

                checkComplete = mazeAlgorithm(maze, mazeSize, vector.inX, vector.inY);
            } else {
                return false;
            }
        }
        return false;
    }

    private static Cell[][] initializeCells(int cellSize, int mazeSize) {
        Cell[][] cells = new Cell[mazeSize][mazeSize];

        for (int y = 0; y < mazeSize; y++) {
            for (int x = 0; x < mazeSize; x++) {
                cells[y][x] = new Cell(cellSize);
            }
        }

        return cells;
    }

    private static boolean validCell(Cell[][] maze, int x, int y) {
        return (0 <= x && x < maze.length) && (0 <= y && y < maze.length) && !maze[y][x].isAccessed();
    }
}
