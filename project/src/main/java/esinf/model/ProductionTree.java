package esinf.model;

import java.util.List;

public class ProductionTree {
    private ProductionNode root;


    public ProductionTree(ProductionNode root) {
        this.root = root;
    }


    public void addNode(ProductionNode parent, ProductionNode child) {
        if (parent != null) {
            parent.addChild(child);
        } else {
            System.out.println("Parent node couldn't be null.");
        }
    }

    public ProductionNode getRoot() {
        return root;
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
            // Imprime a linha do nó com o nome, ID, tipo e quantidade
            System.out.print(prefix);

            // Se for o último nó da ramificação, usa o símbolo especial "└──"
            if (isLast) {
                System.out.print("└── ");
            } else {
                System.out.print("├── ");
            }

            // Detalhes do nó (ID, Nome, Tipo, Quantidade)
            System.out.println("ID: " + node.getId() +
                    ", Name: " + node.getName() +
                    ", Type: " + node.getType() +
                    ", Quantity: " + node.getQuantity());

            // Recursão para os filhos, com recuo adequado para a profundidade
            List<ProductionNode> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                printNode(children.get(i), prefix + (isLast ? "    " : "│   "), i == children.size() - 1);
            }
        }
    }


    // Metodo para buscar um nó pelo ID
    public ProductionNode findNodeById(int id) {
        return findNodeByIdRecursive(root, id);
    }

    // Metodo auxiliar recursivo para buscar um nó
    private ProductionNode findNodeByIdRecursive(ProductionNode current, int id) {
        if (current == null) {
            return null;
        }
        if (current.getId() == id) {
            return current;
        }
        for (ProductionNode child : current.getChildren()) {
            ProductionNode result = findNodeByIdRecursive(child, id);
            if (result != null) {
                return result;
            }
        }
        return null; // Retorna nulo se o nó não for encontrado.
    }
}
