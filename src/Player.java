import java.awt.*;

public class Player extends MazeItem {
    private int x, y;
    private int health;

    Player(int x, int y) {
        this.x = x;
        this.y = y;
        health = 3;
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

    public int getHealth() {
        return health;
    }

    public boolean damage() {
        health--;
        return health == 0;
    }
}
