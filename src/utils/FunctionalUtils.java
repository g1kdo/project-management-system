package utils;

import interfaces.TaskFilter;
import models.task.Task;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionalUtils {
    public static List<Task> filterTasks(List<Task> tasks, TaskFilter filter) {
        return tasks.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }
}
