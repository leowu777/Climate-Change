package project4;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * A simulation of flooding on a map representing a particular world.
 * The simulation can be performed using different algorithms and the results
 * can be visualized.
 *
 *
 * @author Joanna Klukowska
 * @author Leo Wu
 */
public class Simulation {
    // the map of the world
    static Map map;
    // the queue and stack used in the simulation
    static Queue<GridPoint> queue = new LinkedList<GridPoint>();
    static Stack<GridPoint> stack = new Stack<GridPoint>();
    // a 2D array to keep track of which grid points are flooded
    static boolean[][] flooded;

    /**
     * Runs the simulation of flooding on the map using the specified algorithm
     * @param args command line arguments: map file, algorithm, and visualize flag
     */
    public static void main(String[] args)  {

        // check for the correct number of arguments
        if (args.length < 3) {
            System.err.println("Usage: java Simulation <map file> <algorithm> <visualize>");
            System.err.println("\n Possible arguments: ");
            System.err.println("       algorithms: queue, stack, or recursive ");
            System.err.println("       visualize: true, false");
            System.exit(1);
        }

        // load the map from the file specified at args[0]
        File fileMap = new File(args[0]);
        if (!fileMap.exists()) {
            System.err.println("File not found: " + args[0]);
            System.exit(1);
        }
        try {
            map = new Map(fileMap);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File not found " + args[0]);
        }

        // create a 2D array to keep track of which grid poins are flooded
        flooded = new boolean[map.numOfRows()][map.numOfCols()];


        // determine if progress should be visualized based on the value of args[2]
        // check if true, anything else treated as false
        boolean visualize = args[2].equalsIgnoreCase("true");

        // determine which algorithm to use based on the value of args[1]
        String algorithm = args[1];
        if (!algorithm.equalsIgnoreCase("queue")
                && !algorithm.equalsIgnoreCase("stack")
                && !algorithm.equalsIgnoreCase("recursive") ) {
            System.err.println("ERROR: Invalid algorithm: " + args[1]);
            System.err.println("Valid algorithms are: queue, stack, ore recursive");
            System.exit(1);
        }
        // show the map before simulation starts
        // this is displayed regardless of the value of the visualize flag
        visualize();

        switch (algorithm.toLowerCase()) {
            case "queue":
                // use the queue-based algorithm
                simulateWithQueue(visualize);
                break;
            case "stack":
                // use the stack-based algorithm
                simulateWithStack(visualize);
                break;
            case "recursive":
                // use the recursive algorithm
                simulateRecursive(visualize);
                break;
        }

        // show the map after simulation ends
        // this is displayed regardless of the value of the visualize flag
        visualize();
    }

    /**
     * Simulates flooding using a recursive algorithm
     *
     * @param visualize true if the progress should be visualized, false otherwise
     */
    public static void simulateRecursive(boolean visualize) {
        // Create a list to store water sources
        List<GridPoint> list = new ArrayList<>();

        // Iterate through all water sources on the map
        for (GridPoint gridPoint : map.waterSources()) {
            // Call recursion function for each water source
            recursion(visualize,gridPoint);
        }

    }


    /**
    * Recursively checks and marks flooded areas starting from a given grid point. If a point is not above a certain height
    * and not already under water, it is marked as flooded. This method then recursively checks adjacent points (up, down,
    * left, right) from the current point. If visualization is enabled, it will also trigger a visualization update at each step.
    * @param visualize if true, the map visualization will be updated to reflect changes in flooding status.
    * @param point the starting point for the flood check and marking process.
    */
    public static void recursion(boolean visualize, GridPoint point) {
        try {
            // Check if the current point is above the height threshold
            if(!map.aboveHeight(point)){
                // Check if the current point is not already flooded
                if(!underWater(point)){
                    // Mark the current point as flooded
                    flooded[point.row()][point.col()] = true;
                    
                    // Visualize the flooded area if visualization is enabled
                    if(visualize){
                        visualizeShade();
                    }
                    // Recursively flood adjacent points
                    recursion(visualize,point.up());
                    recursion(visualize,point.down());
                    recursion(visualize,point.left());
                    recursion(visualize,point.right());
                }
            }
        } catch (NotOnMapException e) {
            // Handle the exception if the point is not on the map
        }
    }

