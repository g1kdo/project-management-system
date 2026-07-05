package services;

import models.project.Project;
import models.task.Task;

public class ReportService {

    public double calculateProjectCompletionRate(Project project) {
        if (project.getTaskCount() == 0) {
            return 0.0;
        }

        return ((double) calculateTotalCompletedTasks(project) / project.getTaskCount()) * 100;
    }

    public void generateStatusReport(ProjectService service) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║            PROJECT STATUS REPORT           ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("%-10s | %-17s | %-5s | %-9s | %-12s%n", "PROJECT ID", "PROJECT NAME", "TASKS", "COMPLETED", "PROGRESS (%)");
        System.out.println("───────────────────────────────────────────────────────────────────────");

        Project[] projects = service.getAllProjects();
        int totalProjects = service.getProjectCount();

        if (totalProjects == 0) {
            System.out.println("No projects found in system.");
            return;
        }

        double totalCompletionSum = 0.0;

        for (int i = 0; i < totalProjects; i++) {
            Project project = projects[i];
            int totalTasks = project.getTaskCount();

            int completedTasks = calculateTotalCompletedTasks(project);

            double progress = calculateProjectCompletionRate(project);
            totalCompletionSum += progress;

            System.out.printf("%-10s | %-17s | %-5d | %-9d | %.2f%%%n",
                    project.getProjectID(), project.getProjectName(), totalTasks, completedTasks, progress);
        }

        double averageCompletion = totalCompletionSum / totalProjects;

        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("AVERAGE COMPLETION: %.2f%%%n", averageCompletion);
        System.out.println("───────────────────────────────────────────────────────────────────────");
    }

    public int calculateTotalCompletedTasks(Project project) {
        int totalTasks = project.getTaskCount();
        if (totalTasks == 0) {
            return 0;
        }

        int completedCount = 0;

        Task[] tasks = project.getTasks();
        for (int i = 0; i < totalTasks; i++) {
            if (tasks[i] != null && tasks[i].isCompleted()) {
                completedCount++;
            }
        }

        return completedCount;
    }
}
