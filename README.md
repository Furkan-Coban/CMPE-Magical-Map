# **Magical Map 🗺️**  

## 📌 **Project Overview**  
The **Magical Map** project is a pathfinding simulation where a traveler must navigate through an enchanted land using a dynamically revealing map. The traveler follows a set of objectives while overcoming hidden obstacles and making strategic choices based on wizard-given options.  

This project is part of the **CmpE 250: Data Structures and Algorithms** course (Fall 2025).  

## ⚙️ **Features**  

- **Pathfinding with an Enchanted Map:**  
  - The traveler explores a grid-based map where some nodes are initially hidden and revealed only within a certain radius.  
  - The map is divided into nodes with different types affecting movement.  
- **Dynamic Route Calculation:**  
  - The shortest path to each objective is computed using **Dijkstra’s Algorithm**.  
  - If a path becomes impassable due to newly revealed obstacles, it is recalculated dynamically.  
- **Wizard-Assisted Decision Making:**  
  - At certain objectives, the wizard presents multiple options, each unlocking a type of blocked node.  
  - The traveler must choose the option that results in the shortest path to the next objective.  
- **Efficient Graph Representation:**  
  - Nodes and edges are stored using adjacency lists for optimized traversal.  
  - A custom priority queue (`MyPriorityQueue`) is used for fast pathfinding operations.  

## 📜 **Game Rules & Constraints**  

- **Map Structure:**  
  - The map is a **rectangular grid** where each node has **x** and **y** coordinates.  
  - Nodes are categorized by type:  
    - `0`: Freely passable  
    - `1`: Impassable but initially known  
    - `≥2`: Impassable until revealed  
  - Movement is restricted to **adjacent nodes**, with travel times being **floating-point values**.  

- **Visibility Mechanics:**  
  - A **radius-based reveal system** discloses hidden obstacles dynamically.  
  - The traveler recalculates paths whenever an impassable node is discovered.  

- **Wizard’s Assistance:**  
  - At specific objectives, the wizard provides **multiple unlocking options**.  
  - The best choice is determined by which option enables the fastest travel to the next objective.  

## 🚀 How to Run
1. **Compile the code:**
   ```sh
   javac *.java
2. **Run the program with input/output files:**
   ```sh
   java Main <land_file> <travel_time_file> <mission_file>
## 🛠️ Technologies Used
- Java
- File I/O Handling
- Graph Algorithms (Dijkstra’s Algorithm for shortest path)
- Custom Data Structures (Priority Queue, HashMap, and 2D ArrayLists)
## 📌 Author
- Name: Furkan Çoban
- Course: CmpE 250 - Boğaziçi University
- Year: Fall 2024
