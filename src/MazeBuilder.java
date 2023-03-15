import java.util.ArrayList;
import java.util.Arrays;

public class MazeBuilder {
    private static int cellsAccessed = 0;

    public static Tile[][] buildMaze(int cellSize, int mazeSize) {
        Cell[][] cellMaze = initializeCells(cellSize, mazeSize);
        cellMaze[mazeSize/2][mazeSize/2].setAccessed();
        mazeAlgorithm(cellMaze, mazeSize, mazeSize/2, mazeSize/2, 0);

        return convertToTileArray(cellMaze, cellSize, mazeSize);
    }

    private static boolean mazeAlgorithm(Cell[][] maze, int mazeSize, int x, int y, int depth) {
        maze[y][x].populate(depth);
        if (cellsAccessed > mazeSize * mazeSize) return true;

        ArrayList<Vector> possibilities = new ArrayList<>();
        boolean checkComplete = false;

        while (!checkComplete) {
            possibilities.clear();
            for (int ang = 0; ang < 4; ang ++) {
                int newX = DirectionUtils.getX(x, ang);
                int newY = DirectionUtils.getY(y, ang);

                if (validCell(maze, newX, newY)) possibilities.add(new Vector(newX, newY, ang));
            }

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

    private static boolean validCell(Cell[][] maze, int x, int y) {
        return (0 <= x && x < maze.length) && (0 <= y && y < maze.length) && !maze[y][x].isAccessed();
    }

    private static Tile[][] convertToTileArray(Cell[][] cellMaze, int cellSize, int mazeSize) {
        Tile[][] tileMaze = new Tile[1 + cellSize * mazeSize][1 + cellSize * mazeSize];
        Arrays.fill(tileMaze[0], Tile.BARRIER);

        for (int y = 0; y < mazeSize; y++) {
            for (int cellRow = 0; cellRow < cellSize; cellRow++) {
                tileMaze[y * cellSize + cellRow][0] = Tile.BARRIER;
                for (int cellNum = 0; cellNum < mazeSize; cellNum++) {
                    for (int tileNum = 0; tileNum < cellSize; tileNum++) {
                        tileMaze[y * cellSize + cellRow + 1][cellNum * cellSize + tileNum + 1] = cellMaze[y][cellNum].getRow(cellRow)[tileNum];
                    }
                }
            }
        }

        tileMaze[tileMaze.length - 1][0] = Tile.BARRIER;

        return tileMaze;
    }

    private record Vector(int inX, int inY, int wall) {
    }
}
