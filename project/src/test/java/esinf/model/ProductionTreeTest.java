package esinf.model;

import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTreeTest {


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