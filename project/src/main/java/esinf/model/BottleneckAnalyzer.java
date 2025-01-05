//USEI22
package esinf.model;
import esinf.utils.adjacencymapgraph.Edge;
import java.util.*;

/**
 * Classe para identificar atividades gargalo no grafo do projeto.
 *
 * As atividades gargalo são aquelas com o maior número de atividades dependentes
 * (out-degree) ou que aparecem no maior número de caminhos entre o início e o fim.
 */
public class BottleneckAnalyzer {

    private final ActivityGraph graph;

    /**
     * Construtor que inicializa o analisador com o grafo do projeto.
     * @param graph Grafo das atividades do projeto.
     */
    public BottleneckAnalyzer(ActivityGraph graph) {
        this.graph = graph;
    }

    /**
     * Identifica as atividades gargalo com base no maior número de dependentes.
     * @return Lista de atividades com o maior número de dependentes.
     */
    public List<ActivityVertex> identifyByDependents() {
        int maxDependents = 0;
        Map<ActivityVertex, Integer> dependentCounts = new HashMap<>();

        // Itera por todas as atividades no grafo para contar os dependentes
        for (ActivityVertex vertex : graph.vertices()) {
            int outDegree = graph.outDegree(vertex);
            dependentCounts.put(vertex, outDegree);
            maxDependents = Math.max(maxDependents, outDegree);
        }

        // Retorna atividades com o número máximo de dependentes
        List<ActivityVertex> bottlenecks = new ArrayList<>();
        for (Map.Entry<ActivityVertex, Integer> entry : dependentCounts.entrySet()) {
            if (entry.getValue() == maxDependents) {
                bottlenecks.add(entry.getKey());
            }
        }
        return bottlenecks;
    }

    /**
     * Identifica as atividades gargalo com base na frequência em caminhos.
     * @return Lista de atividades que aparecem no maior número de caminhos.
     */
    public List<ActivityVertex> identifyByPathFrequency() {
        // Calcula a frequência de cada atividade em todos os caminhos
        Map<ActivityVertex, Integer> pathFrequency = new HashMap<>();
        ActivityVertex start = findStartVertex();
        ActivityVertex end = findEndVertex();

        if (start == null || end == null) {
            throw new IllegalStateException("Grafo inválido: deve ter pelo menos um início e um fim.");
        }

        // Usa DFS para contar frequências
        findPathsDFS(start, end, new HashSet<>(), new ArrayList<>(), pathFrequency);

        // Encontra a frequência máxima
        int maxFrequency = 0;
        for (int frequency : pathFrequency.values()) {
            maxFrequency = Math.max(maxFrequency, frequency);
        }

        // Retorna atividades com a frequência máxima
        List<ActivityVertex> bottlenecks = new ArrayList<>();
        for (Map.Entry<ActivityVertex, Integer> entry : pathFrequency.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                bottlenecks.add(entry.getKey());
            }
        }
        return bottlenecks;
    }

    /**
     * Método auxiliar para encontrar o vértice inicial (sem predecessores).
     * @return Vértice inicial do grafo.
     */
    private ActivityVertex findStartVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.inDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    /**
     * Método auxiliar para encontrar o vértice final (sem sucessores).
     * @return Vértice final do grafo.
     */
    private ActivityVertex findEndVertex() {
        for (ActivityVertex vertex : graph.vertices()) {
            if (graph.outDegree(vertex) == 0) return vertex;
        }
        return null;
    }

    /**
     * Método auxiliar para realizar DFS e calcular a frequência de cada atividade em caminhos.
     * @param current Atividade atual.
     * @param end Vértice final.
     * @param visited Conjunto de atividades visitadas.
     * @param path Caminho atual.
     * @param pathFrequency Frequência de cada atividade.
     */
    private void findPathsDFS(ActivityVertex current, ActivityVertex end, Set<ActivityVertex> visited,
                              List<ActivityVertex> path, Map<ActivityVertex, Integer> pathFrequency) {
        visited.add(current);
        path.add(current);

        // Se chegou ao vértice final, incrementa a frequência de cada atividade no caminho
        if (current.equals(end)) {
            for (ActivityVertex vertex : path) {
                pathFrequency.put(vertex, pathFrequency.getOrDefault(vertex, 0) + 1);
            }
        } else {
            // Continua a busca para os próximos vértices
            for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(current)) {
                ActivityVertex next = edge.getVDest();
                if (!visited.contains(next)) {
                    findPathsDFS(next, end, visited, path, pathFrequency);
                }
            }
        }

        // Remove a atividade do caminho e da lista de visitados após explorar todos os caminhos
        path.remove(path.size() - 1);
        visited.remove(current);
    }

    /**
     * Retorna uma análise detalhada das atividades gargalo.
     * @return String com as atividades gargalo detalhadas.
     */
    public String getBottleneckAnalysis() {
        StringBuilder result = new StringBuilder();

        // Identifica atividades gargalo por número de dependentes
        List<ActivityVertex> byDependents = identifyByDependents();
        result.append("Atividades Gargalo (Baseadas no Número de Dependentes):\n");
        for (ActivityVertex activity : byDependents) {
            result.append(String.format("- %s (Dependentes: %d)\n", activity.getDescription(), graph.outDegree(activity)));
        }

        // Identifica atividades gargalo por frequência em caminhos
        List<ActivityVertex> byPathFrequency = identifyByPathFrequency();
        result.append("\nAtividades Gargalo (Baseadas na Frequência em Caminhos):\n");
        for (ActivityVertex activity : byPathFrequency) {
            result.append(String.format("- %s (Frequência em Caminhos: %d)\n",
                    activity.getDescription(), graph.outDegree(activity)));
        }

        return result.toString();
    }
}
