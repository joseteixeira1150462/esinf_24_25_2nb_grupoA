package esinf.model;

import esinf.utils.Pair;

import java.util.*;

/**
 * The Factory class is responsible for managing a list of items and a list of machines.
 * It provides functionalities to initialize the factory with these lists, retrieve and update them,
 * as well as to calculate and sort production related data.
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


    /**
     * Calculates the total production time in minutes for each item in the provided list.
     * Each item's total production time is accumulated based on the operations it requires
     * and the machines available that can perform those operations.
     *
     * @param items a list of items for which the total production time needs to be calculated
     * @return a list of pairs, where each pair contains the item ID as a string and the total production time in minutes as an integer.
     *         If the provided items list is empty or does not match the factory's items list, an empty list is returned.
     * Time Complexity : O(n^2) (bubbleSortListByItemID complexity predominance)
     * Space Complexity : O(1)
     */
    public List<Pair<String,Integer>> calculateTotalProductionInMinutes(List<Item> items) {
        //Basic condition --
        if(items.isEmpty() || !(items.equals(this.items))) {
            return Collections.emptyList();
        }
       List<Pair<String,Integer>> productionList = new ArrayList<>();

        for(Item item : items) {
            String key = item.getId();
            int totalProductionCostByItem = 0;

            for(Machine machine : this.machines) {
                //Verificar se a máquina executa uma operação do item
                if(item.getOperations().contains(machine.getOperationName().trim())) {
                    totalProductionCostByItem += machine.getTime(); // Acumula o tempo da máquina
                }
            }

            productionList.add(new Pair<>(key, totalProductionCostByItem));

        }

        //Reverter a ordem da lista, para garantir que a ordenação ascendente do metodo bubbleSortListByItemID funcione.
        Collections.reverse(productionList);
        bubbleSortListByItemID(productionList);

        return productionList;
    }

    /**
     * Sorts a list of pairs using the bubble sort algorithm based on the numerical part of the item ID.
     * The item ID is assumed to be a string that starts with two characters followed by a number.
     *
     * @param list the list of pairs to be sorted, where each pair consists of a string ID and an integer value.
     * Time Complexity: O(n^2)
     * Space Complexity : O(1)
     */
    private void bubbleSortListByItemID(List<Pair<String, Integer>> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int num1 = Integer.parseInt(list.get(j).getFirst().substring(2));
                int num2 = Integer.parseInt(list.get(j + 1).getFirst().substring(2));

                // Compara os números extraídos
                if (num1 > num2) {
                    // Troca os elementos se estiverem na ordem errada
                    Pair<String, Integer> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }


}
