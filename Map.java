import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Map {
    int width, height;
    Node[][] grid;
    Node[][] visibleGrid;
    private ArrayList<ArrayList<ArrayList<Edge>>> neighborList;

    public Map(int width , int height){
        this.width = width;
        this.height = height;
        grid = new Node[width][height];
        visibleGrid = new Node[width][height];
        neighborList = new ArrayList<>();
        // Initialize the neighbor list for all nodes
        for (int i = 0; i < width; i++) {
            ArrayList<ArrayList<Edge>> row = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                row.add(new ArrayList<>());
            }
            neighborList.add(row);
        }

    }
    // Add a node to the grid and set visibility based on type
    public void addNode(int x, int y, int type) {
        grid[x][y] = new Node(x, y, type);
        if(type==0||type==1){
            visibleGrid[x][y] = new Node(x, y, type);
        }
        else{
            visibleGrid[x][y] = new Node(x, y, 0);
        }
    }
    public void  revealNode(int currentX, int currentY, int radius){
        int startX = Math.max(0, currentX - radius);
        int endX = Math.min(width-1, currentX + radius);
        int startY = Math.max(0, currentY - radius);
        int endY = Math.min(height-1, currentY + radius);

        for(int x=startX;x<=endX;x++){
            for(int y=startY;y<=endY;y++){
                if (visibleGrid[x][y].isRevealed()){
                    continue; // Skip already revealed nodes
                }
                double distance = Math.sqrt((currentX-x)*(currentX-x) + (currentY-y)*(currentY-y));
                if(distance<=radius){
                    // Update visibility and mark as revealed
                    visibleGrid[x][y].setType(grid[x][y].getType());
                    visibleGrid[x][y].setRevealed(true);
                    grid[x][y].setRevealed(true);
                }
            }
        }
    }
    // Read edges from a file and initialize the graph's adjacency list
    public void initializeEdges(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            String[] nodes = parts[0].split(",");
            String[] node1 = nodes[0].split("-");
            String[] node2 = nodes[1].split("-");
            double travelTime = Double.parseDouble(parts[1]);

            int x1 = Integer.parseInt(node1[0]);
            int y1 = Integer.parseInt(node1[1]);
            int x2 = Integer.parseInt(node2[0]);
            int y2 = Integer.parseInt(node2[1]);

            // Add both directions for an undirected graph
            addEdge(x1, y1, x2, y2, travelTime);
            addEdge(x2, y2, x1, y1, travelTime);
        }
        reader.close();
    }
    // Add an edge between two nodes with a given cost
    private void addEdge(int x1, int y1, int x2, int y2, double cost) {
        neighborList.get(x1).get(y1).add(new Edge(x2, y2, cost));
    }
    // Get all neighboring edges of a node
    public ArrayList<Edge> getNeighbors(int x, int y) {
        ArrayList<Edge> neighbors = neighborList.get(x).get(y);
        if (neighbors == null) {
            neighbors = new ArrayList<>();
        }
        return neighbors;
    }
    // Unlock nodes of a specific type permanently
    public void realUnlockNodes(int type){
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                if(grid[x][y].getType()==type){
                    visibleGrid[x][y].setType(0);
                    grid[x][y].setType(0);
                }
            }
        }
    }
    // Temporarily unlock nodes of a specific type, returning true if any nodes were unlocked
    public boolean unlockNodes(int type) {
        boolean changed = false;
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                if(grid[x][y].getType()==type){
                    changed = true;
                    visibleGrid[x][y].setType(0);
                }
            }
        }
        return changed;
    }
    // Relock nodes of a specific type, restoring their visibility based on their revealed state
    public void relockNodes(int type) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (grid[x][y].getType() == type) {
                        if (visibleGrid[x][y].isRevealed()) {
                            visibleGrid[x][y].setType(type);
                        } else {
                            visibleGrid[x][y].setType(0); // Reset to original type
                        }
                    }
                }
        }
    }


}
