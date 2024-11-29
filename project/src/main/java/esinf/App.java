package esinf;

import esinf.model.*;
import esinf.utils.DataLoader;
import esinf.utils.Pair;
import esinf.utils.adjacencymapgraph.Graph;


import java.util.*;

/**
 * The App class serves as the entry point of the application.
 * It is responsible for loading items and machines from CSV files,
 * initializing the factory with the loaded data, and generating reports.
 */
public class App {
    /**
     * Path to the CSV file containing items data.
     * This file is expected to have entries representing
     * items with their respective identifier, priority, and list of operations.
     */
    private static final String ARTICLES_CSV_FILE_PATH = "project/src/dataFiles/articles.csv";
    /**
     * The file path to the CSV file containing machine data.
     * This constant is used when loading machine information
     * from the specified CSV file. Each line in the CSV file
     * represents a machine with attributes such as an identifier,
     * operation name, and execution time.
     */
    private static final String MACHINES_CSV_FILE_PATH = "project/src/dataFiles/workstations.csv";

    private static final String ITEMS_CSV_FILE_PATH = "project/src/dataFiles/items.csv";

    private static final String OPERATIONS_CSV_FILE_PATH = "project/src/dataFiles/operations.csv";

    private static final String BILL_OF_OPERATIONS_CSV_FILE_PATH = "project/src/dataFiles/boo_v2.csv";

    private static final String ACTIVITIES_CSV_FILE_PATH = "project/src/dataFiles/activities_v0.csv";

    /**
     * The main method serves as the entry point of the application.
     * It is responsible for loading items and machines from CSV files,
     * initializing the factory with the loaded data, and generating reports.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {

        List<Article> articles = DataLoader.loadArticlesFromCsv(ARTICLES_CSV_FILE_PATH);
        List<Machine> machines = DataLoader.loadMachinesFromCsv(MACHINES_CSV_FILE_PATH);
        List<Item> items = DataLoader.loadItemsFromCSV(ITEMS_CSV_FILE_PATH);
        List<Operation> operations = DataLoader.loadOperationsFromCSV(OPERATIONS_CSV_FILE_PATH);

        ProductionTree tree = DataLoader.loadProductionTreeFromCSV(BILL_OF_OPERATIONS_CSV_FILE_PATH,items,operations);
        ActivityGraph activityVertexGraph = DataLoader.loadActivityMapGraphFromCSV(ACTIVITIES_CSV_FILE_PATH);

        if (articles.isEmpty()) {
            System.err.println("No articles loaded. Please check the CSV file path.");
            return;
        }
        if (machines.isEmpty()) {
            System.err.println("No machines loaded. Please check the CSV path.");
            return;
        }

        if (items.isEmpty()) {
            System.err.println("No items loaded. Please check the CSV path.");
            return;
        }

        if (operations.isEmpty()) {
            System.err.println("No operations loaded. Please check the CSV path.");
            return;
        }

        if(activityVertexGraph.numVertices()== 0){
            System.err.println("No activity graph loaded. Please check the CSV path.");
            return;
        }

        Factory factory = new Factory();
        factory.setArticles(articles);
        factory.setMachines(machines);
        factory.setItems(items);
        factory.setOperations(operations);


        System.out.println("====== Sprint3 --- USEI17 - Building PERT-CPM Graph from activities.csv ======\n");
        System.out.println("******* Activity Graph (Homogeneous and Directional) *******\n");
        System.out.println(activityVertexGraph);

        System.out.println("\n====================== Graph Details ======================");
        activityVertexGraph.graphDetails();

        System.out.println("\n===========================================================");


//        //printFactoryDetails(factory);
//        printFactoryDetailsv2(factory);
//
//        System.out.println("\n====== Sprint2 --- USEI08 - Build the complete production tree of a product.");
//        System.out.println("*******Boo Operations (DFS Approach)*******");
//        tree.printDFSTree();
//        System.out.println("====== Sprint2 --- USEI09 - Efficiently search for specific operations or materials.");
//
//        String id_parentRoot = "20";
//        String name_parentRoot = "varnish bench";
//
//        String id_child = "1010";
//        String name_child = "wood 3cm";
//
//        System.out.println("Search node by id -> "+id_child+"\n"+tree.searchNode(id_child));
//        System.out.println("Search node by id [root] -> "+id_parentRoot+"\n"+tree.searchNode(id_parentRoot));
//        System.out.println("Search node by name -> "+name_child+"\n"+tree.searchNode(name_child));
//        System.out.println("Search node by name [root] -> "+name_parentRoot+"\n"+tree.searchNode(name_parentRoot));
//
//
//        System.out.println("\n====== Sprint2 --- USEI13 -  Calculate the total quantity of materials and the number of times each operation is performed");
//        tree.calculateProductionTotals() ;
//
//
//        System.out.println("\n====== Sprint2 --- USEI11 - Prioritize Quality Checks");
//        Queue<QualityCheck> qc = tree.getQualityCheckSequence();
//        int priority = 1;
//        while(qc.peek() != null) {
//            QualityCheck op = qc.poll();
//            System.out.println("priority " + priority + ": level:" + op.getLevel() +  " operation " + op.getOperation());
//            priority += 1;
//        }
//
//        // printProductionReport(factory);
//        // printOperationTimeReport(factory);
//        // printExecutionReport(factory);
//        // runProcessingSimulation(factory);
//
//        //US12
//        System.out.println("\n =====US12 - Update Material Quantity ======");
//        String item = "finished bench";
//        double quantity = 2.0;
//        System.out.println(tree.updateNodeQuantityWithCascade(item, quantity));
//
//
//        // ===== USEI14 - Identify and prioritize critical path operations =====
//        // Set the depths of all operations in the tree
//        System.out.println("\n====== Sprint2 --- USEI14 - Identify and prioritize critical path operations.");
//        tree.setNodeDepths(tree.getRoot(), 0); // Start from root with depth 0
//        // Tree
//        //tree.printDFSTree();
//        // Prioritize critical path and display operations based on depth
//        tree.prioritizeCriticalPath();
//
//        //===== USEI14 Put the components into production in the simulator (AVL Tree)
//        System.out.println("\n====== USEI15 - Put the components into production in the simulator (AVL Tree) ======");
//        AVLTree avlTree = tree.buildBooTree();
//
//        if (avlTree == null) {
//            System.err.println("Error: Failed to build AVL Tree. Check production tree data.");
//            return;
//        }
//
//        System.out.println("\n--- AVL Tree Structure ---");
//        avlTree.printAVLTree();
//
//        System.out.println("\n--- Production Sequence (By Dependency Level) ---");
//        List<ProductionNode> productionOrder = avlTree.simulateProduction();
//        for (ProductionNode node : productionOrder) {
//            // Generate indentation based on depth for visual hierarchy
//            String indentation = new String(new char[node.getDepth() * 4]).replace('\0', ' ');
//            System.out.println(indentation + "→ ProductionNode { " +
//                    "ID: " + node.getId() +
//                    ", Name: '" + node.getName() + '\'' +
//                    ", Type: '" + node.getType() + '\'' +
//                    ", Quantity: " + node.getQuantity() +
//                    ", Depth: " + node.getDepth() + " }");
//        }
        }


    /**
     * Prints details of the given Factory, including its items and machines.
     *
     * @param factory the Factory whose details are to be printed
     */
    private static void printFactoryDetails(Factory factory) {
        System.out.println("==========Factory==========");
        System.out.println("*******Articles*******");
        factory.getArticles().forEach(System.out::println);

        System.out.println("\n*******Machines*******");
        factory.getMachines().forEach(System.out::println);

    }

