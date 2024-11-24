package esinf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static class UpdateResult {
        final Map<Integer, Double> updates;
        final List<String> messages;

        UpdateResult() {
            this.updates = new HashMap<>();
            this.messages = new ArrayList<>();
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
            double quantityDiff = node.changeQuantity(newQuantity);
            result.updates.put(node.getId(), newQuantity);

            if (quantityDiff == 0) {
                return;
            }
            cascade(node, quantityDiff, result);
        }

        private void cascade(ProductionNode node, double quantityDiff, UpdateResult result) {
            for (ProductionNode child : node.getChildren()) {
                if ("item".equalsIgnoreCase(child.getType())) {
                    double newChildQuantity = child.getQuantity() +
                            (quantityDiff * child.getMultiplier());

                    if (newChildQuantity < 0) {
                        result.messages.add(String.format(
                                "Warning: Attempted negative quantity for %s. Kept at minimum value.",
                                child.getName()));
                        newChildQuantity = 0;
                    }

                    child.setQuantity(newChildQuantity);
                    result.updates.put(child.getId(), newChildQuantity);

                    // Continue cascading down
                    cascade(child, quantityDiff, result);
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
