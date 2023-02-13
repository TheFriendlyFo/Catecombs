import java.util.ArrayList;

public class Catacombs {

    public static final Tile BLANK = new Tile("39m-", true);
    public static final Tile BARRIER = new Tile("39mX", false);
    public static final TallGrass TALL_GRASS = new TallGrass("32m#", true);
    private final Tile[][] worldMap;
    private final Player player;
    private final ArrayList<Enemy> enemies;

    Catacombs(int cellSize, int numCells) {
        worldMap = MazeBuilder.buildMaze(cellSize, numCells);
        player = new Player(1, 1);
        enemies = new ArrayList<>();
        enemies.add(new Enemy(3,3));
    }

    public MazeItem[][] getFOV(int viewRange) {
        MazeItem[][] FOV = new MazeItem[viewRange][viewRange];

        for (int yComplete = 0, iY = 0; yComplete < viewRange;) {
            int yPos = player.y() + iY;
            if (0 <= yPos && yPos < worldMap.length) {
                for (int xComplete = 0, iX = 0; xComplete < viewRange; ) {
                    int xPos = player.x() + iX;
                    if (0 <= xPos && xPos < worldMap.length) {
                        FOV[yPos][xPos] = worldMap[yPos][xPos];
                        xComplete++;
                    }
                    iX = iX <= 0 ? -iX + 1 : -iX;
                }
                yComplete++;
            }
            iY = iY <= 0 ? -iY + 1 : -iY;
        }

        FOV[player.y()][player.x()] = player;
        for (Enemy enemy : enemies) {
            FOV[enemy.getY()][enemy.getX()] = enemy;
            enemy.update(FOV);
        }



        return FOV;
    }

    public void printFOV(MazeItem[][] map) {
        String outerBarrier = (char) 27 + "[36mX" + (char) 27 + "[39m ";

        System.out.println(outerBarrier.repeat(map.length + 2));
        for (MazeItem[] row : map) {
            System.out.print(outerBarrier);
            for (MazeItem column : row) {
                System.out.print(column + " ");
            }
            System.out.println(outerBarrier);
        }
        System.out.println(outerBarrier.repeat(map.length + 2));
    }
}
