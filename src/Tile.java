public class Tile implements MazeItem {
    public static final Tile BLANK = new Tile("39m-", true);
    public static final Tile BARRIER = new Tile("39mX", false);
    public static final Tile TALL_GRASS = new Tile("32m#", true);
    private final String icon;
    private final boolean isPassable;

    Tile(String icon, boolean isPassable) {
        this.icon = icon;
        this.isPassable = isPassable;
    }

    public String toString() {
        return (char) 27 + "[" + icon + (char) 27 + "[39m";
    }

    public boolean isPassable() {
        return isPassable;
    }
}
