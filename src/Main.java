import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.project.Type;
import models.task.Status;
import models.task.Task;
import models.user.RegularUser;
import models.user.User;
import services.concurrency.ConcurrencyService;
import services.concurrency.StreamService;
import services.project.ProjectService;
import services.report.ReportService;
import services.task.TaskService;
import services.user.UserService;
import utils.ConsoleMenu;
import utils.FileUtils;
import utils.RegexValidator;
import utils.ValidationUtils;
import utils.exceptions.InvalidInputException;
import utils.exceptions.ProjectNotFoundException;
import utils.exceptions.TaskNotFoundException;

import java.io.Console;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ProjectService projectService = new ProjectService();
    private static final TaskService taskService = new TaskService();
    private static final ReportService reportService = new ReportService();
    private static final UserService userService = new UserService();
    private static final ConcurrencyService concurrencyService = new ConcurrencyService();
    private static final StreamService streamService = new StreamService();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean running = true;

        FileUtils.loadProjects(projectService.getProjectCatalogMap());

        do {

            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                running = authenticationWorkflow(scan);
                continue;
            }
            ConsoleMenu.printMainMenu(currentUser);
            int choice = ValidationUtils.getValidInt(scan, "Enter your choice: ", 1, 5);

            switch (choice) {
                case 1 ->
                    manageProjectsMenu(scan, currentUser);
                case 2 ->
                    manageTasksMenu(scan, currentUser);
                case 3 ->
                    reportService.generateStatusReport(projectService);
                case 4 ->
                    userService.logout();
                case 5 -> {
                    FileUtils.saveProjects(projectService.getProjectCatalogMap());
                    System.out.println("Thank you for using our Project Management System!\nGoodbye:)");
                    running = false;
                }
            }
        } while (running);

        scan.close();
    }

    private static boolean authenticationWorkflow(Scanner scan) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║               AUTHENTICATION MENU          ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Exit System");

        int choice = ValidationUtils.getValidInt(scan, "Choose an option: ", 1, 3);

        switch (choice) {
            case 1 -> {
                System.out.println("Enter your email: ");
                String username = scan.nextLine().trim();

                System.out.println("Enter your password: ");
                String password = scan.nextLine().trim();

                try {
                    userService.login(username, password);
                } catch (InvalidInputException e) {
                    System.out.println("❌ Authentication Failed: " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    System.out.println("Enter full name: ");
                    String name = scan.nextLine().trim();

                    System.out.println("Enter email: ");
                    String email = scan.nextLine().trim();

                    System.out.println("Enter password: ");
                    String password = scan.nextLine().trim();

                    User newUser = new RegularUser(name, email, password);
                    userService.registerUser(newUser);
                    System.out.println("✓ Registration successful! You can now log in.");
                } catch (InvalidInputException e) {
                    System.out.println("❌ Registration Error: " + e.getMessage());
                }
            }
            case 3 -> {
                FileUtils.saveProjects(projectService.getProjectCatalogMap());
                System.out.println("Thank you for using our Project Management System!\nGoodbye:)");
                return false;
            }
        }
        return true;
    }

    private static void manageProjectsMenu(Scanner scan, User currentUser) {
        ConsoleMenu.printProjectMenu(projectService);

        int choice = ValidationUtils.getValidInt(scan, "Enter filter choice: ", 1, 7);
        System.out.println();

        switch (choice) {
            case 1 ->
                projectService.displayAllProjects();
            case 2 ->
                projectService.displayProjectsByType(Type.SOFTWARE);
            case 3 ->
                projectService.displayProjectsByType(Type.HARDWARE);
            case 4 -> {
                double min = ValidationUtils.getValidDouble(scan, "Enter minimum budget: ", 0.0);
                double max = ValidationUtils.getValidDouble(scan, "Enter maximum budget: ", min);
                projectService.searchByBydgetRange(min, max);
            }
            case 5 ->
                handleCreateProject(scan, currentUser);
            case 6 ->
                filterProjectsWorkflow(scan);
            case 7 -> {
                return;
            }
        }

        // Project detail tracking workflow
        if (projectService.getProjectCatalogMap().isEmpty()) return;
        System.out.print("Enter project ID to view details (or press Enter to skip): ");
        String projectID = scan.nextLine().trim();
        if (!projectID.isEmpty()) {
            try {
                RegexValidator.validateProjectId(projectID);
                Project project = projectService.findProjectById(projectID);
                viewProjectDetailsWorkflow(scan, project, currentUser);
            } catch (InvalidInputException | ProjectNotFoundException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    private static void handleCreateProject(Scanner scan, User currentUser) {
        System.out.println("Select Project Type:");
        System.out.println("1. Software Project");
        System.out.println("2. Hardware Project");
        int choice = ValidationUtils.getValidInt(scan, "Choice: ", 1, 2);

        try {
            System.out.print("Enter Project ID (Format P###, e.g., P004): ");
            String id = scan.nextLine().trim();
            System.out.print("Enter project name: ");
            String name = scan.nextLine().trim();
            System.out.print("Enter description: ");
            String description = scan.nextLine().trim();
            double budget = ValidationUtils.getValidDouble(scan, "Enter budget: Rwf", 0.0);

            Project newProject;
            if (choice == 1) {
                newProject = new SoftwareProject(id, name, description, budget);
                newProject.addMember(currentUser);
            } else {
                newProject = new HardwareProject(id, name, description, budget);
                newProject.addMember(currentUser);
            }

            projectService.addProject(newProject);
            System.out.printf("✓ Project successfully created with ID: %s%n", id);
        } catch (InvalidInputException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

    }

    private static void filterProjectsWorkflow(Scanner scan) {
        double threshold = ValidationUtils.getValidDouble(scan, "Enter minimum completion percentage threshold (0-100): ", 0.0);
        List<Project> filtered = streamService.getProjectsAboveCompletionThreshold(
                projectService.getAllProjects(), reportService, threshold
        );

        System.out.println("\n--- FILTERED PROJECTS (>= " + threshold + "%) ---");
        if (filtered.isEmpty()) {
            System.out.println("No projects found matching the threshold.");
            return;
        }
        filtered.forEach(p -> System.out.printf("- [%s] %s (%.2f%% Completed)%n",
                        p.getProjectID(), p.getProjectName(), reportService.calculateProjectCompletionRate(p))
                );
    }

    public  static void viewProjectDetailsWorkflow(Scanner scan, Project project, User currentUser) {
        ConsoleMenu.printDetailsMenu(project, taskService, reportService);

        int choice = ValidationUtils.getValidInt(scan, "Enter your choice: ",1, 7);
        switch (choice) {
            case 1 -> {
                try {
                    System.out.print("Enter Task ID (Format T###, e.g., T001): ");
                    String taskId = scan.nextLine().trim();
                    RegexValidator.validateTaskId(taskId);

                    System.out.print("Enter Task Name: ");
                    String taskName = scan.nextLine().trim();

                    Task task = new Task(taskId, taskName, Status.PENDING);
                    taskService.addTaskToProject(project, task);
                } catch (InvalidInputException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }

            }
            case 2 -> {
                try {
                    System.out.print("Enter task ID: ");
                    String taskID = scan.nextLine().trim();
                    System.out.println("Enter new Status(Enter c for completed, i for in_progress or p for pending): ");
                    String newStatus = scan.nextLine().trim();

                    taskService.updateTaskStatus(project, taskID, newStatus, currentUser);
                } catch (InvalidInputException | TaskNotFoundException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }

            }
            case 3 -> {
                System.out.print("Enter task ID to remove: ");
                String taskID = scan.nextLine().trim();
                taskService.removeTask(project, taskID, currentUser);
            }
            case 4 -> simulateConcurrencyWorkflow(scan, project);
            case 5 -> projectService.displayTeam(project);
            case 6 -> {
                try {
                    projectService.joinProject(project, currentUser);
                } catch (InvalidInputException e) {
                    System.out.println("❌ Error: " + e.getMessage());;
                }
            }
            case 7 -> manageProjectsMenu(scan, currentUser);
        }
    }

    private static void simulateConcurrencyWorkflow(Scanner scan, Project project) {
        try {
            concurrencyService.simulateConcurrentTaskUpdates(project.getTasks(), "Completed");
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void manageTasksMenu(Scanner scan, User currentUser) {
        System.out.print("Enter the target Project ID to manage tasks: ");
        String projectID = scan.nextLine().trim();
        try {
            Project project = projectService.findProjectById(projectID);
            viewProjectDetailsWorkflow(scan, project, currentUser);
        } catch (ProjectNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

}