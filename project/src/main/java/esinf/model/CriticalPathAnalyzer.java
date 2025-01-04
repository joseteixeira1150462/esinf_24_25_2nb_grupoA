package esinf.model;

import esinf.utils.adjacencymapgraph.Edge;
import java.util.*;

//USEI22
/**
 * Classe responsável por analisar o caminho crítico do projeto no grafo das atividades.
 * O caminho crítico é composto pelas atividades que não possuem folga (slack) e que
 * determinam a duração mínima do projeto.
 */
public class CriticalPathAnalyzer {

    /**
     * Classe que armazena as métricas de cada atividade no grafo.
     * Contém informações sobre os tempos mais cedo e mais tarde, além da folga.
     */
    public static class ActivityMetrics {
        public int earliestStart;  // Início mais cedo da atividade
        public int earliestFinish; // Fim mais cedo da atividade
        public int latestStart;    // Início mais tarde permitido
        public int latestFinish;   // Fim mais tarde permitido
        public int slack;          // Folga (tempo disponível sem atrasar o projeto)

        /**
         * Construtor que inicializa as métricas com valores padrão.
         * Define o início mais cedo e fim mais cedo como 0,
         * o início e fim mais tarde como valores máximos possíveis,
         * e a folga como 0.
         */
        public ActivityMetrics() {
            this.earliestStart = 0;
            this.earliestFinish = 0;
            this.latestStart = Integer.MAX_VALUE;
            this.latestFinish = Integer.MAX_VALUE;
            this.slack = 0;
        }
    }

    private final ActivityGraph graph; // Grafo que representa as atividades do projeto
    private final Map<ActivityVertex, ActivityMetrics> metrics = new HashMap<>();

    /**
     * Construtor que recebe o grafo do projeto e inicializa as métricas das atividades.
     * @param graph Grafo das atividades do projeto.
     */
    public CriticalPathAnalyzer(ActivityGraph graph) {
        this.graph = graph;
        initializeMetrics();
    }

    /**
     * Inicializa as métricas para cada atividade no grafo.
     */
    private void initializeMetrics() {
        for (ActivityVertex vertex : graph.vertices()) {
            metrics.put(vertex, new ActivityMetrics());
        }
    }

    /**
     * Método principal que calcula o caminho crítico do projeto.
     * Realiza a ordenação topológica, o cálculo dos tempos mais cedo e mais tarde,
     * calcula a folga para cada atividade e valida o caminho crítico.
     */
    public void calculateCriticalPath() {
        List<ActivityVertex> topologicalOrder = topologicalSort(); // Ordenação topológica do grafo
        forwardPass(topologicalOrder);  // Calcula os tempos mais cedo (forward pass)
        backwardPass(topologicalOrder); // Calcula os tempos mais tarde (backward pass)
        calculateSlack();  // Calcula a folga para cada atividade
        validateCompleteCriticalPath(); // Valida o caminho crítico completo
    }

