package esinf.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ProductionTree {
    private ProductionNode root; // Nó raiz da árvore
    private Map<Integer, ProductionNode> nodeMapById; // Mapa para busca por ID
    private Map<String, ProductionNode> nodeMapByName; // Mapa para busca por nome

    // Construtor
    public ProductionTree(ProductionNode root) {
        this.root = root;
        this.nodeMapById = new HashMap<>();
        this.nodeMapByName = new HashMap<>();
        if (root != null) {
            buildNodeMap(root); // Constrói os mapas ao inicializar a árvore
        }
    }

    // Adiciona um nó na árvore
    public void addNode(ProductionNode parent, ProductionNode child) {
        if (parent != null) {
            parent.addChild(child); // Adiciona o filho ao pai
            addToNodeMap(child); // Atualiza os mapas
        } else {
            System.out.println("Parent node couldn't be null.");
        }
    }



    private void addToNodeMap(ProductionNode node) {
        nodeMapById.put(node.getId(), node);
        nodeMapByName.put(node.getName().toLowerCase(), node); // Armazena o nome em minúsculas
    }

    // Constrói os mapas de forma recursiva
    private void buildNodeMap(ProductionNode node) {
        addToNodeMap(node); // Adiciona o nó atual
        for (ProductionNode child : node.getChildren()) {
            buildNodeMap(child); // Adiciona os filhos
        }
    }


    public void printDFSTree() {
        if (root != null) {
            System.out.println("------ Tree Structure ------");
            printNode(root, "", true);
            System.out.println("---------------------------");
        } else {
            System.out.println("Production tree is empty");
        }
    }

    private void printNode(ProductionNode node, String prefix, boolean isLast) {
        if (node != null) {
            // Imprime o prefixo e a ramificação
            System.out.print(prefix);
            if (isLast) {
                System.out.print("└── ");
            } else {
                System.out.print("├── ");
            }

            // Imprime detalhes do nó
            System.out.println(node.toString());

            // Obtém os filhos do nó
            List<ProductionNode> children = node.getChildren();

            // Verifica se os filhos existem antes de processar
            if (children != null && !children.isEmpty()) {
                for (int i = 0; i < children.size(); i++) {
                    // Recursão para imprimir os filhos
                    printNode(children.get(i), prefix + (isLast ? "    " : "│   "), i == children.size() - 1);
                }
            }
        }
    }

    //us14
    // Method to get the root of the tree
    public ProductionNode getRoot() {
        return root;
    }
    // Recursively set the depth for each node starting from the root
    public void setNodeDepths(ProductionNode node, int depth) {
        node.setDepth(depth); // Set the depth of the current node
        for (ProductionNode child : node.getChildren()) {
            setNodeDepths(child, depth + 1); // Increment the depth as we move down the tree
        }
    }
    // Fetch all critical path operations and prioritize them using heap
    public void prioritizeCriticalPath() {
        PriorityQueue<ProductionNode> queue = new PriorityQueue<>((a, b) -> Integer.compare(b.getDepth(), a.getDepth()));
        addOperationsToQueue(root, queue); // Add root and its children to the priority queue

        // Display the critical path in order of importance (by depth)
        System.out.println("Critical Path Operations (Prioritized by Depth):");
        while (!queue.isEmpty()) {
            ProductionNode node = queue.poll();
            System.out.println("Node: " + node.getName() + " (Depth: " + node.getDepth() + ")");
        }
    }

    // Recursive method to add operations to the priority queue
    private void addOperationsToQueue(ProductionNode node, PriorityQueue<ProductionNode> queue) {
        queue.add(node); // Add the node to the heap
        for (ProductionNode child : node.getChildren()) {
            addOperationsToQueue(child, queue); // Add all child operations to the queue
        }
    }

//US15
// Insert operations into AVL tree based on their depth
    public AVLTree buildBooTree() {
        AVLTree avlTree = new AVLTree();
        insertOperationsToAVL(root, avlTree);
        return avlTree;
    }

    // Recursive method to insert operations into AVL tree
    private void insertOperationsToAVL(ProductionNode node, AVLTree avlTree) {
        if (node != null) {
            avlTree.insert(node);  // Insert the node into AVL tree
            for (ProductionNode child : node.getChildren()) {
                insertOperationsToAVL(child, avlTree);  // Insert child nodes recursively
            }
        }
    }

    // Busca eficiente por ID
    public ProductionNode findNodeById(int id) {
        return nodeMapById.get(id); // O(1) com HashMap
    }

    // Busca eficiente por nome (case insensitive)
    public ProductionNode findNodeByName(String name) {
        return nodeMapByName.get(name.toLowerCase()); // O(1) com HashMap
    }


    public String searchNode(String identifier) {
        ProductionNode node;
        try {
            int id = Integer.parseInt(identifier);
            node = findNodeById(id);
        } catch (NumberFormatException e) {
            // Caso contrário, busca pelo nome
            node = findNodeByName(identifier);
        }

        if (node != null) {
            // Build the result
            StringBuilder result = new StringBuilder();
            result.append("[Node Found]\n");
            result.append("ID: ").append(node.getId()).append("\n");
            result.append("Name: ").append(node.getName()).append("\n");
            result.append("Type: ").append(node.getType()).append("\n");

            if ("item".equalsIgnoreCase(node.getType())) {
                result.append("Quantity: ").append(node.getQuantity()).append("\n");
            }

            // Include parent details if available
            if (node.getParent() != null) {
                result.append("Parent Operation: ").append(node.getParent().getName()).append("\n");
            }

            return result.toString();
        } else {
            return "Node not found for identifier: " + identifier;
        }
    }
}
