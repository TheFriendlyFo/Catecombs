import java.awt.*;

public class Tile extends MazeItem {
    public static final Tile BLANK = new Tile(' ', true, Color.WHITE);
    public static final Tile BARRIER = new Tile('X', false, new Color(80, 80, 80));
    public static final Tile TALL_GRASS = new Tile('#', true, new Color(44, 156, 69));

    Tile(char icon, boolean isPassable, Color color) {
        this.icon = icon;
        this.isPassable = isPassable;
        this.color = color;
    }
}