    private static void printFactoryDetailsv2(Factory factory) {
        System.out.println("==========Factory==========");
        System.out.println("*******Items*******");
        factory.getItems().forEach(System.out::println);

        System.out.println("\n*******Operations*******");
        factory.getOperations().forEach(System.out::println);
    }

    /**
     * Prints a report of the total production time for each item in the factory.
     *
     * @param factory The factory instance containing items and machines for which the report will be generated.
     */
    private static void printProductionReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI03 - Report of the total production time");
        System.out.printf("%n%-12s | %s%n", "Article ID", "Total production time (min)");
        System.out.println("--------------------------------------------------------");
        List<Pair<String, Integer>> totalProductionTimeReport = factory.calculateTotalProductionInMinutes(factory.getArticles());


        for (Pair<String, Integer> pair : totalProductionTimeReport) {
            System.out.printf("%-12s | %d%n", pair.getFirst(), pair.getSecond());
        }
        System.out.println("============================== X ================================");
    }
    /**
     * Prints a report of the execution time rate of machines, sorted by their percentage contribution to total execution time.
     *
     * This method generates and displays a formatted report that includes:
     * - The Machine ID
     * - The execution time in minutes for each machine
     * - The percentage contribution of each machine to the total execution time
     *
     * The report also identifies:
     * - The machine with the highest contribution to execution time percentage
     * - The machine with the lowest contribution to execution time percentage
     *
     * @param factory the Factory object containing the machine data and execution time information
     *
     * The method performs the following tasks:
     * 1. Retrieves and formats execution time data for each machine.
     * 2. Prints a structured table displaying each machine's execution time and its percentage contribution.
     * 3. Calculates and displays the total execution time across all machines.
     * 4. Identifies and displays the machines with the highest and lowest execution time contributions.
     *
     * Time Complexity: O(n log n) due to sorting operations, where n is the number of machines.
     * Space Complexity: O(n) to store the percentage contribution data for reporting.
     */
    private static void printExecutionReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI05 - Report of the execution time rate, sorted by percentage\n");
        System.out.printf("%-12s | %-18s | %-15s%n", "Machine ID", "Execution Time (min)", "% Execution Time");
        System.out.println("------------------------------------------------------------");

        List<Pair<String, Pair<Integer, Double>>> percentageContribution = factory.operationTimeReport(factory.getMachines());

        for (Pair<String, Pair<Integer, Double>> pair : percentageContribution) {
            System.out.printf("%-12s | %-20d | %-15.2f%n",
                    pair.getFirst(),
                    pair.getSecond().getFirst(),
                    pair.getSecond().getSecond());
        }
        int n = percentageContribution.size();
        System.out.println("\nTotal Execution Time: "+factory.totalExecutionTime() +" min");
        System.out.printf("Machine with the highest contribution to execution time percentage: %s (%.2f%%)%n",
                percentageContribution.get(n - 1).getFirst(),
                percentageContribution.get(n - 1).getSecond().getSecond());

        System.out.printf("Machine with the lowest contribution to execution time percentage: %s (%.2f%%)%n",
                percentageContribution.get(0).getFirst(),
                percentageContribution.get(0).getSecond().getSecond());
        System.out.println("============================== X ================================");
    }
    /**
     * Produces a listing representing the flow dependency between machines, sorted in descending order of processed items.
     *
     * This method generates and prints the machine processing flow, which includes:
     * - A listing of the sequence of machines processing each item, sorted by the item IDs in ascending order (ignoring the 'it' prefix).
     * - A listing of each machine and the machines it transitions to, along with the count of transitions, sorted by the number of transitions in descending order.
     * - The machine with the most flow dependency (i.e., the machine that transitions the most to other machines).
     *
     * @param factory the Factory object containing the items and machines data
     *
     * The method performs the following tasks:
     * 1. Tracks the sequence of machines processing each item and prints the flow.
     * 2. Sorts the machine flow dependency based on the number of transitions and prints the flow.
     * 3. Identifies and prints the machine with the most flow dependency.
     *
     * Time Complexity: O(n^2) due to sorting operations and nested loops.
     * Space Complexity: O(n) where n is the number of items and machines processed.
     */
    private static void printRecordMachineProcessingFlow(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI07 - Produce a listing representing the flow dependency between machines sorted in descending order of processed items\n");

        Map<String, List<String>> itemTracking = factory.trackItemMachineSequence();

        List<String> sortedItemIds = new ArrayList<>(itemTracking.keySet());
        sortedItemIds.sort(Comparator.comparingInt(id -> Integer.parseInt(id.substring(2))) //sorting item id ignoring 'it' prefix
        );

        // Display formated data
        for (String itemId : sortedItemIds) {
            List<String> machines = itemTracking.get(itemId);
            String machineSequence = String.join(" -> ", machines);
            System.out.println(itemId + " : " + machineSequence);
        }
        System.out.println("\n");
        Map<String, Map<String, Integer>> machineFlowMap = factory.getSortedMachineProcessingFlow(factory.recordMachineProcessingFlow());

        for (Map.Entry<String, Map<String, Integer>> entry : machineFlowMap.entrySet()) {
            String machineId = entry.getKey(); // machineId
            Map<String, Integer> transitions = entry.getValue();

            System.out.printf("%s : ", machineId);
            System.out.print("[");

            // transitions list data
            List<String> transitionStrings = new ArrayList<>();
            for (Map.Entry<String, Integer> transitionEntry : transitions.entrySet()) {
                String nextMachineId = transitionEntry.getKey(); // next machine ID
                int count = transitionEntry.getValue(); // transition occurrences

                transitionStrings.add("(" + nextMachineId + "," + count + ")");
            }

            //Validate if all transitions was read.
            if (transitionStrings.isEmpty()) {
                System.out.print("No transitions");
            } else {
                System.out.print(String.join(",", transitionStrings)); // join with ','
            }

            System.out.println("]");
        }
        List<Pair<String,Integer>> mostFlowDependencyMachine = factory.displayMachineWithMostFlowDependency(machineFlowMap);
        System.out.println("\nMachine with the most flow dependency: " + mostFlowDependencyMachine.get(0).getFirst() + " -> " + mostFlowDependencyMachine.get(0).getSecond() + " transitions.");
        System.out.println("============================== X ================================");
    }


    private static void printOperationTimeReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI04 - Report of the total operation time");
        System.out.printf("%n%-12s %s%n", "Operation", "Total operation time (min)");
        System.out.println("--------------------------------------------------------");
        List<Pair<String, Integer>> totalOroductionTimeReport = factory.calculateTotalOperationTimeInMinutes();


        for (Pair<String, Integer> pair : totalOroductionTimeReport) {
            System.out.printf("%-12s %d%n", pair.getFirst(), pair.getSecond());
        }
    }

    //US8
    private static void runProcessingSimulation(Factory factory) {
        System.out.println("\n====== US8 - Simulação de Processamento por Prioridade ======");
        factory.runSimulation();
        System.out.println("============================== Fim da Simulação ==============================");
    }

}
