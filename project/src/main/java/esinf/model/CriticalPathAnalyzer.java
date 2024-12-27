package esinf.model;

import esinf.utils.adjacencymapgraph.Edge;
import java.util.*;

public class CriticalPathAnalyzer {

    public static class ActivityMetrics {
        public int earliestStart;
        public int earliestFinish;
        public int latestStart;
        public int latestFinish;
        public int slack;

        public ActivityMetrics() {
            this.earliestStart = 0;
            this.earliestFinish = 0;
            this.latestStart = Integer.MAX_VALUE;
            this.latestFinish = Integer.MAX_VALUE;
            this.slack = 0;
        }
    }

    private final ActivityGraph graph;
    private final Map<ActivityVertex, ActivityMetrics> metrics = new HashMap<>();

    public CriticalPathAnalyzer(ActivityGraph graph) {
        this.graph = graph;
        initializeMetrics();
    }

    private void initializeMetrics() {
        for (ActivityVertex vertex : graph.vertices()) {
            metrics.put(vertex, new ActivityMetrics());
        }
    }

    public void calculateCriticalPath() {
        List<ActivityVertex> topologicalOrder = topologicalSort();
        forwardPass(topologicalOrder);
        backwardPass(topologicalOrder);
        calculateSlack();
        validateCompleteCriticalPath();
    }

    private List<ActivityVertex> topologicalSort() {
        List<ActivityVertex> order = new ArrayList<>();
        Map<ActivityVertex, Integer> inDegree = new HashMap<>();

        for (ActivityVertex vertex : graph.vertices()) {
            List<Edge<ActivityVertex, String>> incomingEdges = new ArrayList<>();
            for (Edge<ActivityVertex, String> edge : graph.incomingEdges(vertex)) {
                incomingEdges.add(edge);
            }
            inDegree.put(vertex, incomingEdges.size());
        }

        Queue<ActivityVertex> queue = new LinkedList<>();
        for (Map.Entry<ActivityVertex, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        while (!queue.isEmpty()) {
            ActivityVertex current = queue.poll();
            order.add(current);

            for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(current)) {
                ActivityVertex neighbor = edge.getVDest();
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }

        if (order.size() != graph.numVertices()) {
            throw new IllegalStateException("Graph contains a cycle");
        }
        return order;
    }

    private void forwardPass(List<ActivityVertex> topologicalOrder) {
        for (ActivityVertex vertex : topologicalOrder) {
            ActivityMetrics currentMetrics = metrics.get(vertex);

            for (Edge<ActivityVertex, String> edge : graph.incomingEdges(vertex)) {
                ActivityVertex predecessor = edge.getVOrig();
                int finishTime = metrics.get(predecessor).earliestFinish;
                currentMetrics.earliestStart = Math.max(currentMetrics.earliestStart, finishTime);
            }
            currentMetrics.earliestFinish = currentMetrics.earliestStart + vertex.getDuration();
        }
    }

    private void backwardPass(List<ActivityVertex> topologicalOrder) {
        Collections.reverse(topologicalOrder);

        for (ActivityVertex vertex : topologicalOrder) {
            if (graph.outDegree(vertex) == 0) {
                ActivityMetrics m = metrics.get(vertex);
                m.latestFinish = m.earliestFinish;
                m.latestStart = m.latestFinish - vertex.getDuration();
            }
        }

        for (ActivityVertex vertex : topologicalOrder) {
            ActivityMetrics currentMetrics = metrics.get(vertex);

            if (graph.outDegree(vertex) > 0) {
                currentMetrics.latestFinish = Integer.MAX_VALUE;
                for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(vertex)) {
                    ActivityVertex successor = edge.getVDest();
                    currentMetrics.latestFinish = Math.min(currentMetrics.latestFinish,
                            metrics.get(successor).latestStart);
                }
                currentMetrics.latestStart = currentMetrics.latestFinish - vertex.getDuration();
            }
        }
    }

    private void calculateSlack() {
        for (Map.Entry<ActivityVertex, ActivityMetrics> entry : metrics.entrySet()) {
            ActivityMetrics m = entry.getValue();
            m.slack = m.latestStart - m.earliestStart;
        }
    }

    private boolean validateCompleteCriticalPath() {
        List<ActivityVertex> criticalPath = getCriticalPath();
        if (criticalPath.isEmpty()) {
            throw new IllegalStateException("No critical path found");
        }

        ActivityVertex start = findStartVertex();
        ActivityVertex end = findEndVertex();

        if (!criticalPath.get(0).equals(start)) {
            throw new IllegalStateException("Critical path does not start at the project start");
        }

        if (!criticalPath.get(criticalPath.size() - 1).equals(end)) {
            throw new IllegalStateException("Critical path does not end at the project end");
        }

        return true;
    }

    public List<ActivityVertex> getCriticalPath() {
        List<ActivityVertex> criticalPath = new ArrayList<>();
        ActivityVertex start = findStartVertex();
        if (start == null) return criticalPath;

        Set<ActivityVertex> visited = new HashSet<>();
        findCriticalPathDFS(start, criticalPath, visited, new ArrayList<>());
        return criticalPath;
    }

    private ActivityVertex findStartVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.inDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    private ActivityVertex findEndVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.outDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    private void findCriticalPathDFS(ActivityVertex current, List<ActivityVertex> result,
                                     Set<ActivityVertex> visited, List<ActivityVertex> currentPath) {
        visited.add(current);
        currentPath.add(current);

        if (graph.outDegree(current) == 0) {
            if (result.isEmpty() || currentPath.size() > result.size()) {
                result.clear();
                result.addAll(currentPath);
            }
        } else {
            for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(current)) {
                ActivityVertex next = edge.getVDest();
                if (!visited.contains(next) && metrics.get(next).slack == 0) {
                    findCriticalPathDFS(next, result, visited, currentPath);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);
        visited.remove(current);
    }

    public String getDetailedCriticalPathAnalysis() {
        StringBuilder result = new StringBuilder();

        result.append("Critical Path Analysis:\n");
        result.append("=====================\n\n");

        result.append("Activities:\n");
        int index = 1;
        for (ActivityVertex activity : graph.vertices()) {
            ActivityMetrics m = metrics.get(activity);
            result.append(String.format("%d. Activity: %s\n", index++, activity.getDescription()));
            result.append(String.format("   Duration: %d\n", activity.getDuration()));
            result.append(String.format("   Earliest Start: %d\n", m.earliestStart));
            result.append(String.format("   Earliest Finish: %d\n", m.earliestFinish));
            result.append(String.format("   Latest Start: %d\n", m.latestStart));
            result.append(String.format("   Latest Finish: %d\n", m.latestFinish));
            result.append(String.format("   Slack: %d\n\n", m.slack));
        }

        result.append("Total Project Duration: ").append(getTotalProjectDuration()).append(" units\n");
        return result.toString();
    }

    public int getTotalProjectDuration() {
        int maxFinish = 0;
        for (ActivityMetrics metrics : metrics.values()) {
            if (metrics.earliestFinish > maxFinish) {
                maxFinish = metrics.earliestFinish;
            }
        }
        return maxFinish;
    }

    public Map<ActivityVertex, ActivityMetrics> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }
}
