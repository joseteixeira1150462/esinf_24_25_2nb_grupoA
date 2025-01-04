package esinf.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class CriticalPathAnalyzerTest {

    private ActivityGraph graph;
    private CriticalPathAnalyzer analyzer;

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

        // Initialize the CriticalPathAnalyzer
        analyzer = new CriticalPathAnalyzer(graph);
    }

    @Test
    void testCalculateCriticalPath() {
        // Calculate the critical path
        analyzer.calculateCriticalPath();

        // Retrieve metrics for each activity
        Map<ActivityVertex, CriticalPathAnalyzer.ActivityMetrics> metrics = analyzer.getMetrics();

        // Assert that the critical path exists
        List<ActivityVertex> criticalPath = analyzer.getCriticalPath();
        assertNotNull(criticalPath);
        assertFalse(criticalPath.isEmpty());

        // Verify the critical path is correct (in this case, A1 -> A2 -> A3 -> A4)
        assertEquals(4, criticalPath.size());
        assertEquals("Activity 1", criticalPath.get(0).getDescription());
        assertEquals("Activity 2", criticalPath.get(1).getDescription());
        assertEquals("Activity 3", criticalPath.get(2).getDescription());
        assertEquals("Activity 4", criticalPath.get(3).getDescription());

        // Check for slack for critical path activities
        for (ActivityVertex activity : criticalPath) {
            CriticalPathAnalyzer.ActivityMetrics m = metrics.get(activity);
            assertEquals(0, m.slack);  // Critical path activities should have no slack
        }
    }

    @Test
    void testGetTotalProjectDuration() {
        // Calculate the critical path and project duration
        analyzer.calculateCriticalPath();

        // Get total project duration
        int totalDuration = analyzer.getTotalProjectDuration();
        assertEquals(14, totalDuration);  // The total duration should be 14 (5 + 3 + 4 + 2)
    }

    @Test
    void testDetailedCriticalPathAnalysis() {
        // Calculate the critical path
        analyzer.calculateCriticalPath();

        // Retrieve the detailed critical path analysis
        String analysis = analyzer.getDetailedCriticalPathAnalysis();
        assertNotNull(analysis);
        assertTrue(analysis.contains("Critical Path Analysis"));
        assertTrue(analysis.contains("Activities:"));
        assertTrue(analysis.contains("Total Project Duration"));
    }

    @Test
    void testEarlyAndLateStartTimes() {
        // Calculate the critical path
        analyzer.calculateCriticalPath();

        // Retrieve metrics for each activity
        Map<ActivityVertex, CriticalPathAnalyzer.ActivityMetrics> metrics = analyzer.getMetrics();

        // Verify the early and late start times for each activity
        CriticalPathAnalyzer.ActivityMetrics activity1Metrics = metrics.get(new ActivityVertex("A1", "Activity 1", 5, "days", 100.0, "USD"));
        CriticalPathAnalyzer.ActivityMetrics activity2Metrics = metrics.get(new ActivityVertex("A2", "Activity 2", 3, "days", 50.0, "USD"));
        CriticalPathAnalyzer.ActivityMetrics activity3Metrics = metrics.get(new ActivityVertex("A3", "Activity 3", 4, "days", 75.0, "USD"));
        CriticalPathAnalyzer.ActivityMetrics activity4Metrics = metrics.get(new ActivityVertex("A4", "Activity 4", 2, "days", 30.0, "USD"));

        // Activity 1 starts at 0 and finishes at 5
        assertEquals(0, activity1Metrics.earliestStart);
        assertEquals(5, activity1Metrics.earliestFinish);
        assertEquals(0, activity1Metrics.latestStart);
        assertEquals(5, activity1Metrics.latestFinish);

        // Activity 2 starts at 5 and finishes at 8
        assertEquals(5, activity2Metrics.earliestStart);
        assertEquals(8, activity2Metrics.earliestFinish);
        assertEquals(5, activity2Metrics.latestStart);
        assertEquals(8, activity2Metrics.latestFinish);

        // Activity 3 starts at 8 and finishes at 12
        assertEquals(8, activity3Metrics.earliestStart);
        assertEquals(12, activity3Metrics.earliestFinish);
        assertEquals(8, activity3Metrics.latestStart);
        assertEquals(12, activity3Metrics.latestFinish);

        // Activity 4 starts at 12 and finishes at 14
        assertEquals(12, activity4Metrics.earliestStart);
        assertEquals(14, activity4Metrics.earliestFinish);
        assertEquals(12, activity4Metrics.latestStart);
        assertEquals(14, activity4Metrics.latestFinish);
    }
}
