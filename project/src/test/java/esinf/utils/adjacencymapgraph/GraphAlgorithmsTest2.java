package esinf.utils.adjacencymapgraph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest2 {
    @Test
    public void testTopSortAcyclicGraph() {
        Graph<String, Integer> graph = new Graph<>(true);
        graph.insertVertex("A");
        graph.insertVertex("B");
        graph.insertVertex("C");
        graph.insertVertex("D");
        graph.insertEdge("A", "B", 1, 0.0);
        graph.insertEdge("A", "C", 1, 0.0);
        graph.insertEdge("B", "D", 1, 0.0);
        graph.insertEdge("C", "D", 1, 0.0);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertTrue(isCycle);
        assertEquals(4, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
        assertEquals("C", result.get(2));
        assertEquals("D", result.get(3));
    }

    @Test
    public void testTopSortCyclicGraph() {
        Graph<String, Integer> graph = new Graph<>(true);
        graph.insertVertex("A");
        graph.insertVertex("B");
        graph.insertVertex("C");
        graph.insertEdge("A", "B", 1, 0.0);
        graph.insertEdge("B", "C", 1, 0.0);
        graph.insertEdge("C", "A", 1, 0.0);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertFalse(isCycle);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTopSortEmptyGraph() {
        Graph<String, Integer> graph = new Graph<>(true);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertTrue(isCycle);
        assertTrue(result.isEmpty());
    }
    @Test
    public void testTopSortAcyclicGraphXYZ() {
        Graph<String, Integer> graph = new Graph<>(true);
        graph.insertVertex("X");
        graph.insertVertex("Y");
        graph.insertVertex("Z");
        graph.insertVertex("W");
        graph.insertEdge("X", "Y", 1, 0.0);
        graph.insertEdge("X", "Z", 1, 0.0);
        graph.insertEdge("Y", "W", 1, 0.0);
        graph.insertEdge("Z", "W", 1, 0.0);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertTrue(isCycle);
        assertEquals(4, result.size());
        assertEquals("X", result.get(0));
        assertEquals("Y", result.get(1));
        assertEquals("Z", result.get(2));
        assertEquals("W", result.get(3));
    }

    @Test
    public void testTopSortAcyclicGraphEFGH() {
        Graph<String, Integer> graph = new Graph<>(true);
        graph.insertVertex("E");
        graph.insertVertex("F");
        graph.insertVertex("G");
        graph.insertVertex("H");
        graph.insertEdge("E", "F", 1, 0.0);
        graph.insertEdge("E", "G", 1, 0.0);
        graph.insertEdge("F", "H", 1, 0.0);
        graph.insertEdge("G", "H", 1, 0.0);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertTrue(isCycle);
        assertEquals(4, result.size());
        assertEquals("E", result.get(0));
        assertEquals("F", result.get(1));
        assertEquals("G", result.get(2));
        assertEquals("H", result.get(3));
    }

    @Test
    public void testTopSortAcyclicGraphIJKL() {
        Graph<String, Integer> graph = new Graph<>(true);
        graph.insertVertex("I");
        graph.insertVertex("J");
        graph.insertVertex("K");
        graph.insertVertex("L");
        graph.insertEdge("I", "J", 1, 0.0);
        graph.insertEdge("I", "K", 1, 0.0);
        graph.insertEdge("J", "L", 1, 0.0);
        graph.insertEdge("K", "L", 1, 0.0);

        List<String> result = new ArrayList<>();
        boolean isCycle = GraphAlgorithms.topSort(graph, result);

        assertTrue(isCycle);
        assertEquals(4, result.size());
        assertEquals("I", result.get(0));
        assertEquals("J", result.get(1));
        assertEquals("K", result.get(2));
        assertEquals("L", result.get(3));
    }

}