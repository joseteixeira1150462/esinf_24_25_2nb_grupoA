/*
* A collection of graph algorithms.
*/
package esinf.utils.adjacencymapgraph;


import java.util.*;


/**
 *
 * @author DEI-ESINF
 */

public class GraphAlgorithms {
    
   /**
   * Performs breadth-first search of a Graph starting in a Vertex 
   * @param g Graph instance
   * @return qbfs a queue with the vertices of breadth-first search 
   */
    public static<V,E> LinkedList<V> BreadthFirstSearch(Graph<V,E> g, V vert){

        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> result = new LinkedList<>();
        Queue<V> queue = new LinkedList<>();
        HashSet<V> visited = new HashSet<>();

        if (!g.validVertex(vert)) return result;

        queue.add(vert);
        visited.add(vert);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            result.add(current);

            for (V adj : g.adjVertices(current)) {
                if (!visited.contains(adj)) {
                    queue.add(adj);
                    visited.add(adj);
                }
            }
        }

        return result;
    }
   
   /**
   * Performs depth-first search starting in a Vertex   
   * @param g Graph instance
   * @param vOrig Vertex of graph g that will be the source of the search
   * @param visited set of discovered vertices
   * @param qdfs queue with vertices of depth-first search
   */
    private static<V,E> void DepthFirstSearch(Graph<V,E> g, V vOrig, HashSet<V> visited ,LinkedList<V> qdfs){
        visited.add(vOrig);
        qdfs.add(vOrig);

        for (V adj : g.adjVertices(vOrig)) {
            if (!visited.contains(adj)) {
                DepthFirstSearch(g, adj, visited, qdfs);
            }
        }
    }  
  
   /**
   * @param g Graph instance
   * @param vert information of the Vertex that will be the source of the search
   * @return qdfs a queue with the vertices of depth-first search 
   */
    public static<V,E> LinkedList<V> DepthFirstSearch(Graph<V,E> g, V vert){
        if (!g.validVertex(vert)) return null;

        LinkedList<V> qdfs = new LinkedList<>();
        HashSet<V> visited = new HashSet<>();
        DepthFirstSearch(g, vert, visited, qdfs);
        return qdfs;
    }
   
    /**
   * Returns all paths from vOrig to vDest
   * @param g Graph instance
   * @param vOrig Vertex that will be the source of the path
   * @param vDest Vertex that will be the end of the path
   * @param path stack with vertices of the current path (the path is in reverse order)
   * @param paths ArrayList with all the paths (in correct order)
   */
    private static<V,E> void allPaths(Graph<V,E> g, V vOrig, V vDest,  
                                           LinkedList<V> path, ArrayList<LinkedList<V>> paths){
        path.add(vOrig);

        if (vOrig.equals(vDest)) {
            paths.add(new LinkedList<>(path)); // Found a path, add to paths list
        } else {
            for (V adj : g.adjVertices(vOrig)) {
                if (!path.contains(adj)) { // Avoid revisiting vertices
                    allPaths(g, adj, vDest, path, paths);
                }
            }
        }

        path.removeLast(); // Backtrack
    }
    
   /**
   * @param g Graph instance
   * @param vOrig the Vertex origin
   * @param vDest  the Vertex destination
   * @return paths ArrayList with all paths from voInf to vdInf 
   */
    public static<V,E> ArrayList<LinkedList<V>> allPaths(Graph<V,E> g, V vOrig, V vDest){

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        LinkedList<V> path = new LinkedList<>();
        allPaths(g, vOrig, vDest, path, paths);
        return paths;
    }
    
    /**
   * Computes shortest-path distance from a source vertex to all reachable 
   * vertices of a graph g with nonnegative edge weights
   * This implementation uses Dijkstra's algorithm
   * @param g Graph instance
   * @param vOrig Vertex that will be the source of the path
   * @param visited set of discovered vertices
   * @param pathKeys minimum path vertices keys
   * @param dist minimum distances
   */
    protected static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig,
                                                    boolean[] visited, Map<V, V> pathKeys, double[] dist) {
        // Initialize all distances as infinity and all path keys as null
        for (V vert : g.vertices()) {
            dist[g.getKey(vert)] = Double.POSITIVE_INFINITY;
            pathKeys.put(vert, null);
        }
        dist[g.getKey(vOrig)] = 0; // Distance from origin vertex to itself is 0

        // Priority queue to process the vertices
        PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(v -> dist[g.getKey(v)]));
        pq.add(vOrig);

        // While there are vertices in the queue
        while (!pq.isEmpty()) {
            V current = pq.poll();
            int currentKey = g.getKey(current);

            // Skip if the vertex has already been visited
            if (visited[currentKey]) {
                continue;
            }
            visited[currentKey] = true;

            for (Edge<V, E> edge : g.outgoingEdges(current)) {
                V adj = g.opposite(current, edge);
                int adjKey = g.getKey(adj);
                double newDist = dist[currentKey] + edge.getWeight();

                // Update the distance if a shorter path is found
                if (newDist < dist[adjKey]) {
                    dist[adjKey] = newDist;
                    pathKeys.put(adj, current);

                    // Reinsert the vertex to the priority queue to ensure it's processed with the updated distance
                    pq.add(adj);  // Re-insert the vertex with the updated distance
                }
            }
        }
    }


    /**
    * Extracts from pathKeys the minimum path between voInf and vdInf
    * The path is constructed from the end to the beginning
    * @param g Graph instance
    * @param vOrig information of the Vertex origin
    * @param vDest information of the Vertex destination
    * @param pathKeys minimum path vertices keys
    * @param path stack with the minimum path (correct order)
    */
    protected static<V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, Map<V, V> pathKeys, LinkedList<V> path) {
        V current = vDest;
        while (current != null && !current.equals(vOrig)) {
            path.add(current);
            current = pathKeys.get(current);
        }

        if (current != null) {
            path.add(vOrig);
        }

        Collections.reverse(path);
    }

    //shortest-path between vOrig and vDest
    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            shortPath.clear();
            return 0;
        }

        boolean[] visited = new boolean[g.numVertices()];
        Map<V, V> pathKeys = new HashMap<>();
        double[] dist = new double[g.numVertices()];

        // Calculate the minimum distances
        shortestPathLength(g, vOrig, visited, pathKeys, dist);

        if (dist[g.getKey(vDest)] == Double.POSITIVE_INFINITY) {
            shortPath.clear();
            return 0;
        }

        // Reconstruct the path
        getPath(g, vOrig, vDest, pathKeys, shortPath);

        return dist[g.getKey(vDest)];
    }


    //shortest-path between voInf and all other
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists) {
        if (!g.validVertex(vOrig)) {
            return false;
        }

        Map<V, V> pathKeys = new HashMap<>();
        double[] dist = new double[g.numVertices()];
        boolean[] visited = new boolean[g.numVertices()];


        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[g.getKey(vOrig)] = 0.0;

        // Calcula as distâncias mínimas (Dijkstra)
        shortestPathLength(g, vOrig, visited, pathKeys, dist);


        paths.clear();  // Limpar caminhos antes de adicionar novos
        dists.clear();  // Limpar distâncias antes de adicionar novas

        for (V vert : g.vertices()) {
            if (dist[g.getKey(vert)] == Double.POSITIVE_INFINITY) {
                paths.add(new LinkedList<>());
                dists.add(Double.MAX_VALUE);
            } else {
                LinkedList<V> path = new LinkedList<>();
                getPath(g, vOrig, vert, pathKeys, path);
                paths.add(path);
                dists.add(dist[g.getKey(vert)]);
            }
        }
        return true;
    }

    public static <V, E> void checkForCircularDependencies(Graph<V, E> graph) {
        Set<V> visited = new HashSet<>();
        Edge<V,E> cycles = null;

        // Check from each vertex
        for (V vertex : graph.vertices()) {
            if (!visited.contains(vertex)) {
                dfsDetectCycles(graph, vertex, visited, cycles);
            }
        }

        // Display results
        if (cycles==null) {
            System.out.println("No circular dependencies found - project can be scheduled!");
        } else {
            System.out.println("Warning: Circular dependencies detected!");
            System.out.println(describeCycle(graph, cycles));
        }
    }


    /**
     * Detects and returns all circular dependencies in a project graph
     * @param graph The project graph where vertices are activities and edges are dependencies
     * @return List of cycles, where each cycle is represented as a list of edges that form the cycle
     */
    public static <V, E> Edge<V, E> detectCircularDependencies(Graph<V, E> graph) {
        Edge<V, E> cycleEdge = null; // o (eventual) edge que causou erro, se estiver null -> ok
        Set<V> visited = new HashSet<>(); // Mantém o caminho atual

        for (V vertex : graph.vertices()) {
            dfsDetectCycles(graph, vertex, visited, cycleEdge);
        }

        return cycleEdge ;
    }

    /**
     * DFS implementation for cycle detection
     */
    private static <V, E> void dfsDetectCycles(
            Graph<V, E> graph,
            V current,
            Set<V> visited,
            Edge<V, E> cycles) {

        visited.add(current); // Marcar o vértice no caminho atual

        // iterar edges do grafo atual
        for (Edge<V, E> edge : graph.outgoingEdges(current)) {
            V nextActivity = graph.opposite(current, edge);

            // Base Case: Verificar se há um ciclo direto ou indireto
            if (nextActivity.toString().equals(current.toString())  || visited.contains(nextActivity)) {
                cycles = edge;
                return;
            }

            // O próximo vértice não foi visitado ainda, continue a DFS
            dfsDetectCycles(graph, nextActivity, visited, cycles);
        }
    }



    private static <V> boolean containsCycle(List<List<V>> cycles, List<V> newCycle) {
        for (List<V> existingCycle : cycles) {
            if (isSameCycle(existingCycle, newCycle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to compare two cycles
     */
    private static <V> boolean isSameCycle(List<V> cycle1, List<V> cycle2) {
        if (cycle1.size() != cycle2.size()) {
            return false;
        }

        // Create a double-length version of cycle1 to handle rotations
        List<V> doubledCycle = new ArrayList<>(cycle1);
        doubledCycle.addAll(cycle1);

        // Try to find cycle2 in the doubled cycle1
        for (int i = 0; i < cycle1.size(); i++) {
            boolean matches = true;
            for (int j = 0; j < cycle2.size(); j++) {
                if (!doubledCycle.get(i + j).equals(cycle2.get(j))) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }


    /**
     * Helper method to get a human-readable representation of a cycle
     * @param graph The project graph
     * @param cycle List of edges forming a cycle
     * @return String representation of the cycle's activities
     */
    public static <V, E> String describeCycle(Graph<V, E> graph, Edge<V, E> cycle) {
        if (cycle == null) return "No cycle";

        // Iniciar a descrição do ciclo
        StringBuilder description = new StringBuilder("Circular Dependency Detected!");
        V startVertex = cycle.getVOrig();
        V nextVertex = graph.opposite(startVertex, cycle);

        if (startVertex == nextVertex) {
            description.append("Self Connecting Circular Dependency:");
            description.append("ActivityVertex [ID: ").append(startVertex).append(" -> ").append(nextVertex).append("]");
        }

        else {
            description.append("Indirect Circular Dependency, the destination was already visited:");
            description.append("ActivityVertex [ID: ").append(startVertex).append(" -> ").append(nextVertex).append("]");
        }


        return description.toString();
    }

    /**
     * Helper method to get a human-readable representation of a cycle
     * @param graph The project graph
     * @param cycle List of edges forming a cycle
     * @return String representation of the cycle's activities
     */
//    public static <V, E> String describeCycle(Graph<V, E> graph, List<Edge<V, E>> cycle) {
//        if (cycle.isEmpty()) return "No cycle";
//
//        // Iniciar a descrição do ciclo
//        StringBuilder description = new StringBuilder("Circular Dependency: ");
//        V startVertex = cycle.get(0).getVOrig();
//        description.append("ActivityVertex [ID: ").append(startVertex).append("]");
//
//        // Itera pelas arestas do ciclo, construindo a sequência dos vértices
//        for (Edge<V, E> edge : cycle) {
//            V nextVertex = graph.opposite(startVertex, edge);
//            description.append("  -> ActivityVertex [ID: ").append(nextVertex).append("]");
//            startVertex = nextVertex;
//        }
//
//        return description.toString();
//    }

    /**
     * Reverses the path
     * @param path stack with path
     * @return reversed path
     */
    private static <V> LinkedList<V> revPath(LinkedList<V> path) {

        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }

    //US19
    /**
     * Topological Sort Algorithm
     * @param <V> The type of vertices in the graph.
     * @param <E> The type of edges in the graph.
     * @param g The directed graph on which to perform the topological sort.
     * @param topsort A list of vertices in topological order if the graph is acyclic.
     *        Otherwise, the list may be incomplete, and `isCycle` will be false.
     * @return A boolean to indicate whether the graph contains a cycle or not.
     *                If the graph contains a cycle, the topological sort cannot be performed.
     *
     *     Complexity:
     *     Time Complexity: O(V + E), where V is the number of vertices and E is the number of edges in the graph.
     *     This is because:
     *     1. Initializing the in-degree map takes O(V).
     *     2. Processing each vertex in the queue takes O(V).
     *     3. Iterating over all edges during adjacency list traversal takes O(E).
     */
    public static<V,E> boolean topSort(Graph<V,E> g, List<V> topsort){

        Map<V, Integer> degreeIn = new HashMap();
        Queue<V> queueAux = new LinkedList<V>();

        /*
         * for (all V vertices in g) {
         * degreeIn[idxV] = g.inDegree(V)
         * if (degreeIn[idxV] == 0) Add vertex V to queue-aux }
         */
        for (V vertice : g.vertices()) {
            degreeIn.put(vertice, g.inDegree(vertice));
            if (g.inDegree(vertice) == 0) {
                queueAux.add(vertice);
            }
        }

        /*
         * numVerts=0
         */
        int numVerts = 0;

        /*
         * while (!queue-aux is Empty){
         *     vOrig f remove first vertex from queue-aux
         *     add vOrig to topsort
         *     numVerts++
         *     for (each vAdj of vOrig){
         *         degreeIn[vAdj]--
         *         if (degreeIn[vAdj] == 0) add vertex VAdj to queue-aux
         *     }
         * }
         * */
        while (!queueAux.isEmpty()) {
            V vOrig = queueAux.poll();
            topsort.add(vOrig);
            numVerts++;
            for (V vAdj : g.adjVertices(vOrig)) {
                degreeIn.put(vAdj, degreeIn.get(vAdj) - 1);
                if (degreeIn.get(vAdj) == 0)
                    queueAux.add(vAdj);
            }

        }

        /*
         * if (numVerts < g.numVertices()) //Graph has a cycle
         * return false
         * return true
         */
        if (numVerts < g.numVertices()) {
            return false;
        }
        return true;
    }


    //US20
    /**
     * Calculate Earliest and Latest Start and Finish Times for all activities in a directed graph.
     * @param <V> The type of vertices (activities) in the graph.
     * @param <E> The type of edges (dependencies) in the graph.
     * @param g The directed graph representing the project.
     * @param durations A map of vertices to their respective durations.
     * @return A map containing ES, EF, LS, LF, and Slack for each activity.
     * Complexity:
     *  Time Complexity: O(V + E), where V is the number of vertices and E is the number of edges in the graph.
     *  This is because:
     *  1. Topological sorting takes O(V + E).
     *  2. Calculating Earliest Start and Finish times takes O(V + E).
     *  3. Calculating Latest Start and Finish times takes O(V + E).
     *  4. Constructing the result map takes O(V).
     */
    public static <V, E> Map<V, Map<String, Integer>> calculateTimes(Graph<V, E> g, Map<V, Integer> durations) {

        Map<V, Map<String, Integer>> result = new HashMap<>();


        List<V> topOrder = new ArrayList<V>();
        boolean isCycle = topSort(g, topOrder);


        Map<V, Integer> ES = new HashMap<>();
        Map<V, Integer> EF = new HashMap<>();
        for (V v : g.vertices()) {
            ES.put(v, 0);
            EF.put(v, 0);
        }


        for (V v : topOrder) {
            int maxEF = 0;
            for (V pred : g.incomingVertices(v)) {
                maxEF = Math.max(maxEF, EF.get(pred));
            }
            ES.put(v, maxEF);
            EF.put(v, maxEF + durations.get(v));
        }


        Map<V, Integer> LS = new HashMap<>();
        Map<V, Integer> LF = new HashMap<>();
        int maxEF = topOrder.stream().mapToInt(EF::get).max().orElse(0);

        for (V v : g.vertices()) {
            LF.put(v, maxEF);
            LS.put(v, maxEF);
        }


        List<V> reverseOrder = new ArrayList<>(topOrder);
        Collections.reverse(reverseOrder);

        for (V v : reverseOrder) {
            int minLS = maxEF;
            for (V succ : g.adjVertices(v)) {
                minLS = Math.min(minLS, LS.get(succ));
            }
            LF.put(v, minLS);
            LS.put(v, minLS - durations.get(v));
        }


        for (V v : g.vertices()) {
            int slack = LS.get(v) - ES.get(v);
            Map<String, Integer> times = new HashMap<>();
            times.put("ES", ES.get(v));
            times.put("EF", EF.get(v));
            times.put("LS", LS.get(v));
            times.put("LF", LF.get(v));
            times.put("Slack", slack);
            result.put(v, times);
        }

        return result;
    }
}