    /**
     * Simulates flooding using a stack-based algorithm
     *
     * @param visualize true if the progress should be visualized, false otherwise
     */
    public static void simulateWithStack(boolean visualize) {

        Stack<GridPoint> stack = new Stack<>();
        for (GridPoint gridPoint : map.waterSources()) {
            stack.add(gridPoint);
        }
        while (!stack.isEmpty()) {

            // Pop the next grid point from the stack for evaluation.
            GridPoint point = stack.pop();
            if(underWater(point)){
                continue;
            }
            try {
                // Check if the current point is below the flood height threshold.
                if(!map.aboveHeight(point)){
                    flooded[point.row()][point.col()] = true;
                    // If visualization is enabled, update the visual representation of the map.
                    if(visualize){ 
                        visualizeShade();
                    }
                }
            } catch (NotOnMapException e) {
                // Exception caught when a point is not on the map. Ignored, as no action is required for out-of-bounds points.
            }
            // Explore adjacent points (up, down, left, right) for flooding, using recursion to depth-first search.
            isFlooded(point.up(),stack);
            isFlooded(point.down(),stack);
            isFlooded(point.left(),stack);
            isFlooded(point.right(),stack);
        }
    }


    /**
    * Evaluates whether a given grid point is considered flooded based on its elevation and potentially marks it for further processing.
    * This method checks if the grid point's elevation is below a certain threshold, indicating that the point is flooded.
    * If the point meets the criteria, it is added to a stack. This stack may be used for subsequent processing or analysis of flooded areas.
    *
    * @param point The grid point to evaluate for flooding.
    * @param stack The stack to which the flooded point is added. This allows for managing the order of processing or analysis of points.
    */
    private static void isFlooded(GridPoint point, Stack<GridPoint> stack) {
        try {
            if(!map.aboveHeight(point)){
                stack.add(point);
            }
        } catch (NotOnMapException e) {
            // Exception caught and not handled; indicates the point is outside map boundaries and thus not processed further.
        }
    }


    /**
     * Simulates flooding using a queue-based algorithm
     *
     * @param visualize true if the progress should be visualized, false otherwise
     */
    public static void simulateWithQueue(boolean visualize) {

        Queue<GridPoint> queue = new LinkedList<>();
        for (GridPoint gridPoint : map.waterSources()) {
            queue.add(gridPoint);
        }
        while (!queue.isEmpty()) {
            GridPoint point = queue.remove();
            // Skip processing for points already underwater to avoid redundancy.
            if(underWater(point)){
                continue;
            }
            try {
                // Check if the current point is flooded by comparing its elevation.
                if(!map.aboveHeight(point)){
                    // Mark the grid point as flooded.
                    flooded[point.row()][point.col()] = true;
                    // If visualization is enabled, update the visual representation of the map.
                    if(visualize){
                        visualizeShade();
                    }
                }
            } catch (NotOnMapException e) {
                // If a NotOnMapException is caught, it indicates the point is not on the map. 
                // The catch block is left empty, effectively skipping the point.
            }
            // Recursively check adjacent points (up, down, left, right) to see if they are flooded.
            isFlooded(point.up(),queue);
            isFlooded(point.down(),queue);
            isFlooded(point.left(),queue);
            isFlooded(point.right(),queue);
        }
    }

    /**
    * Checks if a grid point is flooded based on its elevation compared to a predefined height.
    * This method assesses whether the specified grid point is below a certain height, indicating potential flooding.
    * If the point is considered flooded, it is added to a queue for further analysis or action.
    *
    * @param point the grid point to check for flooding
    * @param queue the queue to add the point to if it is flooded. This queue may be used for subsequent processing or tracking of flooded areas.
    */
    public static void isFlooded(GridPoint point, Queue<GridPoint> queue) {
        try {
            // Check if the grid point's elevation is below the predefined height.
            if(!map.aboveHeight(point)){
                queue.add(point);
            }
        } catch (NotOnMapException e) {
            // Exception handling can be implemented here if needed.
        }

    }

    /**
     * Determines whether a grid point is flooded
     *
     * @param g the grid point
     * @return true if the grid point is flooded, false otherwise
     */
    public static boolean underWater ( GridPoint g) {
        return flooded[g.row()][g.col()];
    }

