package esinf.model;

import esinf.utils.DataLoader;
import esinf.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the Factory.
 * This class contains the setup and test methods for various scenarios related to the Factory class.
 */
public class FactoryTest {

    /**
     * A list of items that are used in the factory tests.
     * Each item in the list represents an entity with an identifier,
     * priority, and a list of operations that need to be performed.
     */
    private List<Item> items;
    /**
     * A list of Machine objects representing the machines used in a factory.
     * Each machine in the list performs a specific operation and has a unique identifier.
     */
    private List<Machine> machines;


    /**
     * The file path to the CSV file containing item data for testing purposes.
     */
    private static final String ITEMS_CSV_FILE_PATH = "src/dataFiles/artigos.csv";
    /**
     * The file path for the CSV file containing machine data.
     * <p>
     * This constant holds the path to a CSV file that stores the information regarding
     * various machines used in the factory. The file is located in the `src/dataFiles` directory
     * and is named `maquinas.csv`.
     */
    private static final String MACHINES_CSV_FILE_PATH = "src/dataFiles/maquinas.csv";

    /**
     * A method to set up the test environment before each test.
     * This method loads items and machines from their respective CSV files,
     * using the paths defined by the fields ITEMS_CSV_FILE_PATH and MACHINES_CSV_FILE_PATH.
     * It populates the class members `items` and `machines` for use in the tests.
     */
    @BeforeEach
    void setUp() {
        // Carregar os itens e máquinas dos arquivos CSV
        items = DataLoader.loadItemsFromCsv(ITEMS_CSV_FILE_PATH);
        machines = DataLoader.loadMachinesFromCsv(MACHINES_CSV_FILE_PATH);
    }

