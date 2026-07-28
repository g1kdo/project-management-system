package services.task;

import models.project.Project;
import models.task.Status;
import models.task.Task;
import models.user.Role;
import models.user.User;
import utils.exceptions.InvalidInputException;
import utils.exceptions.TaskNotFoundException;

import java.util.List;

/**
 * Business service layer responsible for manipulating task assignments.
 * Enforces duplicate constraints and implements role-based modifications.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class TaskService {

    public void addTaskToProject(Project project, Task task) {
        if (project == null || task == null)
            throw new InvalidInputException("Project or Task cannot be null.");

        // Duplication check within the project's own task list using Streams
        boolean duplicateExists = project.getTasks().stream()
                .anyMatch(t -> t.getTaskID().equalsIgnoreCase(task.getTaskID()) || t.getTaskName().equalsIgnoreCase(task.getTaskName()));

        if (duplicateExists)
            throw new InvalidInputException("A task with the same ID or Name already exists in this project.");

        project.addTask(task);
        System.out.printf("✓ Task \"%s\" added successfully to Project %s:)%n", project.getProjectName(), project.getProjectID());
    }

    public void displayTasksForProject(Project project) {
        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found for this project.");
            return;
        }

        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("%-4s | %-20s | %-12s%n", "ID", "TASK NAME", "STATUS");
        System.out.println("───────────────────────────────────────────────────────────────────────");

        tasks.forEach(task ->
                System.out.printf("%-4s | %-20s | %-12s%n", task.getTaskID(), task.getTaskName(), task.getStatus().name())
        );
        System.out.println("───────────────────────────────────────────────────────────────────────");
    }

    public void updateTaskStatus(Project project, String taskId, String newStatus, User currentUser) {
        // Enforce user permission checks
        if (currentUser == null || !Role.ADMIN.equals(currentUser.getRole())) {
            throw new InvalidInputException("❌ Error: Action denied. Only Admin users can update task statuses.");
        }

        Task taskToUpdate = project.getTasks().stream()
                .filter(t -> t.getTaskID().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task ID '" + taskId + "' not found in this project:("));

        taskToUpdate.setStatus(newStatus);
        System.out.printf("Task \"%s\" marked as %s.%n", taskToUpdate.getTaskName(), taskToUpdate.getStatus().name());
    }

    public void removeTask(Project project, String taskId, User currentUser) {
        // Enforce user permission checks
        if (currentUser == null || !Role.ADMIN.equals(currentUser.getRole())) {
            throw new InvalidInputException("❌ Error: Action denied. Only Admin users can remove tasks.");
        }

        Task taskToRemove = project.getTasks().stream()
                .filter(t -> t.getTaskID().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task ID '" + taskId + "' not found in this project:("));

        project.getTasks().remove(taskToRemove);

        System.out.printf("✓ Task \"%s\" removed successfully from Project %s:)%n",
                taskToRemove.getTaskName(), project.getProjectID());
    }
}
