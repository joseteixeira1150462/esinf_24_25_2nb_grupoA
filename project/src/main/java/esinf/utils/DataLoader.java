package esinf.utils;

import esinf.model.*;
import esinf.utils.adjacencymapgraph.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The DataLoader class is responsible for loading data from CSV files
 * into Article and Machine objects.
 * It provides methods to parse CSV files and convert them into
 * lists of Article or Machine instances.
 */

public class DataLoader {

    /**
     * Loads a list of items from a CSV file.
     * Each line of the CSV represents an item with an id, priority,
     * and a set of operations.
     *
     * @param fileName the path to the CSV file containing the items
     * @return a list of Article objects loaded from the file
     * @throws RuntimeException if an I/O error occurs during file reading
     */
    public static List<Article> loadArticlesFromCsv(String fileName) {
        List<Article> articles = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                // Ignorar linhas em branco
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(";");
                String id = values[0].trim(); //id_item
                String priority = values[1].trim(); //priority
                List<String> operations = new ArrayList<>();

                // Adicionar as operações não vazias à lista
                for (int i = 2; i < values.length; i++) {
                    if (!values[i].trim().isEmpty()) {
                        operations.add(values[i].trim());
                    }
                }
                articles.add(new Article(id, priority, operations));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }

    /**
     * Loads a list of machines from a CSV file.
     * Each line of the CSV represents a machine with an id, operation name, and execution time.
     *
     * @param fileName the path to the CSV file containing the machines
     * @return a list of Machine objects loaded from the file
     * @throws RuntimeException if an I/O error occurs during file reading
     */
    public static List<Machine> loadMachinesFromCsv(String fileName) {
        List<Machine> machines = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                // Ignorar linhas em branco
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(";");
                // Verificar se há pelo menos duas colunas (id_machine e operationName)
                if (isValidLine(values)) {
                    continue; // Ignorar linhas incompletas
                }

                String id = values[0].trim();
                String operationName = values[1].trim();
                int time = Integer.parseInt(values[2].trim()); //time in minutes

                machines.add(new Machine(id, operationName, time));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return machines;
    }

