package esinf.model;

import org.junit.jupiter.api.Test;
import java.util.Queue;
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


    /**
     * Tests the getQualityCheckSequence method with a tree containing multiple nodes,
     * including both "Operation" and "Item" types.
     * Verifies that the method correctly adds only "Operation" nodes to the priority queue.
     * The tree structure is:
     * - Root (Operation)
     *   - Child1 (Operation)
     *     - Child3 (Operation)
     *   - Child2 (Item)
     * Expected: The priority queue contains 3 "Operation" nodes.
     */
    @Test
    void getQualityCheckSequence() {
        ProductionNode root = new ProductionNode(1, "Root", "Operation", 1);
        ProductionNode child1 = new ProductionNode(2, "Child1", "Operation", 1);
        ProductionNode child2 = new ProductionNode(3, "Child2", "Item", 1);
        ProductionNode child3 = new ProductionNode(4, "Child3", "Operation", 1);

        ProductionTree tree = new ProductionTree(root);
        tree.addNode(root, child1);
        tree.addNode(root, child2);
        tree.addNode(child1, child3);

        Queue<QualityCheck> qcSequence = tree.getQualityCheckSequence();

        assertEquals(3, qcSequence.size());
    }

    /**
     * Tests the getQualityCheckSequence method with an empty tree (null root).
     * Verifies that the method returns an empty priority queue when there are no nodes in the tree.
     */
    @Test
    void getQualityCheckSequence_emptyTree() {
        ProductionTree tree = new ProductionTree(null);
        Queue<QualityCheck> qcSequence = tree.getQualityCheckSequence();
        assertTrue(qcSequence.isEmpty());
    }

}