package esinf.model;

import java.util.ArrayList;
import java.util.List;

public class ProductionNode {
    private int id; // ID da operação ou item
    private String name; // Nome da operação ou item
    private String type; // Tipo: "operation" ou "item"
    private double quantity; // Quantidade (Pode ser decimal segundo o exemplo dado)
    private ProductionNode parent; //Referenciação ao nó pai
    private List<ProductionNode> children; // Lista de filhos


    public ProductionNode(int id, String name, String type, double quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public ProductionNode getParent() {
        return parent;
    }

    public void setParent(ProductionNode parent) {
        this.parent = parent;
    }

    public void addChild(ProductionNode child) {
        // Adiciona o filho apenas se ele ainda não existir
        if (!this.children.contains(child)) {
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void setQuantity(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public double changeQuantity(double newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        double oldQuantity = this.quantity;
        this.quantity = newQuantity;
        return newQuantity/oldQuantity;
    }

    //US14
    private int depth; // Depth of the operation (critical path depth)

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public List<ProductionNode> getChildren() {
        return children;
    }
//US15


    @Override
    public String toString() {
        return "ProductionNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", childrenCount=" + children.size() +
                '}';
    }
}