    /**
     * Visualizes the current state of the world. The visualization is done by
     * printing the map to the console. The map is printed with an indication of
     * which grid points are flooded and which are not.
     *
     * WARNING: The visualization does not work well when the elevation
     * numbers are large or when the map itself is large.
     */
    public static void visualize() {
        visualizeBasic();
    }

    /**
     * Visualizes the current state of the world. The visualization is done by
     * printing un-flooded grid points with their with values truncated to integers
     * and flooded grid points are shaded.
     */
    public static void visualizeBasic() {
        //clearScreen();
        for (int i = 0; i < flooded.length; i++) {
            for (int j = 0; j < flooded[0].length; j++) {
                if (flooded[i][j]) {
                    System.out.print("░░ ");
                } else {
                    try {
                        System.out.print(
                                String.format("%2.0f ", map.elevation(new GridPoint(i,j))));
                    } catch (NotOnMapException e) {}
                }
            }
            System.out.println();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) { }
    }


    /**
     * Visualizes the current state of the world. The visualization is done by
     * printing un-flooded grid points with one decimal place and flooded grid points
     * using "XXX" sequence.
     */
    public static void visualizeValues()  {
        clearScreen();
        for (int i = 0; i < flooded.length; i++) {
            for (int j = 0; j < flooded[0].length; j++) {
                if (flooded[i][j]) {
                    System.out.print(" XXX ");
                } else {
                    try {
                        System.out.print(
                                String.format(" %3.1f ", map.elevation(new GridPoint(i,j))));
                    } catch (NotOnMapException e) {}
                }
            }
            System.out.println();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) { }
    }

    /**
     * Visualizes the current state of the world. The visualization is done using
     * four different shades. The flooded areas are shown as "  ". The un-flooded
     * areas are shaded based on their elevation: the lowest 25% of the elevations
     * are shown as "░░", the next 25% are shown as "▒▒", the next 25% are shown as
     * "▓▓", and the highest 25% are shown as "██".
     *
     * Disclaimer: This visualization does not work well if your system is not
     * set to use UTF-8 encoding.
     */
    public static void visualizeShade()  {
        clearScreen();

        double highPoint = map.maxElevation();
        double lowPoint = map.minElevation();
        double range = highPoint - lowPoint;
        double quarterRange = range / 4;

        //draw the upper border
        System.out.print("╔");
        for (int i = 0; i < flooded[0].length; i++) {
            System.out.print("══");
        }
        System.out.println("╗");

        //draw the map with side borders
        for (int i = 0; i < flooded.length; i++) {
            System.out.print("║");
            for (int j = 0; j < flooded[0].length; j++) {
                if (flooded[i][j]) {
                    System.out.print("  ");
                } else {
                    try {
                        System.out.print(
                                shade( map.elevation(new GridPoint(i,j)), lowPoint, quarterRange));
                    } catch (NotOnMapException e) {

                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.print("║");
            System.out.println();
        }
        //draw the lower border
        System.out.print("╚");
        for (int i = 0; i < flooded[0].length; i++) {
            System.out.print("══");
        }
        System.out.println("╝");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) { }
    }

    /**
     * Determine the appropriate shade based on the elevation of a grid point.
     * @param elevation the elevation of the grid point
     * @param lowPoint the lowest elevation on the map
     * @param quarterRange the range of elevations that correspond to each shade
     * @return the appropriate shade for the grid point based on its elevation
     */
    private static String shade(double elevation, double lowPoint, double quarterRange) throws UnsupportedEncodingException {
        // Redirect System.out to use a PrintStream using UTF-8 charset.
        FileOutputStream fos2 = new FileOutputStream(FileDescriptor.out);
        PrintStream ps2 = new PrintStream(fos2, true, StandardCharsets.UTF_8.toString());
        System.setOut(ps2);

        if (elevation < lowPoint + quarterRange) {
            return "░░";
        } else if (elevation < lowPoint + 2 * quarterRange) {
            return "▒▒";
        } else if (elevation < lowPoint + 3 * quarterRange) {
            return "▓▓";
        } else {
            return "██";
        }
    }

    /**
     * Clear screen for the purpose of visualization.
     */
    public static void clearScreen() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }
}