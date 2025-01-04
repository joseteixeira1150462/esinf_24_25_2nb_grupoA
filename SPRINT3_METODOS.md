## Métodos Implementados por cada elemento

### **USEI17 - Construção de um Grafo PERT-CPM**
Como utilizador, pretendo importar dados de um arquivo CSV e criar um grafo PERT-CPM. Para atender a esse requisito, as seguintes implementações foram realizadas:

#### **Estrutura do Ficheiro `activities.csv`**
```txt
<act_id, act_descr, duration, duration_unit, cost, cost_unit, prev_act_id1, ..., prev_act_idN>
```

#### **Packages Utilizados**
- **`adjencymapgraph`** e **`adjencymatrixgraph`** foram adicionados ao projeto. Eles servem como base para a construção do grafo de atividades, garantindo que a estrutura atenda às necessidades do sistema.

#### **Classes Implementadas**
1. **`ActivityGraph.java`**
   - Esta classe é responsável por representar o grafo de atividades.
   
   **Implementação:**
   ```java
   public class ActivityGraph extends Graph<ActivityVertex, String> {

       public ActivityGraph() {
           super(true); // Grafo direcionado
       }

       // Adiciona uma atividade ao grafo
       public void addActivity(ActivityVertex activity) {
           this.insertVertex(activity);
       }

       // Adiciona uma dependência entre atividades
       public void addDependency(ActivityVertex origin, ActivityVertex destination, String edgeLabel) {
           this.insertEdge(origin, destination, edgeLabel, destination.getCost());
       }

       // Exibe detalhes do grafo
       public void graphDetails() {
           System.out.println("Vertices: " + this.numVertices() +
               "Edges: " + this.numEdges());
       }

       @Override
       public String toString() {
           StringBuilder sb = new StringBuilder();

           // Definir cores ANSI para formatação no terminal
            String resetColor = "\033[0m";   // Reset de cor
            String yellowColor = "\033[0;33m"; // Amarelo para IncomingEddges
            String blueColor = "\033[0;34m";  // Azul para OutcomingEdges


           // Iterar pelos vértices e dependências
           for (ActivityVertex vertex : this.vertices()) {
               sb.append("Activity [ID: ").append(vertex.getId())
                   .append(", Description: ").append(vertex.getDescription())
                   .append(", Duration: ").append(vertex.getDuration())
                   .append(" ").append(vertex.getDurationUnit())
                   .append(", Cost: ").append(vertex.getCost())
                   .append(" ").append(vertex.getCostUnit()).append("]
                ");

               // Mostrar dependências de entrada
               boolean hasIncoming = false;
               for (Edge<ActivityVertex, String> edge : this.incomingEdges(vertex)) {
                   ActivityVertex source = edge.getVOrig();
                   sb.append("    ").append(blueColor).append("<- Dependency Activity [ID: ").append(source.getId())
                       .append(", Description: ").append(source.getDescription())
                       .append(", Cost: ").append(source.getCost())
                       .append("]").append(resetColor).append("");
                   hasIncoming = true;
               }
               if (!hasIncoming) {
                   sb.append("    ").append(blueColor).append("(No incoming dependencies)").append(resetColor).append("");
               }

               // Mostrar dependências de saída
               boolean hasOutgoing = false;
               for (Edge<ActivityVertex, String> edge : this.outgoingEdges(vertex)) {
                   ActivityVertex dest = (ActivityVertex) edge.getVDest();
                   sb.append("    ").append(yellowColor).append("-> Dependency Activity [ID: ").append(dest.getId())
                       .append(", Description: ").append(dest.getDescription())
                       .append(", Cost: ").append(dest.getCost())
                       .append("]").append(resetColor).append("");
                   hasOutgoing = true;
               }
               if (!hasOutgoing) {
                   sb.append("    ").append(yellowColor).append("(No outgoing dependencies)").append(resetColor).append("");
               }

               sb.append("");
           }

           return sb.toString();
       }
   }
   ```

