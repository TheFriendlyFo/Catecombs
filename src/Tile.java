public class Tile implements MazeItem {
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
