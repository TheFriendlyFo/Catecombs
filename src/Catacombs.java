import java.util.ArrayList;

public class Catacombs {
    private final Tile[][] worldMap;
    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final FOV fov;

    Catacombs(int cellSize, int numCells) {
        worldMap = MazeBuilder.buildMaze(cellSize, numCells);
        player = new Player(4, 4);
        enemies = new ArrayList<>();

        Enemy.setTarget(player);

        fov = new FOV(worldMap, 15);
        fov.focus(player, enemies);
        fov.display();
    }

    public void update() {
        player.move(worldMap);
        fov.focus(player, enemies);
        fov.updateEnemies(enemies);
        fov.generateEnemies(enemies);
        fov.display();
    }
}
