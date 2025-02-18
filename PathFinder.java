
import java.util.ArrayList;

public class PathFinder {
    private Map map;

    public PathFinder(Map map) {
        this.map = map;
    }

    // Check if a given path is valid (all nodes in the path are traversable)
    public boolean isPathValid(Map map, ArrayList<Node> path){
        for(Node node:path){
            if(map.visibleGrid[node.x][node.y].getType()!=0){
                return false; // Path is invalid if any node is not traversable
            }
        }
        return true;
    }
    // Implementation of Dijkstra's algorithm to find the shortest path between two points
    public ArrayList<Node> dijkstra(Map map, int startX, int startY, int targetX, int targetY) {
        MyPriorityQueue<Node> pq = new MyPriorityQueue<>(new NodeComparator());
        ArrayList<ArrayList<Double>> distances = new ArrayList<>(); // Distance matrix
        ArrayList<ArrayList<Node>> previous = new ArrayList<>(); // Matrix to store previous nodes in the path
        // Initialize the distance matrix and the priority queue
        for (int x = 0; x < map.width; x++) {
            ArrayList<Double> distanceRow = new ArrayList<>();
            ArrayList<Node> previousRow = new ArrayList<>();
            for (int y = 0; y < map.height; y++) {
                distanceRow.add(Double.MAX_VALUE);
                previousRow.add(null);
                map.visibleGrid[x][y].setCost(Double.MAX_VALUE);
            }
            distances.add(distanceRow);
            previous.add(previousRow);
        }

        distances.get(startX).set(startY, 0.0);
        Node startNode = map.visibleGrid[startX][startY];
        startNode.setCost(0.0);
        pq.add(startNode);

        // Dijkstra's algorithm loop
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentX = current.x;
            int currentY = current.y;
            // Stop if the target node is reached
            if (currentX == targetX && currentY == targetY) {
                Node targetNode = map.visibleGrid[targetX][targetY];
                targetNode.setCost(distances.get(currentX).get(currentY));
                break;
            }
            // Update distances for neighbors of the current node
            for (Edge edge : map.getNeighbors(current.x, current.y)) {
                int neighbourX = edge.neighborX;
                int neighbourY = edge.neighborY;
                // Skip neighbors that are not traversable
                if (map.visibleGrid[neighbourX][neighbourY].getType() != 0) {
                    continue;
                }

                double newDistance = distances.get(currentX).get(currentY) + edge.cost;
                if (newDistance < distances.get(neighbourX).get(neighbourY)) {
                    distances.get(neighbourX).set(neighbourY, newDistance);
                    previous.get(neighbourX).set(neighbourY, current);
                    Node neighborNode = map.visibleGrid[neighbourX][neighbourY];
                    neighborNode.setCost(newDistance);
                    pq.add(neighborNode);
                }
            }
        }
        // Reconstruct and return the path
        return reconstructPath(previous, startX, startY, targetX, targetY);
    }
    private ArrayList<Node> reconstructPath(ArrayList<ArrayList<Node>> previous, int startX, int startY, int targetX, int targetY) {
        ArrayList<Node> path = new ArrayList<>();
        int currentX = targetX;
        int currentY = targetY;

        // Trace back from the target to the start
        while (currentX != startX || currentY != startY) {
            Node currentNode = previous.get(currentX).get(currentY);
            if(currentNode!=null) {
                path.add(0, currentNode); // Add nodes to the front of the path
                currentX = currentNode.x;
                currentY = currentNode.y;
            }

        }
        // Add the target node to the path and return it
        path.add(map.visibleGrid[targetX][targetY]);
        return path;
    }


    // Traverse through objectives, revealing nodes and recalculating paths as needed
    public String traverseObjectives(Map map, ArrayList<Objective> objectives, int radius) {
        StringBuilder sb = new StringBuilder();
        int currentX = objectives.getFirst().x;
        int currentY = objectives.getFirst().y;
        for(int i = 1; i<objectives.size();i++) {

            Objective objective = objectives.get(i);
            map.revealNode(currentX,currentY,radius); // Reveal the area around the current position
            ArrayList<Node> path = dijkstra(map, currentX, currentY, objective.x, objective.y);
            // Handle path recalculation if the path becomes invalid
            while (true) {

                boolean pathChanged = false;
                for (int j = 1; j < path.size(); j++) { // Skip the starting node
                    Node step = path.get(j);
                    sb.append("Moving to ").append(step.x).append("-").append(step.y).append("\n");

                    currentX = step.x;
                    currentY = step.y;
                    map.revealNode(currentX, currentY, radius); // Reveal nodes in the radius

                    // Check if the path becomes invalid
                    if (!isPathValid(map, path)) {
                        sb.append("Path is impassable!\n");
                        path = dijkstra(map, currentX, currentY, objective.x, objective.y);
                        pathChanged = true;
                        break;
                    }
                }
                // Break if the objective is reached
                if (!pathChanged) {
                    Node lastStep = path.get(path.size() - 1);
                    if (lastStep.x == objective.x && lastStep.y == objective.y) {
                        sb.append("Objective ").append(i).append(" reached!\n");
                        break;
                    }
                }
            }
            // Handle the wizard mechanism if there are options
            if (!objective.options.isEmpty()) {
                Objective nextObj = objectives.get(i + 1);
                String wizardRes = wizardOption(map, nextObj, currentX, currentY, objective.options);
                sb.append(wizardRes).append("\n");
            }

        }

        return sb.toString();

    }

    // Choose the best option to unlock nodes for the next objective
    public String wizardOption(Map map, Objective nextObj, int currentX, int currentY, ArrayList<Integer>options){
        double bestCost = Double.MAX_VALUE;
        int bestOption = -1;
        // Evaluate each option to find the optimal one
        for (int option : options) {
            boolean valid = map.unlockNodes(option);
            if (!valid) {
                map.relockNodes(option); // Lock the nodes back if the option is invalid that is it does not affect the path
                continue;
            }
            ArrayList<Node> path = dijkstra(map, currentX, currentY, nextObj.x, nextObj.y);
            double cost = calculatePathCost(path);
            // Update the best option based on path cost
            if (cost < bestCost) {
                bestCost = cost;
                bestOption = option;
            }
            map.relockNodes(option);
        }
        if(bestOption==-1){
            bestOption = options.getFirst(); // Choose the first option if no valid option is found (that is not possible in our project)
        }
        map.realUnlockNodes(bestOption); // Unlock the best option permanently.
        return "Number " + bestOption + " is chosen!";
    }

    // Calculate the total cost of a path
    private double calculatePathCost(ArrayList<Node> path) {
        double cost = path.isEmpty() ? Double.MAX_VALUE : path.get(path.size() - 1).getCost();
        return cost;


    }

}

