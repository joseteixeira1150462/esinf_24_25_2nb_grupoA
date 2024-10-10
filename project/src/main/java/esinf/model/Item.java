package esinf.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Item class represents an item with an identifier, priority, and a list of operations.
 */
public class Item {
    /**
     * Represents the unique identifier for an item.
     */
    private String id;
    /**
     * Represents the priority level of the item.
     * <p>
     * The priority is used to determine the importance or urgency of the item.
     */
    private String priority;
    /**
     * A list of operations associated with the item.
     */
    private List<String> operations;

    /**
     * Constructs an Item with the specified identifier, priority, and list of operations.
     *
     * @param id the unique identifier for the item
     * @param priority the priority level of the item
     * @param operations the list of operations associated with the item
     */
    public Item(String id, String priority, List<String> operations) {
        this.id = id;
        this.priority = priority;
        this.operations = operations;
    }

    /**
     * Constructs an Item object with a specified id and priority.
     *
     * @param id the unique identifier for the item
     * @param priority the priority level of the item
     */
    public Item(String id, String priority) {
        this.id = id;
        this.priority = priority;
        this.operations = new ArrayList<String>();
    }

    /**
     * Retrieves the unique identifier for this item.
     *
     * @return the unique identifier of this item
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the priority level of the item.
     *
     * @return the priority level of the item
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Gets the list of operations associated with the item.
     *
     * @return the list of operations
     */
    public List<String> getOperations() {
        return operations;
    }

    /**
     * Sets the unique identifier for the item.
     *
     * @param id the new identifier for the item
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the priority level of the item.
     *
     * @param priority the new priority level to be set for the item
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Sets the list of operations associated with the item.
     *
     * @param operations the list of operations to be set
     */
    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    /**
     * Adds a new operation to the list of operations associated with the item.
     *
     * @param operation The operation to be added to the item's list of operations.
     */
    public void addOperation(String operation) {
        this.operations.add(operation.trim());
    }

    /**
     * Returns a string representation of the Item object.
     *
     * @return a string representation of the Item object, including its ID, priority, and operations.
     */
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", priority='" + priority + '\'' +
                ", operations=" + operations.toString() +
                '}';
    }
}
