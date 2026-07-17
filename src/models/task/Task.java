package models.task;

import interfaces.Completable;
import utils.exceptions.InvalidInputException;

/**
 * Represents an individual project assignment.
 * Implements the {@link interfaces.Completable} interface to track status life cycles.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class Task implements Completable {

    private static int idCounter = 1;
    private final String taskID;
    private String taskName;
    private Status status;

    public Task(String taskName, Status status) {
        if (taskName == null || taskName.strip().isEmpty())
            throw new InvalidInputException("Task name cannot be empty.");
        this.taskID = String.format("TSK%03d", idCounter++);
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

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isCompleted() {
        return Status.COMPLETED.equals(this.status);
    }
}