    /**
     * Tests the `calculateTotalProductionInMinutes` method of the `Factory` class with preloaded data.
     *
     * This test verifies if the `calculateTotalProductionInMinutes` method correctly calculates
     * the total production time for each item based on the loaded data (items and machines).
     * It creates a `Factory` instance with specific items and machines data, then calls the method
     * to compute the total production time and compares the actual results with the expected results.
     *
     * Expected results are predefined and represent the sum of operation times for each item.
     * Each item has a series of operations, each with an associated time, and the expected result
     * for each item is the sum of these times.
     *
     * This test ensures that:
     * - The `calculateTotalProductionInMinutes` method correctly sums the operation times for each item.
     * - The method returns the results in the correct format (a list of pairs where each pair consists of
     *   an item ID and its corresponding total production time).
     * - The order of the results matches the expected order.
     */
    @Test
    void testCalculateTotalProductionWithLoadedData() {
        Factory factory = new Factory(items, machines);


        List<Pair<String, Integer>> actual = factory.calculateTotalProductionInMinutes(factory.getItems());

        List<Pair<String, Integer>> expected = Arrays.asList(
                new Pair<>("it1", 290),  // OperacaoA (120) + OperacaoB (80) + OperacaoC (90)
                new Pair<>("it2", 190),  // OperacaoD (130) + OperacaoE (60)
                new Pair<>("it3", 150),  // OperacaoF (150)
                new Pair<>("it4", 350),  // OperacaoG (110) + OperacaoH (140) + OperacaoI (100)
                new Pair<>("it5", 220),  // OperacaoJ (125) + OperacaoK (95)
                new Pair<>("it6", 190),  // OperacaoL (85) + OperacaoM (105)
                new Pair<>("it7", 255),  // OperacaoN (75) + OperacaoO (65) + OperacaoP (115)
                new Pair<>("it8", 135),  // OperacaoQ (135)
                new Pair<>("it9", 370),  // OperacaoR (145) + OperacaoS (155) + OperacaoT (70)
                new Pair<>("it10", 250), // OperacaoU (160) + OperacaoV (90)
                new Pair<>("it11", 325), // OperacaoW (50) + OperacaoX (180) + OperacaoY (95)
                new Pair<>("it12", 110), // OperacaoZ (110)
                new Pair<>("it13", 345), // OperacaoAA (130) + OperacaoBB (140) + OperacaoCC (125)
                new Pair<>("it14", 165), // OperacaoDD (165)
                new Pair<>("it15", 250), // OperacaoA (120) + OperacaoAA (130)
                new Pair<>("it16", 395), // OperacaoJ (125) + OperacaoBB (140) + OperacaoCC (125)
                new Pair<>("it17", 325), // OperacaoDD (165) + OperacaoU (160)
                new Pair<>("it18", 130), // OperacaoAA (130)
                new Pair<>("it19", 360), // OperacaoG (110) + OperacaoCC (125) + OperacaoZ (125)
                new Pair<>("it20", 365), // OperacaoJ (125) + OperacaoZ (110) + OperacaoAA (130)
                new Pair<>("it21", 145), // OperacaoR (145)
                new Pair<>("it22", 225), // OperacaoS (155) + OperacaoT (70)
                new Pair<>("it23", 285), // OperacaoU (160) + OperacaoJ (125)
                new Pair<>("it24", 300), // OperacaoV (90) + OperacaoB (80) + OperacaoAA (130)
                new Pair<>("it25", 230), // OperacaoY (95) + OperacaoQ (135)
                new Pair<>("it26", 250), // OperacaoAA (130) + OperacaoA (120)
                new Pair<>("it27", 395), // OperacaoBB (140) + OperacaoCC (125)
                new Pair<>("it28", 165), // OperacaoDD (165)
                new Pair<>("it29", 190), // OperacaoE (60) + OperacaoAA (130)
                new Pair<>("it30", 275)  // OperacaoG (110) + OperacaoDD (165)
        );

        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getFirst(), actual.get(i).getFirst());
        }
    }

    /**
     * Tests the `calculateTotalProductionInMinutes` method of the `Factory` class
     * when provided with an empty list of items. The method is expected to return
     * an empty list of production pairs.
     *
     * This test initializes a `Factory` instance with an empty list of items
     * and a predefined list of machines. It then calls the
     * `calculateTotalProductionInMinutes` method with an empty list of items
     * and asserts that the returned list is also empty.
     */
    @Test
    void testCalculateTotalProductionWithEmptyItems() {
        List<Machine> machines = Arrays.asList(new Machine("m1", "cut",10), new Machine("m2", "clear",20));
        Factory factory = new Factory(new ArrayList<>(), machines);

        List<Pair<String, Integer>> actual = factory.calculateTotalProductionInMinutes(new ArrayList<>());

        assertEquals(actual, new ArrayList<>());
    }

    /**
     * Tests the method Factory#calculateTotalProductionInMinutes(List) to ensure it correctly
     * calculates the total production time for a list of items, each with different operations.
     * The test creates and uses a predefined list of items and machines, and checks if the
     * calculated production times match the expected values.
     */
    @Test
    void testCalculateTotalProductionWithDifferentItems() {

        List<Item> items = Arrays.asList(
                new Item("id1", "Low", Arrays.asList("operationA", "operationB")),
                new Item("id2", "High", Collections.singletonList("operationC")),
                new Item("id3", "Medium", Arrays.asList("operationA", "operationC"))
        );


        List<Machine> machines = Arrays.asList(
                new Machine("m1", "operationA", 10),
                new Machine("m2", "operationB", 15),
                new Machine("m3", "operationC", 20)
        );


        Factory factory = new Factory(items, machines);


        List<Pair<String, Integer>> actual = factory.calculateTotalProductionInMinutes(factory.getItems());


        List<Pair<String, Integer>> expected = Arrays.asList(
                new Pair<>("id1", 25),  // 10 (operation1) + 15 (operation2)
                new Pair<>("id2", 20),  // 20 (operation3)
                new Pair<>("id3", 30)   // 10 (operation1) + 20 (operation3)
        );


        assertEquals(expected, actual);
    }


}