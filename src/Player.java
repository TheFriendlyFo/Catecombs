import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Player extends MazeItem {
    private int x, y;
    private int health;
    private FOV fov;
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();

    Player(int x, int y) {
        this.x = x;
        this.y = y;
        health = 3;
        icon = '0';
        color = Color.BLUE;
        isPassable = true;
    }

    public void setFov(FOV fov) {
        this.fov = fov;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void incX(int inc) {
        if (fov.isPassable(x + inc, y)) x += inc;
    }

    public void incY(int inc) {
        if (fov.isPassable(x, y + inc)) y += inc;
    }

    public int getHealth() {
        return health;
    }

    public void damage() {
        health--;
    }

    public void tick() {
        if (isKeyPressed(KeyEvent.VK_W)) incY(-1);
        if (isKeyPressed(KeyEvent.VK_S)) incY(1);
        if (isKeyPressed(KeyEvent.VK_A)) incX(-1);
        if (isKeyPressed(KeyEvent.VK_D)) incX(1);
    }


    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (Player.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) pressedKeys.put(event.getKeyCode(), true);
                else if (event.getID() == KeyEvent.KEY_RELEASED) pressedKeys.put(event.getKeyCode(), false);
                return false;
            }
        });
    }

    public static boolean isKeyPressed(int keyCode) { // Any key code from the KeyEvent class
        return pressedKeys.getOrDefault(keyCode, false);
    }

}
