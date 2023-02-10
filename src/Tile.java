public class Tile {
    public static final Tile BLANK = new Tile('·', true);
    public static final Tile BARRIER = new Tile('X', false);
    private final char icon;
    private final boolean isPassable;

    Tile(char icon, boolean isPassable) {
        this.icon = icon;
        this.isPassable = isPassable;
    }

    public String toString() {
        return String.valueOf(icon);
    }

    public boolean isPassable() {
        return isPassable;
    }
}
