package esinf.model;

import esinf.utils.adjacencymapgraph.Edge;
import esinf.utils.adjacencymapgraph.Graph;

/**
 * Representa um grafo de atividades, onde cada vértice é uma atividade e as arestas representam as dependências entre elas.
 * Este grafo é direcionado, ou seja, as dependências entre atividades possuem direção (de uma atividade para outra).
 *
 * Permite adicionar atividades e definir as dependências entre elas.
 */
public class ActivityGraph extends Graph<ActivityVertex, String> {

    /**
     * Construtor do grafo de atividades. Inicializa um grafo direcionado.
     */
    public ActivityGraph() {
        super(true); // Grafo direcionado
    }

    /**
     * Adiciona uma atividade ao grafo.
     *
     * @param activity A atividade a ser adicionada ao grafo.
     */
    public void addActivity(ActivityVertex activity) {
        this.insertVertex(activity);
    }

    /**
     * Adiciona uma dependência entre duas atividades no grafo.
     *
     * @param origin A atividade de origem.
     * @param destination A atividade de destino.
     * @param edgeLabel Custo da aresta
     */
    public void addDependency(ActivityVertex origin, ActivityVertex destination, String edgeLabel) {
        this.insertEdge(origin, destination, edgeLabel, destination.getCost());
    }

    public void graphDetails(){
        System.out.println("Vertices: "+ this.numVertices()+
                "\nEdges: "+ this.numEdges());
    }

    /**
     * Retorna uma representação em formato de string do grafo de atividades, incluindo detalhes de cada atividade e as suas respetivas
     * dependências, caso existam.
     *
     * @return A representação em string do grafo de atividades.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Definir cores ANSI para formatação no terminal
        String resetColor = "\033[0m";   // Reset de cor
        String yellowColor = "\033[0;33m"; // Amarelo para IncomingEddges
        String blueColor = "\033[0;34m";  // Azul para OutcomingEdges

        for (ActivityVertex vertex : this.vertices()) {
            sb.append("Activity [ID: ").append(vertex.getId())
                    .append(", Description: ").append(vertex.getDescription())
                    .append(", Duration: ").append(vertex.getDuration())
                    .append(" ").append(vertex.getDurationUnit())
                    .append(", Cost: ").append(vertex.getCost())
                    .append(" ").append(vertex.getCostUnit()).append("]\n");

            boolean hasIncoming = false;
            for (Edge<ActivityVertex, String> edge : this.incomingEdges(vertex)) {
                ActivityVertex source = edge.getVOrig();
                // Incluindo o custo na label da aresta
                sb.append("    ").append(blueColor).append("<- Dependency Activity [ID: ").append(source.getId())
                        .append(", Description: ").append(source.getDescription())
                        .append(", Cost: ").append(source.getCost())
                        .append("]").append(resetColor).append("\n");
                hasIncoming = true;
            }
            if (!hasIncoming) {
                sb.append("    ").append(blueColor).append("(No incoming dependencies)").append(resetColor).append("\n");
            }

            // Mostrar dependências de saída
            boolean hasOutgoing = false;
            for (Edge<ActivityVertex, String> edge : this.outgoingEdges(vertex)) {
                ActivityVertex dest = (ActivityVertex) edge.getVDest();
                // Incluindo o custo na label da aresta
                sb.append("    ").append(yellowColor).append("-> Dependency Activity [ID: ").append(dest.getId())
                        .append(", Description: ").append(dest.getDescription())
                        .append(", Cost: ").append(dest.getCost())  // Aqui é onde você exibe o custo da aresta
                        .append("]").append(resetColor).append("\n");
                hasOutgoing = true;
            }
            if (!hasOutgoing) {
                sb.append("    ").append(yellowColor).append("(No outgoing dependencies)").append(resetColor).append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}