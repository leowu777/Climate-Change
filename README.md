# Terrain Simulation

## Overview

This project simulates the exploration of a grid-based terrain map where the hiker navigates through different points. The simulation involves custom exceptions and various classes that work together to represent and manipulate the map, allowing for custom handling of errors such as when a point is not on the map.

## Features

- **Grid-Based Map Representation**: The map is structured as a grid where each point represents a different location with unique attributes.
- **Custom Exception Handling**: The project includes custom exceptions like `NotOnMapException` to handle cases where points outside of the map are referenced.
- **Hiker Navigation**: The simulation allows the hiker to move across the grid, ensuring valid moves based on map boundaries and other constraints.
- **Robust Simulation**: Implements algorithms to handle navigation, check for valid map points, and track the hiker's progress.

## Project Structure

The project consists of the following key classes:

- **Simulation.java**: The main class that coordinates the execution of the terrain exploration simulation. It initializes the map and manages the navigation logic.
- **Map.java**: Represents the grid-based terrain where the hiker moves. It contains methods for manipulating and querying the map.
- **GridPoint.java**: Represents a point on the grid with specific attributes such as coordinates, allowing the simulation to track hiker movement.
- **NotOnMapException.java**: A custom exception to handle errors when a point outside the map boundaries is referenced.

## How to Run

1. **Compilation**: Ensure you have a Java compiler installed. You can compile the project with the following command:
   ```bash
   javac *.java
   ```

2. **Execution**: After compiling, run the `Simulation` class, which serves as the main entry point for the program:
   ```bash
   java Simulation
   ```

3. **Input**: The program accepts input in the form of grid points to simulate hiker movement. If a point outside the map is referenced, the program will throw a `NotOnMapException`.

## Key Concepts

- **Grid Representation**: The map is represented as a grid where each point is defined by its coordinates and attributes. The hiker can navigate through valid points on the map.
- **Exception Handling**: The project includes robust exception handling through custom exceptions, ensuring the program gracefully handles invalid map references.
- **Dynamic Simulation**: The simulation dynamically adjusts to map input and tracks the hiker's movements across the grid.

## Example

An example of input might define a map as a 5x5 grid where the hiker starts at point (0, 0) and attempts to navigate to point (4, 4). If a move references an invalid point, the program will throw a `NotOnMapException`.

## Future Improvements

- **Interactive Grid Visualization**: Adding a graphical interface to visualize the map and the hiker's progress.
- **Improved Pathfinding Algorithms**: Implementing more sophisticated pathfinding algorithms to optimize the hiker's movement across the terrain.
- **Customizable Map Generation**: Allow users to input custom map sizes and attributes for more varied simulations.

## Requirements

- Java 8 or later
- Terminal or command-line interface for compiling and running the project

## License

This project is open-source and available under the MIT License.
