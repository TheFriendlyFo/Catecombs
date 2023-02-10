import java.util.ArrayList;

public class MazeBuilder {
    private static final Point[] directions = new Point[]{
            new Point(0, 1),
            new Point(1, 0),
            new Point(0, -1),
            new Point(-1, 0)};
    private static int cellsAccessed = 0;
    private record Vector(int inX, int inY, int wall) {
    }

    public static Cell[][] buildMaze(int cellSize, int mazeSize) {
        Cell[][] maze = initializeCells(cellSize, mazeSize);
        mazeAlgorithm(maze, mazeSize, 0, 0);
        return maze;
    }

    public static boolean mazeAlgorithm(Cell[][] maze, int mazeSize, int x, int y) {
        if (cellsAccessed > mazeSize * mazeSize) return true;

        ArrayList<Vector> possibilities = new ArrayList<>();
        boolean checkComplete = false;

        while (!checkComplete) {
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

        return true;
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