    /**
     * Ordenação topológica para processar atividades na ordem dos dependentes.
     * @return Lista das atividades ordenadas topologicamente.
     */
    private List<ActivityVertex> topologicalSort() {
        List<ActivityVertex> order = new ArrayList<>();
        Map<ActivityVertex, Integer> inDegree = new HashMap<>();

        // Calcula o grau de entrada (número de dependências) para cada atividade
        for (ActivityVertex vertex : graph.vertices()) {
            List<Edge<ActivityVertex, String>> incomingEdges = new ArrayList<>();
            for (Edge<ActivityVertex, String> edge : graph.incomingEdges(vertex)) {
                incomingEdges.add(edge);
            }
            inDegree.put(vertex, incomingEdges.size());
        }

        // Adiciona atividades sem dependências a uma fila
        Queue<ActivityVertex> queue = new LinkedList<>();
        for (Map.Entry<ActivityVertex, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        // Processa as atividades na ordem topológica
        while (!queue.isEmpty()) {
            ActivityVertex current = queue.poll();
            order.add(current);

            for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(current)) {
                ActivityVertex neighbor = edge.getVDest();
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }

        // Verifica se o grafo tem ciclos
        if (order.size() != graph.numVertices()) {
            throw new IllegalStateException("Graph contains a cycle");
        }
        return order;
    }

    /**
     * Calcula os tempos mais cedo para cada atividade, baseando-se nos predecessores.
     * @param topologicalOrder Lista de atividades ordenadas topologicamente.
     */
    private void forwardPass(List<ActivityVertex> topologicalOrder) {
        for (ActivityVertex vertex : topologicalOrder) {
            ActivityMetrics currentMetrics = metrics.get(vertex);

            // Atualiza o início mais cedo com base nos predecessores
            for (Edge<ActivityVertex, String> edge : graph.incomingEdges(vertex)) {
                ActivityVertex predecessor = edge.getVOrig();
                int finishTime = metrics.get(predecessor).earliestFinish;
                currentMetrics.earliestStart = Math.max(currentMetrics.earliestStart, finishTime);
            }
            currentMetrics.earliestFinish = currentMetrics.earliestStart + vertex.getDuration();
        }
    }

    /**
     * Calcula os tempos mais tarde para cada atividade, considerando os sucessores.
     * @param topologicalOrder Lista de atividades ordenadas topologicamente.
     */
    private void backwardPass(List<ActivityVertex> topologicalOrder) {
        Collections.reverse(topologicalOrder);

        // Inicializa os tempos mais tarde para as atividades finais
        for (ActivityVertex vertex : topologicalOrder) {
            if (graph.outDegree(vertex) == 0) {
                ActivityMetrics m = metrics.get(vertex);
                m.latestFinish = m.earliestFinish;
                m.latestStart = m.latestFinish - vertex.getDuration();
            }
        }

        // Atualiza os tempos mais tarde para atividades com sucessores
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

    /**
     * Calcula a folga (slack) para cada atividade.
     * A folga é a diferença entre o tempo mais tarde de início e o tempo mais cedo de início.
     */
    private void calculateSlack() {
        for (Map.Entry<ActivityVertex, ActivityMetrics> entry : metrics.entrySet()) {
            ActivityMetrics m = entry.getValue();
            m.slack = m.latestStart - m.earliestStart;
        }
    }

    /**
     * Valida se o caminho crítico é completo e começa/termina corretamente.
     * @return True se o caminho crítico for válido, false caso contrário.
     * @throws IllegalStateException Se o caminho crítico não começar ou terminar corretamente.
     */
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

    /**
     * Retorna o caminho crítico (atividades com folga = 0).
     * @return Lista de atividades no caminho crítico.
     */
    public List<ActivityVertex> getCriticalPath() {
        List<ActivityVertex> criticalPath = new ArrayList<>();
        ActivityVertex start = findStartVertex();
        if (start == null) return criticalPath;

        Set<ActivityVertex> visited = new HashSet<>();
        findCriticalPathDFS(start, criticalPath, visited, new ArrayList<>());
        return criticalPath;
    }

    /**
     * Encontra o vértice inicial do grafo (sem predecessores).
     * @return Vértice inicial do grafo.
     */
    private ActivityVertex findStartVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.inDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    /**
     * Encontra o vértice final do grafo (sem sucessores).
     * @return Vértice final do grafo.
     */
    private ActivityVertex findEndVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.outDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    /**
     * Busca em profundidade para encontrar o caminho crítico, baseado na folga.
     * @param current Atividade atual no caminho.
     * @param result Lista de atividades no caminho crítico.
     * @param visited Conjunto de atividades visitadas.
     * @param currentPath Caminho atual sendo explorado.
     */
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

    /**
     * Retorna uma análise detalhada das atividades no caminho crítico.
     * @return String com a análise detalhada.
     */
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

    /**
     * Calcula a duração total do projeto, baseada no maior tempo de término das atividades.
     * @return Duração total do projeto em unidades de tempo.
     */
    public int getTotalProjectDuration() {
        int maxFinish = 0;
        for (ActivityMetrics metrics : metrics.values()) {
            if (metrics.earliestFinish > maxFinish) {
                maxFinish = metrics.earliestFinish;
            }
        }
        return maxFinish;
    }

    /**
     * Retorna as métricas de todas as atividades.
     * @return Mapa imutável com as métricas de todas as atividades.
     */
    public Map<ActivityVertex, ActivityMetrics> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }
}
