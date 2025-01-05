## Métodos Implementados por cada elemento

### **USEI17 - Construção de um Grafo PERT-CPM**
### Autor : JOSÉ TEIXEIRA 1150462
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



### Análise de Complexidade: `loadActivityMapGraphFromCSV`

### Operações e Complexidades

#### 1. **Adição do Vértice Inicial `Start`**
- Operação: Criação de um vértice e adição ao grafo.
- Complexidade: **O(1)**.


#### 2. **Leitura do Arquivo CSV**
A leitura do arquivo processa todas as linhas, com as seguintes operações principais:

##### a. **Divisão da Linha (`split`)**
- Divide uma linha do arquivo em campos usando `split`.
- Complexidade por linha: **O(C)**, onde **C** é o número médio de caracteres por linha.

##### b. **Criação de um Vértice de Atividade**
- Criação do vértice envolve atribuições simples.
- Complexidade: **O(1)**.

##### c. **Leitura de Dependências**
Para cada linha:
1. Dependências são processadas:
    - Se agrupadas em uma string (e.g., `"A,B,C"`), são divididas usando `split`.
    - Complexidade: Proporcional ao número de dependências **D**.
2. Cada dependência é pesquisada no grafo usando `findVertexById`:
    - Pesquisa linear nos vértices do grafo.
    - Complexidade: **O(V)** por dependência, onde **V** é o número de vértices.

   Assim, a complexidade para processar dependências é:
   \[
   O(D \cdot V)
   \]

#### 3. **Conexão do Vértice `Start`**
- Operação: Conecta o vértice `Start` às atividades iniciais (sem dependências de entrada).
- Envolve verificar o grau de entrada (`inDegree`) de todos os vértices:
    - Complexidade por vértice: Proporcional ao número de arestas **E**.
- Complexidade total: **O(V + E)**.



#### 4. **Adição do Vértice Final `End`**
- Operação: Criação do vértice final.
- Complexidade: **O(1)**.


#### 5. **Conexão do Vértice `End`**
- Operação: Conecta o vértice `End` às atividades finais (sem dependências de saída).
- Semelhante à conexão do vértice `Start`, verificando o grau de saída (`outDegree`).
- Complexidade total: **O(V + E)**.

---

### Complexidade Total

1. **Leitura do Arquivo e Criação dos Vértices:**

   `O(N * (C + D * V))`
Onde:

    - **N**: Número de linhas no arquivo CSV.
    - **C**: Número médio de caracteres por linha.
    - **D**: Número médio de dependências por linha.
    - **V**: Número total de vértices no grafo.

2. **Conexão dos Vértices Iniciais (`Start`):** **O(V + E)**
3. **Conexão dos Vértices Finais (`End`):** **O(V + E)**

A complexidade geral é dominada pela etapa de leitura e processamento das dependências:

`O(N * (C + D * V)) + O(V + E)`

---

### Casos Especiais
- **Grafo Denso:** Se `E <> V^2`, as operações relacionadas a arestas podem ter impacto significativo.

---

### Possíveis Otimizações 

1. **Reduzir o custo de `findVertexById`:**
    - Usar uma estrutura como `HashMap` ou `HashSet` para armazenar os vértices.
    - Complexidade da busca seria reduzida de **O(V)** para **O(1)**.


Com essas otimizações, a complexidade seria reduzida para:
`O(N * (C + D)) + O(V + E)`


## Testes Unitários
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


- **test.java.esinf.utils.`DataLoaderTest`**
 ```java
    @Test
void testLoadActivityMapGraphFromCSV();
private void assertVerticesExist(ActivityGraph graph, String... vertexIds); //aux method
private void assertEdgesExist(ActivityGraph graph, String... vertexPairs); //aux method
private ActivityVertex findVertexById(ActivityGraph graph, String id); //aux method
 ```  
    
---
### **USEI18 - Detect Circular Dependencies on Project Activities**
### Author : JOSÉ TEIXEIRA 1150462
As a user, I want to validate the project graph to ensure there are no circular dependencies among activities, as this would
make the project unable to be scheduled.

