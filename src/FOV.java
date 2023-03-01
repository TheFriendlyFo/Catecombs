import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
public class FOV extends JFrame {

    private final JTextArea display;
    private final JFrame j;
    private final Tile[][] worldMap;
    private final int viewRange;
    private final int[] frame;
    private final MazeItem[][] fov;

    public FOV(JFrame j, Tile[][] worldMap, int viewRange) {
        this.j = j;
        display = new JTextArea();
        setUpGraphics();

        this.worldMap = worldMap;
        this.viewRange = viewRange;
        frame = new int[4];
        fov = new MazeItem[viewRange][viewRange];
    }

    private void setUpGraphics() {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("JetBrainsMonoNL-ExtraBold.ttf").getPath())).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0.5);

        display.setFont(font.deriveFont(attributes));
        //display.setFocusable(false);
        display.setEnabled(false);

        j.add(display);
    }

    private boolean withinFrame(Enemy enemy) {
        return (frame[0] <= enemy.x() && enemy.x() <= frame[1]) && (frame[2] <= enemy.y() && enemy.y() <= frame[3]);
    }

    private void focusFrame(Player player) {
        int x = player.x(), y = player.y();
        int r = viewRange / 2;

        frame[0] = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1 : 0);
        frame[1] = frame[0] + viewRange - 1;
        frame[2] = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1 : 0);
        frame[3] = frame[2] + viewRange - 1;
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
        StringBuilder printer = new StringBuilder();

        printer.append("X".repeat(fov.length + 2)).append("\n");
        for (MazeItem[] row : fov) {
            printer.append("X");
            for (MazeItem column : row) {
                printer.append(column);
            }
            printer.append("X").append("\n");
        }
        printer.append("X".repeat(fov.length + 2));

        display.setText(printer.toString());
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
