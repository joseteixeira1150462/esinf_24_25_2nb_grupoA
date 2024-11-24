package esinf.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class AVLTreeTest {
    private AVLTree tree;
    private ProductionNode node1, node2, node3, node4, node5;

    @Before
    public void setUp() {
        tree = new AVLTree();

        // Create test nodes
        node1 = new ProductionNode(1, "Operation1", "operation", 10.0);
        node2 = new ProductionNode(2, "Operation2", "operation", 20.0);
        node3 = new ProductionNode(3, "Operation3", "operation", 30.0);
        node4 = new ProductionNode(4, "Item1", "item", 40.0);
        node5 = new ProductionNode(5, "Item2", "item", 50.0);

        // Set depths manually
        node1.setDepth(1);
        node2.setDepth(2);
        node3.setDepth(2);
        node4.setDepth(3);
        node5.setDepth(1);

        // Set up parent-child relationships
        node1.addChild(node2);
        node1.addChild(node3);
        node2.addChild(node4);
    }

    @Test
    public void testInsertAndSize() {
        assertEquals(0, tree.getSize());

        tree.insert(node1);
        assertEquals(1, tree.getSize());

        tree.insert(node2);
        assertEquals(2, tree.getSize());

        // Insert duplicate node (same ID and depth)
        tree.insert(node1);
        assertEquals(2, tree.getSize()); // Size shouldn't change
    }

    @Test
    public void testParentChildRelationships() {
        // Test that parent-child relationships are maintained
        assertEquals(node1, node2.getParent());
        assertEquals(node1, node3.getParent());
        assertEquals(node2, node4.getParent());
        assertNull(node1.getParent());

        // Test children list
        assertTrue(node1.getChildren().contains(node2));
        assertTrue(node1.getChildren().contains(node3));
        assertTrue(node2.getChildren().contains(node4));
        assertEquals(2, node1.getChildren().size());
        assertEquals(1, node2.getChildren().size());
        assertEquals(0, node4.getChildren().size());
    }

    @Test
    public void testBalancing() {
        // Test Left-Left case
        tree.insert(node4); // depth 3
        tree.insert(node2); // depth 2
        tree.insert(node1); // depth 1

        AVLTreeNode root = tree.getRoot();
        assertEquals(2, root.operation.getId());
        assertTrue(Math.abs(tree.getBalance(root)) <= 1);

        // Verify parent-child relationships are maintained after rotation
        assertNotNull(node4.getParent());
        assertNotNull(node2.getParent());
    }

    @Test
    public void testSimulateProduction() {
        // Insert nodes with different depths and types
        tree.insert(node4); // item, depth 3
        tree.insert(node2); // operation, depth 2
        tree.insert(node3); // operation, depth 2
        tree.insert(node1); // operation, depth 1
        tree.insert(node5); // item, depth 1

        List<ProductionNode> productionOrder = tree.simulateProduction();

        assertEquals(5, productionOrder.size());

        // Verify depth ordering
        for (int i = 0; i < productionOrder.size() - 1; i++) {
            assertTrue(productionOrder.get(i).getDepth() <= productionOrder.get(i + 1).getDepth());
        }

        // Verify ID ordering within same depth
        for (int i = 0; i < productionOrder.size() - 1; i++) {
            if (productionOrder.get(i).getDepth() == productionOrder.get(i + 1).getDepth()) {
                assertTrue(productionOrder.get(i).getId() < productionOrder.get(i + 1).getId());
            }
        }
    }

    @Test
    public void testPopulateFromProductionTree() {
        // Create a production tree with both operations and items
        ProductionNode root = new ProductionNode(1, "RootOp", "operation", 10.0);
        ProductionNode child1 = new ProductionNode(2, "ChildItem1", "item", 20.0);
        ProductionNode child2 = new ProductionNode(3, "ChildOp2", "operation", 30.0);

        root.setDepth(0);
        child1.setDepth(1);
        child2.setDepth(1);

        root.addChild(child1);
        root.addChild(child2);

        tree.populateFromProductionTree(root);

        assertEquals(3, tree.getSize());

        // Verify parent-child relationships are maintained
        assertEquals(root, child1.getParent());
        assertEquals(root, child2.getParent());

        List<ProductionNode> productionOrder = tree.simulateProduction();
        assertEquals(root.getId(), productionOrder.get(0).getId());
    }

    @Test
    public void testMixedTypeNodes() {
        // Test handling of both operation and item nodes
        ProductionNode operation1 = new ProductionNode(1, "Operation1", "operation", 10.0);
        ProductionNode item1 = new ProductionNode(2, "Item1", "item", 20.0);
        ProductionNode operation2 = new ProductionNode(3, "Operation2", "operation", 30.0);

        operation1.setDepth(1);
        item1.setDepth(2);
        operation2.setDepth(2);

        tree.insert(operation1);
        tree.insert(item1);
        tree.insert(operation2);

        assertEquals(3, tree.getSize());

        List<ProductionNode> productionOrder = tree.simulateProduction();
        assertEquals("operation", productionOrder.get(0).getType());

        // Verify both types are handled correctly
        boolean hasOperation = false;
        boolean hasItem = false;
        for (ProductionNode node : productionOrder) {
            if (node.getType().equals("operation")) hasOperation = true;
            if (node.getType().equals("item")) hasItem = true;
        }
        assertTrue(hasOperation && hasItem);
    }

    @Test
    public void testQuantityHandling() {
        // Test handling of decimal quantities
        ProductionNode decimalNode = new ProductionNode(6, "DecimalOp", "operation", 10.5);
        decimalNode.setDepth(1);
        tree.insert(decimalNode);

        AVLTreeNode root = tree.getRoot();
        assertEquals(10.5, root.operation.getQuantity(), 0.001);
    }

    @Test
    public void testEmptyTree() {
        assertNull(tree.getRoot());
        assertEquals(0, tree.getSize());

        List<ProductionNode> productionOrder = tree.simulateProduction();
        assertTrue(productionOrder.isEmpty());
    }
}