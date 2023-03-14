import java.awt.*;

public class MazeItem {
    public char icon;
    public Color color;
    public boolean isPassable;
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
