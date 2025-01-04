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

### **USEI20 - Calculate Earliest and Latest Start and Finish Times**
Como utilizador, pretendo calcular os tempos de início mais cedo (ES) e de término mais tarde (LF) para cada atividade, com base nas dependências.
Isto ajudará a identificar a folga para atividades não críticas e a garantir um planeamento preciso do cronograma.

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
Os testes unitários foram implementados na classe **`GraphAlgorithmsTest3`**, garantindo a validação do método. O seguinte método de teste foi desenvolvido:
    
```java
@Test
public void testCalculateTimes() {
}
```
