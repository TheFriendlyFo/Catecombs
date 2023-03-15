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
        j.setSize(340, 450);
        j.setLocation(600,250);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        KeyTracker keyLog = new KeyTracker();
        j.addKeyListener(keyLog);


        enemies = new ArrayList<>();
        Tile[][] worldMap = MazeBuilder.buildMaze(cellSize, numCells);
        fov = new FOV(j, worldMap, 21);
        player = new Player(fov.getMapSize()/2, fov.getMapSize()/2);
        Enemy.setTarget(player);
        fov.focus(player, enemies);
        fov.display();
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
