package esinf.model;

import java.util.*;

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

    public Map<String,Integer> calculateTotalProductionInMinutes(List<Item> items) {
        //Basic condition
        if(items.isEmpty() || !(items.equals(this.items))) {
            return null;
        }
        Map<String,Integer> reportTotalCostForItem = new HashMap<>(); //dummy way to sort the map by key
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for(Item item : items) {
            String key = item.getId();
            int totalProductionCostByItem = 0;

            for(Machine machine : this.machines) {
                //Verificar se a máquina executa uma operação do item
                if(item.getOperations().contains(machine.getOperationName().trim())) {
                    totalProductionCostByItem += machine.getTime(); // Acumula o tempo da máquina
                }
            }

            reportTotalCostForItem.put(key, totalProductionCostByItem);

        }
        // Ordenar as chaves numericamente com base no número após "it"
        reportTotalCostForItem.entrySet()
                .stream()
                .sorted((entry1,entry2)->{
                    // Extrair o número da string "it"
                    int num1= Integer.parseInt(entry1.getKey().substring(2));
                    int num2 = Integer.parseInt(entry2.getKey().substring(2));
                    return Integer.compare(num1,num2);
                })
                .forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue())); // Adiciona no LinkedHashMap ordenado

        return sortedMap;
    }

}
