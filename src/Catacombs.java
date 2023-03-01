import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Catacombs  implements ActionListener {

    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final FOV fov;

    Catacombs(int cellSize, int numCells) {
        JFrame j = new JFrame("Catacombs");
        j.add(new JLabel());
        j.addKeyListener(new KeyTracker());
        j.setSize(500, 500);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Tile[][] worldMap = MazeBuilder.buildMaze(cellSize, numCells);

        player = new Player(4, 4);
        enemies = new ArrayList<>();

        Enemy.setTarget(player);

        fov = new FOV(j, worldMap, 21);
        fov.focus(player, enemies);
        fov.display();
    }

    public void update() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public class KeyTracker extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.move(fov,-1, 0);
                case KeyEvent.VK_D -> player.move(fov,1, 0);
                case KeyEvent.VK_W -> player.move(fov,0, -1);
                case KeyEvent.VK_S -> player.move(fov,0, 1);
            }

            fov.focus(player, enemies);
            fov.updateEnemies(enemies);
            fov.generateEnemies(enemies);
            fov.display();
        }

    }
}
