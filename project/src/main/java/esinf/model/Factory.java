package esinf.model;

import esinf.utils.Pair;

import java.util.*;

/**
 * The Factory class is responsible for managing a list of articles and a list of machines.
 * It provides functionalities to initialize the factory with these lists, retrieve and update them,
 * as well as to calculate and sort production related data.
 */
public class Factory {

    /**
     * A list of articles that are managed by the Factory.
     * Each item represents an element with its own identifier, priority, and a list of operations.
     */
    private List<Article> articles;
    /**
     * A list of Machine objects managed by the Factory.
     * Each Machine represents a piece of equipment with a unique ID, operation name, and operation time.
     */
    private List<Machine> machines;

    private List<Item> items;

    private List<Operation> operations;

    /**
     * A map to manage the available machines for each operation.
     * Key: Operation name (e.g., "OperA") -> Value: Queue of machines (e.g., [mac1, mac2, mac3])
     * Example:
     * K: OperA -> V: [mac1, mac2, mac3]
     * K: OperB -> V: [mac3, mac4, mac5]
     */
    private Map<String,Queue<Machine>> availableMachines;

    /**
     * A map to manage the operation queues for each operation.
     * Key: Operation name (e.g., "OperA") -> Value: Queue of articles to be processed (e.g., [item1, item2, item3])
     * K: OperA -> V: [item1, item2, item3]
     * K: OperB -> V: [item4, item2, item5]
     */
    private Map<String, Queue<Article>> operationQueues;

    /**
     * A map to track the progress of each item during the simulation.
     * Key: Article -> Value: Index of the current operation being processed for the item.
     * This map helps to know which operation each item is currently in.
     */
    private Map<Article, Integer> itemProgress;





    /**
     * Constructs a new Factory instance with empty lists for articles and machines.
     * This no-argument constructor initializes the factory to have an empty
     * ArrayList for both articles and machines.
     */
    public Factory() {
        this.articles = new ArrayList<>();
        this.machines = new ArrayList<>();
        this.availableMachines = new HashMap<>();
        this.operationQueues = new HashMap<>();
        this.itemProgress = new HashMap<>();
    }

    /**
     * Constructs a new Factory instance with specified lists of articles and machines.
     *
     * @param articles the list of articles to be managed by the factory
     * @param machines the list of machines to be used by the factory
     */
    public Factory(List<Article> articles, List<Machine> machines) {
        this.articles = articles;
        this.machines = machines;
    }

    public Factory(List<Article> articles, List<Machine> machines,List<Item> items, List<Operation> operations) {
        this.articles = articles;
        this.machines = machines;
        this.items = items;
        this.operations = operations;
    }



