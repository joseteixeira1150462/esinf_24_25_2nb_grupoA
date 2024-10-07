package esinf;

import esinf.model.Factory;
import esinf.model.Item;
import esinf.model.Machine;
import esinf.utils.DataLoader;

import java.util.List;

/**
 * The App class is the starting point for loading data from CSV files
 * and initializing the factory with items and machines. It uses the
 * DataLoader class to load Item and Machine objects from specified CSV files.
 * Then, it sets up the Factory instance with these items and machines,
 * and prints their details to the console.
 */
public class App
{
    /**
     * Represents the file path to the CSV file containing item data.
     * This file is used by the {@link DataLoader#loadItemsFromCsv(String)}
     * method to load a list of items into the application.
     */
    private static final String ITEMS_FILE_CSV = "src/dataFiles/artigos.csv";
    /**
     * The filename and path for the CSV file that contains machine data.
     * This file is expected to be located in the 'src/dataFiles' directory and named 'maquinas.csv'.
     * It is used by the DataLoader class to load machine information into the application.
     */
    private static final String MACHINES_FILE_CSV = "src/dataFiles/maquinas.csv";

    public static void main( String[] args )
    {
        List<Item> items = DataLoader.loadItemsFromCsv(ITEMS_FILE_CSV);
        List<Machine> machines = DataLoader.loadMachinesFromCsv(MACHINES_FILE_CSV);

        Factory factory = new Factory();
        factory.setItems(items);
        factory.setMachines(machines);


        System.out.println("==========Factory==========");
        System.out.println("*******Items*******");
        for(Item item : factory.getItems()){System.out.println(item);}
        System.out.println("\n");
        System.out.println("*******Machines*******");
        for(Machine machine : factory.getMachines()){System.out.println(machine);}
    }
}
