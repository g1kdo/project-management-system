package models.task;

import interfaces.Completable;
import utils.RegexValidator;
import utils.ValidationUtils;
import utils.exceptions.InvalidInputException;

/**
 * Represents an individual project assignment.
 * Implements the {@link interfaces.Completable} interface to track status life cycles.
 *
 * @author Katy Great Adonai
 * @version 3.0
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
        this.status = ValidationUtils.validateAndNormalizeStatus(status);
    }

    @Override
    public boolean isCompleted() {
        return Status.COMPLETED.equals(this.status);
    }
}