    /**
     * Retrieves the list of articles managed by the factory.
     *
     * @return the list of articles
     */
    public List<Article> getArticles() {
        return articles;
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
     * Sets the list of articles for this factory.
     *
     * @param articles the list of articles to be set
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Sets the list of machines for the factory.
     *
     * @param machines the list of Machine objects to be assigned to the factory
     */
    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    /**
     * Calculates the total production time in minutes for each item in the provided list.
     * Each item's total production time is accumulated based on the operations it requires
     * and the machines available that can perform those operations.
     *
     * @param articles a list of articles for which the total production time needs to be calculated
     * @return a list of pairs, where each pair contains the item ID as a string and the total production time in minutes as an integer.
     *         If the provided articles list is empty or does not match the factory's articles list, an empty list is returned.
     * Time Complexity : O(n^2) (bubbleSortListByID complexity predominance)
     * Space Complexity : O(1)
     */
    public List<Pair<String,Integer>> calculateTotalProductionInMinutes(List<Article> articles) {
        //Basic condition --
        if(articles.isEmpty() || !(articles.equals(this.articles))) {
            return Collections.emptyList();
        }
       List<Pair<String,Integer>> productionList = new ArrayList<>();

        for(Article article : articles) {
            String key = article.getId();
            int totalProductionCostByItem = 0;

            for(Machine machine : this.machines) {
                //Verificar se a máquina executa uma operação do article
                if(article.getOperations().contains(machine.getOperationName().trim())) {
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
     * Time Complexity: O(n^2) (due to bubble sort)
     * Space Complexity: O(1)
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
     * Records the flow of machine processing by tracking how articles move between machines.
     *
     * @return a sorted map where the key is the machine ID and the value is another map,
     *         which contains the next machine and the count of transitions.
     * Time Complexity: O(m * n), where m is the number of articles and n is the average number of operations per item
     * Space Complexity: O(m * n)
     */
    public Map<String, Map<String, Integer>> recordMachineProcessingFlow() {
        Map<String, List<String>> itemMachineFlowMap = trackItemMachineSequence();
        Map<String, Map<String, Integer>> machineFlowMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : itemMachineFlowMap.entrySet()) {
            List<String> machineSequence = entry.getValue();

            for (int i = 0; i < machineSequence.size() - 1; i++) {

                String currentMachineId = machineSequence.get(i);
                String nextMachineId = machineSequence.get(i + 1);

                machineFlowMap.putIfAbsent(currentMachineId, new HashMap<>());
                Map<String, Integer> transitions = machineFlowMap.get(currentMachineId);


                transitions.put(nextMachineId, transitions.getOrDefault(nextMachineId, 0) + 1);
            }
        }
        return getSortedMachineProcessingFlow(machineFlowMap);
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
     * Time Complexity: O(m * n), where m is the number of machines and n is the number of articles
     * Space Complexity: O(m * n)
     */
    public List<Pair<String, Integer>> calculateTotalOperationTimeInMinutes() {
        if (this.machines.isEmpty()) {
        return Collections.emptyList();
        }

    List<Pair<String, Integer>> operationTimeList = new ArrayList<>();

        for (Machine machine : this.machines) {
        String operationName = machine.getOperationName();
        int totalTimeForOperation = 0;

        for (Article article : this.articles) {
            if (article.getOperations().contains(operationName)) {
                totalTimeForOperation += machine.getTime();
            }
        }

        operationTimeList.add(new Pair<>(operationName, totalTimeForOperation));
        }

        return operationTimeList;
    }

    //US8
    //Executa a simulação
    /**
     * Runs the simulation process. It manages the execution of operations based on the available machines,
     * item priorities, and operation queues. The simulation processes articles operation by operation,
     * assigning available machines to articles, and advancing the clock based on operation completion times.
     * Time Complexity: O(p + o log o), where p is the number of operations and o is the total number of operations performed.
     * Space Complexity: O(m + k + o), where m is the number of articles, k is the number of machines, and o is the number of operations.
     */
    public void runSimulation() {

        if (this.machines.size() == 0 || this.articles.size() == 0)
            return;

        initializeAvailableMachines();
        initializeOperationQueues();

        Queue<RunOperation> sequence = new PriorityQueue<>(Comparator.comparingInt(RunOperation::getEndTime));

        int clock = 1;
        boolean operBeingExecuted = true;

        Map<Article, Integer> itemEndTimes = new HashMap<>();

        while (operBeingExecuted) {

            for (String operation : operationQueues.keySet()) {
                Queue<Article> articles = operationQueues.get(operation);

                while (articles.peek() != null) {
                    Machine machine = availableMachines.get(operation).poll();
                    if (machine == null)
                        break;
                   // Article article = article.poll();

               //     RunOperation runOper = new RunOperation(operation, machine, article, clock);
               //     sequence.add(runOper);
                 //   runOper.start();
                }

            }

            while (sequence.peek() != null && sequence.peek().getEndTime() <= clock) {

                RunOperation endingOpr = sequence.poll();
                endingOpr.end();
                availableMachines.get(endingOpr.getOperationName()).add(endingOpr.getMachine());

                Article article = endingOpr.getItem();
                itemEndTimes.put(article, endingOpr.getEndTime());

                int currentOperationIndex = itemProgress.get(article);
                if (currentOperationIndex + 1 < article.getOperations().size()) {
                    operationQueues.get(article.getOperations().get(currentOperationIndex + 1)).add(article);
                    itemProgress.put(article, currentOperationIndex + 1);
                }
            }

            if (sequence.size() == 0)
                operBeingExecuted = false;
            else
                clock += sequence.peek().getEndTime();
        }

        calculateTotalTimeForItems(itemEndTimes);
    }

    /**
     * Calculates the total time each item took to complete all its operations.
     * The total time is determined by subtracting 1 (start time) from the end time of the last operation.
     *
     * @param itemEndTimes a map where the key is the item and the value is the end time of its last operation.
     * Time Complexity: O(m), where m is the number of articles.
     * Space Complexity: O(m), as it requires storage for the itemEndTimes map.
     */
    public void calculateTotalTimeForItems(Map<Article, Integer> itemEndTimes) {
        LinkedHashMap<Article, Integer> sortedMapById = new LinkedHashMap<>(itemEndTimes);

        for (Article article : sortedMapById.keySet()) {
            int totalTime = sortedMapById.get(article) - 1;
            System.out.println("Article " + article.getId() + " levou um total de " + totalTime + " minutos para completar todas as operações.");
        }
    }

    /**
     * Initializes the operation queues (operationQueues) associated with each operation.
     * Items are organized by priority and added to the queue for the corresponding operation.
     * Time Complexity: O(m + n), where m is the number of articles and n is the number of machines.
     * Space Complexity: O(m + p), where m is the number of articles and p is the number of operations.
     */
    private void initializeOperationQueues() {

        this.operationQueues = new HashMap<String, Queue<Article>>();

        for (Machine machine : this.machines) {
            String operation = machine.getOperationName();
            operationQueues.putIfAbsent(operation, new PriorityQueue<Article>(Comparator.comparing(Article::getPriority, this::comparePriority)));
        }

        for (Article article : this.articles) {
            List<String> operations = article.getOperations();
            if (operations == null || operations.isEmpty()) {
                System.err.println("Erro: o article " + article.getId() + " não possui operações definidas.");
            }

            operationQueues.get(operations.get(0)).add(article);
            itemProgress.put(article, 0);
        }
    }

    /**
     * Initializes the queues of available machines (availableMachines) for each operation.
     * Machines are prioritized by their processing time.
     * Time Complexity: O(n), where n is the number of machines.
     * Space Complexity: O(n), as it requires storage for all machines in the availableMachines map.
     */
    private void initializeAvailableMachines() {
        availableMachines = new HashMap<>();
        for (Machine machine : this.machines) {
            String operation = machine.getOperationName();
            availableMachines.putIfAbsent(operation, new PriorityQueue<Machine>(Comparator.comparingInt(Machine::getTime)));
            availableMachines.get(operation).add(machine);
        }
    }

    /**
     * Compares the priority levels of two articles (High, Medium, Low) for correct ordering.
     *
     * @param priority1 The priority level of the first item.
     * @param priority2 The priority level of the second item.
     * @return A negative integer, zero, or a positive integer if the first priority is higher,
     *         equal to, or lower than the second priority.
     * Time Complexity: O(1), as the list of priorities has a fixed length.
     * Space Complexity: O(1).
     */
    private int comparePriority(String priority1, String priority2) {
        List<String> priorities = Arrays.asList("High", "Medium", "Low");
        return Integer.compare(priorities.indexOf(priority1), priorities.indexOf(priority2));
    }



    /**
     * Calculates the total execution time for the given list of machines.
     *
     * @param machines a list of Machine objects for which the total execution time needs to be calculated
     * @return the total execution time in minutes for all machines in the list; returns 0 if the list is empty or does not match the factory's machines list
     * Time Complexity: O(n), where n is the number of machines
     * Space Complexity: O(1)
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
     * Space Complexity: O(1)
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

    /**
     * Tracks the sequence of machines that process each item based on the operations required by the item.
     * For each item, it creates a list of machine IDs that can perform the operations listed in the item.
     *
     * @return a map where the key is the item ID and the value is a list of machine IDs that can process the item.
     *         Each machine is responsible for executing one or more operations required by the item.
     * Time Complexity: O(m * n), where m is the number of articles and n is the number of machines.
     * Space Complexity: O(m * k), where k is the average number of operations per item.
     */
    public Map<String,List<String>> trackItemMachineSequence(){
        Map<String,List<String>> itemMachineFlowMap = new HashMap<>();
        for(Article article : this.articles) {
            List<String> operations = article.getOperations();
            List<String> machineIds = new ArrayList<>();
            for(Machine machine : machines) {
                if(operations.contains(machine.getOperationName().trim())) {
                    machineIds.add(machine.getId());
                }
            }
                    itemMachineFlowMap.put(article.getId(), machineIds);
        }
        return itemMachineFlowMap;
    }

    /**
     * Sorts a map of machine processing flow based on the total number of transitions.
     *
     * @param unsorted the unsorted map where the key is the machine ID and the value is another map of transitions
     * @return a sorted map where the machines with the most transitions appear first
     * Time Complexity: O(n^2), where n is the number of machines
     * Space Complexity: O(1)
     */
    public Map<String, Map<String, Integer>> getSortedMachineProcessingFlow(Map<String, Map<String, Integer>> unsorted) {

        Map<String, Integer> transitionCount = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : unsorted.entrySet()) {
            String machineId = entry.getKey();
            int totalTransitions = 0;

            for (Integer count : entry.getValue().values()) {
                totalTransitions += count;
            }

            transitionCount.put(machineId, totalTransitions);
        }

        // array containing sorted machines by transition
        String[] sortedMachines = new String[transitionCount.size()];
        int index = 0;

        for (String machineId : transitionCount.keySet()) {
            sortedMachines[index++] = machineId; // stores id on array
        }

        //Bubble sort machines in reverse order of occurrences number
        for (int i = 0; i < sortedMachines.length - 1; i++) {
            for (int j = 0; j < sortedMachines.length - 1 - i; j++) {
                if (transitionCount.get(sortedMachines[j]) < transitionCount.get(sortedMachines[j + 1])) {
                    String temp = sortedMachines[j];
                    sortedMachines[j] = sortedMachines[j + 1];
                    sortedMachines[j + 1] = temp;
                }
            }
        }

        //Final map with sorted data
        Map<String, Map<String, Integer>> sortedFlowMap = new LinkedHashMap<>();

        for (String machineId : sortedMachines) {
            sortedFlowMap.put(machineId, unsorted.get(machineId));
        }

        return sortedFlowMap;
    }

    /**
     * Displays the machine with the highest flow dependency, i.e., the machine that has
     * the most transitions from other machines.
     *
     * @param machineFlowMap a map where the key is the machine ID and the value is another map of transitions
     * @return a list containing a single pair, where the key is the machine ID and the value is the number of transitions
     * Time Complexity: O(n), where n is the number of machines
     * Space Complexity: O(1)
     */
    public List<Pair<String,Integer>> displayMachineWithMostFlowDependency(Map<String, Map<String, Integer>> machineFlowMap) {
        String maxMachineId = "";
        int maxTransitions = 0;

        for (Map.Entry<String, Map<String, Integer>> entry : machineFlowMap.entrySet()) {
            String machineId = entry.getKey();
            int totalTransitions = 0;

            // Count transitions
            for (int count : entry.getValue().values()) {
                totalTransitions += count;
            }

            // Updating maxTransitions if there's more transitions to register
            if (totalTransitions > maxTransitions) {
                maxTransitions = totalTransitions;
                maxMachineId = machineId;
            }
        }

        List<Pair<String, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(maxMachineId, maxTransitions));
        return list;
    }


}
