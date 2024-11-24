package esinf.model;

public class AVLTreeNode {
    ProductionNode operation;  // The operation (ProductionNode) for this tree node
    int height;  // Height of the node (used for AVL balancing)
    AVLTreeNode left, right;  // Left and right children

    public AVLTreeNode(ProductionNode operation) {
        this.operation = operation;
        this.height = 1;  // Start with a height of 1 for a new node
        this.left = null;
        this.right = null;
    }

    // Getter for the operation (ProductionNode)
    public ProductionNode getOperation() {
        return operation;
    }

    // Getter for the height
    public int getHeight() {
        return height;
    }

    // Setter for the height
    public void setHeight(int height) {
        this.height = height;
    }

    // Getter for left child
    public AVLTreeNode getLeft() {
        return left;
    }

    // Setter for left child
    public void setLeft(AVLTreeNode left) {
        this.left = left;
    }

    // Getter for right child
    public AVLTreeNode getRight() {
        return right;
    }

    // Setter for right child
    public void setRight(AVLTreeNode right) {
        this.right = right;
    }

    // Override toString method for easier printing of AVLTreeNode
    @Override
    public String toString() {
        return "AVLTreeNode{" +
                "operation=" + operation +  // Use the toString method of the ProductionNode class
                ", height=" + height +
                '}';
    }
}
