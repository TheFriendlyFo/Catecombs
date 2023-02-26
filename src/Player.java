import java.util.Scanner;

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
        return (char) 27 + "[34m0" + (char) 27 + "[39m";
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    public void move(Tile[][] worldMap) {
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;

        while (!valid) {
            System.out.println("Enter direction to move (WASD):");
            System.out.print("> ");
            String move = scanner.nextLine();

            int xAdd = DirectionUtils.getX(move);
            int yAdd = DirectionUtils.getY(move);
            if (xAdd == 0 && yAdd == 0) break;
            if (worldMap[y + yAdd][x + xAdd].isPassable()) {
                x += xAdd;
                y += yAdd;
                valid = true;
            }
        }
    }
}
