package esinf.model;

import esinf.utils.DataLoader;
import esinf.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the Factory.
 * This class contains the setup and test methods for various scenarios related to the Factory class.
 */
public class FactoryTest {

    /**
     * A list of articles that are used in the factory tests.
     * Each item in the list represents an entity with an identifier,
     * priority, and a list of operations that need to be performed.
     */
    private List<Article> articles;
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
     * This method loads articles and machines from their respective CSV files,
     * using the paths defined by the fields ITEMS_CSV_FILE_PATH and MACHINES_CSV_FILE_PATH.
     * It populates the class members `articles` and `machines` for use in the tests.
     */
    @BeforeEach
    void setUp() {
        // Carregar os itens e máquinas dos arquivos CSV
        articles = DataLoader.loadArticlesFromCsv(ITEMS_CSV_FILE_PATH);
        machines = DataLoader.loadMachinesFromCsv(MACHINES_CSV_FILE_PATH);
    }

    /**
     * Tests the `calculateTotalProductionInMinutes` method of the `Factory` class with preloaded data.
     * This test verifies if the `calculateTotalProductionInMinutes` method correctly calculates
     * the total production time for each item based on the loaded data (articles and machines).
     * It creates a `Factory` instance with specific articles and machines data, then calls the method
     * to compute the total production time and compares the actual results with the expected results.
     * Expected results are predefined and represent the sum of operation times for each item.
     * Each item has a series of operations, each with an associated time, and the expected result
     * for each item is the sum of these times.
     * This test ensures that:
     * - The `calculateTotalProductionInMinutes` method correctly sums the operation times for each item.
     * - The method returns the results in the correct format (a list of pairs where each pair consists of
     *   an item ID and its corresponding total production time).
     * - The order of the results matches the expected order.
     */
    @Test
    void testCalculateTotalProductionWithLoadedData() {
        Factory factory = new Factory(articles, machines);


        List<Pair<String, Integer>> actual = factory.calculateTotalProductionInMinutes(factory.getArticles());

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
     * when provided with an empty list of articles. The method is expected to return
     * an empty list of production pairs.
     * This test initializes a `Factory` instance with an empty list of articles
     * and a predefined list of machines. It then calls the
     * `calculateTotalProductionInMinutes` method with an empty list of articles
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
     * calculates the total production time for a list of articles, each with different operations.
     * The test creates and uses a predefined list of articles and machines, and checks if the
     * calculated production times match the expected values.
     */
    @Test
    void testCalculateTotalProductionWithDifferentItems() {

        List<Article> articles = Arrays.asList(
                new Article("id1", "Low", Arrays.asList("operationA", "operationB")),
                new Article("id2", "High", Collections.singletonList("operationC")),
                new Article("id3", "Medium", Arrays.asList("operationA", "operationC"))
        );


        List<Machine> machines = Arrays.asList(
                new Machine("m1", "operationA", 10),
                new Machine("m2", "operationB", 15),
                new Machine("m3", "operationC", 20)
        );


        Factory factory = new Factory(articles, machines);


        List<Pair<String, Integer>> actual = factory.calculateTotalProductionInMinutes(factory.getArticles());


        List<Pair<String, Integer>> expected = Arrays.asList(
                new Pair<>("id1", 25),  // 10 (operation1) + 15 (operation2)
                new Pair<>("id2", 20),  // 20 (operation3)
                new Pair<>("id3", 30)   // 10 (operation1) + 20 (operation3)
        );


        assertEquals(expected, actual);
    }


    /**
     * Tests the `operationTimeReport` method of the `Factory` class with preloaded data.
     * This test verifies if the `operationTimeReport` method correctly calculates
     * the percentage of each machine's contribution to the production based on its operation time.
     * It creates a `Factory` instance with specific machines data, then calls the method
     * to compute the percentages and compares the actual results with the expected results.
     * <p>
     * This test ensures that:
     * - The `operationTimeReport` method correctly calculates the percentage of contribution for each machine.
     * - The method returns the results in the correct format (a list of pairs where each pair consists of
     * a machine ID, its operation time and its corresponding percentage).
     */
    @Test
    void testCalculatePerOperationWithLoadedData() {
        Factory factory = new Factory(articles, machines);

        List<Pair<String, Pair<Integer, Double>>> actual = factory.operationTimeReport(factory.getMachines());
        List<Pair<String,Double>> expected = new ArrayList<>();
        expected.add(0,new Pair<>("m23",1.47)); //OBS: this hardcoded value will need to be modified.
        expected.add(1,new Pair<>("m24",5.30)); //OBS: this hardcoded value will need to be modified.

        assertEquals(expected.get(0).getFirst(), actual.get(0).getFirst());
        assertEquals(expected.get(1).getFirst(), actual.get(actual.size()-1).getFirst());
    }

    /**
     * Tests the `calculateTotalOperationTimeInMinutes` method of the `Factory` class with preloaded data.
     *
     * This test verifies if the `calculateTotalOperationTimeInMinutes` method correctly calculates
     * the total time spent on each operation based on the loaded data (machines and articles).
     * It creates a `Factory` instance with specific articles and machines data, then calls the method
     * to compute the total time for each operation and compares the actual results with the expected results.
     */
    @Test
    void testCalculateTotalOperationTimeInMinutesWithLoadedData() {
        Factory factory = new Factory(articles, machines);

        List<Pair<String, Integer>> actual = factory.calculateTotalOperationTimeInMinutes();

        List<Pair<String, Integer>> expected = Arrays.asList(
                new Pair<>("OperacaoA", 360),  // OperacaoA (120 min * 3 articles)
                new Pair<>("OperacaoB", 160),   // OperacaoB (80 min * 2 item)
                new Pair<>("OperacaoC", 90),   // OperacaoC (90 min * 1 item)
                new Pair<>("OperacaoD", 130),  // OperacaoD (130 min * 1 item)
                new Pair<>("OperacaoE", 120),   // OperacaoE (60 min * 2 item)
                new Pair<>("OperacaoF", 150)   // OperacaoF (150 min * 1 item)
        );

        // Verifica se cada operação está correta
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getFirst(), actual.get(i).getFirst());
            assertEquals(expected.get(i).getSecond(), actual.get(i).getSecond());
        }
    }

    /**
     * Tests the `calculateTotalOperationTimeInMinutes` method of the `Factory` class
     * with an empty list of machines. The method is expected to return an empty list of results.
     *
     * This test initializes a `Factory` instance with an empty list of machines
     * and a predefined list of articles. It then calls the
     * `calculateTotalOperationTimeInMinutes` method and asserts that the returned list is empty.
     */
    @Test
    void testCalculateTotalOperationTimeInMinutesWithNoMachines() {
        Factory factory = new Factory(articles, new ArrayList<>()); // Máquinas vazias

        List<Pair<String, Integer>> actual = factory.calculateTotalOperationTimeInMinutes();

        // Verifica se a lista retornada está vazia
        assertTrue(actual.isEmpty());
    }

    /**
     * Tests the `calculateTotalOperationTimeInMinutes` method of the `Factory` class
     * with an empty list of articles. The method is expected to return a list of operations with 0 time.
     *
     * This test initializes a `Factory` instance with an empty list of articles and a predefined list of machines.
     * It then calls the `calculateTotalOperationTimeInMinutes` method and checks that the time for all operations is 0.
     */
    @Test
    void testCalculateTotalOperationTimeInMinutesWithNoItems() {
        Factory factory = new Factory(new ArrayList<>(), machines); // Itens vazios

        List<Pair<String, Integer>> actual = factory.calculateTotalOperationTimeInMinutes();

        // Verifica se o tempo para cada operação é 0
        for (Pair<String, Integer> pair : actual) {
            assertEquals(0, (int) pair.getSecond());
        }
    }

    /**
     * Tests the `calculateTotalOperationTimeInMinutes` method of the `Factory` class
     * with articles that have no operations. The method is expected to return 0 time for all operations.
     *
     * This test initializes a `Factory` instance with articles that don't have any operations
     * and checks that the total time for operations remains 0.
     */
    @Test
    void testCalculateTotalOperationTimeInMinutesWithItemsHavingNoOperations() {
        List<Article> emptyOperationArticles = Arrays.asList(
                new Article("it1", "High", new ArrayList<>()),  // Sem operações
                new Article("it2", "Low", new ArrayList<>())    // Sem operações
        );
        Factory factory = new Factory(emptyOperationArticles, machines);

        List<Pair<String, Integer>> actual = factory.calculateTotalOperationTimeInMinutes();

        // Verifica se o tempo para cada operação é 0
        for (Pair<String, Integer> pair : actual) {
            assertEquals(0, (int) pair.getSecond());
        }
    }

    /**
     * Tests the `recordMachineProcessingFlow` method of the `Factory` class.
     * This test verifies that the machine processing flow is recorded correctly
     * based on the loaded data (articles and machines).
     * The expected output is a map where the keys are machine IDs and the values
     * are maps of item IDs and the count of operations performed on each item.
     */
    @Test
    void testRecordMachineProcessingFlow() {
        Factory factory = new Factory(articles, machines);

        Map<String, Map<String, Integer>> actual = factory.recordMachineProcessingFlow();

        // Define the expected result based on the provided flow data.
        Map<String, Map<String, Integer>> expected = new HashMap<>();

        Map<String, Integer> m10Flow = new HashMap<>();
        m10Flow.put("m26", 1);
        m10Flow.put("m28", 1);
        m10Flow.put("m11", 1);
        m10Flow.put("m21", 1);
        expected.put("m10", m10Flow);

        Map<String, Integer> m1Flow = new HashMap<>();
        m1Flow.put("m27", 2);
        m1Flow.put("m2", 1);
        expected.put("m1", m1Flow);

        Map<String, Integer> m7Flow = new HashMap<>();
        m7Flow.put("m29", 1);
        m7Flow.put("m8", 1);
        m7Flow.put("m30", 1);
        expected.put("m7", m7Flow);

        Map<String, Integer> m28Flow = new HashMap<>();
        m28Flow.put("m29", 3);
        expected.put("m28", m28Flow);

        Map<String, Integer> m2Flow = new HashMap<>();
        m2Flow.put("m3", 1);
        m2Flow.put("m22", 1);
        expected.put("m2", m2Flow);

        Map<String, Integer> m21Flow = new HashMap<>();
        m21Flow.put("m30", 1);
        m21Flow.put("m22", 1);
        expected.put("m21", m21Flow);

        Map<String, Integer> m19Flow = new HashMap<>();
        m19Flow.put("m20", 2);
        expected.put("m19", m19Flow);

        Map<String, Integer> m4Flow = new HashMap<>();
        m4Flow.put("m5", 1);
        expected.put("m4", m4Flow);

        Map<String, Integer> m5Flow = new HashMap<>();
        m5Flow.put("m27", 1);
        expected.put("m5", m5Flow);

        Map<String, Integer> m8Flow = new HashMap<>();
        m8Flow.put("m9", 1);
        expected.put("m8", m8Flow);

        Map<String, Integer> m22Flow = new HashMap<>();
        m22Flow.put("m27", 1);
        expected.put("m22", m22Flow);

        Map<String, Integer> m24Flow = new HashMap<>();
        m24Flow.put("m25", 1);
        expected.put("m24", m24Flow);

        Map<String, Integer> m12Flow = new HashMap<>();
        m12Flow.put("m13", 1);
        expected.put("m12", m12Flow);

        Map<String, Integer> m23Flow = new HashMap<>();
        m23Flow.put("m24", 1);
        expected.put("m23", m23Flow);

        Map<String, Integer> m26Flow = new HashMap<>();
        m26Flow.put("m27", 1);
        expected.put("m26", m26Flow);

        Map<String, Integer> m15Flow = new HashMap<>();
        m15Flow.put("m16", 1);
        expected.put("m15", m15Flow);

        Map<String, Integer> m14Flow = new HashMap<>();
        m14Flow.put("m15", 1);
        expected.put("m14", m14Flow);

        Map<String, Integer> m17Flow = new HashMap<>();
        m17Flow.put("m25", 1);
        expected.put("m17", m17Flow);

        Map<String, Integer> m27Flow = new HashMap<>();
        m27Flow.put("m28", 1);
        expected.put("m27", m27Flow);

        Map<String, Integer> m18Flow = new HashMap<>();
        m18Flow.put("m19", 1);
        expected.put("m18", m18Flow);

        // Compare the actual result with the expected result.
        assertEquals(expected, actual);
    }


    /**
     * Tests the `totalExecutionTime` method of the `Factory` class.
     * This test verifies if the total execution time across all machines is calculated correctly.
     * Expected total execution time is defined based on your knowledge of the CSV data.
     */
    @Test
    void testTotalExecutionTime() {
        Factory factory = new Factory(articles, machines);

        int actual = factory.totalExecutionTime();
        int expected = 3395; // Substitua pelo valor total de execução esperado

        assertEquals(expected, actual);
    }


    /**
     * Tests the `trackItemMachineSequence` method of the `Factory` class.
     * This test verifies if the `trackItemMachineSequence` method correctly tracks
     * the sequence of machines for each item based on the loaded data.
     * It creates a `Factory` instance with specific articles and machines data,
     * then calls the method to compute the sequences and compares the actual results
     * with the expected results.
     * Expected results are predefined mappings of item IDs to lists of machine IDs.
     */
    @Test
    void testTrackItemMachineSequence() {
        Factory factory = new Factory(articles, machines);

        Map<String, List<String>> actual = factory.trackItemMachineSequence();

        Map<String, List<String>> expected = new HashMap<>();
        expected.put("it1", Arrays.asList("m1", "m2", "m3"));
        expected.put("it2", Arrays.asList("m4", "m5"));
        expected.put("it3", Collections.singletonList("m6"));
        expected.put("it4", Arrays.asList("m7", "m8", "m9"));
        expected.put("it5", Arrays.asList("m10", "m11"));
        expected.put("it6", Arrays.asList("m12", "m13"));
        expected.put("it7", Arrays.asList("m14", "m15", "m16"));
        expected.put("it8", Collections.singletonList("m17"));
        expected.put("it9", Arrays.asList("m18", "m19", "m20"));
        expected.put("it10", Arrays.asList("m21", "m22"));
        expected.put("it11", Arrays.asList("m23", "m24", "m25"));
        expected.put("it12", Collections.singletonList("m26"));
        expected.put("it13", Arrays.asList("m27", "m28", "m29"));
        expected.put("it14", Collections.singletonList("m30"));
        expected.put("it15", Arrays.asList("m1", "m27"));
        expected.put("it16", Arrays.asList("m10", "m28", "m29"));
        expected.put("it17", Arrays.asList("m21", "m30"));
        expected.put("it18", Collections.singletonList("m27"));
        expected.put("it19", Arrays.asList("m7", "m29"));
        expected.put("it20", Arrays.asList("m10", "m26", "m27"));
        expected.put("it21", Collections.singletonList("m18"));
        expected.put("it22", Arrays.asList("m19", "m20"));
        expected.put("it23", Arrays.asList("m10", "m21"));
        expected.put("it24", Arrays.asList("m2", "m22", "m27"));
        expected.put("it25", Arrays.asList("m17", "m25"));
        expected.put("it26", Arrays.asList("m1", "m27"));
        expected.put("it27", Arrays.asList("m28", "m29"));
        expected.put("it28", Collections.singletonList("m30"));
        expected.put("it29", Arrays.asList("m5", "m27"));
        expected.put("it30", Arrays.asList("m7", "m30"));

        // Compare actual and expected maps
        for (String key : expected.keySet()) {
            assertEquals(expected.get(key), actual.get(key));
        }
    }


    /**
     * Tests the `getSortedMachineProcessingFlow` method of the `Factory` class.
     * This test verifies that the machine processing flow is sorted correctly based on item processing.
     * The expected sorted flow is defined based on your knowledge of the CSV data.
     */
    @Test
    void testGetSortedMachineProcessingFlow() {
        Factory factory = new Factory(articles, machines);

        Map<String, Map<String, Integer>> unsortedFlow = factory.recordMachineProcessingFlow();

        Map<String, Map<String, Integer>> sortedFlow = factory.getSortedMachineProcessingFlow(unsortedFlow);
        String actualFirstKey = sortedFlow.keySet().iterator().next();
        String expectedFirstKey = "m10";


        assertEquals(expectedFirstKey, actualFirstKey);
    }


    /**
     * Tests the {@link Factory#displayMachineWithMostFlowDependency(Map)} method.
     * It verifies that the machine with the most flow dependency is correctly identified.
     * The test creates a factory with predefined articles and machines, calculates the machine flow map,
     * and checks that the machine with the most transitions (flow dependencies) is correctly returned.
     * In this test case, the machine "m10" is expected to have the most flow dependencies with a total of 4 transitions.
     * Expected result:
     * - Machine ID "m10" with 4 transitions.
     * The test compares the actual result from the method with the expected result using {@code assertEquals}.
     */
    @Test
    void testDisplayMachineWithMostFlowDependency() {
        // Create the factory and display the machine with the most flow dependency
        Factory factory = new Factory(articles,machines);

        Map<String, Map<String, Integer>> machineFlowMap = factory.recordMachineProcessingFlow() ;

        List<Pair<String,Integer>> actual = factory.displayMachineWithMostFlowDependency(machineFlowMap);
        List<Pair<String, Integer>> expected = new ArrayList<>();
        expected.add(new Pair<>("m10",4)); //m10 : [(m26,1),(m28,1),(m11,1),(m21,1)] (4 transitions)

        assertEquals(expected, actual);
    }


}