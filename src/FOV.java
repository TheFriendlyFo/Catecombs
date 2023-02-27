import java.util.ArrayList;

public class FOV {
    private final Tile[][] worldMap;
    private final int viewRange;
    private final int[] frame;
    private final MazeItem[][] fov;

    public FOV(Tile[][] worldMap, int viewRange) {
        this.worldMap = worldMap;
        this.viewRange = viewRange;
        frame = new int[4];
        fov = new MazeItem[viewRange][viewRange];
    }

    private boolean withinFrame(Enemy enemy) {
        return (frame[0] <= enemy.x() && enemy.x() <= frame[1]) && (frame[2] <= enemy.y() && enemy.y() <= frame[3]);
    }

    private void focusFrame(Player player) {
        int x = player.x(), y = player.y();
        int r = viewRange / 2;

        frame[0] = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1 : 0);
        frame[1] = Math.min(x + r, worldMap.length - 1) + (x - r < 0 ? r - x : 0);
        frame[2] = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1 : 0);
        frame[3] = Math.min(y + r, worldMap.length - 1) + (y - r < 0 ? r - y : 0);
    }

    public void focus(Player player, ArrayList<Enemy> enemies) {
        focusFrame(player);

        for (int yi = frame[2]; yi <= frame[3]; yi++) {
            if (frame[1] + 1 - frame[0] >= 0)
                System.arraycopy(worldMap[yi], frame[0], fov[yi - frame[2]], 0, frame[1] + 1 - frame[0]);
        }

        for (Enemy enemy : enemies) {
            if (withinFrame(enemy)) {
                fov[adjustedY(enemy)][adjustedX(enemy)] = enemy;
            }
        }

        fov[player.y() - frame[2]][player.x() - frame[0]] = player;
    }

    public void updateEnemies(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (!withinFrame(enemy)) continue;

            fov[adjustedY(enemy)][adjustedX(enemy)] = worldMap[enemy.y()][enemy.x()];
            enemy.move(this);
            if (withinFrame(enemy)) {
                fov[adjustedY(enemy)][adjustedX(enemy)] = enemy;
            }
        }
    }

    public void display() {
        StringBuilder printer = new StringBuilder("\n".repeat(20));
        String outerBarrier = (char) 27 + "[36mX" + (char) 27 + "[39m ";

        printer.append(outerBarrier.repeat(fov.length + 2)).append("\n");
        for (MazeItem[] row : fov) {
            printer.append(outerBarrier);
            for (MazeItem column : row) {
                printer.append(column).append(" ");
            }
            printer.append(outerBarrier).append("\n");
        }
        printer.append(outerBarrier.repeat(fov.length + 2));
        System.out.println(printer);
    }

    public void generateEnemies(ArrayList<Enemy> enemies) {
        for (int y = 0; y < fov.length; y++) {
            for (int x = 0; x < fov.length; x++) {
                if (fov[y][x] == Tile.TALL_GRASS && Math.random() < 0.0025) {
                    Enemy newEnemy = new Enemy(x + frame[0], y + frame[2]);
                    enemies.add(newEnemy);
                    fov[y][x] = newEnemy;
                }
            }
        }
    }

    public boolean isPassable(int x, int y) {
        x -= frame[0];
        y -= frame[2];
        boolean inBoundsX = (0 <= x && x < fov.length);
        boolean inBoundsY = (0 <= y && y < fov.length);
        return inBoundsX && inBoundsY && fov[y][x].isPassable();
    }

    private int adjustedX(Enemy enemy) {
        return enemy.x() - frame[0];
    }

    private int adjustedY(Enemy enemy) {
        return enemy.y() - frame[2];
    }
}
