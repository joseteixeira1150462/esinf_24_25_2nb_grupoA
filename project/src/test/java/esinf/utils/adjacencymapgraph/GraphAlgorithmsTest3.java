package esinf.utils.adjacencymapgraph;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class GraphAlgorithmsTest3 {

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