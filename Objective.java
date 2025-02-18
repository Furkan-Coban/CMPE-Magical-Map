import java.util.ArrayList;

public class Objective {
    public int x, y;
    public static int radius;
    public ArrayList<Integer> options; // Wizard options

    public Objective(int x, int y, ArrayList<Integer> options) {
        this.x = x;
        this.y = y;
        this.options = options;
    }
}