import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.task.Status;
import models.user.AdminUser;
import models.user.RegularUser;
import models.user.User;
import services.ProjectService;
import services.ReportService;
import services.TaskService;
import utils.ConsoleMenu;
import utils.ValidationUtils;
import utils.exceptions.InvalidInputException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskNotFoundException;

import java.util.Scanner;

public class Main {

    private static ProjectService projectService = new ProjectService();
    private static TaskService taskService = new TaskService();
    private static ReportService reportService = new ReportService();

    private static User[] users = new User[] {
            new AdminUser("Katy Great Adonai", "katygreatado@gmail.com"),
            new RegularUser("Aline NZIKWINKUNDA", "nzikaline@gmail.com")
    };


    private static User currentUser = users[0];

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean running = true;

        seedSampleTasks();

        do {
            ConsoleMenu.printMainMenu(currentUser);
            int choice = ValidationUtils.getValidInt(scan, "Enter your choice: ", 1, 5);

            switch (choice) {
                case 1 ->
                    manageProjectsMenu(scan);
                case 2 ->
                    manageTasksMenu(scan);
                case 3 ->
                    reportService.generateStatusReport(projectService);
                case 4 ->
                    switchUser(scan);
                case 5 -> {
                    System.out.println("Thank you for using our Project Management System!\nGoodbye:)");
                    running = false;
                }
            }
        } while (running);

        scan.close();
    }

    private static void seedSampleTasks() {
        Project p1 = projectService.findProjectById("PRJ001");
        if (p1 != null) {
            taskService.addTaskToProject(p1, "Design Database");
            taskService.addTaskToProject(p1, "Implement API");
            taskService.addTaskToProject(p1, "Write Unit Tests");
        }
        Project p2 = projectService.findProjectById("PRJ002");
        if (p2 != null) {
            taskService.addTaskToProject(p2, "Sensor Wiring Blueprint");
            taskService.addTaskToProject(p2, "Calibrate Firmware");
        }
    }

    private static void manageProjectsMenu(Scanner scan) {
        ConsoleMenu.printProjectMenu(projectService);

        int choice = ValidationUtils.getValidInt(scan, "Enter filter choice: ", 1, 6);
        System.out.println();

        switch (choice) {
            case 1 ->
                projectService.displayAllProjects();
            case 2 ->
                projectService.displayProjectsByType("Software");
            case 3 ->
                projectService.displayProjectsByType("Hardware");
            case 4 -> {
                double min = ValidationUtils.getValidDouble(scan, "Enter minimum budget: ", 0.0);
                double max = ValidationUtils.getValidDouble(scan, "Enter maximum budget: ", min);
                projectService.searchByBydgetRange(min, max);
            }
            case 5 ->
                handleCreateProject(scan);
            case 6 -> {
                return;
            }
        }

        // Project detail tracking workflow
        System.out.print("Enter project ID to view details (or 0 to return): ");
        String projectID = scan.nextLine().trim();
        if (!projectID.equals("0")) {
            try {
                Project project = projectService.findProjectById(projectID);
                viewProjectDetailsWorkflow(scan, project);
            } catch (ProjectNotFoundException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    private static void handleCreateProject(Scanner scan) {
        System.out.println("Select Project Type:");
        System.out.println("1. Software Project");
        System.out.println("2. Hardware Project");
        int choice = ValidationUtils.getValidInt(scan, "Choice: ", 1, 2);

        System.out.print("Enter project name: ");
        String name = scan.nextLine().trim();
        System.out.print("Enter description: ");
        String description = scan.nextLine().trim();
        double budget = ValidationUtils.getValidDouble(scan, "Enter budget: Rwf", 0.0);

        try {
            Project newProject;
            if (choice == 1) {
                newProject = new SoftwareProject(name, description, budget);
                newProject.addMember(currentUser);
            } else {
                newProject = new HardwareProject(name, description, budget);
                newProject.addMember(currentUser);
            }

            projectService.addProject(newProject);
            System.out.printf("✓ Project successfully created with ID: %s%n", newProject.getProjectID());
        } catch (InvalidInputException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

    }

    public  static void viewProjectDetailsWorkflow(Scanner scan, Project project) {
        ConsoleMenu.printDetailsMenu(project, taskService, reportService);

        int choice = ValidationUtils.getValidInt(scan, "Enter your choice: ",1, 6);
        switch (choice) {
            case 1 -> {
                System.out.print("Enter task name: ");
                String taskName = scan.nextLine().trim();
                try {
                    taskService.addTaskToProject(project, taskName);
                } catch (InvalidInputException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }

            }
            case 2 -> {
                System.out.print("Enter task ID: ");
                String taskID = scan.nextLine().trim();
                System.out.println("Enter new Status(Enter c for completed, i for in_progress or p for pending): ");
                String newStatus = scan.nextLine().trim();

                try {
                    Status status = switch (newStatus.toLowerCase()) {
                        case "c" -> Status.COMPLETED;
                        case "i" -> Status.IN_PROGRESS;
                        case "p" -> Status.PENDING;
                        default -> throw new InvalidInputException("❌ Error: Invalid status. Please choose from [Pending p, In Progress i, Completed c].");
                    };
                    taskService.updateTaskStatus(project, taskID, status, currentUser);
                } catch (InvalidInputException | TaskNotFoundException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }

            }
            case 3 -> {
                System.out.print("Enter task ID to remove: ");
                String taskID = scan.nextLine().trim();
                taskService.removeTask(project, taskID, currentUser);
            }
            case 4 -> projectService.displayTeam(project);
            case 5 -> {
                try {
                    projectService.joinProject(project, currentUser);
                } catch (InvalidInputException e) {
                    System.out.println("❌ Error: " + e.getMessage());;
                }
            }
            case 6 -> manageProjectsMenu(scan);
        }
    }

    private static void manageTasksMenu(Scanner scan) {
        System.out.print("Enter the target Project ID to manage tasks: ");
        String projectID = scan.nextLine().trim();
        try {
            Project project = projectService.findProjectById(projectID);
            viewProjectDetailsWorkflow(scan, project);
        } catch (ProjectNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void switchUser(Scanner scan) {
        System.out.println("Select User Profile: ");
        for (int i = 0; i < users.length; i++) {
            System.out.printf("%d. %s (%s)%n", (i + 1), users[i].getUserName(), users[i].getRole());
        }
        int userChoice = ValidationUtils.getValidInt(scan, "Select profile number: ", 1, users.length);
        currentUser = users[userChoice - 1];
        System.out.printf("✓ Switched profile to %s.%n", currentUser.getUserName());
    }
}