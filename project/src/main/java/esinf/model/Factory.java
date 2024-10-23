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
     * Time Complexity : O(n^2) (bubbleSortListByID complexity predominance)
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
                    totalProductionCostByItem += machine.getTime();
                }
            }

            productionList.add(new Pair<>(key, totalProductionCostByItem));

        }

        //Reverter a ordem da lista, para garantir que a ordenação ascendente do metodo bubbleSortListByID funcione.
        Collections.reverse(productionList);
        bubbleSortListByID(productionList);

        return productionList;
    }


    /**
     * Generates a report on the operation times for a list of machines.
     * Each machine's execution time and its percentage contribution to the total execution time
     * are included in the report.
     *
     * @param machines the list of Machine objects for which the report is generated
     * @return a list of pairs, where each pair contains a machine ID as a string and another pair.
     *         The nested pair consists of the execution time of the machine (integer) and its
     *         percentage contribution to the total execution time (double).
     *         If the provided machines list is empty or does not match the factory's machines list,
     *         an empty list is returned.
     */
    public List<Pair<String,Pair<Integer,Double>>> operationTimeReport(List<Machine> machines) {
        if (machines.isEmpty() || !(machines.equals(this.machines))) {
            return Collections.emptyList();
        }

        List<Pair<String,Pair<Integer,Double>>> percentageContList = new ArrayList<>();
        int totalExecutionTime = calculateFullExecutionTime(machines);
        double percentContribution;

        for (Machine machine : machines) {
            String machineId = machine.getId();
            int executionTime = machine.getTime();
            percentContribution = ((double) machine.getTime() / totalExecutionTime) * 100;
            percentageContList.add(new Pair<>(machineId, new Pair<>(executionTime,percentContribution)));
        }

        bubbleSortListByPercentage(percentageContList);

        return percentageContList;
    }

    /**
     * Calculates the total execution time for all the machines in this factory.
     *
     * @return the total execution time in minutes for all the machines in the factory.
     */
    public int totalExecutionTime() {
        return calculateFullExecutionTime(this.machines);
    }



    //US4
    /**
     * US4 - Calculates the total execution time for each operation in the factory.
     *
     * This method iterates over all available machines, checks the operations they perform,
     * and sums the total time spent on each operation. The result is a list of pairs where
     * the first element is the operation name and the second is the total time in minutes.
     *
     * @return a list of pairs (operation name, total time in minutes) for each operation performed in the factory.
     *         Returns an empty list if no machines are available.
     */
    public List<Pair<String, Integer>> calculateTotalOperationTimeInMinutes() {
        if (this.machines.isEmpty()) {
        return Collections.emptyList();
        }

    List<Pair<String, Integer>> operationTimeList = new ArrayList<>();

        for (Machine machine : this.machines) {
        String operationName = machine.getOperationName();
        int totalTimeForOperation = 0;

        for (Item item : this.items) {
            if (item.getOperations().contains(operationName)) {
                totalTimeForOperation += machine.getTime();
            }
        }

        operationTimeList.add(new Pair<>(operationName, totalTimeForOperation));
        }

        return operationTimeList;
    }





    /**
     * Calculates the total execution time for the given list of machines.
     *
     * @param machines a list of Machine objects for which the total execution time needs to be calculated
     * @return the total execution time in minutes for all machines in the list; returns 0 if the list is empty or does not match the factory's machines list
     */
    private int calculateFullExecutionTime(List<Machine> machines) {
        if(machines.isEmpty()|| !(machines.equals(this.machines))) {
            return 0;
        }

        int fullExecutionTime = 0;
        for(Machine machine : machines) {
            int time = machine.getTime();
            fullExecutionTime += time;

        }
        return fullExecutionTime;
    }

    /**
     * Sorts a list of pairs using the bubble sort algorithm based on the numerical part of the item ID.
     * The item ID is assumed to be a string that starts with two characters followed by a number.
     *
     * @param list the list of pairs to be sorted, where each pair consists of a string ID and an integer value.
     * Time Complexity: O(n^2)
     * Space Complexity : O(1)
     */

    private <U> void bubbleSortListByID(List<Pair<String,U>> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Extrai o número contido no id, ignorando o prefixo 'it'
                int num1 = Integer.parseInt(list.get(j).getFirst().substring(2));
                int num2 = Integer.parseInt(list.get(j + 1).getFirst().substring(2));

                // Compara os números extraídos
                if (num1 > num2) {
                    Pair<String, U> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }


    /**
     * Sorts a list of pairs using the bubble sort algorithm based on the second value of the nested pair.
     * The second value in the nested pair is assumed to implement the {@link Comparable} interface for ordering.
     *
     * @param list the list of pairs to be sorted, where each pair contains a first element of type U and
     *             a second element which is a pair of types K and T. The sorting is done based on the T value
     *             within the nested pair.
     * Time Complexity: O(n^2)
     * Space Complexity : O(1)
     */
    private <U,K,T extends Comparable<T>> void bubbleSortListByPercentage(List<Pair<U, Pair<K, T>>> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Pair<K, T> second1 = list.get(j).getSecond();
                Pair<K, T> second2 = list.get(j + 1).getSecond();

                if (second1.getSecond().compareTo(second2.getSecond()) > 0) {
                    Pair<U, Pair<K, T>> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

}
