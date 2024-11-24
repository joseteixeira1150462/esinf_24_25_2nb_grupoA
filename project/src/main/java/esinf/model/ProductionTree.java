package esinf.model;

import java.util.*;

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

//US11
    /**
     * Generates a priority queue of quality checks based on the production tree.
     * Quality checks are prioritized by their level in the production process,
     * with higher levels indicating higher priority.
     *
     * @return A priority queue containing quality checks ordered by priority level.
     * Time Complexity: O(N log N)
     * The method goes through each node (O(N)) and adds each "Operation" to the priority queue (O(log N)).
     */
    public Queue<QualityCheck> getQualityCheckSequence() {
        Queue<QualityCheck> qcSequence = new PriorityQueue<QualityCheck>(Comparator.comparingInt(QualityCheck::getLevel));
        addQualityChecksToQueue(root, qcSequence, 0);
        return qcSequence;
    }


    /**
     * Recursively traverses the production tree to add quality checks for operations
     * into the provided priority queue. The priority of each quality check is determined
     * by its level in the tree.
     *
     * @param node The current node in the production tree being processed.
     * @param qc   The priority queue where quality checks are added.
     * @param level The current level of the node in the production tree.
     * Time Complexity: O(N log N)
     * The method goes through each node (O(N)) and adds each "Operation" to the priority queue (O(log N)).
     */
    private void addQualityChecksToQueue(ProductionNode node, Queue<QualityCheck> qc, int level) {
        if (node != null) {

            if (node.getType().equals("Operation")) {
                qc.add(new QualityCheck(node.getName(), level));
            }

            List<ProductionNode> children = node.getChildren();

            if (children != null && !children.isEmpty()) {
                for (int i = 0; i < children.size(); i++) {
                    addQualityChecksToQueue(children.get(i), qc, level+1);
                }
            }
        }
    }

}
