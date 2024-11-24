package esinf.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class ProductionTreeCriticalPathTest {
    private ProductionTree tree;
    private ProductionNode root;
    private ProductionNode operation1;
    private ProductionNode operation2;
    private ProductionNode item1;
    private ProductionNode item2;
    private ProductionNode operation3;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @Before
    public void setUp() {
        // Create a test tree structure
        root = new ProductionNode(1, "RootOperation", "operation", 1.0);
        operation1 = new ProductionNode(2, "Operation1", "operation", 2.0);
        operation2 = new ProductionNode(3, "Operation2", "operation", 3.0);
        item1 = new ProductionNode(4, "Item1", "item", 4.0);
        item2 = new ProductionNode(5, "Item2", "item", 5.0);
        operation3 = new ProductionNode(6, "Operation3", "operation", 6.0);

        tree = new ProductionTree(root);
        tree.addNode(root, operation1);
        tree.addNode(root, operation2);
        tree.addNode(operation1, item1);
        tree.addNode(operation2, item2);
        tree.addNode(operation2, operation3);

        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testSetNodeDepths() {
        // Set depths starting from root
        tree.setNodeDepths(tree.getRoot(), 0);

        // Verify depths are set correctly
        assertEquals(0, root.getDepth());
        assertEquals(1, operation1.getDepth());
        assertEquals(1, operation2.getDepth());
        assertEquals(2, item1.getDepth());
        assertEquals(2, item2.getDepth());
        assertEquals(2, operation3.getDepth());
    }

    @Test
    public void testSetNodeDepthsWithUnbalancedTree() {
        // Create an unbalanced branch
        ProductionNode operation4 = new ProductionNode(7, "Operation4", "operation", 7.0);
        ProductionNode item3 = new ProductionNode(8, "Item3", "item", 8.0);
        tree.addNode(operation3, operation4);
        tree.addNode(operation4, item3);

        // Set depths
        tree.setNodeDepths(tree.getRoot(), 0);

        // Verify depths in the longer branch
        assertEquals(0, root.getDepth());
        assertEquals(2, operation3.getDepth());
        assertEquals(3, operation4.getDepth());
        assertEquals(4, item3.getDepth());
    }

    @Test
    public void testPrioritizeCriticalPath() {
        // Set depths first
        tree.setNodeDepths(tree.getRoot(), 0);

        // Capture the output of prioritizeCriticalPath
        tree.prioritizeCriticalPath();
        String output = outputStreamCaptor.toString().trim();

        // Verify that the output contains all nodes in the correct order
        assertTrue(output.contains("Critical Path Operations (Prioritized by Depth)"));

        // Check order: deeper nodes should appear before shallower nodes
        int indexItem1 = output.indexOf("Item1");
        int indexItem2 = output.indexOf("Item2");
        int indexOperation3 = output.indexOf("Operation3");
        int indexOperation1 = output.indexOf("Operation1");
        int indexOperation2 = output.indexOf("Operation2");
        int indexRoot = output.indexOf("RootOperation");

        // Verify that nodes with depth 2 appear before nodes with depth 1
        assertTrue(indexItem1 < indexOperation1);
        assertTrue(indexItem2 < indexOperation2);
        assertTrue(indexOperation3 < indexOperation2);

        // Verify root appears last (depth 0)
        assertTrue(indexRoot > indexOperation1);
        assertTrue(indexRoot > indexOperation2);
    }

    @Test
    public void testCriticalPathWithSingleNode() {
        // Create a tree with only root node
        ProductionNode singleRoot = new ProductionNode(1, "SingleRoot", "operation", 1.0);
        ProductionTree singleNodeTree = new ProductionTree(singleRoot);

        // Set depth
        singleNodeTree.setNodeDepths(singleNodeTree.getRoot(), 0);

        // Verify critical path
        singleNodeTree.prioritizeCriticalPath();
        String output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("SingleRoot"));
        assertTrue(output.contains("Depth: 0"));
    }



    @Test
    public void testSetNodeDepthsWithEmptyChildren() {
        // Create a node with no children
        ProductionNode leafNode = new ProductionNode(9, "LeafNode", "operation", 9.0);
        tree.addNode(operation1, leafNode);

        // Set depths
        tree.setNodeDepths(tree.getRoot(), 0);

        // Verify leaf node depth
        assertEquals(2, leafNode.getDepth());

        // Verify it doesn't throw exception when trying to process children
        tree.prioritizeCriticalPath();
    }

    @Test
    public void testNodeDepthConsistency() {
        // Set depths multiple times
        tree.setNodeDepths(tree.getRoot(), 0);
        tree.setNodeDepths(tree.getRoot(), 0);

        // Verify depths remain consistent
        assertEquals(0, root.getDepth());
        assertEquals(1, operation1.getDepth());
        assertEquals(1, operation2.getDepth());
        assertEquals(2, item1.getDepth());
        assertEquals(2, item2.getDepth());
        assertEquals(2, operation3.getDepth());
    }
}