**`java.esinf.utils.adjencymapgraph.GraphAlgorithms`**
```java
  public static <V, E> void checkForCircularDependencies(Graph<V, E> graph) {
    Set<V> visited = new HashSet<>();
    Edge<V,E> cycles = null;

    // Check from each vertex
    for (V vertex : graph.vertices()) {
        if (!visited.contains(vertex)) {
            dfsDetectCycles(graph, vertex, visited, cycles);
        }
    }

    // Display results
    if (cycles==null) {
        System.out.println("No circular dependencies found - project can be scheduled!");
    } else {
        System.out.println("Warning: Circular dependencies detected!");
        System.out.println(describeCycle(graph, cycles));
    }
}


/**
 * Detects and returns all circular dependencies in a project graph
 * @param graph The project graph where vertices are activities and edges are dependencies
 * @return List of cycles, where each cycle is represented as a list of edges that form the cycle
 */
public static <V, E> Edge<V, E> detectCircularDependencies(Graph<V, E> graph) {
    Edge<V, E> cycleEdge = null; // o (eventual) edge que causou erro, se estiver null -> ok
    Set<V> visited = new HashSet<>(); // Mantém o caminho atual

    for (V vertex : graph.vertices()) {
        dfsDetectCycles(graph, vertex, visited, cycleEdge);
    }

    return cycleEdge ;
}

/**
 * DFS implementation for cycle detection
 */
private static <V, E> void dfsDetectCycles(
        Graph<V, E> graph,
        V current,
        Set<V> visited,
        Edge<V, E> cycles) {

    visited.add(current); // Marcar o vértice no caminho atual

    // iterar edges do grafo atual
    for (Edge<V, E> edge : graph.outgoingEdges(current)) {
        V nextActivity = graph.opposite(current, edge);

        // Base Case: Verificar se há um ciclo direto ou indireto
        if (nextActivity.toString().equals(current.toString())  || visited.contains(nextActivity)) {
            cycles = edge;
            return;
        }

        // O próximo vértice não foi visitado ainda, continue a DFS
        dfsDetectCycles(graph, nextActivity, visited, cycles);
    }
}



private static <V> boolean containsCycle(List<List<V>> cycles, List<V> newCycle) {
    for (List<V> existingCycle : cycles) {
        if (isSameCycle(existingCycle, newCycle)) {
            return true;
        }
    }
    return false;
}

/**
 * Helper method to compare two cycles
 */
private static <V> boolean isSameCycle(List<V> cycle1, List<V> cycle2) {
    if (cycle1.size() != cycle2.size()) {
        return false;
    }

    // Create a double-length version of cycle1 to handle rotations
    List<V> doubledCycle = new ArrayList<>(cycle1);
    doubledCycle.addAll(cycle1);

    // Try to find cycle2 in the doubled cycle1
    for (int i = 0; i < cycle1.size(); i++) {
        boolean matches = true;
        for (int j = 0; j < cycle2.size(); j++) {
            if (!doubledCycle.get(i + j).equals(cycle2.get(j))) {
                matches = false;
                break;
            }
        }
        if (matches) {
            return true;
        }
    }
    return false;
}


/**
 * Helper method to get a human-readable representation of a cycle
 * @param graph The project graph
 * @param cycle List of edges forming a cycle
 * @return String representation of the cycle's activities
 */
public static <V, E> String describeCycle(Graph<V, E> graph, Edge<V, E> cycle) {
    if (cycle == null) return "No cycle";

    // Iniciar a descrição do ciclo
    StringBuilder description = new StringBuilder("Circular Dependency Detected!");
    V startVertex = cycle.getVOrig();
    V nextVertex = graph.opposite(startVertex, cycle);

    if (startVertex == nextVertex) {
        description.append("Self Connecting Circular Dependency:");
        description.append("ActivityVertex [ID: ").append(startVertex).append(" -> ").append(nextVertex).append("]");
    }

    else {
        description.append("Indirect Circular Dependency, the destination was already visited:");
        description.append("ActivityVertex [ID: ").append(startVertex).append(" -> ").append(nextVertex).append("]");
    }


    return description.toString();
    }
```

This code provides methods to detect circular dependencies in a directed graph representing a project activity workflow. It uses Depth-First Search (DFS) to identify cycles and offers utilities to compare, describe, and report detected cycles.

### Key Methods:
1. **`checkForCircularDependencies`**:
    - Checks for cycles in the graph and prints results.
2. **`detectCircularDependencies`**:
    - Detects and returns the first edge that causes a cycle or `null` if no cycle exists.
