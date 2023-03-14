import java.awt.*;
import java.util.ArrayList;

public class Enemy extends MazeItem {
    private static Player target;
    private int turn;
    private int x, y;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        turn = -3;

        color = Color.RED;
        icon = '>';
        isPassable = false;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    public boolean move(FOV fov) {
        if (turn != 1) {
            turn++;
            return false;
        }
        turn = 0;

        ArrayList<Node> openSet = new ArrayList<>();
        ArrayList<Node> closedSet = new ArrayList<>();
        Node start = new Node(x, y);
        start.setCost(0);
        openSet.add(start);

        while (openSet.size() > 0) {
            QuickSort.sort(openSet);
            Node currentNode = openSet.remove(0);
            closedSet.add(currentNode);

            if (currentNode.x == target.x() && currentNode.y == target.y()) {
                while (currentNode.parent != start) {
                    currentNode = currentNode.parent;
                }

                icon = DirectionUtils.getIcon(currentNode.x - x, currentNode.y - y);
                MazeItem nextMove = fov.getItem(currentNode.x, currentNode.y);

                if (nextMove == target) {
                    x = currentNode.x;
                    y = currentNode.y;
                    return true;
                } else if (nextMove.getClass() != Enemy.class) {
                    x = currentNode.x;
                    y = currentNode.y;
                    return false;
                }
            }

            for (Node neighbor : currentNode.getNeighbors(fov, closedSet)) {
                int cost = currentNode.cost + neighbor.getCost();

                if (cost < neighbor.cost || closedSet.contains(neighbor)) {
                    neighbor.setCost(cost);
                    neighbor.setParent(currentNode);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return false;
    }

    private static class Node implements QuickSort.Comparable{
        private final int x, y;
        private int cost;
        private Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            cost = Integer.MAX_VALUE;
            parent = null;
        }

        public int getCost() {
            return Math.abs(x - target.x()) + Math.abs(y - target.y());
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public ArrayList<Node> getNeighbors(FOV fov, ArrayList<Node> closedSet) {
            ArrayList<Node> neighbors = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                int neighborX = Math.floorMod(DirectionUtils.getX(x, i), );
                int neighborY = DirectionUtils.getY(y, i);

                if (!fov.getItem(neighborX, neighborY).isPassable()) continue;

                Node neighbor = new Node(neighborX, neighborY);
                if (!closedSet.contains(neighbor)) neighbors.add(neighbor);
            }

            return neighbors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int compareTo(QuickSort.Comparable compare) {
            Node node = (Node) compare;
            return Integer.compare(cost, node.cost);
        }
    }
}
