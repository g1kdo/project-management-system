package utils;

import models.project.Project;
import models.user.User;
import services.project.ProjectService;
import services.report.ReportService;
import services.task.TaskService;
import utils.exceptions.EmptyProjectException;

public class ConsoleMenu {
    public static void printMainMenu(User currentUser) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║     JAVA PROJECT MANAGEMENT SYSTEM         ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.printf("Welcome %s (%s), good to see you again!%n%n", currentUser.getUserName(), currentUser.getRole());
        System.out.println("Main Menu:");
        System.out.println("-----------");
        System.out.println("1. Manage Projects");
        System.out.println("2. Manage Tasks/Team");
        System.out.println("3. View Status Reports");
        System.out.println("4. Switch User");
        System.out.println("5. Exit");
    }

    public  static void printProjectMenu(ProjectService service) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║               PROJECT CATALOG              ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("Filter Options:");
        System.out.printf("1. View All Projects (%d)%n", service.getAllProjects().size());
        System.out.println("2. Software Projects Only");
        System.out.println("3. Hardware Projects Only");
        System.out.println("4. Search by Budget Range");
        System.out.println("5. Add New Project");
        System.out.println("6. Filter Projects by Completion Rate");
        System.out.println("7. Back to Main Menu");
    }

    public static void printDetailsMenu(Project project, TaskService taskService, ReportService reportService) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.printf("║           PROJECT DETAILS: %-15s║%n", project.getProjectID());
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("Project Name: " + project.getProjectName());
        System.out.println("Type: " + project.getProjectDetails());
        System.out.println("Team Size: " + project.getTeamSize());
        System.out.printf("Budget: Rwf%,.2f%n", project.getBudget());
        System.out.println("\nAssociated Tasks:");
        taskService.displayTasksForProject(project);
        try {
            System.out.printf("Completion Rate: %.2f%%%n%n", reportService.calculateProjectCompletionRate(project));
        } catch (EmptyProjectException e) {
            System.out.println("Info: " + e.getMessage());
        }

        System.out.println("Options:");
        System.out.println("1. Add New Task");
        System.out.println("2. Update Task Status");
        System.out.println("3. Remove Task");
        System.out.println("4. Simulate Concurrent Task Updates");
        System.out.println("5. View Assigned Team Members");
        System.out.println("6. Join This Project Team");
        System.out.println("7. Back to Catalog");
    }
}
