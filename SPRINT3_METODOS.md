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
               "
        Edges: " + this.numEdges());
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
                       .append("]").append(resetColor).append("
                    ");
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
      ##### **Método Implementado**
        O método abaixo realiza a leitura do ficheiro `activities.csv` e constrói um grafo orientado representando as atividades e as suas respetivas dependências.
        ```java
        // Leitura do ficheiro activities.csv e construção do grafo orientado
        public static ActivityGraph loadActivityMapGraphFromCSV(String activitiesCsvFilePath) {
            ActivityGraph graph = new ActivityGraph();
            String line;

            try (BufferedReader br = new BufferedReader(new FileReader(activitiesCsvFilePath))) {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(";");

                    // Lê os dados da atividade
                    int id = Integer.parseInt(fields[0]); // Lê o act_id
                    String description = fields[1];
                    int duration = Integer.parseInt(fields[2]);
                    String durationUnit = fields[3];
                    double cost = Double.parseDouble(fields[4]);
                    String costUnit = fields[5];

                    // Cria o vértice de atividade com os dados do CSV e adiciona a atividade
                    ActivityVertex activity = new ActivityVertex(id, description, duration, durationUnit, cost, costUnit);
                    graph.addActivity(activity);

                    // Lê as dependências (se existirem)
                    for (int i = 6; i < fields.length; i++) {
                        if (!fields[i].isEmpty()) {
                            int dependencyId = Integer.parseInt(fields[i]);
                            ActivityVertex dependency = findVertexById(graph, dependencyId); // Busca o vértice de dependência
                            if (dependency != null) {
                                graph.addDependency(dependency, activity, "Dependency Activity");  // Usa o método addDependency
                            }
                        }
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