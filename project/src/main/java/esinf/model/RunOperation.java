package esinf.model;

/**
 * The RunOperation class represents an operation being executed by a machine on an article.
 * It stores information about the operation's name, the machine performing the operation,
 * the article being processed, and the start and end times of the operation.
 */
public class RunOperation {


        private String operationName;
        private Machine machine;
        private Article article;
        private int startTime;
        private int endTime;

    /**
     * Constructs a new RunOperation.
     *
     * @param operationName The name of the operation being executed.
     * @param machine The machine performing the operation.
     * @param article The article being processed.
     * @param startTime The start time of the operation.
     */
        public RunOperation(String operationName, Machine machine, Article article, int startTime) {
            this.operationName = operationName;
            this.machine = machine;
            this.article = article;
            this.startTime = startTime;
            this.endTime = startTime + machine.getTime();
        }


        /**
         * Gets the name of the operation being performed.
        *
        * @return The operation name.
        */
        public String getOperationName() {
            return this.operationName;
        }
        /**
        * Gets the name of the operation being performed.
        *
        * @return The machine name.
        */
        public Machine getMachine() {
            return this.machine;
        }

        /**
        * Gets the name of the operation being performed.
        *
        * @return The article name.
        */
        public Article getItem() {
            return this.article;
        }

        /**
        * Gets the name of the operation being performed.
        *
        * @return start time.
        */
        public int getStartTime() {
            return this.startTime;
        }

        /**
        * Gets the name of the operation being performed.
        *
        * @return start end.
        */
        public int getEndTime() {
            return this.endTime;
        }

        /**
        * Starts the operation, logging information about the operation and the machine.
        * This method simulates the start of the operation and prints a message.
        */
        public void start() {
            // Usar aqui para guardar estatisticas
            System.out.println("Processando article  " + article.getId() + " na maquina " + machine.getId() + " para a operacao " + operationName + " com tempo " + machine.getTime() + " minutos.");

        }

        /**
        * Ends the operation, logging information about the completion of the operation.
        * This method simulates the end of the operation and prints a message.
        */
        public void end() {
            // Usar aqui para guardar estatisticas
            System.out.println("Terminou article  " + article.getId() + " na maquina " + machine.getId() + " para a operacao " + operationName + " com tempo " + machine.getTime());
        }

         /**
        * Prints the total time taken for an article to complete all its operations.
        *
        * @param totalTime The total time taken to complete all operations for the article.
        */
        public void printTotalTime(int totalTime) {
        System.out.println("Article " + article.getId() + " levou um total de " + totalTime + " minutos para completar todas as operações.");
        }


        /**
        * Returns a string representation of the operation.
        *
        * @return A string representing the operation, including the operation name, machine, and article.
        */
        @Override
        public String toString() {
            return "Operation{" +
                    "operationName='" + operationName + '\'' +
                    ", machine='" + machine.getId() + '\'' +
                    ", article=" + article.getId() +
                    '}';
        }
}
