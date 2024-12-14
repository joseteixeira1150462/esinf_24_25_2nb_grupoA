package esinf.utils;

import esinf.model.Article;
import esinf.model.Machine;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
