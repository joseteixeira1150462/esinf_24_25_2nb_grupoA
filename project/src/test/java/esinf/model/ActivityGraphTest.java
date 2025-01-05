package esinf.model;


import esinf.utils.adjacencymapgraph.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ActivityGraphTest {
    private ActivityGraph graph;
    private ActivityVertex activity1;
    private ActivityVertex activity2;
    private ActivityVertex activity3;

    @BeforeEach
    void setUp() {
        graph = new ActivityGraph();

        activity1 = new ActivityVertex("A", "Start", 2, "hours", 100, "USD");
        activity2 = new ActivityVertex("B", "Intermediate", 3, "hours", 150, "USD");
        activity3 = new ActivityVertex("C", "End", 1, "hour", 50, "USD");
    }

    @Test
    void testAddActivity() {
        graph.addActivity(activity1);
        graph.addActivity(activity2);

        assertEquals(2, graph.numVertices(), "Graph should have 2 vertices after adding 2 activities.");

        boolean containsActivity1 = false;
        boolean containsActivity2 = false;
        for (ActivityVertex vertex : graph.vertices()) {
            if (vertex.equals(activity1)) containsActivity1 = true;
            if (vertex.equals(activity2)) containsActivity2 = true;
        }

        assertTrue("Graph should contain activity1.",containsActivity1);
        assertTrue("Graph should contain activity2.",containsActivity2);
    }

    @Test
    void testAddDependency() {
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addDependency(activity1, activity2, "Start to Intermediate");

        assertEquals(1, graph.numEdges(), "Graph should have 1 edge after adding a dependency.");

        boolean dependencyFound = false;
        for (Edge edge : graph.outgoingEdges(activity1)) {
            if (edge.getVDest().equals(activity2) && edge.getElement().equals("Start to Intermediate")) {
                dependencyFound = true;
                break;
            }
        }

        assertTrue("Graph should have an edge from activity1 to activity2 with correct label.",dependencyFound);
    }

    @Test
    void testToString() {
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addActivity(activity3);
        graph.addDependency(activity1, activity2, "Start to Intermediate");
        graph.addDependency(activity2, activity3, "Intermediate to End");

        String output = graph.toString();

        assertTrue(String.valueOf(output.contains("Activity [ID: 1, Description: Start")), true);
        assertTrue(String.valueOf(output.contains("<- Dependency Activity [ID: 1")), true);
        assertTrue(String.valueOf(output.contains("-> Dependency Activity [ID: 3")), true);
    }

    @Test
    void testGraphDetails() {
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addDependency(activity1, activity2, "Start to Intermediate");

        graph.graphDetails();

        assertEquals(2, graph.numVertices(), "Graph should have 2 vertices.");
        assertEquals(1, graph.numEdges(), "Graph should have 1 edge.");
    }
}
