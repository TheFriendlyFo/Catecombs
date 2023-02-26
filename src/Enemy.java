import java.util.ArrayList;

public class Enemy implements MazeItem {

    private int x, y;
    private char icon;

    //private int depth;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        icon = '>';
        //depth = 0;
    }

    static ArrayList<Pt> rankMoves(Pt move, Player target, MazeItem[][] map, ArrayList<Pt> previousMoves) {
        ArrayList<int[]> moves = new ArrayList<>();
        ArrayList<Pt> returnMoves = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int newX = move.x() + DirectionUtils.getX(i);
            int newY = move.y() + DirectionUtils.getY(i);
            int distance = getDistance(newX, newY, target);

            boolean inBoundsX = (0 <= newX && newX < map.length);
            boolean inBoundsY = (0 <= newY && newY < map.length);
            boolean newMove = !previousMoves.contains(new Pt(newX, newY));
            if (!(inBoundsX && inBoundsY && newMove && map[newY][newX].isPassable())) continue;

            for (int j = 0; j <= i; j++) {
                if (j == moves.size() || moves.get(j)[2] > distance) {
                    moves.add(j, new int[]{newX, newY, distance});
                    break;
                }
            }
        }

        for (int[] ints : moves) {
            returnMoves.add(new Pt(ints[0], ints[1]));
        }
        return returnMoves;
    }

    private static int getDistance(int x, int y, Player player) {
        return (int) (Math.sqrt(Math.pow(x - player.x(), 2) + Math.pow(y - player.y(), 2)) * 100);
    }

    public void move(Tile[][] map, Player target) {
        if (Math.random() > 0.7) return;

        //depth = 0;
        move(x, y, map, target, new ArrayList<>());
    }

    private boolean move(int currentX, int currentY, Tile[][] map, Player target, ArrayList<Pt> previousMoves) {
        previousMoves.add(new Pt(currentX, currentY));

        //depth++;
        //if (depth > 10000000) return false;

        if (currentX == target.x() && currentY == target.y()) {
            icon = DirectionUtils.getIcon(previousMoves.get(1).x() - x, previousMoves.get(1).y() - y);
            x = previousMoves.get(1).x();
            y = previousMoves.get(1).y();
            return true;
        }

        for (Pt move : rankMoves(new Pt(currentX, currentY), target, map, previousMoves)) {
            if (move(move.x(), move.y(), map, target, previousMoves)) return true;
        }

        previousMoves.remove(previousMoves.size() - 1);
        return false;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public String toString() {
        return (char) 27 + "[31m" + icon + (char) 27 + "[39m";
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    private record Pt(int x, int y) {

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() == Pt.class) {
                Pt ptObj = (Pt) obj;
                return (ptObj.x() == x && ptObj.y() == y);
            }
            return false;
        }
    }


}
