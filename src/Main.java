public class Main {
    public static void main(String[] args) {
        Cell[][] maze = MazeBuilder.buildMaze(3, 4);
        for (Cell[] cells : maze) {
            for (int i = 0; i < 4; i++) {
                for (Cell cell : cells) {
                    cell.printRow(i);
                }
                System.out.println();
            }
        }

    }
}