package esinf.model;

import esinf.utils.adjacencymapgraph.Vertex;

import java.util.Objects;

/**
 * Classe que representa um vértice de atividade no grafo de atividades.
 * Cada vértice possui informações sobre a atividade, incluindo seu ID, descrição,
 * duração, custo e unidades de duração e custo. Esta classe estende a classe
 * {@link Vertex}, onde o vértice é associado a um elemento de tipo {@link ActivityVertex}.
 *
 * @author josteixeira1150462
 */
public class ActivityVertex{

    private String id;            // ID da atividade
    private String description;   // Descrição da atividade
    private int duration;         // Duração da atividade
    private String durationUnit;  // Unidade de duração (dias, horas, etc.)
    private double cost;          // Custo da atividade
    private String costUnit;      // Unidade de custo (USD, EUR, etc.)

    /**
     * Construtor da classe ActivityVertex.
     *
     * @param id ID único da atividade.
     * @param description Descrição detalhada da atividade.
     * @param duration Duração da atividade em um valor numérico.
     * @param durationUnit Unidade da duração (exemplo: dias, horas).
     * @param cost Custo total da atividade.
     * @param costUnit Unidade monetária do custo (exemplo: USD, EUR).
     */
    public ActivityVertex(String id, String description, int duration, String durationUnit, double cost, String costUnit) {
        this.id = id;
        this.description = description;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.cost = cost;
        this.costUnit = costUnit;
    }

    /**
     * Retorna o ID da atividade.
     *
     * @return O ID da atividade.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID da atividade.
     *
     * @param id O novo ID da atividade.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna a descrição da atividade.
     *
     * @return A descrição da atividade.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Define a descrição da atividade.
     *
     * @param description A nova descrição da atividade.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retorna a duração da atividade.
     *
     * @return A duração da atividade.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Define a duração da atividade.
     *
     * @param duration A nova duração da atividade.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Retorna a unidade de medida da duração da atividade.
     *
     * @return A unidade de duração (exemplo: dias, horas).
     */
    public String getDurationUnit() {
        return durationUnit;
    }

    /**
     * Define a unidade de medida da duração da atividade.
     *
     * @param durationUnit A nova unidade de duração (exemplo: dias, horas).
     */
    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    /**
     * Retorna o custo da atividade.
     *
     * @return O custo da atividade.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Define o custo da atividade.
     *
     * @param cost O novo custo da atividade.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Retorna a unidade de medida do custo da atividade.
     *
     * @return A unidade monetária do custo (exemplo: USD, EUR).
     */
    public String getCostUnit() {
        return costUnit;
    }

    /**
     * Define a unidade de medida do custo da atividade.
     *
     * @param costUnit A nova unidade monetária do custo (exemplo: USD, EUR).
     */
    public void setCostUnit(String costUnit) {
        this.costUnit = costUnit;
    }

    /**
     * Retorna uma representação em formato de string da atividade.
     *
     * @return A string contendo as informações da atividade.
     */
    @Override
    public String toString() {
        return "ActivityVertex [ID: " + id + ", Description: " + description +
                ", Duration: " + duration + " " + durationUnit + ", Cost: " + cost + " " + costUnit + "]";
    }

    /**
     * Compara este objeto com outro para verificar se são iguais.
     *
     * @param o O objeto a ser comparado.
     * @return {@code true} se os objetos forem iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityVertex)) return false;
        ActivityVertex activityVertex = (ActivityVertex) o;
        return duration == activityVertex.duration && Double.compare(cost, activityVertex.cost) == 0
                && Objects.equals(id, activityVertex.id)
                && Objects.equals(description, activityVertex.description)
                && Objects.equals(durationUnit, activityVertex.durationUnit)
                && Objects.equals(costUnit, activityVertex.costUnit);
    }

    /**
     * Retorna o código hash do objeto.
     *
     * @return O código hash baseado nos atributos da atividade.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, description, duration, durationUnit, cost, costUnit);
    }
}