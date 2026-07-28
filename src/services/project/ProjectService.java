package services.project;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.project.Type;
import models.user.User;
import utils.exceptions.InvalidInputException;
import utils.exceptions.ProjectNotFoundException;

import java.util.*;

/**
 * Business service layer responsible for managing project operations.
 * Handles project creation, catalog searches, and team member alignments
 * using internal data structures.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class ProjectService {

    private final Map<String, Project> projectCatalog;

    public ProjectService() {
        this.projectCatalog = new HashMap<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        addProject(new SoftwareProject("P001","Alpha Tracker", "Task tracking app for startups", 15000000.00));
        addProject(new HardwareProject("P002", "IoT Sensor Kit", "Sensor prototype for smart devices", 10000000.00));
        addProject(new SoftwareProject("P003", "Beta Portal", "Customer onboarding dashboard", 45000000.00));
        addProject(new HardwareProject("P004", "Smart Thermostat", "Home automation temperature grid", 22000000.00));
        addProject(new SoftwareProject("P005", "Data Pipeline", "Real-time analytics syncing tool", 60000000.00));
    }

    public void addProject(Project project) {
        if (project == null)
            throw new InvalidInputException("Cannot add a null project.");
        if (projectCatalog.containsKey(project.getProjectID().toUpperCase()))
            throw new InvalidInputException("Project ID '" + project.getProjectID() + "' already exists.");

        projectCatalog.put(project.getProjectID().toUpperCase(), project);
    }

    public Collection<Project> getAllProjects() {
        return projectCatalog.values();
    }

    public Map<String, Project> getProjectCatalogMap() {
        return projectCatalog;
    }

    public Project findProjectById(String id) {
        if (id == null)
            throw new InvalidInputException("Project ID cannot be null.");

        Project project = projectCatalog.get(id.toUpperCase());
        if (project == null)
            throw new ProjectNotFoundException("Project ID '" + id + "' does not exist.");

        return project;
    }

    public void displayAllProjects() {
        if (projectCatalog.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }


        projectCatalog.values().forEach(project ->  {
            project.displayProject();
            System.out.println("───────────────────────────────────────────────────────────────────────");
        });
    }

    public void displayProjectsByType(Type type) {
        boolean found = false;
        for (Project project : projectCatalog.values()) {
            boolean matchesType = false;

            if (type == Type.SOFTWARE && project instanceof SoftwareProject) {
                matchesType = true;
            } else if (type == Type.HARDWARE && project instanceof HardwareProject) {
                matchesType = true;
            }

            if (matchesType) {
                project.displayProject();
                System.out.println("───────────────────────────────────────────────────────────────────────");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No " + type + " project found.");
        }
    }

    public void  searchByBydgetRange(double min, double max) {
        boolean found = false;
        for (Project project : projectCatalog.values()) {
            if (project.getBudget() >= min && project.getBudget() <= max) {
                project.displayProject();
                System.out.println("───────────────────────────────────────────────────────────────────────");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No projects found within that budget range.");
        }
    }

    public void joinProject(Project project, User user) {
        if (project == null || user == null)
            throw new InvalidInputException("Project or User not found:(");

        project.addMember(user);
        System.out.printf("✓ User '%s' has successfully joined Project %s.%n", user.getUserName(), project.getProjectID());
    }

    public void displayTeam(Project project) {
        if (project == null)
            throw new InvalidInputException("Project cannot be found:(");

        Set<User> members = project.getMembers();
        System.out.println("\n--- PROJECT TEAM MEMBERS ---");
        if (members.isEmpty()) {
            System.out.println("No members assigned to this project yet.");
            return;
        }
        members.forEach(member ->
                System.out.printf("- %s [%s] (%s)%n", member.getUserName(), member.getRole(), member.getUserEmail())
                );
    }
}
