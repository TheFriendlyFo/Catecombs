import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;
import javax.swing.text.*;

public class FOV {
    private final JTextPane display;
    private final SimpleAttributeSet set;
    private final Document doc;
    private final Tile[][] worldMap;
    private final int[] frame;
    private final int viewRange;
    private final MazeItem[][] fov;
    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Enemy> deleteQueue;

    public FOV(Player player, ArrayList<Enemy> enemies, JFrame j, Tile[][] worldMap, int viewRange) {
        display = new JTextPane();
        doc = display.getStyledDocument();
        set = new SimpleAttributeSet();
        j.add(display);
        setUpGraphics();

        this.player = player;
        this.enemies = enemies;
        this.worldMap = worldMap;
        this.viewRange = viewRange;
        frame = new int[4];
        fov = new MazeItem[viewRange][viewRange];
        deleteQueue = new ArrayList<>();
    }

    private void setUpGraphics() {
        Font font;
        String fontName = "JetBrainsMonoNL-Light.ttf";
        File fontFile = new File(Objects.requireNonNull(getClass().getResource(fontName)).getPath());

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0.5);
        display.setFont(font.deriveFont(attributes));

        display.setFocusable(false);
    }

    public boolean withinFrame(Enemy enemy) {
        return (frame[0] <= enemy.x() && enemy.x() <= frame[1]) && (frame[2] <= enemy.y() && enemy.y() <= frame[3]);
    }

    private void focusFrame() {
        int x = player.x(), y = player.y();
        int r = viewRange / 2;

        frame[0] = Math.max(x - r, 0) - (x + r >= worldMap.length ? r + x - worldMap.length + 1 : 0);
        frame[1] = frame[0] + viewRange - 1;
        frame[2] = Math.max(y - r, 0) - (y + r >= worldMap.length ? r + y - worldMap.length + 1 : 0);
        frame[3] = frame[2] + viewRange - 1;
    }

    public void focus() {
        focusFrame();

        for (int yi = frame[2]; yi <= frame[3]; yi++) {
            if (frame[1] + 1 - frame[0] >= 0)
                System.arraycopy(worldMap[yi], frame[0], fov[yi - frame[2]], 0, frame[1] + 1 - frame[0]);
        }

        for (Enemy enemy : enemies) {
            if (withinFrame(enemy)) {
                fov[enemy.y() - frame[2]][enemy.x() - frame[0]] = enemy;
            }
        }

        fov[player.y() - frame[2]][player.x() - frame[0]] = player;
    }

    public void display() {
        display.setText("");

        appendToPane("X".repeat(fov.length + 2) + "\n", Color.BLACK);
        for (MazeItem[] row : fov) {
            appendToPane("X", Color.BLACK);
            for (MazeItem column : row) {
                appendToPane(column.getIcon(), column.getColor());
            }
            appendToPane("X\n", Color.BLACK);
        }
        appendToPane("X".repeat(fov.length + 2) + "\n\n", Color.BLACK);
        appendToPane("Player Stats:\n", Color.BLACK);
        appendToPane("Health: ", Color.BLACK);
        appendToPane("* ".repeat(Math.max(0, player.getHealth())), Color.RED);
    }

    public void generateEnemies() {
        for (int y = 0; y < fov.length; y++) {
            for (int x = 0; x < fov.length; x++) {
                if (fov[y][x] == Tile.TALL_GRASS && Math.random() < 0.001 ) {
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

    private void appendToPane(String msg, Color c) {
        StyleConstants.setForeground(set, c);
        display.setCharacterAttributes(set, true);

        try {
            doc.insertString(doc.getLength(), msg, set);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToDeleteQueue(Enemy enemy) {
        deleteQueue.add(enemy);
    }

    public void processDeleteQueue() {
        for (Enemy enemy : deleteQueue) {
            enemies.remove(enemy);
        }
        deleteQueue.clear();
    }
}