3. **`dfsDetectCycles`**:
    - Implements Depth-First Search to detect cycles.
4. **`containsCycle`** and **`isSameCycle`**:
    - Ensure new cycles are unique compared to previously detected cycles.
5. **`describeCycle`**:
    - Provides a human-readable description of the detected cycle.

---

### Complexity Analysis

#### 1. **Cycle Detection (`checkForCircularDependencies` or `detectCircularDependencies`)**
- **Outer Loop**: Iterates over all vertices in the graph: `O(V)`.
- **DFS Traversal**: Visits each vertex and edge:`O(V + E)`
  (where **V** is the number of vertices and **E** is the number of edges).

#### 2. **DFS Implementation (`dfsDetectCycles`)**
- Processes outgoing edges for each vertex:
  `O(E)`

#### 3. **Cycle Comparison (`containsCycle`)**
- Compares the new cycle against all existing cycles:
  `O(C * K^2)`
  (where **C** is the number of detected cycles, and **K** is the average cycle length).

#### 4. **Cycle Equality Check (`isSameCycle`)**
- Checks if two cycles are identical considering rotations:
  
  `O(K^2)`
  
  (for cycles of size **K**).

#### 5. **Cycle Description (`describeCycle`)**
- Generates a human-readable description of the cycle: 
  `O(1)`
  (fixed processing for the edge and vertices involved).

---

### Overall Complexity

- **Time Complexity**: Dominated by graph traversal using DFS:
  `O(V + E)`
  If multiple cycles are detected, the comparison step adds:
  `O(C * K^2)`
  
#### Total: **O(V + E + C * K^2)**.

- **Space Complexity**:
    - **Visited Set**: **O(V)** to track visited vertices.
    - **Cycle Storage**: **O(C * K)** for storing detected cycles.

---

### Possible Enhancements
- The algorithm is efficient for sparse graphs but can be costly for:
    1. Dense graphs : `E <> V^2`.
    2. Graphs with many cycles.
- Optimizations such as limiting cycle detection or optimizing cycle comparison could improve performance for complex graphs.

---
- **Unit tests : 0 tests**
---
### **USEI19 - Topological Sort of Project Activities**

### Autor : DIOGO MARQUES 1190516

Como utilizador, pretendo realizar uma ordenação topológica das atividades. Este método fornece uma sequência para a execução das atividades, sendo respeitada todas as restrições de dependência.

#### **Descrição do Método**
A ordenação topológica é realizada utilizando o algoritmo **Kahn's Algorithm**, que verifica se o grafo não é ciclico e retorna uma sequencia topológica. Caso o grafo contenha ciclos, a ordenação topológica não é possível.

#### **Complexidade**
Complexidade: O(V + E), onde V é o número de vértices e E é o número de arestas no grafo
#### **Implementação**
A implementação foi realizada na classe **`GraphAlgorithms`**.

```java
public static<V,E> boolean topSort(Graph<V,E> g, List<V> topsort){

    Map<V, Integer> degreeIn = new HashMap();
    Queue<V> queueAux = new LinkedList<V>();

    /*
     * for (all V vertices in g) {
     * degreeIn[idxV] = g.inDegree(V)
     * if (degreeIn[idxV] == 0) Add vertex V to queue-aux }
     */
    for (V vertice : g.vertices()) {
        degreeIn.put(vertice, g.inDegree(vertice));
        if (g.inDegree(vertice) == 0) {
            queueAux.add(vertice);
        }
    }

    /*
     * numVerts=0
     */
    int numVerts = 0;

    /*
     * while (!queue-aux is Empty){
     *     vOrig f remove first vertex from queue-aux
     *     add vOrig to topsort
     *     numVerts++
     *     for (each vAdj of vOrig){
     *         degreeIn[vAdj]--
     *         if (degreeIn[vAdj] == 0) add vertex VAdj to queue-aux
     *     }
     * }
     * */
    while (!queueAux.isEmpty()) {
        V vOrig = queueAux.poll();
        topsort.add(vOrig);
        numVerts++;
        for (V vAdj : g.adjVertices(vOrig)) {
            degreeIn.put(vAdj, degreeIn.get(vAdj) - 1);
            if (degreeIn.get(vAdj) == 0)
                queueAux.add(vAdj);
        }

    }

    /*
     * if (numVerts < g.numVertices()) //Graph has a cycle
     * return false
     * return true
     */
    if (numVerts < g.numVertices()) {
        return false;
    }
    return true;
}
```

