
public class Player implements MazeItem {
    private int x, y;

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
        return "0";
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    public void move(FOV fov, int moveX, int moveY) {
        if (fov.isPassable(x + moveX, y + moveY)) {
            x += moveX;
            y += moveY;
        }
    }
}
