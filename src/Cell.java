public class Cell {
    private final Tile[][] tiles;
    private boolean isAccessed;
    private final int size;

    Cell(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
        isAccessed = false;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tiles[y][x] = x == size - 1 ||  y == size - 1 ? Tile.BARRIER : Tile.BLANK;
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
    }

    public void setAccessed() {
        isAccessed = true;
    }

    public Tile[] getRow(int row) {
        return tiles[row];
    }

    public boolean isAccessed() {
        return isAccessed;
    }
}
