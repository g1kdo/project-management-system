package services.report;

import models.project.Project;
import models.task.Status;
import models.task.Task;
import services.project.ProjectService;
import utils.exceptions.EmptyProjectException;

import java.util.Collection;
import java.util.List;

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

        Collection<Project> projects = service.getAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects found in system.");
            return;
        }

        double totalCompletionSum = 0.0;

        for (Project project : projects) {
            int completedTasks = calculateTotalCompletedTasks(project);

            try {
                double progress = calculateProjectCompletionRate(project);
                totalCompletionSum += progress;

                System.out.printf("%-10s | %-17s | %-5d | %-9d | %.2f%%%n",
                        project.getProjectID(), project.getProjectName(), project.getTasks().size(), completedTasks, progress);
            } catch (EmptyProjectException e) {
                System.out.println("Info: " + e.getMessage());
            }

        }

        double averageCompletion = totalCompletionSum / projects.size();

        System.out.println("───────────────────────────────────────────────────────────────────────");
        System.out.printf("AVERAGE COMPLETION: %.2f%%%n", averageCompletion);
        System.out.println("───────────────────────────────────────────────────────────────────────");
    }

    public int calculateTotalCompletedTasks(Project project) {
        if (project == null || project.getTasks() == null) {
            return 0;
        }

        return (int) project.getTasks().stream()
                .filter(task -> task != null && task.isCompleted())
                .count();
    }
}
