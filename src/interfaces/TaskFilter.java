package interfaces;

import models.task.Task;

@FunctionalInterface
public interface TaskFilter {
    boolean test(Task task);
}
