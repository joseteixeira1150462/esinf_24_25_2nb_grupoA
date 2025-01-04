# Big O Complexity Analysis

## Time Complexity Analysis for `CriticalPathAnalyzer` Class

### **Methods and Descriptions**:

1. **`initializeMetrics()`**:
   - **Description**: Initializes the metrics for each vertex (activity) in the graph.
   - **Time Complexity**: **O(N)** where N is the number of vertices in the graph.

2. **`calculateCriticalPath()`**:
   - **Description**: Main function that calculates the critical path by performing a forward pass, backward pass, and calculating slack for each activity.
   - **Time Complexity**: **O(N + E)** where N is the number of vertices and E is the number of edges.
     - This involves topological sorting (O(N + E)) followed by two passes (forward and backward), each iterating over the vertices and edges.

3. **`topologicalSort()`**:
   - **Description**: Performs topological sorting of the graph to determine the order of activities.
   - **Time Complexity**: **O(N + E)** where N is the number of vertices and E is the number of edges.
     - The algorithm processes each vertex and edge once. It's a depth-first search-based sorting with an extra pass for calculating in-degrees.

4. **`forwardPass(List<ActivityVertex> topologicalOrder)`**:
   - **Description**: Calculates the earliest start and finish times for each activity in the forward pass.
   - **Time Complexity**: **O(N + E)** where N is the number of vertices and E is the number of edges.
     - This pass iterates over the vertices and their incoming edges.

5. **`backwardPass(List<ActivityVertex> topologicalOrder)`**:
   - **Description**: Calculates the latest start and finish times for each activity in the backward pass.
   - **Time Complexity**: **O(N + E)** where N is the number of vertices and E is the number of edges.
     - This pass iterates over the vertices and their outgoing edges.

6. **`calculateSlack()`**:
   - **Description**: Calculates the slack (time available without delaying the project) for each activity.
   - **Time Complexity**: **O(N)** where N is the number of vertices.
     - Iterates over the vertices once to calculate slack.

7. **`validateCompleteCriticalPath()`**:
   - **Description**: Validates that the critical path starts and ends correctly in the graph.
   - **Time Complexity**: **O(N)** where N is the number of vertices.
     - It checks the start and end of the critical path.

8. **`getCriticalPath()`**:
   - **Description**: Returns the critical path, consisting of activities with no slack.
   - **Time Complexity**: **O(N + E)** where N is the number of vertices and E is the number of edges.
     - The method performs a DFS search for the critical path.

9. **`getDetailedCriticalPathAnalysis()`**:
   - **Description**: Returns a detailed string analysis of the critical path activities.
   - **Time Complexity**: **O(N)** where N is the number of vertices.
     - Iterates over all the vertices to generate detailed activity information.

10. **`getTotalProjectDuration()`**:
    - **Description**: Calculates the total project duration based on the earliest finish times of the activities.
    - **Time Complexity**: **O(N)** where N is the number of vertices.
      - This operation involves a single scan over the activities to find the maximum finish time.

11. **`getMetrics()`**:
    - **Description**: Returns the metrics for all activities in the project.
    - **Time Complexity**: **O(1)**
      - Accesses the metrics map.

### **Overall Complexity**:

The time complexity for the `CriticalPathAnalyzer` class is **O(N + E)** in most cases, where:
- \( N \) is the number of vertices (activities) in the graph
- \( E \) is the number of edges (dependencies between activities)

---

## Time Complexity Analysis for `BottleneckAnalyzer` Class

### Key Methods and Their Time Complexities:

#### 1. **`identifyByDependents`**:
This method identifies bottleneck activities based on the number of dependents (out-degree). The time complexity is as follows:
- Iterates over all vertices in the graph (**O(N)**).
- For each vertex, calculates the out-degree, which is an **O(1)** operation.

**Time Complexity**: **O(N)**, where N is the number of vertices (activities).

#### 2. **`identifyByPathFrequency`**:
This method identifies bottleneck activities based on the frequency of activities appearing in paths. It performs a Depth-First Search (DFS) for exploring all possible paths. The time complexity is as follows:
- DFS traversal of the graph takes **O(N + E)**.
- In the worst case, for a graph with many paths, the time complexity can become exponential, **O(2^N)**, due to the recursive nature of DFS in exploring all paths from start to end.

**Time Complexity**: **O(N + E)** for DFS, but the worst-case time complexity is **O(2^N)**, where N is the number of vertices (due to exploring all paths).

#### 3. **`findStartVertex` and `findEndVertex`**:
These helper methods find the start and end vertices by iterating over all vertices in the graph.

**Time Complexity**: **O(N)**, where N is the number of vertices.

#### 4. **Overall Time Complexity**:
- **Best-case Time Complexity**: **O(N + E)** when not many paths are explored.
- **Worst-case Time Complexity**: **O(2^N)** due to DFS searching all paths.

### **Conclusion**:
- **Best-case Time Complexity**: **O(N + E)**
- **Worst-case Time Complexity**: **O(2^N)**