2. **`ActivityVertex.java`**
   - Esta classe representa os vértices do grafo, ou seja, as atividades individuais.  
    **Atributos principais**:
     - `id` (int): Identificador único da atividade.
     - `description` (String): Descrição textual da atividade.
     - `duration` (int): Duração da atividade.
     - `durationUnit` (String): Unidade de tempo da duração (por exemplo, "dias" ou "horas").
     - `cost` (double): Custo associado à atividade.
     - `costUnit` (String): Unidade de custo (por exemplo, "USD" ou "EUR").

3. **`DataLoader.java`**
    - Esta classe é a responsável pela leitura dos ficheiros CSV e a modelação dos mesmos nas várias estruturas de dados.
      ### **Método Implementado**
        O método abaixo realiza a leitura do ficheiro `small_project.csv` ou `larger project`e constrói um grafo orientado representando as atividades e as suas respetivas dependências.
        ```java
        // Leitura do ficheiro activities.csv e construção do grafo orientado
          public static ActivityGraph loadActivityMapGraphFromCSV(String activitiesCsvFilePath) {
        ActivityGraph graph = new ActivityGraph();
        String line;

        // Adiciona o vértice Start
        ActivityVertex startVertex = new ActivityVertex("Start", "Start of process", 0, "", 0, "€");
        graph.addActivity(startVertex);

        try (BufferedReader br = new BufferedReader(new FileReader(activitiesCsvFilePath))) {
            br.readLine(); // Ignora o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Criação do vértice de atividade
                String id = fields[0];
                String description = fields[1];
                int duration = Integer.parseInt(fields[2]);
                String durationUnit = fields[3];
                double cost = Double.parseDouble(fields[4]);
                String costUnit = "€";

                ActivityVertex activity = new ActivityVertex(id, description, duration, durationUnit, cost, costUnit);
                graph.addActivity(activity);

                // Lê e adiciona dependências
                for (int i = 5; i < fields.length; i++) {
                    if (!fields[i].isEmpty()) {
                        String dependenciesField = fields[i];

                        if (dependenciesField.startsWith("\"") && dependenciesField.endsWith("\"")) {
                            dependenciesField = dependenciesField.substring(1, dependenciesField.length() - 1);
                        }

                        String[] dependencies = dependenciesField.split(",");
                        for (String dependencyId : dependencies) {
                            dependencyId = dependencyId.trim().replace("\"", "");
                            ActivityVertex dependency = findVertexById(graph, dependencyId);
                            if (dependency != null) {
                                graph.addDependency(dependency, activity, "Dependency Activity");
                            }
                        }
                    }
                }
            }

            // Conecta o Start às atividades iniciais (sem dependências de entrada)
            for (ActivityVertex vertex : graph.vertices()) {
                if (graph.inDegree(vertex) == 0 && !vertex.equals(startVertex)) {
                    graph.addDependency(startVertex, vertex, "Start Dependency");
                }
            }

            // Adiciona o vértice End
            ActivityVertex endVertex = new ActivityVertex("End", "End of process", 0, "", 0, "€");
            graph.addActivity(endVertex);

            // Conecta o End às atividades finais (sem dependências de saída)
                for (ActivityVertex vertex : graph.vertices()) {
                    if (graph.outDegree(vertex) == 0 && !vertex.equals(startVertex) && !vertex.equals(endVertex)) {
                        graph.addDependency(vertex, endVertex, "End Dependency");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        return graph;
       }

        // Método para buscar um vértice por ID dentro do grafo
        private static ActivityVertex findVertexById(ActivityGraph graph, int id) {
            for (ActivityVertex vertex : graph.vertices()) {
                if (vertex.getId() == id) {
                    return vertex;
                }
            }
            return null; // Retorna null se não encontrar o vértice com o ID
        }
Com essas implementações, o sistema está apto a importar dados de um CSV, criar os vértices e as arestas necessárias, e estruturar o grafo PERT-CPM de maneira eficiente.

### Testes Unitários
 - **test.java.esinf.model.`ActivityGraphTest`** (4 tests)
    ```java
    @Test
    void testAddActivity()

    @Test
    void testAddDependency()

    @Test
    void testToString()

    @Test
    void testGraphDetails()

