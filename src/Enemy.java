import java.util.ArrayList;

public class Enemy implements MazeItem {
    private static Player target;

    private int x, y;
    private char icon;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        icon = '>';
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

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    public ArrayList<Node> getNeighbors(Node centerNode, FOV fov, ArrayList<Node> closedSet) {
        ArrayList<Node> neighbors = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int x = DirectionUtils.getX(centerNode.x(), i);
            int y = DirectionUtils.getY(centerNode.y, i);

            if (!fov.isPassable(x, y)) continue;

            Node neighbor = new Node(x, y);
            if (!closedSet.contains(neighbor)) neighbors.add(neighbor);
        }

        return neighbors;
    }

    public void move(FOV fov) {
        ArrayList<Node> openSet = new ArrayList<>();
        ArrayList<Node> closedSet = new ArrayList<>();
        openSet.add(new Node(x, y, 0));

        while (openSet.size() > 0) {
            Node currentNode = openSet.remove(0);
            closedSet.add(currentNode);

            if (currentNode.x() == target.x() && currentNode.y == target.y()) {
                while (currentNode.parent.parent != null) {
                    currentNode = currentNode.parent;
                }

                icon = DirectionUtils.getIcon(currentNode.x() - x, currentNode.y - y);
                x = currentNode.x();
                y = currentNode.y;
            }

            for (Node neighbor : getNeighbors(currentNode, fov, closedSet)) {
                int cost = currentNode.cost() + getCost(neighbor);

                if (cost < neighbor.cost() || closedSet.contains(neighbor)) {
                    neighbor.setCost(cost);
                    neighbor.setParent(currentNode);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
    }

    public int getCost(Node node) {
        return Math.abs(node.x() - target.x()) + Math.abs(node.y - target.y());
    }

    private static class Node {
        private final int x, y;
        private int cost;
        private Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            cost = Integer.MAX_VALUE;
            parent = null;
        }

        Node(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            parent = null;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public int cost() {
            return cost;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
