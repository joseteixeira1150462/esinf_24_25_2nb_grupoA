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
    * @param voInf information of the Vertex origin
    * @param vdInf information of the Vertex destination 
    * @param pathkeys minimum path vertices keys  
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
}