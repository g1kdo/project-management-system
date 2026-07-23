package services.report;

import models.project.Project;
import models.task.Task;
import services.project.ProjectService;
import utils.exceptions.EmptyProjectException;

public class ReportService {

    public double calculateProjectCompletionRate(Project project) {
        if (project.getTasks().isEmpty()) {
            throw new EmptyProjectException("Project '" + project.getProjectName() + "' has no tasks to calculate completion progress.");
        }

        return ((double) calculateTotalCompletedTasks(project) / project.getTasks().size()) * 100;
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
            int totalTasks = project.getTasks().size();

            int completedTasks = calculateTotalCompletedTasks(project);

            try {
                double progress = calculateProjectCompletionRate(project);
                totalCompletionSum += progress;

                System.out.printf("%-10s | %-17s | %-5d | %-9d | %.2f%%%n",
                        project.getProjectID(), project.getProjectName(), totalTasks, completedTasks, progress);
            } catch (EmptyProjectException e) {
                System.out.println("Info: " + e.getMessage());
            }

        }

        double averageCompletion = totalCompletionSum / totalProjects;

        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("AVERAGE COMPLETION: %.2f%%%n", averageCompletion);
        System.out.println("───────────────────────────────────────────────────────────────────────");
    }

    public int calculateTotalCompletedTasks(Project project) {
        int totalTasks = project.getTasks().size();
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
