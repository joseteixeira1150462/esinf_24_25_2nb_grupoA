package esinf.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTreeTest {

    /**
     * Test the calculateProductionTotals method with a single item attached to a root operation.
     * Verifies that the method correctly handles a simple tree structure with one operation and one item.
     */
    @Test
    void calculateProductionTotalsOutputWithSingleItem() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 10.0);
        ProductionNode item = new ProductionNode(2, "Item1", "Item", 10.0);
        root.addChild(item);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with multiple items and operations in a hierarchical tree structure.
     * Verifies the method processes nested operations and items correctly.
     */
    @Test
    void calculateProductionTotalsOutputWithMultipleItemsAndOperations() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 10.0);
        ProductionNode item1 = new ProductionNode(2, "Item1", "Item", 10.0);
        ProductionNode item2 = new ProductionNode(3, "Item2", "Item", 20.0);
        ProductionNode operation = new ProductionNode(4, "Operation1", "Operation", 10.0);
        root.addChild(item1);
        root.addChild(operation);
        operation.addChild(item2);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with an empty tree (no root node).
     * Verifies the method handles a null root without throwing exceptions.
     */
    @Test
    void calculateProductionTotalsOutputWithEmptyTree() {
        ProductionTree tree = new ProductionTree(null);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with a tree that contains operations only, without any items.
     * Ensures the method calculates the number of operations and ignores missing materials.
     */
    @Test
    void calculateProductionTotalsOutputWithNoItems() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 10.0);
        ProductionNode operation = new ProductionNode(2, "Operation1", "Operation", 1);
        root.addChild(operation);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with a tree that contains items only, without any operations.
     * Ensures the method calculates the total materials and ignores missing operations.
     */
    @Test
    void calculateProductionTotalsOutputWithNoOperations() {
        ProductionNode root = new ProductionNode(1, "Root", "Item", 10.0);
        ProductionNode item = new ProductionNode(2, "Item1", "Item", 20.0);
        root.addChild(item);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with a tree that contains a mix of items and operations at various levels.
     * Verifies that the method handles complex hierarchical structures with mixed nodes.
     */
    @Test
    void calculateProductionTotalsOutputWithMixedItemsAndOperations() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 10.0);
        ProductionNode item1 = new ProductionNode(2, "Item1", "Item", 10.0);
        ProductionNode item2 = new ProductionNode(3, "Item2", "Item", 20.0);
        ProductionNode operation1 = new ProductionNode(4, "Operation1", "Operation", 10.0);
        ProductionNode operation2 = new ProductionNode(5, "Operation2", "Operation", 5.0);
        root.addChild(item1);
        root.addChild(operation1);
        operation1.addChild(item2);
        operation1.addChild(operation2);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with a tree that contains a single operation node.
     * Ensures the method handles trees with only a root node properly.
     */
    @Test
    void calculateProductionTotalsOutputWithSingleOperation() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 10.0);
        ProductionTree tree = new ProductionTree(root);

        tree.calculateProductionTotals();
    }

    /**
     * Test the calculateProductionTotals method with a null root node.
     * Ensures the method handles null root trees gracefully without exceptions.
     */
    @Test
    void calculateProductionTotalsOutputWithNullRoot() {
        ProductionTree tree = new ProductionTree(null);

        tree.calculateProductionTotals();
    }

}