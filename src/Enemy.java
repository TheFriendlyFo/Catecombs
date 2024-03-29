import java.awt.*;
import java.util.ArrayList;

public class Enemy extends MazeItem {
    private static Player target;
    private static FOV fov;
    private int x, y;
    private int tick;

    Enemy(int x, int y) {
        tick = -5;
        this.x = x;
        this.y = y;

        color = Color.RED;
        icon = '>';
        isPassable = false;
    }

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    public static void setFov(FOV fov) {
        Enemy.fov = fov;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void tick() {
        tick++;
        if (tick == 3) {
            tick = 0;
            move();
            checkCollision();
        }
    }

    public void move() {
        ArrayList<Node> openSet = new ArrayList<>();
        ArrayList<Node> closedSet = new ArrayList<>();
        Node start = new Node(x, y);
        start.setCost(0);
        openSet.add(start);

        move(openSet, closedSet);
    }

    private void move(ArrayList<Node> openSet, ArrayList<Node> closedSet) {
        if (openSet.size() == 0) return;
        QuickSort.sort(openSet);
        Node currentNode = openSet.remove(0);
        closedSet.add(currentNode);

        if (currentNode.x == target.x() && currentNode.y == target.y()) {
            Node moveNode = currentNode.traceBack();

            icon = DirectionUtils.getIcon(moveNode.x - x, moveNode.y - y);
            x = moveNode.x;
            y = moveNode.y;

            return;
        }

        for (Node neighbor : currentNode.getNeighbors(closedSet)) {
            int cost = currentNode.cost + neighbor.getCost();

            if (cost < neighbor.cost || closedSet.contains(neighbor)) {
                neighbor.setCost(cost);
                neighbor.setParent(currentNode);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        move(openSet, closedSet);
    }

    public void checkCollision() {
        if (x == target.x() && y == target.y()) {
            fov.addToDeleteQueue(this);
            target.damage();
        }
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

        public ArrayList<Node> getNeighbors(ArrayList<Node> closedSet) {
            ArrayList<Node> neighbors = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                int neighborX = DirectionUtils.getX(x, i);
                int neighborY = DirectionUtils.getY(y, i);

                if (!fov.isPassable(neighborX, neighborY)) continue;

                Node neighbor = new Node(neighborX, neighborY);
                if (!closedSet.contains(neighbor)) neighbors.add(neighbor);
            }

            return neighbors;
        }

        public Node traceBack() {
            Node tracker = this;
            while (tracker.parent.parent != null) {
                tracker = tracker.parent;
            }
            return tracker;
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
