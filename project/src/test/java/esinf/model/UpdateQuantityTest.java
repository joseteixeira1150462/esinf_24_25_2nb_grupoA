package esinf.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductionNodeTest {
        private ProductionNode root;
        private ProductionNode item1;
        private ProductionNode item2;
        private ProductionNode item3;
        private ProductionNode subItem1;
        private ProductionNode subItem2;
        private ProductionTree tree;

        @BeforeEach
        void setUp() {
            // Create a test tree structure
            root = new ProductionNode(1, "Root", "operation",1);
            item1 = new ProductionNode(2, "Item1", "item",100.0);
            item2 = new ProductionNode(3, "Item2", "item",50.0);
            item3 = new ProductionNode(4, "Item3", "item",75.0);
            subItem1 = new ProductionNode(5, "SubItem1", "item",200.0);
            subItem2 = new ProductionNode(6, "SubItem2", "item",300.0);
            tree = new ProductionTree(root);
            // Set up relationships
            root.addChild(item1);
            root.addChild(item2);
            root.addChild(item3);
            item1.addChild(subItem1);
            item1.addChild(subItem2);
        }

        @Test
        void testUpdateByIdSuccess() {
            String result = tree.updateNodeQuantityWithCascade("Item1", 150.0);

//            assertTrue(result.contains("Primary Update:"));
//            assertTrue(result.contains("Item1 (ID: 2): 150.00"));
//            assertTrue(result.contains("Cascading Updates:"));
//            assertTrue(result.contains("SubItem1 (ID: 5): 300.00"));
//            assertTrue(result.contains("SubItem2 (ID: 6): 450.00"));

            assertEquals(150.0, item1.getQuantity());
            assertEquals(300.0, subItem1.getQuantity());
            assertEquals(450.0, subItem2.getQuantity());
        }

        @Test
        void testUpdateByNameSuccess() {
            String result = tree.updateNodeQuantityWithCascade("Item1", 75.0);

            //assertTrue(result.contains("Item1 (ID: 2): 75.00"));
            assertEquals(75.0, item1.getQuantity(), 0.001);
            assertEquals(150.0, subItem1.getQuantity(), 0.001);
            assertEquals(225.0, subItem2.getQuantity(), 0.001);
        }

        @Test
        void testUpdateNonExistentNode() {
            String result = tree.updateNodeQuantityWithCascade("999",50);
            assertTrue(result.contains("Error: Node not found"));
        }

        @Test
        void testUpdateOperationNode() {
            String result = tree.updateNodeQuantityWithCascade("1", 100.0);
            assertTrue(result.contains("Error: Cannot update quantity for node type: operation"));
        }

        @Test
        void testUpdateWithNegativeQuantity() {
            String result = tree.updateNodeQuantityWithCascade("2", -50.0);

            //assertTrue(result.contains("Warning: Attempted negative quantity"));
            assertEquals(200.0, subItem1.getQuantity(), 0.001);
            assertEquals(300.0, subItem2.getQuantity(), 0.001);
        }

        @Test
        void testUpdateWithZeroQuantity() {
            String result = tree.updateNodeQuantityWithCascade("2", 0.0);

            //assertTrue(result.contains("Item1 (ID: 2): 0.00"));
            assertEquals(0.0, item1.getQuantity(), 0.001);
            assertEquals(0.0, subItem1.getQuantity(), 0.001);
            assertEquals(0.0, subItem2.getQuantity(), 0.001);
        }

        @Test
        void testNoChangeInQuantity() {
            item1.setQuantity(100.0);
            String result = tree.updateNodeQuantityWithCascade("2", 100.0);

            assertFalse(result.contains("Cascading Updates:"));
            assertEquals(100.0, item1.getQuantity(), 0.001);
            assertEquals(200.0, subItem1.getQuantity(), 0.001);
            assertEquals(300.0, subItem2.getQuantity(), 0.001);
        }
    }
