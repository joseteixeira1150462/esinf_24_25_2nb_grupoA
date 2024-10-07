package esinf.utils;

import esinf.model.Item;
import esinf.model.Machine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DataLoader class is responsible for loading data from CSV files
 * into Item and Machine objects.
 * It provides methods to parse CSV files and convert them into
 * lists of Item or Machine instances.
 */

public class DataLoader {

    /**
     * Loads a list of items from a CSV file.
     * Each line of the CSV represents an item with an id, priority,
     * and a set of operations.
     *
     * @param fileName the path to the CSV file containing the items
     * @return a list of Item objects loaded from the file
     * @throws RuntimeException if an I/O error occurs during file reading
     */
    public static List<Item> loadItemsFromCsv(String fileName) {
        List<Item> items = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                // Ignorar linhas em branco
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                // Verificar se a linha tem o formato correto
                if (isValidLine(values)) {
                    continue; // Ignorar linhas incompletas
                }

                String id = values[0]; //id_item
                String priority = values[1]; //priority
                List<String> operations = new ArrayList<>();

                // Adicionar as operações não vazias à lista
                for (int i = 2; i < values.length; i++) {
                    if (!values[i].trim().isEmpty()) {
                        operations.add(values[i].trim());
                    }
                }
                items.add(new Item(id, priority, operations));
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return items;
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

                String[] values = line.split(",");
                // Verificar se há pelo menos duas colunas (id_machine e operationName)
                if (isValidLine(values)) {
                    continue; // Ignorar linhas incompletas
                }

                String id = values[0];
                String operationName = values[1];
                int time = Integer.parseInt(values[2].trim()); //time in minutes

                machines.add(new Machine(id, operationName, time));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return machines;
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
