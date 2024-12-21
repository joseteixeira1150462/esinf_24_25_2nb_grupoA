package esinf.utils;

import esinf.model.ActivityGraph;
import esinf.model.ActivityVertex;
import esinf.model.Article;
import esinf.model.Machine;
import esinf.utils.adjacencymapgraph.Edge;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * The DataLoaderTest class is a JUnit test case that verifies the functionality of the DataLoader class.
 * It focuses on testing the loading of items and machines from CSV files.
 */
public class DataLoaderTest extends TestCase {

    /**
     * Tests the DataLoader's ability to load items from a CSV file.
     * This method creates a temporary CSV file with sample data,
     * utilizes the DataLoader's loadItemsFromCsv method to retrieve
     * items from the file, and validates the loaded items against
     * the expected values.
     *
     * The temporary CSV file is deleted after the test completes.
     *
     * @throws IOException if an I/O error occurs during file manipulation.
     */
    public void testLoadArticlesFromCsv() {
        try (FileWriter file = new FileWriter("testItems.csv")) {
            file.write("id,priority\n");
            file.write("testId1,testPriority1\n");
        } catch (IOException e) {
            fail("Exception thrown during test: " + e.toString());
        }

        List<Article> articles = DataLoader.loadArticlesFromCsv("testItems.csv");

        assertEquals(1, articles.size());
        assertEquals("testId1", articles.get(0).getId());
        assertEquals("testPriority1", articles.get(0).getPriority());

        // Remove the test file from the project
        deleteFile(new File("testItems.csv"));
    }

    /**
     * Verifies the DataLoader's capability to load machine data from a CSV file.
     *
     * This method:
     * 1. Creates a temporary CSV file with predefined machine data.
     * 2. Uses the DataLoader's loadMachinesFromCsv method to read and parse the CSV file.
     * 3. Validates the parsed machine data against the expected values.
     * 4. Cleans up the temporary file after the test execution.
     *
     * Asserts:
     * - The number of machines loaded from the CSV is as expected.
     * - The attributes of the loaded machine match the predefined values.
     */
    public void testLoadMachinesFromCsv() {
        try (FileWriter file = new FileWriter("testMachines.csv")) {
            file.write("id,operationName,time\n");
            file.write("testId1,testOperation1,3\n");
        } catch (IOException e) {
            fail("Exception thrown during test: " + e.toString());
        }

        List<Machine> machines = DataLoader.loadMachinesFromCsv("testMachines.csv");

        assertEquals(1, machines.size());
        assertEquals("testId1", machines.get(0).getId());
        assertEquals("testOperation1", machines.get(0).getOperationName());
        assertEquals(3, machines.get(0).getTime());

        // Remove the test file from the project
        deleteFile(new File("testMachines.csv"));
    }

    public void testLoadActivityMapGraphFromCSV() {
        try (FileWriter file = new FileWriter("testActivityMapGraph.csv")) {
            file.write("ActivKey,descr,duration,duration-unit,tot-cost,predecessors\n");
            file.write("A,High level analysis,1,week,30,\n");
            file.write("B,Order Hardware platform,4,week,2500,\n");
            file.write("C,Installation and commissioning of hardware,2,week,1250,B\n");
            file.write("D,Detailed analysis of core modules,3,week,30,A\n");
            file.write("E,Detailed analysis of supporting modules,2,week,30,D\n");
            file.write("F,Programming of core modules,4,week,20,C,D\n");
            file.write("G,Programming of supporting modules,3,week,20,E,F\n");
            file.write("H,Quality assurance of core modules,2,week,30,F\n");
            file.write("I,Quality assurance of supporting modules,1,week,30,G\n");
            file.write("J,Application Manual,1,week,550,G\n");
            file.write("K,User Manual,1,week,750,G\n");
            file.write("L,Core and supporting module training,2,week,1500,H,I,K\n");
        } catch (IOException e) {
            fail("Erro ao criar o arquivo CSV de teste: " + e.getMessage());
        }

        ActivityGraph graph = DataLoader.loadActivityMapGraphFromCSV("testActivityMapGraph.csv");

        assertNotNull(graph);
        assertEquals(14, graph.numVertices());
        assertEquals(18, graph.numEdges());

        assertVerticesExist(graph, "Start", "End", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");
        assertEdgesExist(graph,
                "Start", "A", "Start", "B",  // Start → A, Start → B
                "A", "D",                  // A → D
                "D", "E", "D", "F",         // D → E, D → F
                "B", "C",                  // B → C
                "C", "F",                  // C → F
                "E", "G",                  // E → G
                "F", "G", "F", "H",         // F → G, F → H
                "G", "I", "G", "J", "G", "K",  // G → I, G → J, G → K
                "H", "L",                  // H → L
                "I", "L",                  // I → L
                "K", "L",                  // K → L
                "J", "End",                // J → End
                "L", "End");               // L → End
        System.out.println("testLoadActivityMapGraphFromCSV()");
        deleteFile(new File("testActivityMapGraph.csv"));
    }


    private void assertVerticesExist(ActivityGraph graph, String... vertexIds) {
        for (String vertexId : vertexIds) {
            assertNotNull(findVertexById(graph, vertexId));
        }
    }

    private void assertEdgesExist(ActivityGraph graph, String... vertexPairs) {
        if (vertexPairs.length % 2 != 0) {
            throw new IllegalArgumentException("Vertex pairs must be even (start, end).");
        }

        // Iterar pelos pares de vértices
        for (int i = 0; i < vertexPairs.length; i += 2) {
            String startId = vertexPairs[i];
            String endId = vertexPairs[i + 1];

            // Obter os vértices no grafo
            ActivityVertex start = findVertexById(graph, startId);
            ActivityVertex end = findVertexById(graph, endId);

            // Verificar se a aresta existe (start → end)
            Edge<ActivityVertex, String> edge = graph.getEdge(start, end);
            assertNotNull("Edge missing: " + startId + " → " + endId, edge);

            // Opcional: Verificar se não há aresta na direção reversa (A → start // B-> start)
            Edge<ActivityVertex, String> reverseEdge = graph.getEdge(end, start);
            assertNull("Unexpected reverse edge: " + endId + " → " + startId, reverseEdge);
        }
    }


    /**
     * Auxilia a encontrar um vértice pelo ID no grafo.
     */
    private ActivityVertex findVertexById(ActivityGraph graph, String id) {
        return StreamSupport.stream(graph.vertices().spliterator(), false)
                .filter(vertex -> vertex.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    /**
     * Deletes the specified file from the file system.
     * If the file does not exist, an error message is printed.
     * If the file exists and is deleted successfully, a success message is printed.
     * If the file exists but cannot be deleted, a warning message is printed.
     *
     * @param file the File object representing the file to be deleted
     */
    private void deleteFile(File file) {
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                System.out.println("Success: "+file.getName()+".csv has been deleted.");
            } else {
                System.out.println("Warning: "+file.getName()+".csv has not been deleted.");
            }
        } else {
            System.out.println("Error: "+file.getName()+" can not be found or doesn't exist in the system.");
        }
    }
}
