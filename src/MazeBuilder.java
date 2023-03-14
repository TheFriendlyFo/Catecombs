import java.util.ArrayList;
import java.util.Arrays;

public class MazeBuilder {
    private static int cellsAccessed = 0;

    public static Tile[][] buildMaze(int cellSize, int mazeSize) {
        Cell[][] cellMaze = initializeCells(cellSize, mazeSize);
        cellMaze[0][0].setAccessed();
        mazeAlgorithm(cellMaze, mazeSize, 0, 0, 0);

        return convertToTileArray(cellMaze, cellSize, mazeSize);
    }

    private static boolean mazeAlgorithm(Cell[][] maze, int mazeSize, int x, int y, int depth) {
        System.out.printf("(%s, %s):\n", x, y);
        maze[y][x].populate(depth);
        if (cellsAccessed > mazeSize * mazeSize) return true;

        ArrayList<Vector> possibilities = new ArrayList<>();
        boolean checkComplete = false;

        while (!checkComplete) {
            possibilities.clear();
            for (int ang = 0; ang < 4; ang ++) {
                int newX = Math.floorMod(DirectionUtils.getX(x, ang), mazeSize);
                int newY = Math.floorMod(DirectionUtils.getY(y, ang), mazeSize);
                System.out.printf("(%s, %s)\n", newX, newY);
                System.out.println(maze[newX][newY].isAccessed());


                if (!maze[newY][newX].isAccessed()) possibilities.add(new Vector(newX, newY, ang));
            }
            System.out.println();

            if (possibilities.size() > 0) {
                Vector vector = possibilities.get((int) (Math.random() * possibilities.size()));

                int wall = vector.wall;
                if (wall == 1 || wall == 0) {
                    maze[y][x].lowerWall(wall);
                } else {
                    maze[vector.inY][vector.inX].lowerWall(wall - 2);
                }

                maze[vector.inY][vector.inX].setAccessed();

                cellsAccessed++;
                checkComplete = mazeAlgorithm(maze, mazeSize, vector.inX, vector.inY, depth + 1);
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

    private static Tile[][] convertToTileArray(Cell[][] cellMaze, int cellSize, int mazeSize) {
        Tile[][] tileMaze = new Tile[cellSize * mazeSize][cellSize * mazeSize];

        for (int y = 0; y < mazeSize; y++) {
            for (int cellRow = 0; cellRow < cellSize; cellRow++) {
                for (int cellNum = 0; cellNum < mazeSize; cellNum++) {
                    for (int tileNum = 0; tileNum < cellSize; tileNum++) {
                        tileMaze[y * cellSize + cellRow][cellNum * cellSize + tileNum] = cellMaze[y][cellNum].getRow(cellRow)[tileNum];
                    }
                }
            }
        }

        return tileMaze;
    }

    private record Vector(int inX, int inY, int wall) {
    }
}
