import java.util.ArrayList;

public class Catacombs {
    private final Tile[][] worldMap;
    private final Player player;
    private final ArrayList<Enemy> enemies;

    Catacombs(int cellSize, int numCells) {
        worldMap = MazeBuilder.buildMaze(cellSize, numCells);
        player = new Player(4, 4);
        enemies = new ArrayList<>();
    }

    public int[] getFOVFrame(int viewRange){
        int x = player.x(), y = player.y();
        int r = viewRange/2;

        int xStart = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1: 0);
        int xEnd = Math.min(x + r, worldMap.length - 1) + (x - r < 0 ? r - x : 0);
        int yStart = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1: 0);
        int yEnd = Math.min(y + r, worldMap.length - 1) + (y - r < 0 ? r - y : 0);

        return new int[]{xStart, xEnd, yStart, yEnd};
    }

    public MazeItem[][] getFOV(int viewRange, int[] frame) {
        MazeItem[][] FOV = new MazeItem[viewRange][viewRange];

        for (int yi = frame[2]; yi <= frame[3]; yi++) {
            for (int xi = frame[0]; xi <= frame[1]; xi++) {
                FOV[yi - frame[2]][xi - frame[0]] = (xi == player.x() && yi == player.y()) ? player : worldMap[yi][xi];
            }
        }

        for (Enemy enemy : enemies) {
            if ((frame[0] <= enemy.x() && enemy.x() < frame[1]) && (frame[2] <= enemy.y() && enemy.y() < frame[3])){
                FOV[enemy.y() - frame[2]][enemy.x() - frame[0]] = enemy;
            }
        }
        return FOV;
    }

    public void printFOV(MazeItem[][] map) {
        StringBuilder printer = new StringBuilder();
        String outerBarrier = (char) 27 + "[36mX" + (char) 27 + "[39m ";

        printer.append(outerBarrier.repeat(map.length + 2)).append("\n");
        for (MazeItem[] row : map) {
            printer.append(outerBarrier);
            for (MazeItem column : row) {
                printer.append(column).append(" ");
            }
            printer.append(outerBarrier).append("\n");
        }
        printer.append(outerBarrier.repeat(map.length + 2));
        System.out.println(printer);
    }

    public void update() {
        int[] frame = getFOVFrame(9);

        player.move(worldMap);

        for (Enemy enemy : enemies) {
            if ((frame[0] <= enemy.x() && enemy.x() < frame[1]) && (frame[2] <= enemy.y() && enemy.y() < frame[3])){
                enemy.move(worldMap, player);
            }
        }

        MazeItem[][] FOV = getFOV(9, frame);

        for (int y = 0; y < FOV.length; y++) {
            for (int x = 0; x < FOV.length; x++) {
                if (FOV[y][x] == Tile.TALL_GRASS && Math.random() < 0.0025) {
                    enemies.add(new Enemy(x + frame[0], y + frame[2]));
                }
            }

        }

        printFOV(FOV);
    }
}
