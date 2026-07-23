package models.task;

import interfaces.Completable;
import utils.RegexValidator;
import utils.exceptions.InvalidInputException;

/**
 * Represents an individual project assignment.
 * Implements the {@link interfaces.Completable} interface to track status life cycles.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class Task implements Completable {

    private final String taskID;
    private String taskName;
    private Status status;

    public Task(String taskID, String taskName, Status status) {
        RegexValidator.validateTaskId(taskID);
        if (taskName == null || taskName.strip().isEmpty())
            throw new InvalidInputException("Task name cannot be empty.");
        this.taskID = taskID;
        this.taskName = taskName;
        this.status = status;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Status getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = validateAndNormalizeStatus(status);
    }

    private Status validateAndNormalizeStatus(String status) {
        if (status == null)
            throw new InvalidInputException("Status cannot be null.");

        return switch (status.toLowerCase()) {
            case "c" -> Status.COMPLETED;
            case "i" -> Status.IN_PROGRESS;
            case "p" -> Status.PENDING;
            default -> throw new InvalidInputException("❌ Error: Invalid status. Please choose from [Pending p, In Progress i, Completed c].");
        };
    }

    @Override
    public boolean isCompleted() {
        return Status.COMPLETED.equals(this.status);
    }
}
