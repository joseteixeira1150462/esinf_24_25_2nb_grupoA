package esinf;

import esinf.model.Factory;
import esinf.model.Item;
import esinf.model.Machine;
import esinf.utils.DataLoader;
import esinf.utils.Pair;

import java.util.List;

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
    private static final String ITEMS_CSV_FILE_PATH = "project/src/dataFiles/artigos.csv";
    /**
     * The file path to the CSV file containing machine data.
     * This constant is used when loading machine information
     * from the specified CSV file. Each line in the CSV file
     * represents a machine with attributes such as an identifier,
     * operation name, and execution time.
     */
    private static final String MACHINES_CSV_FILE_PATH = "project/src/dataFiles/maquinas.csv";

    /**
     * The main method serves as the entry point of the application.
     * It is responsible for loading items and machines from CSV files,
     * initializing the factory with the loaded data, and generating reports.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        List<Item> items = DataLoader.loadItemsFromCsv(ITEMS_CSV_FILE_PATH);
        List<Machine> machines = DataLoader.loadMachinesFromCsv(MACHINES_CSV_FILE_PATH);

        if (items.isEmpty()) {
            System.err.println("No items loaded. Please check the CSV file path.");
            return;
        }
        if (machines.isEmpty()) {
            System.err.println("No machines loaded. Please check the CSV path.");
            return;
        }

        Factory factory = new Factory();
        factory.setItems(items);
        factory.setMachines(machines);

        printFactoryDetails(factory);
        printProductionReport(factory);
        printExecutionReport(factory);
        printOperationTimeReport(factory);


    }

    /**
     * Prints details of the given Factory, including its items and machines.
     *
     * @param factory the Factory whose details are to be printed
     */
    private static void printFactoryDetails(Factory factory) {
        System.out.println("==========Factory==========");
        System.out.println("*******Items*******");
        factory.getItems().forEach(System.out::println);

        System.out.println("\n*******Machines*******");
        factory.getMachines().forEach(System.out::println);
    }

    /**
     * Prints a report of the total production time for each item in the factory.
     *
     * @param factory The factory instance containing items and machines for which the report will be generated.
     */
    private static void printProductionReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI03 - Report of the total production time");
        System.out.printf("%n%-12s %s%n", "Item ID", "Total production time (min)");
        System.out.println("--------------------------------------------------------");
        List<Pair<String, Integer>> totalProductionTimeReport = factory.calculateTotalProductionInMinutes(factory.getItems());


        for (Pair<String, Integer> pair : totalProductionTimeReport) {
            System.out.printf("%-12s %d%n", pair.getFirst(), pair.getSecond());
        }
    }

    private static void printExecutionReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI05 - Report of the execution time rate, sorted by percentage");
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


    //US4
    private static void printOperationTimeReport(Factory factory) {
        System.out.println("\n====== Sprint1 --- USEI04 - Report of the total operation time");
        System.out.printf("%n%-12s %s%n", "Operation", "Total operation time (min)");
        System.out.println("--------------------------------------------------------");
        List<Pair<String, Integer>> totalOroductionTimeReport = factory.calculateTotalOperationTimeInMinutes();


        for (Pair<String, Integer> pair : totalOroductionTimeReport) {
            System.out.printf("%-12s %d%n", pair.getFirst(), pair.getSecond());
        }
    }

}
