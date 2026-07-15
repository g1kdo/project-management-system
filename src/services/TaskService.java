package services;

import models.project.Project;
import models.task.Status;
import models.task.Task;
import models.user.Role;
import models.user.User;
import utils.exceptions.InvalidInputException;
import utils.exceptions.TaskNotFoundException;

public class TaskService {

    public void addTaskToProject(Project project, String taskName) {
        // Duplication check within the project's own task array
        Task[] tasks = project.getTasks();
        for (int i = 0; i < project.getTaskCount(); i++) {
            if (tasks[i].getTaskName().equalsIgnoreCase(taskName)) {
                throw new InvalidInputException("❌ Error: A task with this name already exists in the project:(");
            }
        }

        Task newTask = new Task(taskName, Status.PENDING); // By default the initial status is pending
        project.addTask(newTask);
        System.out.printf("✓ Task \"%s\" added successfully to Project %s:)%n", taskName, project.getProjectID());
    }

    public void displayTasksForProject(Project project) {
        int taskCount = project.getTaskCount();
        if (taskCount == 0) {
            System.out.println("No tasks found for this project:(");
            return;
        }

        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("%-4s | %-20s | %-12s%n", "ID", "TASK NAME", "STATUS");
        System.out.println("───────────────────────────────────────────────────────────────────────");

        Task[] tasks = project.getTasks();
        for (int i = 0; i < taskCount; i++) {
            System.out.printf("%-4s | %-20s | %-12s%n", tasks[i].getTaskID(), tasks[i].getTaskName(), tasks[i].getStatus().name());
        }
        System.out.println("───────────────────────────────────────────────────────────────────────");
    }

    public void updateTaskStatus(Project project, String taskId, Status newStatus, User currentUser) {
        // Enforce user permission checks
        if (!Role.ADMIN.equals(currentUser.getRole())) {
            throw new InvalidInputException("❌ Error: Action denied. Only Admin users can update task statuses.");
        }

        Task[] tasks = project.getTasks();
        for (int i = 0; i < project.getTaskCount(); i++) {
            if (tasks[i].getTaskID().equalsIgnoreCase(taskId)) {
                tasks[i].setStatus(newStatus);
                System.out.printf("Task \"%s\" marked as %s.%n", tasks[i].getTaskName(), newStatus.name());
            }
        }
        throw new TaskNotFoundException("❌ Error: Task ID '" + taskId + "' not found in this project:(");
    }

    public void removeTask(Project project, String taskId, User currentUser) {
        // Enforce user permission checks
        if (!Role.ADMIN.equals(currentUser.getRole())) {
            System.out.println("❌ Error: Action denied. Only Admin users can remove tasks.");
            return;
        }

        Task[] tasks = project.getTasks();
        int count = project.getTaskCount();
        int targetIndex = -1;

        for (int i = 0; i < count; i++) {
            if (tasks[i].getTaskID().equalsIgnoreCase(taskId)) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            System.out.println("❌ Error: Task Id not found:(");
            return;
        }

        // shift items over to fill the gap
        for (int i = targetIndex; i < count - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[count - 1] = null; // clean up the tail
        project.setTaskCount(count - 1); // decrement taskCount

        System.out.println("✓ Task removed successfully:)");
    }
}