#### **Testes Unitários**
Os testes unitários foram implementados na classe **`ActivityGraphAlgorithmsTest`**, garantindo a validação do método em diferentes cenários. Os seguintes métodos de teste foram desenvolvidos:

```java
@Test
public void testTopSortAcyclicGraph() {
}

@Test
public void testTopSortCyclicGraph() {
}

@Test
public void testTopSortEmptyGraph() {
}

@Test
public void testTopSortAcyclicGraphXYZ() {
}

@Test
public void testTopSortAcyclicGraphEFGH() {
}

@Test
public void testTopSortAcyclicGraphIJKL() {
}

```

### **USEI20 - Calculate Earliest and Latest Start and Finish Times**

### Autor : DIOGO MARQUES 1190516

Como utilizador, pretendo calcular os tempos de início mais cedo (ES) e de término mais tarde (LF) para cada atividade, com base nas dependências.
Isto ajudará a identificar a folga para atividades não críticas e a garantir um planeamento preciso do cronograma.

#### **Complexidade**
Complexidade: O(V + E), onde V é o número de vértices e E é o número de arestas no grafo. Isso ocorre porque: 1. A ordenação topológica leva O(V + E). 2. O cálculo dos tempos de início mais cedo (ES) e término mais cedo (EF) leva O(V + E). 3. O cálculo dos tempos de início mais tarde (LS) e término mais tarde (LF) também leva O(V + E). 4. A construção do mapa de resultados tem complexidade O(V)

#### **Descrição do Método**
O método `calculateTimes` calcula os tempos de início mais cedo (ES), término mais cedo (EF), início mais tarde (LS), término mais tarde (LF) e slack para cada atividade em um grafo dirigido, considerando suas dependências. Ele utiliza ordenação topológica para garantir a execução das atividades na ordem correta, calcula os tempos com base nas dependências e determina o slack de cada atividade. O resultado é um mapa com os tempos e o slack de cada atividade no projeto.

```java
public static <V, E> Map<V, Map<String, Integer>> calculateTimes(Graph<V, E> g, Map<V, Integer> durations) {

        Map<V, Map<String, Integer>> result = new HashMap<>();


        List<V> topOrder = new ArrayList<V>();
        boolean isCycle = topSort(g, topOrder);


        Map<V, Integer> ES = new HashMap<>();
        Map<V, Integer> EF = new HashMap<>();
        for (V v : g.vertices()) {
            ES.put(v, 0);
            EF.put(v, 0);
        }


        for (V v : topOrder) {
            int maxEF = 0;
            for (V pred : g.incomingVertices(v)) {
                maxEF = Math.max(maxEF, EF.get(pred));
            }
            ES.put(v, maxEF);
            EF.put(v, maxEF + durations.get(v));
        }


        Map<V, Integer> LS = new HashMap<>();
        Map<V, Integer> LF = new HashMap<>();
        int maxEF = topOrder.stream().mapToInt(EF::get).max().orElse(0);

        for (V v : g.vertices()) {
            LF.put(v, maxEF);
            LS.put(v, maxEF);
        }


        List<V> reverseOrder = new ArrayList<>(topOrder);
        Collections.reverse(reverseOrder);

        for (V v : reverseOrder) {
            int minLS = maxEF;
            for (V succ : g.adjVertices(v)) {
                minLS = Math.min(minLS, LS.get(succ));
            }
            LF.put(v, minLS);
            LS.put(v, minLS - durations.get(v));
        }


        for (V v : g.vertices()) {
            int slack = LS.get(v) - ES.get(v);
            Map<String, Integer> times = new HashMap<>();
            times.put("ES", ES.get(v));
            times.put("EF", EF.get(v));
            times.put("LS", LS.get(v));
            times.put("LF", LF.get(v));
            times.put("Slack", slack);
            result.put(v, times);
        }

        return result;
    }

```
#### **Testes Unitários**
Os testes unitários foram implementados na classe **`ActivityGraphAlgorithmsTest`**, garantindo a validação do método. O seguinte método de teste foi desenvolvido:

```java
@Test
public void testCalculateTimes() {
}
```

![Diagrama SPRINT3](esinf.jpg)


