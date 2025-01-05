package esinf.model;

import esinf.utils.adjacencymapgraph.Graph;
import esinf.utils.adjacencymapgraph.GraphAlgorithms;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class ActivityGraphAlgorithmsTest {

    private Graph<String, String> projectGraph;
    private Map<String, Integer> durations;

    @Before
    public void setUp() {
        projectGraph = new Graph<>(true); // Directed graph
        durations = new HashMap<>();


        projectGraph.insertVertex("Start");
        projectGraph.insertVertex("A");
        projectGraph.insertVertex("B");
        projectGraph.insertVertex("C");
        projectGraph.insertVertex("D");
        projectGraph.insertVertex("E");
        projectGraph.insertVertex("End");


        projectGraph.insertEdge("Start", "A", "dependency", 0);
        projectGraph.insertEdge("Start", "B", "dependency", 0);
        projectGraph.insertEdge("A", "C", "dependency", 0);
        projectGraph.insertEdge("A", "E", "dependency", 0);
        projectGraph.insertEdge("C", "D", "dependency", 0);
        projectGraph.insertEdge("E", "End", "dependency", 0);
        projectGraph.insertEdge("D", "End", "dependency", 0);
        projectGraph.insertEdge("B", "End", "dependency", 0);


        durations.put("Start", 0);
        durations.put("A", 20);
        durations.put("B", 50);
        durations.put("C", 25);
        durations.put("D", 15);
        durations.put("E", 60);
        durations.put("End", 0);
    }


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

    @org.junit.Test
    public void testCalculateTimes() {
        Map<String, Map<String, Integer>> result = GraphAlgorithms.calculateTimes(projectGraph, durations);


        Map<String, Integer> expectedStart = new HashMap<>();
        expectedStart.put("ES", 0);
        expectedStart.put("EF", 0);
        expectedStart.put("LS", 0);
        expectedStart.put("LF", 0);
        expectedStart.put("Slack", 0);

        Map<String, Integer> expectedA = new HashMap<>();
        expectedA.put("ES", 0);
        expectedA.put("EF", 20);
        expectedA.put("LS", 0);
        expectedA.put("LF", 20);
        expectedA.put("Slack", 0);

        Map<String, Integer> expectedB = new HashMap<>();
        expectedB.put("ES", 0);
        expectedB.put("EF", 50);
        expectedB.put("LS", 30);
        expectedB.put("LF", 80);
        expectedB.put("Slack", 30);

        Map<String, Integer> expectedC = new HashMap<>();
        expectedC.put("ES", 20);
        expectedC.put("EF", 45);
        expectedC.put("LS", 40);
        expectedC.put("LF", 65);
        expectedC.put("Slack", 20);

        Map<String, Integer> expectedD = new HashMap<>();
        expectedD.put("ES", 45);
        expectedD.put("EF", 60);
        expectedD.put("LS", 65);
        expectedD.put("LF", 80);
        expectedD.put("Slack", 20);

        Map<String, Integer> expectedE = new HashMap<>();
        expectedE.put("ES", 20);
        expectedE.put("EF", 80);
        expectedE.put("LS", 20);
        expectedE.put("LF", 80);
        expectedE.put("Slack", 0);

        Map<String, Integer> expectedEnd = new HashMap<>();
        expectedEnd.put("ES", 80);
        expectedEnd.put("EF", 80);
        expectedEnd.put("LS", 80);
        expectedEnd.put("LF", 80);
        expectedEnd.put("Slack", 0);

        assertEquals(expectedStart, result.get("Start"));
        assertEquals(expectedA, result.get("A"));
        assertEquals(expectedB, result.get("B"));
        assertEquals(expectedC, result.get("C"));
        assertEquals(expectedD, result.get("D"));
        assertEquals(expectedE, result.get("E"));
        assertEquals(expectedEnd, result.get("End"));
    }

}