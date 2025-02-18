

public class Node {
    public int x;
    public int y;
    private int type;
    private double cost;
    boolean revealed;

    public Node(int x, int y, int type, double cost) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.revealed = false; // Nodes are not revealed by default
        this.cost = cost;
    }
    // Constructor with default cost as positive infinity
    public Node(int x, int y, int type) {
        this(x, y, type, Double.POSITIVE_INFINITY);
    }

    public double getCost() {
        return cost;
    }

    public int getType() {
        return type;
    }

    // Check if the node has been revealed
    public boolean isRevealed() {
        return revealed;
    }


    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
