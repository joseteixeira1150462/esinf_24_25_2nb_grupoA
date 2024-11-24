package esinf.model;

import java.util.*;

public class AVLTree {
    private AVLTreeNode root;

    // Insert a new operation or item into the AVL Tree (ordered by depth/dependency level)
    public void insert(ProductionNode node) {
        root = insertNode(root, node);
    }

    // Recursive insert method to add a new node and maintain AVL balance
    private AVLTreeNode insertNode(AVLTreeNode node, ProductionNode operation) {
        if (node == null) {
            return new AVLTreeNode(operation); // Insert new node at the leaf position
        }

        // Insert based on dependency level (depth)
        if (operation.getDepth() < node.operation.getDepth()) {
            node.left = insertNode(node.left, operation);
        } else if (operation.getDepth() > node.operation.getDepth()) {
            node.right = insertNode(node.right, operation);
        } else {
            // If same depth, use ID as secondary ordering criteria
            if (operation.getId() < node.operation.getId()) {
                node.left = insertNode(node.left, operation);
            } else if (operation.getId() > node.operation.getId()) {
                node.right = insertNode(node.right, operation);
            } else {
                return node; // No duplicates
            }
        }

        // Update height after insertion
        node.height = Math.max(height(node.left), height(node.right)) + 1;

        // Balance the tree if necessary
        return balanceNode(node, operation);
    }

    // Balance the node based on balance factor
    private AVLTreeNode balanceNode(AVLTreeNode node, ProductionNode operation) {
        int balance = getBalance(node);

        // Left-Left Case (Right rotation)
        if (balance > 1 && (operation.getDepth() < node.left.operation.getDepth() ||
                (operation.getDepth() == node.left.operation.getDepth() &&
                        operation.getId() < node.left.operation.getId()))) {
            return rightRotate(node);
        }

        // Right-Right Case (Left rotation)
        if (balance < -1 && (operation.getDepth() > node.right.operation.getDepth() ||
                (operation.getDepth() == node.right.operation.getDepth() &&
                        operation.getId() > node.right.operation.getId()))) {
            return leftRotate(node);
        }

        // Left-Right Case (Left-right rotation)
        if (balance > 1 && (operation.getDepth() > node.left.operation.getDepth() ||
                (operation.getDepth() == node.left.operation.getDepth() &&
                        operation.getId() > node.left.operation.getId()))) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left Case (Right-left rotation)
        if (balance < -1 && (operation.getDepth() < node.right.operation.getDepth() ||
                (operation.getDepth() == node.right.operation.getDepth() &&
                        operation.getId() < node.right.operation.getId()))) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Standard AVL Tree operations
    private AVLTreeNode rightRotate(AVLTreeNode y) {
        AVLTreeNode x = y.left;
        AVLTreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLTreeNode leftRotate(AVLTreeNode x) {
        AVLTreeNode y = x.right;
        AVLTreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int height(AVLTreeNode node) {
        return (node == null) ? 0 : node.height;
    }

    // Getter for the root node
    public AVLTreeNode getRoot() {
        return root;
    }

    // Getter for the size of the tree
    public int getSize() {
        return getSizeRecursive(root);
    }

    // Recursive method to calculate the size (number of nodes) of the tree
    private int getSizeRecursive(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + getSizeRecursive(node.left) + getSizeRecursive(node.right);
    }

    int getBalance(AVLTreeNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Method to simulate production by processing nodes in the correct dependency order
    public List<ProductionNode> simulateProduction() {
        List<ProductionNode> productionOrder = new ArrayList<>();
        Map<Integer, List<ProductionNode>> depthMap = new HashMap<>();

        // First, group nodes by depth
        groupNodesByDepth(root, depthMap);

        // Get all depths and sort them
        List<Integer> depths = new ArrayList<>(depthMap.keySet());
        Collections.sort(depths);

        // Process nodes depth by depth (from lowest to highest dependency)
        for (Integer depth : depths) {
            List<ProductionNode> nodesAtDepth = depthMap.get(depth);
            Collections.sort(nodesAtDepth, new Comparator<ProductionNode>() {
                @Override
                public int compare(ProductionNode n1, ProductionNode n2) {
                    return Integer.compare(n1.getId(), n2.getId());
                }
            });
            productionOrder.addAll(nodesAtDepth);
        }

        return productionOrder;
    }

    // Helper method to group nodes by their depth level
    private void groupNodesByDepth(AVLTreeNode node, Map<Integer, List<ProductionNode>> depthMap) {
        if (node != null) {
            int depth = node.operation.getDepth();
            List<ProductionNode> nodesAtDepth = depthMap.get(depth);
            if (nodesAtDepth == null) {
                nodesAtDepth = new ArrayList<>();
                depthMap.put(depth, nodesAtDepth);
            }
            nodesAtDepth.add(node.operation);

            groupNodesByDepth(node.left, depthMap);
            groupNodesByDepth(node.right, depthMap);
        }
    }

    // Method to populate the AVL tree from a production tree
    public void populateFromProductionTree(ProductionNode rootNode) {
        if (rootNode != null) {
            Queue<ProductionNode> queue = new LinkedList<>();
            queue.offer(rootNode);

            while (!queue.isEmpty()) {
                ProductionNode current = queue.poll();
                insert(current);

                // Add all children to the queue
                for (ProductionNode child : current.getChildren()) {
                    queue.offer(child);
                }
            }
        }
    }

    // Method to print the production order after simulating the production process
    public void printProductionOrder() {
        List<ProductionNode> order = simulateProduction();
        System.out.println("Production Order:");
        for (ProductionNode node : order) {
            System.out.println(String.format("Depth: %d, ID: %d, Name: %s",
                    node.getDepth(), node.getId(), node.getName()));
        }
    }

    // Method to print the AVL tree structure for debugging
    public void printAVLTree() {
        System.out.println("AVL Tree Structure:");
        printAVLTreeHelper(root, "", true);
    }

    // Helper method to recursively print the AVL tree structure
    private void printAVLTreeHelper(AVLTreeNode node, String prefix, boolean isLast) {
        if (node != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            ProductionNode operation = node.operation;
            System.out.println("ProductionNode{" +
                    "ID=" + operation.getId() +
                    ", Name='" + operation.getName() + '\'' +
                    ", Type='" + operation.getType() + '\'' +
                    ", Quantity=" + operation.getQuantity() +
                    ", Depth=" + operation.getDepth() +
                    '}');

            printAVLTreeHelper(node.left, prefix + (isLast ? "    " : "│   "), node.right == null);
            if (node.right != null) {
                printAVLTreeHelper(node.right, prefix + (isLast ? "    " : "│   "), true);
            }
        }
    }

    // Method for inorder traversal with indentation for visual representation
    public void inorderTraversal() {
        inorderTraversalHelper(root, 0);
    }

    private void inorderTraversalHelper(AVLTreeNode node, int depth) {
        if (node != null) {
            inorderTraversalHelper(node.left, depth + 1);

            String indentation = new String(new char[depth * 4]).replace('\0', ' ');
            System.out.println(indentation + "→ ProductionNode { " +
                    "ID: " + node.operation.getId() +
                    ", Name: '" + node.operation.getName() + '\'' +
                    ", Type: '" + node.operation.getType() + '\'' +
                    ", Quantity: " + node.operation.getQuantity() +
                    ", Depth: " + depth + " }");

            inorderTraversalHelper(node.right, depth + 1);
        }
    }

}
