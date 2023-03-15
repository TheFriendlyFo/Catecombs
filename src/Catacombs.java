import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Catacombs {

    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final FOV fov;

    Catacombs(int cellSize, int numCells) {
        JFrame j = new JFrame("Catacombs");
        j.setSize(340, 450);
        j.setLocation(600,250);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        KeyTracker keyLog = new KeyTracker();
        j.addKeyListener(keyLog);


        Tile[][] worldMap = MazeBuilder.buildMaze(cellSize, numCells);

        enemies = new ArrayList<>();
        player = new Player(worldMap.length/2, worldMap.length/2);
        fov = new FOV(player, enemies, j, worldMap, 21);

        Enemy.setTarget(player);
        Enemy.setFov(fov);

        fov.focus();
        fov.display();
    }

    public class KeyTracker extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> movePlayer(-1, 0);
                case KeyEvent.VK_D -> movePlayer(1, 0);
                case KeyEvent.VK_W -> movePlayer(0, -1);
                case KeyEvent.VK_S -> movePlayer(0, 1);
            }

            fov.focus();
            fov.generateEnemies();
            fov.display();
        }

    }

    public void movePlayer(int moveX, int moveY) {
        if (fov.isPassable(player.x() + moveX, player.y() + moveY)) {
            player.incX(moveX);
            player.incY(moveY);
        }
    }
}
