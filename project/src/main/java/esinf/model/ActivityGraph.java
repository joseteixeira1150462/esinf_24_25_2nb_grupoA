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
        // Para cada vértice (atividade), exibe seus detalhes e dependências
        for (ActivityVertex vertex : this.vertices()) {
            sb.append("Activity [ID: ").append(vertex.getId())
                    .append(", Description: ").append(vertex.getDescription())
                    .append(", Duration: ").append(vertex.getDuration())
                    .append(" ").append(vertex.getDurationUnit())
                    .append(", Cost: ").append(vertex.getCost())
                    .append(" ").append(vertex.getCostUnit()).append("]\n");

            sb.append("  Dependencies:\n");
            // Exibe as atividades dependentes
            for (Edge<ActivityVertex, String> edge : this.outgoingEdges(vertex)) {
                ActivityVertex dest = (ActivityVertex) edge.getVDest(); // Obtém o destino da aresta
                sb.append("    -> Activity [ID: ").append(dest.getId())
                        .append(", Description: ").append(dest.getDescription())
                        .append(", Duration: ").append(dest.getDuration())
                        .append(" ").append(dest.getDurationUnit())
                        .append(", Cost: ").append(dest.getCost())
                        .append(" ").append(dest.getCostUnit()).append("]\n");
            }

            // Caso não haja dependências
            if (!this.outgoingEdges(vertex).iterator().hasNext()) {
                sb.append("    (No dependencies)\n");
            }

            sb.append("\n");
        }
        return sb.toString();
    }
}