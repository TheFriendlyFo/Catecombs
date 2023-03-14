import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class FOV extends JFrame {

    private final JFrame jFrame;
    private final JTextPane display;
    private final MazeItem[][] fov;
    private final Tile[][] worldMap;
    private final int viewRange;
    private int lowXBound, lowYBound;

    public FOV(JFrame j, Tile[][] worldMap, int viewRange) {
        display = new JTextPane();
        jFrame = j;
        jFrame.add(display);
        setUpGraphics();

        this.worldMap = worldMap;
        this.viewRange = viewRange - 1;
        fov = new MazeItem[viewRange][viewRange];
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
        return (lowXBound <= enemy.x() && enemy.x() <= lowXBound + viewRange)
                && (lowYBound <= enemy.y() && enemy.y() <= lowYBound + viewRange);
    }

    private void centerOn(Player player) {
        int r = (viewRange + 1) / 2;
        lowXBound = player.x() - r;
        lowYBound = player.y() - r;
    }

    public MazeItem getItem(int x, int y) {
        x -= lowXBound;
        y -= lowYBound;
        return fov[y][x];
    }

    public void focus(Player player, ArrayList<Enemy> enemies) {
        centerOn(player);

        for (int row = lowYBound; row <= lowYBound + viewRange; row++) {
            for (int col = lowXBound; col <= lowXBound + viewRange; col++) {
                fov[row - lowYBound][col - lowXBound] = worldMap[Math.floorMod(row, worldMap.length)][Math.floorMod(col, worldMap.length)];
            }
        }

        for (Enemy enemy : enemies) {
            if (withinFrame(enemy)) {
                fov[enemy.y() - lowYBound][enemy.x() - lowXBound] = enemy;
            }
        }

        fov[player.y() - lowYBound][player.x() - lowXBound] = player;
    }

    public void updateEnemies(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (!withinFrame(enemy)) continue;

            fov[enemy.y() - lowYBound][enemy.x() - lowXBound] = worldMap[enemy.y()][enemy.x()];

            if (enemy.move(this)) jFrame.removeKeyListener(jFrame.getKeyListeners()[0]);

            if (withinFrame(enemy)) {
                fov[enemy.y() - lowYBound][enemy.x() - lowXBound] = enemy;
            }
        }
    }

    public void updateDisplay() {
        display.setText("");

        appendToPane(display, "X".repeat(fov.length + 2) + "\n", Color.BLACK);
        for (MazeItem[] row : fov) {
            appendToPane(display, "X", Color.BLACK);
            for (MazeItem column : row) {
                appendToPane(display, column.getIcon(), column.getColor());
            }
            appendToPane(display, "X\n", Color.BLACK);
        }
        appendToPane(display, "X".repeat(fov.length + 2), Color.BLACK);
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet set = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        set = sc.addAttribute(set, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(set, false);
        tp.replaceSelection(msg);
    }

    public void generateEnemies(ArrayList<Enemy> enemies) {
        for (int y = 0; y < fov.length; y++) {
            for (int x = 0; x < fov.length; x++) {
                if (fov[y][x] == Tile.TALL_GRASS && Math.random() < 0.001) {
                    Enemy newEnemy = new Enemy(x + lowXBound, y + lowYBound);
                    enemies.add(newEnemy);
                    fov[y][x] = newEnemy;
                }
            }
        }
    }
}
