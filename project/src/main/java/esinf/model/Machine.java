package esinf.model;

/**
 * Represents a machine with a unique ID, an operation name,
 * and the time required to complete the operation.
 */
public class Machine {
    /**
     * The unique identifier for the machine.
     */
    private String id;
    /**
     * The name of the operation that the machine performs.
     */
    private String operationName;
    /**
     * The time required for the machine to complete its operation, measured in minutes.
     */
    private int time;

    /**
     * Constructs a Machine with the specified ID, operation name, and time.
     *
     * @param id the unique identifier for the machine
     * @param operationName the name of the operation the machine performs
     * @param time the time required for the machine to complete its operation, in minutes
     */
    public Machine(String id, String operationName, int time) {
        this.id = id;
        this.operationName = operationName;
        this.time = time;
    }

    /**
     * Retrieves the unique identifier of the Machine.
     *
     * @return The unique identifier of the Machine.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the name of the operation associated with this machine.
     *
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Retrieves the time required to complete the operation.
     *
     * @return the time in minutes required for the operation
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the unique identifier for this machine.
     *
     * @param id the new unique identifier for this machine
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name of the operation performed by the machine.
     *
     * @param operationName the name of the operation to set
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Sets the time required to complete the operation.
     *
     * @param time the time in minutes to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Returns a string representation of the Machine object.
     *
     * @return A string that contains the machine's ID, operation name, and time in minutes.
     */
    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", operationName='" + operationName + '\'' +
                ", time=" + time + " min" +
                '}';
    }
}
