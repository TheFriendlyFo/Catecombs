import java.util.ArrayList;

public class FOV {
    private final Tile[][] worldMap;
    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final int viewRange;
    private final int[] frame;
    private final MazeItem[][] FOV;

    public FOV(Tile[][] worldMap, Player player, ArrayList<Enemy> enemies, int viewRange) {
        this.worldMap = worldMap;
        this.player = player;
        this.enemies = enemies;
        this.viewRange = viewRange;
        frame = new int[4];
        FOV = new MazeItem[viewRange][viewRange];
    }

    private boolean withinFrame(int x, int y) {
        return (frame[0] <= x && x < frame[1]) && (frame[2] <= y && y < frame[3]);
    }

    private void focusFrame(){
        int x = player.x(), y = player.y();
        int r = viewRange/2;

        frame[0] = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1: 0);
        frame[1] = Math.min(x + r, worldMap.length - 1) + (x - r < 0 ? r - x : 0);
        frame[2] = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1: 0);
        frame[3] = Math.min(y + r, worldMap.length - 1) + (y - r < 0 ? r - y : 0);
    }

    private void focus() {
        focusFrame();

        for (int yi = frame[2]; yi <= frame[3]; yi++) {
            if (frame[1] + 1 - frame[0] >= 0)
                System.arraycopy(worldMap[yi], frame[0], FOV[yi - frame[2]], 0, frame[1] + 1 - frame[0]);
        }

        for (Enemy enemy : enemies) {
            if (withinFrame(enemy.x(), enemy.y())){
                FOV[enemy.y() - frame[2]][enemy.x() - frame[0]] = enemy;
            }
        }

        FOV[player.y() - frame[2]][player.x() - frame[0]] = player;
    }
}
