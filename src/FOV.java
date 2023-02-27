import java.util.ArrayList;

public class FOV {
    private final Tile[][] worldMap;
    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final int viewRange;
    private MazeItem[][] FOV;

    public FOV(Tile[][] worldMap, Player player, ArrayList<Enemy> enemies, int viewRange) {
        this.worldMap = worldMap;
        this.player = player;
        this.enemies = enemies;
        this.viewRange = viewRange;
    }

    private int[] getFOVFrame(int viewRange){
        int x = player.x(), y = player.y();
        int r = viewRange/2;

        int xStart = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1: 0);
        int xEnd = Math.min(x + r, worldMap.length - 1) + (x - r < 0 ? r - x : 0);
        int yStart = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1: 0);
        int yEnd = Math.min(y + r, worldMap.length - 1) + (y - r < 0 ? r - y : 0);

        return new int[]{xStart, xEnd, yStart, yEnd};
    }

    public void focus() {
        MazeItem[][] FOV = new MazeItem[viewRange][viewRange];

        for (int yi = frame[2]; yi <= frame[3]; yi++) {
            for (int xi = frame[0]; xi <= frame[1]; xi++) {
                FOV[yi - frame[2]][xi - frame[0]] = worldMap[yi][xi];
            }
        }

        for (Enemy enemy : enemies) {
            if ((frame[0] <= enemy.x() && enemy.x() < frame[1]) && (frame[2] <= enemy.y() && enemy.y() < frame[3])){
                FOV[enemy.y() - frame[2]][enemy.x() - frame[0]] = enemy;
            }
        }

        FOV[player.y() - frame[2]][player.x() - frame[0]] = player;

        this.FOV = FOV;
    }
}
