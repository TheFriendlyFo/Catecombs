import java.awt.*;

public class MazeItem {
    char icon;
    Color color;
    boolean isPassable;

    public boolean isPassable() {
        return isPassable;
    }

    public String getIcon() {
        return String.valueOf(icon);
    }

    public Color getColor() {
        return color;
    }
}
