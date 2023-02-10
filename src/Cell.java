public class Cell {
    private final Tile[][] tiles;
    // The cell's location in the overall map
    // The 4 surrounding walls of the cell.
    // A wall being false means it is unpassable, or raised, otherwise it is lowered.
    // The indices are arranged in the following pattern:
    //   1
    // 0
    private boolean isAccessed;
    private final int size;

    Cell(int size) {
        this.size = size + 1;
        this.tiles = new Tile[this.size][this.size];
        isAccessed = false;

        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                tiles[y][x] = x == this.size - 1 ||  y == this.size - 1 ? Tile.BARRIER : Tile.BLANK;
            }
        }
    }

    public void lowerWall(int wall) {
        int y, x;

        if (wall == 0) {
            x = -1;
            y = size - 1;
        } else {
            x = size - 1;
            y = -1;
        }

        int length = (int) (Math.random() * (size -2) + 1);
        int position = (int) (Math.random() * (size - length));

        for (int i = position; i < position + length; i++) {
            tiles[y == -1 ? i : size - 1][x == -1 ? i : size - 1] = Tile.BLANK;
        }

        isAccessed = true;
    }

    public void printRow(int row) {
        for (Tile tile : tiles[row]) {
            System.out.print(tile + " ");
        }
    }

    public boolean isAccessed() {
        return isAccessed;
    }
}
