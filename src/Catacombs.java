import javax.swing.*;
import java.util.ArrayList;

public class Catacombs {

    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final FOV fov;

    Catacombs(int cellSize, int numCells) {
        JFrame j = new JFrame("Catacombs");
        j.setSize(340, 500);
        j.setLocation(600,250);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        Tile[][] worldMap = MazeBuilder.buildMaze(cellSize, numCells);

        enemies = new ArrayList<>();
        player = new Player(worldMap.length/2, worldMap.length/2);
        fov = new FOV(player, enemies, j, worldMap, 21);
        player.setFov(fov);

        Enemy.setTarget(player);
        Enemy.setFov(fov);

        fov.focus();
        fov.display();

        Timer clock = new Timer(110, e -> tick());
        clock.start();
    }

    private void tick() {
        player.tick();
        for (Enemy enemy : enemies) {
            enemy.tick();
        }
        fov.processDeleteQueue();
        fov.generateEnemies();
        fov.focus();
        fov.display();
    }
}
