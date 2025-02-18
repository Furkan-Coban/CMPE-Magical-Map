import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    private static ArrayList<Objective> parseObjectives(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        ArrayList<Objective> objectives = new ArrayList<>();

        // First line is the radius of sight
        int radius = Integer.parseInt(reader.readLine());
        Objective.radius = radius;
        // Parse objectives
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            ArrayList<Integer> options = new ArrayList<>();

            // If there are wizard options, add them
            for (int i = 2; i < parts.length; i++) {
                options.add(Integer.parseInt(parts[i]));
            }
            objectives.add(new Objective(x, y, options));
        }
        reader.close();
        return objectives;
    }
    // Initialize the map from the given file
    public static  Map initializeMap(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        // First line specifies map dimensions
        String[] dimensions = reader.readLine().split(" ");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        Map map = new Map(width, height);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int type = Integer.parseInt(parts[2]);
            map.addNode(x, y, type);
        }
        reader.close();
        return map;
    }
    public static void main(String[] args) throws IOException {
        String nodesFile = args[0];
        String edgesFile = args[1];
        String objectivesFile = args[2];
        String outputFile = args[3];
        // Initialize the map and parse edges and objectives
        Map map = initializeMap(nodesFile);
        map.initializeEdges(edgesFile);
        ArrayList<Objective> objectives = parseObjectives(objectivesFile);
        int radius = Objective.radius;
        // Create a PathFinder instance and traverse objectives
        PathFinder pathFinder = new PathFinder(map);
        String result = pathFinder.traverseObjectives(map, objectives, radius);
        // Write the result to an output file
        Files.writeString(Path.of(outputFile), result);

    }
}