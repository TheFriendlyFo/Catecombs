public class Main {
    public static void main(String[] args) {
       Tile[][] maze = MazeBuilder.buildMaze(6, 4);

        for (Tile[] tiles : maze) {
            for (Tile tile : tiles) {
                System.out.print(tile + " ");
            }
            System.out.println();
        }

    }
}