    //Sprint 2-> items.csv
    public static List<Item> loadItemsFromCSV(String fileName) {
        List<Item> items = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                // Ignorar linhas em branco
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(";");
                String id = values[0].trim();
                String itemName = values[1].trim();

                items.add(new Item(id, itemName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    //Sprint 2 -> operations.csv
    public static List<Operation> loadOperationsFromCSV(String fileName) {
        List<Operation> operations = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                // Ignorar linhas em branco
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(";");
                String id = values[0].trim();
                String itemName = values[1].trim();

                operations.add(new Operation(id, itemName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return operations;
    }

    // Sprint 2 -> boo_v2.csv
    public static ProductionTree loadProductionTreeFromCSV(String fileName, List<Item> items, List<Operation> operations) {
        Map<Integer, ProductionNode> nodeMap = new HashMap<>();
        ProductionNode root = null;
        String line;

        // Create maps for item and operation names
        Map<Integer, String> itemNameMap = items.stream()
                .collect(Collectors.toMap(item -> Integer.parseInt(item.getId()), Item::getName));

        Map<Integer, String> operationNameMap = operations.stream()
                .collect(Collectors.toMap(operation -> Integer.parseInt(operation.getId()), Operation::getName));

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] values = line.split(";");

                ProductionNode operationNode = processPrimaryData(values, nodeMap, operationNameMap, itemNameMap);

                processOperationDependencies(values, operationNode, nodeMap, operationNameMap);

                processConsumedItems(values, operationNode, nodeMap, itemNameMap);

                if (root == null) {
                    root = operationNode;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ProductionTree(root);
    }

    //Sprint3- USEI17
    public static ActivityGraph loadActivityMapGraphFromCSV(String activitiesCsvFilePath) {
        ActivityGraph graph = new ActivityGraph();
        String line;

        // Adiciona o vértice Start
        ActivityVertex startVertex = new ActivityVertex("Start", "Start of process", 0, "", 0, "€");
        graph.addActivity(startVertex);

        try (BufferedReader br = new BufferedReader(new FileReader(activitiesCsvFilePath))) {
            br.readLine(); // Ignora o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Criação do vértice de atividade
                String id = fields[0].trim();
                String description = fields[1];
                int duration = Integer.parseInt(fields[2]);
                String durationUnit = fields[3];
                double cost = Double.parseDouble(fields[4]);
                String costUnit = "€";

                ActivityVertex activity = new ActivityVertex(id, description, duration, durationUnit, cost, costUnit);
                graph.addActivity(activity);

                // Lê e adiciona dependências
                for (int i = 5; i < fields.length; i++) {
                    if (!fields[i].isEmpty()) {
                        String dependenciesField = fields[i];
                        if (dependenciesField.startsWith("\"") && dependenciesField.endsWith("\"")) {
                            dependenciesField = dependenciesField.substring(1, dependenciesField.length() - 1);
                        }
                        String[] dependencies = dependenciesField.split(",");
                        for (String dependencyId : dependencies) {
                            dependencyId = dependencyId.trim().replace("\"", "");
                            ActivityVertex dependency = findVertexById(graph, dependencyId);
                            if (dependency != null) {
                                graph.addDependency(dependency, activity, "Dependency Activity");
                            }
                        }
                    }
                }
            }

            // Conecta o Start às atividades iniciais (sem dependências de entrada)
            for (ActivityVertex vertex : graph.vertices()) {
                if (graph.inDegree(vertex) == 0 && !vertex.equals(startVertex)) {
                    graph.addDependency(startVertex, vertex, "Start Dependency");
                }
            }

            // Adiciona o vértice End
            ActivityVertex endVertex = new ActivityVertex("End", "End of process", 0, "", 0, "€");
            graph.addActivity(endVertex);

            // Conecta o End às atividades finais (sem dependências de saída)
            for (ActivityVertex vertex : graph.vertices()) {
                if (graph.outDegree(vertex) == 0 && !vertex.equals(startVertex) && !vertex.equals(endVertex)) {
                    graph.addDependency(vertex, endVertex, "End Dependency");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    //==============[Private] Helper methods section ===============


    private static ActivityVertex findVertexById(ActivityGraph graph, String id) {
        // Obtém o Iterator
        for (ActivityVertex vertex : graph.vertices()) {
            if (vertex.getId().equals(id)) {
                return vertex;  // Retorna o vértice encontrado
            }
        }
        return null;  // Retorna null se não encontrar o vértice
    }



    private static double parseQuantity(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0; // Retorna 0 se a entrada for nula ou vazia
        }
        try {
            // Remove caracteres indesejados como parênteses e espaços
            String sanitizedValue = value.replaceAll("[()]", "").trim();

            // Substitui vírgulas por pontos e ajusta o formato
            if (sanitizedValue.contains(",")) {
                sanitizedValue = sanitizedValue.replace(",", ".").trim();
            }

            // Converte para double
            return Double.parseDouble(sanitizedValue);
        } catch (NumberFormatException e) {
            // Log para valores inválidos
            System.err.println("Formato inválido de quantidade: " + value);
            return 0; // Retorna 0 em caso de erro
        }
    }

    private static ProductionNode processPrimaryData(String[] values,
                                                     Map<Integer, ProductionNode> nodeMap,
                                                     Map<Integer, String> operationNameMap,
                                                     Map<Integer, String> itemNameMap) {
        try {
            int opId = Integer.parseInt(values[0].trim());
            int itemId = Integer.parseInt(values[1].trim());
            double itemQnt = parseQuantity(values[2].trim());

            // Criação do nó da operação
            ProductionNode operationNode = nodeMap.computeIfAbsent(opId,
                    id -> new ProductionNode(id, operationNameMap.getOrDefault(id, "Unknown Operation"), "Operation", 0));

            // Criação do nó do item e ligação como child
            ProductionNode itemNode = nodeMap.computeIfAbsent(itemId,
                    id -> new ProductionNode(id, itemNameMap.getOrDefault(id, "Unknown Item"), "Item", itemQnt));
            operationNode.addChild(itemNode);

            return operationNode;
        } catch (NumberFormatException e) {
            System.err.println("Erro ao processar dados principais: " + Arrays.toString(values));
            return null;
        }
    }

    private static void processOperationDependencies(String[] values,
                                                     ProductionNode operationNode,
                                                     Map<Integer, ProductionNode> nodeMap,
                                                     Map<Integer, String> operationNameMap) {
        for (int i = 3; i < values.length; i++) {
            if (values[i].trim().equals("(")) {
                // Encontrar dependências na forma de pares (op_id, op_qtd)
                for (int j = i + 1; j < values.length - 1; j += 2) {
                    String depOpIdStr = values[j].trim();
                    String depOpQntStr = values[j + 1].trim();

                    if (depOpIdStr.isEmpty() || depOpQntStr.isEmpty() || depOpIdStr.equals(")")) break;

                    try {
                        int depOpId = Integer.parseInt(depOpIdStr);
                        double depOpQnt = parseQuantity(depOpQntStr);

                        ProductionNode depOpNode = nodeMap.computeIfAbsent(depOpId,
                                id -> new ProductionNode(id, operationNameMap.getOrDefault(id, "Unknown Operation"), "Operation", depOpQnt));
                        operationNode.addChild(depOpNode);
                    } catch (NumberFormatException e) {
                        System.err.println("Dependência inválida ignorada: " + depOpIdStr + ", " + depOpQntStr);
                    }
                }
                break; // Após encontrar o primeiro bloco de dependências
            }
        }
    }

    private static void processConsumedItems(String[] values,
                                             ProductionNode operationNode,
                                             Map<Integer, ProductionNode> nodeMap,
                                             Map<Integer, String> itemNameMap) {
        for (int i = 10; i < values.length; i++) {
            if (values[i].trim().equals("(")) {
                // Encontrar itens consumidos na forma de pares (item_id, item_qtd)
                for (int j = i + 1; j < values.length - 1; j += 2) {
                    String itemIdStr = values[j].trim();
                    String itemQntStr = values[j + 1].trim();

                    if (itemIdStr.isEmpty() || itemQntStr.isEmpty() || itemIdStr.equals(")")) break;

                    try {
                        int consumedItemId = Integer.parseInt(itemIdStr);
                        double consumedItemQnt = parseQuantity(itemQntStr);

                        ProductionNode consumedItemNode = nodeMap.computeIfAbsent(consumedItemId,
                                id -> new ProductionNode(consumedItemId, itemNameMap.getOrDefault(consumedItemId, "Unknown Item"), "Item", consumedItemQnt));
                        operationNode.addChild(consumedItemNode);
                    } catch (NumberFormatException e) {
                        System.err.println("Item consumido inválido ignorado: " + itemIdStr + ", " + itemQntStr);
                    }
                }
                break;
            }
        }
    }


    /**
     * Validates if a line from the CSV file is correctly formatted.
     * The line is considered valid if it has at least two non-empty columns.
     *
     * @param values the array of strings representing a CSV line split by commas
     * @return true if the line contains at least 2 columns is valid and it's not empty,
     * false otherwise.
     */
    private static boolean isValidLine(String[] values) {
        // A linha é válida se contém pelo menos 2 colunas
        return values.length < 2 || values[0].trim().isEmpty() || values[1].trim().isEmpty();
    }
}
