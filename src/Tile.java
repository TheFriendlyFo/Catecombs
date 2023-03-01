public class Tile implements MazeItem {
    public static final Tile BLANK = new Tile(" ", true);
    public static final Tile BARRIER = new Tile("X", false);
    public static final Tile TALL_GRASS = new Tile("~", true);
    private final String icon;
    private final boolean isPassable;

    Tile(String icon, boolean isPassable) {
        this.icon = icon;
        this.isPassable = isPassable;
    }

    public String toString() {
        return icon;
    }

    public boolean isPassable() {
        return isPassable;
    }
}