- **test.java.esinf.model.`ActivityVertexTest`** (only Getters and Setters)


## CriticalPathAnalyzer Class Documentation - USEI22
**JavaDocs**
- D:\ISEP\24_25\ESINF\PI\Sprint3_stage\esinf_24_25_2nb_grupoA\out\esinf\model\CriticalPathAnalyzer.html

### Class Overview USEI22
The `CriticalPathAnalyzer` class is designed to analyze a project’s critical path by calculating the earliest and latest start and finish times, as well as the slack for each activity in the project. It performs topological sorting on the project's activity graph, calculates the critical path, and provides detailed information about each activity and the overall project duration.

### Key Methods and Functionality

#### **ActivityMetrics Class**
The `ActivityMetrics` class stores the timing and slack information for each activity in the project.

- **earliestStart**: The earliest time that an activity can start without delaying the project.
- **earliestFinish**: The earliest time that an activity can finish.
- **latestStart**: The latest time that an activity can start without delaying the project.
- **latestFinish**: The latest time that an activity can finish without delaying the project.
- **slack**: The amount of time that an activity can be delayed without affecting the project’s overall completion time.

**Constructor:**
```java
public ActivityMetrics() {
    this.earliestStart = 0;
    this.earliestFinish = 0;
    this.latestStart = Integer.MAX_VALUE;
    this.latestFinish = Integer.MAX_VALUE;
    this.slack = 0;
}
````

# BottleneckAnalyzer Class - USEI23
**JavaDocs**
- D:\ISEP\24_25\ESINF\PI\Sprint3_stage\esinf_24_25_2nb_grupoA\out\esinf\model\BottleneckAnalyzer.html
The `BottleneckAnalyzer` class identifies bottleneck activities in the project graph. Bottleneck activities are those that have the highest number of dependent activities (out-degree) or appear in the most paths from the start to the end of the project.

## Class Overview USEI22

### Dependencies
- `ActivityGraph`: Represents the graph of activities in the project.
- `ActivityVertex`: Represents a vertex (activity) in the project graph.
- `Edge`: Represents the relationship between two activities.

### Key Methods
1. **identifyByDependents()**  
   Identifies bottleneck activities based on the maximum number of dependent activities (out-degree). It calculates the out-degree of each activity and returns those with the highest number of dependents.
   
2. **identifyByPathFrequency()**  
   Identifies bottleneck activities based on the frequency of each activity appearing in paths from the project start to the project end. It uses a Depth-First Search (DFS) to find all paths and counts the occurrences of each activity.

3. **getBottleneckAnalysis()**  
   Provides a detailed analysis of bottleneck activities, including those identified by dependent count and path frequency. It formats the results in a readable string.

### Private Helper Methods
- **findStartVertex()**  
  Finds the starting vertex in the graph (activity with no predecessors).
  
- **findEndVertex()**  
  Finds the ending vertex in the graph (activity with no successors).

- **findPathsDFS()**  
  A recursive helper method that performs Depth-First Search (DFS) to explore all paths from the start to the end vertex, updating the frequency of each activity in the path.

---

## Class Definition

```java
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

        if (current.equals(end)) {
            // Incrementa a frequência de cada atividade no caminho atual
            for (ActivityVertex vertex : path) {
                pathFrequency.put(vertex, pathFrequency.getOrDefault(vertex, 0) + 1);
            }
        } else {
            for (Edge<ActivityVertex, String> edge : graph.outgoingEdges(current)) {
                ActivityVertex next = edge.getVDest();
                if (!visited.contains(next)) {
                    findPathsDFS(next, end, visited, path, pathFrequency);
                }
            }
        }

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
```