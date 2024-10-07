package esinf.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Factory class represents a manufacturing plant that manages a list of items and a list of machines.
 * It allows the user to get and set these lists through various methods, as well as initialize the factory
 * with predefined lists of items and machines.
 */
public class Factory {

    /**
     * A list of items that are managed by the Factory.
     * Each item represents an element with its own identifier, priority, and a list of operations.
     */
    private List<Item> items;
    /**
     * A list of Machine objects managed by the Factory.
     * Each Machine represents a piece of equipment with a unique ID, operation name, and operation time.
     */
    private List<Machine> machines;


    /**
     * Constructs a new Factory instance with empty lists for items and machines.
     * This no-argument constructor initializes the factory to have an empty
     * ArrayList for both items and machines.
     */
    public Factory() {
        this.items = new ArrayList<>();
        this.machines = new ArrayList<>();
    }

    /**
     * Constructs a new Factory instance with specified lists of items and machines.
     *
     * @param items the list of items to be managed by the factory
     * @param machines the list of machines to be used by the factory
     */
    public Factory(List<Item> items, List<Machine> machines) {
        this.items = items;
        this.machines = machines;
    }

    /**
     * Retrieves the list of items managed by the factory.
     *
     * @return the list of items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Retrieves the list of machines associated with the factory.
     *
     * @return the list of machines
     */
    public List<Machine> getMachines() {
        return machines;
    }

    /**
     * Sets the list of items for this factory.
     *
     * @param items the list of items to be set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Sets the list of machines for the factory.
     *
     * @param machines the list of Machine objects to be assigned to the factory
     */
    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }
}
