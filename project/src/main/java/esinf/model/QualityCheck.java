package esinf.model;

/**
 * Represents a quality check operation within a production process.
 * Each quality check is associated with an operation and a priority level.
 * Higher levels indicate higher priority for the quality check.
 */
public class QualityCheck {

    /**
     * Priority level of the quality check.
     */
    private int level;

    /**
     * Name of the operation associated with the quality check.
     */
    private String operation;

    /**
     * Constructs a QualityCheck instance with the specified operation and level.
     *
     * @param operation the name of the operation
     * @param level     the priority level of the quality check
     */
    public QualityCheck(String operation, int level) {
        this.level = level;
        this.operation = operation;
    }

    /**
     * Gets the priority level of the quality check.
     *
     * @return the priority level of the quality check
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Gets the name of the operation associated with the quality check.
     *
     * @return the name of the operation
     */
    public String getOperation() {
        return this.operation;
    }
}
