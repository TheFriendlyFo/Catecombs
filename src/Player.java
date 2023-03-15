import java.awt.*;

public class Player extends MazeItem {
    private int x, y;

    Player(int x, int y) {
        this.x = x;
        this.y = y;
        icon = '0';
        color = Color.BLUE;
        isPassable = true;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void incX(int inc) {
        x += inc;
    }

    public void incY(int inc) {
        y += inc;
    }
}
