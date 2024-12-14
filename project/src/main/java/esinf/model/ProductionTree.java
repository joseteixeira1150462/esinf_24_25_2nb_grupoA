package esinf.model;

import java.util.*;
import java.util.ArrayList;
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


    //US13 inicio

    /**
     * Calculates and displays the total quantity of materials and the number of times each operation is performed
     * in the production process.
     * This method traverses the production tree recursively to gather the required data.
     * complexity O(N + M + O)
     * - O(N): Traverses the entire production tree with N nodes.
     * - O(M): Iterates through the materials map, where M is the number of distinct materials.
     * - O(O): Iterates through the operations map, where O is the number of distinct operations.
     */
    public void calculateProductionTotals() {
        HashMap<String, Double> materials = new HashMap<String, Double>();
        HashMap<String, Integer> operations = new HashMap<String, Integer>();

        traverseAndCalculateTotals(root, materials, operations);

        System.out.println("Materials------- ");
        for (Map.Entry<String, Double> material : materials.entrySet()) {
            System.out.println("Material " + material.getKey() + " quantity " + material.getValue());
        }

        System.out.println("Operations------- ");
        for (Map.Entry<String, Integer> operation : operations.entrySet()) {
            System.out.println("Operation " + operation.getKey() + " ntimes " + operation.getValue());
        }
    }

    /**
     * Recursively traverses the production tree to calculate the total quantity of materials
     * and the number of times each operation is performed.
     *
     * @param node       The current node being processed in the production tree.
     * @param materials  A map to store the total quantity of each material.
     * @param operations A map to store the total count of each operation.
     * complexity O(N)
     * - O(N): Traverses the entire production tree with N nodes. Each node is processed once.
     */
    private void traverseAndCalculateTotals(ProductionNode node, Map<String, Double> materials, Map<String, Integer> operations) {
        if (node != null) {

            if (node.getType().equals("Item")) {
                Double quantity = materials.getOrDefault(node.getName(), 0.0);
                materials.put(node.getName(), quantity + node.getQuantity());
            } else if (node.getType().equals("Operation")) {
                Integer totalOps = operations.getOrDefault(node.getName(), 0);
                operations.put(node.getName(), totalOps + 1);
            }

            List<ProductionNode> children = node.getChildren();

            if (children != null && !children.isEmpty()) {
                for (int i = 0; i < children.size(); i++) {
                    traverseAndCalculateTotals(children.get(i), materials, operations);
                }
            }
        }
    }






    //US13 fim

    private static class UpdateResult {
        final Map<Integer, Double> updates;
        final List<String> messages;

        UpdateResult() {
            this.updates = new HashMap<>();
            this.messages = new ArrayList<>();
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

    /**
         * Updates a node's quantity and handles cascading updates to related nodes.
         *
         * @param identifier The ID or name of the node to update
         * @param newQuantity The new quantity to set
         * @return A string describing all updates made
         *
         * Metodo Principal (updateNodeQuantityWithCascade):
            * A busca inicial do nó é O(n) no pior caso (quando procura por nome)
            * A construção da resposta é O(m) onde m é o número de nós atualizados
         *
         * Complexidade das Operações em Cascata:
            * cascadeUp: O(h * s) onde:
                * h é a altura da árvore
                * s é o número médio de irmãos (siblings) por nó
         *
         * cascadeDown: O(d) onde:
            * d é o número total de descendentes do nó sendo atualizado
         *
         * Complexidade Total:
            * No pior caso: O(n + h*s + d)
            * onde n é o número total de nós na árvore
         */
        public String updateNodeQuantityWithCascade(String identifier, double newQuantity) {
            ProductionNode node;

            // Encontrar o nó
            try {
                int id = Integer.parseInt(identifier);
                node = findNodeById(id);
            } catch (NumberFormatException e) {
                node = findNodeByName(identifier);
            }

            if (node == null) {
                return "Error: Node not found for identifier: " + identifier;
            }

            // Verifica se o nó é do tipo "item"
            if (!"item".equalsIgnoreCase(node.getType())) {
                return "Error: Cannot update quantity for node type: " + node.getType() + ". Only items can have quantities.";
            }

            try {
                UpdateResult result = new UpdateResult();

                // Atualiza em forma de cascata
                updateNodeAndDependencies(node, newQuantity, result);

                // Cria a resposta final
                StringBuilder response = new StringBuilder();
                response.append("Cascade Update Results:\n\n");
                response.append("Primary Update:\n");
                response.append(String.format("- %s (ID: %d): %.2f\n\n",
                        node.getName(), node.getId(), newQuantity));

                // Reporta atualizações efetuadas enquanto se corre a tree
                if (result.updates.size() > 1) {
                    response.append("Cascading Updates:\n");
                    for (Map.Entry<Integer, Double> update : result.updates.entrySet()) {
                        if (update.getKey() != node.getId()) {
                            ProductionNode updatedNode = findNodeById(update.getKey());
                            response.append(String.format("- %s (ID: %d): %.2f\n",
                                    updatedNode.getName(), updatedNode.getId(), updatedNode.getQuantity()));
                        }
                    }
                }

                // Aviso sobre mensagens ou erros
                if (!result.messages.isEmpty()) {
                    response.append("\nNotes:\n");
                    for (String message : result.messages) {
                        response.append("- ").append(message).append("\n");
                    }
                }
                return response.toString();
            } catch (IllegalArgumentException e) {
                return "Error: " + e.getMessage();
            }
        }

        /**
         * Atualiza os nós e dependentes de forma recursiva.
         */
        private void updateNodeAndDependencies(ProductionNode node, double newQuantity, UpdateResult result) {
            double racio = node.changeQuantity(newQuantity);
            result.updates.put(node.getId(), newQuantity);

            if (racio == 1) {
                return;
            }
            cascade(node, racio, result);
        }

        private void cascade(ProductionNode node, double racio, UpdateResult result) {
            for (ProductionNode child : node.getChildren()) {
                if ("item".equalsIgnoreCase(child.getType())) {
                    double newChildQuantity = (child.getQuantity() * racio);

                    if (newChildQuantity <= 0.00) {
                        result.messages.add(String.format(
                                "Warning: Attempted negative quantity for %s.",
                                child.getName()));
                        newChildQuantity = 0;
                    }

                    child.setQuantity(newChildQuantity);
                    result.updates.put(child.getId(), newChildQuantity);

                    // Continue cascading down
                    cascade(child, racio, result);
                }
            }
        }

        /**
         * Metodo para atualizar utilizando o numero do ID.
         */
        public String updateNodeQuantityWithCascade(int id, double newQuantity) {
            return updateNodeQuantityWithCascade(String.valueOf(id), newQuantity);
        }

}
