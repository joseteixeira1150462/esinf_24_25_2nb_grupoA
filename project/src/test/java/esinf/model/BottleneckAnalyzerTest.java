package esinf.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class BottleneckAnalyzerTest {

    private ActivityGraph graph;
    private BottleneckAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        // Initialize the ActivityGraph (this assumes it's set up correctly)
        graph = new ActivityGraph();

        // Create some example ActivityVertices
        ActivityVertex activity1 = new ActivityVertex("A1", "Activity 1", 5, "days", 100.0, "USD");
        ActivityVertex activity2 = new ActivityVertex("A2", "Activity 2", 3, "days", 50.0, "USD");
        ActivityVertex activity3 = new ActivityVertex("A3", "Activity 3", 4, "days", 75.0, "USD");
        ActivityVertex activity4 = new ActivityVertex("A4", "Activity 4", 2, "days", 30.0, "USD");

        // Add activities to the graph
        graph.addActivity(activity1);
        graph.addActivity(activity2);
        graph.addActivity(activity3);
        graph.addActivity(activity4);

        // Add dependencies between activities
        graph.addDependency(activity1, activity2, "Dependency 1");
        graph.addDependency(activity2, activity3, "Dependency 2");
        graph.addDependency(activity3, activity4, "Dependency 3");

        // Initialize the BottleneckAnalyzer
        analyzer = new BottleneckAnalyzer(graph);
    }

    @Test
    void testIdentifyByDependents() {
        // Identify bottleneck activities based on the number of dependents (out-degree)
        List<ActivityVertex> bottlenecks = analyzer.identifyByDependents();

        // Check that the bottlenecks list is not null
        assertNotNull(bottlenecks);

        // Expect Activity 2 and Activity 3 to be bottlenecks, as they each have 1 dependent activity
        assertTrue(bottlenecks.contains(new ActivityVertex("A2", "Activity 2", 3, "days", 50.0, "USD")));
        assertTrue(bottlenecks.contains(new ActivityVertex("A3", "Activity 3", 4, "days", 75.0, "USD")));
    }

    @Test
    void testIdentifyByPathFrequency() {
        // Identify bottleneck activities based on the frequency in paths
        List<ActivityVertex> bottlenecks = analyzer.identifyByPathFrequency();

        // Check that the bottlenecks list is not null
        assertNotNull(bottlenecks);

        // Expect Activity 2 and Activity 3 to be bottlenecks, as they appear in all paths
        assertTrue(bottlenecks.contains(new ActivityVertex("A2", "Activity 2", 3, "days", 50.0, "USD")));
        assertTrue(bottlenecks.contains(new ActivityVertex("A3", "Activity 3", 4, "days", 75.0, "USD")));
    }

    @Test
    void testGetBottleneckAnalysis() {
        // Perform the analysis
        String analysis = analyzer.getBottleneckAnalysis();

        // Check that the analysis contains the expected details
        assertNotNull(analysis);
        assertTrue(analysis.contains("Atividades Gargalo"));
        assertTrue(analysis.contains("Baseadas no Número de Dependentes"));
        assertTrue(analysis.contains("Baseadas na Frequência em Caminhos"));
    }
}
