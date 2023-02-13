public class Player implements MazeItem {
    int x, y;

    Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public String toString() {
        return (char) 27 + "[34m0" + (char) 27 + "[39m";
    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
