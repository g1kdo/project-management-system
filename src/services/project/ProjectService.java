package services.project;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.user.User;
import utils.exceptions.InvalidInputException;
import utils.exceptions.ProjectNotFoundException;

import java.util.Set;

/**
 * Business service layer responsible for managing project operations.
 * Handles project creation, catalog searches, and team member alignments
 * using internal data structures.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class ProjectService {

    private Project[] projects;
    private int projectCount;

    public ProjectService() {
        this.projectCount = 0;
        this.projects = new Project[100]; // System capacity of 100 projects
        initializeSampleData();
    }

    private void initializeSampleData() {
        addProject(new SoftwareProject("Alpha Tracker", "Task tracking app for startups", 15000000.00));
        addProject(new HardwareProject("IoT Sensor Kit", "Sensor prototype for smart devices", 10000000.00));
        addProject(new SoftwareProject("Beta Portal", "Customer onboarding dashboard", 45000000.00));
        addProject(new HardwareProject("Smart Thermostat", "Home automation temperature grid", 22000000.00));
        addProject(new SoftwareProject("Data Pipeline", "Real-time analytics syncing tool", 60000000.00));
    }

    public void addProject(Project project) {
            if (projectCount < projects.length) {
                projects[projectCount++] = project;
            } else {
                System.out.println("❌ Error: Maximum project capacity reached.");
            }
    }

    public Project[] getAllProjects() {
        return projects;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public Project findProjectById(String id) {
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getProjectID().equalsIgnoreCase(id)) {
                return projects[i];
            }
        }
        throw new ProjectNotFoundException("Project ID '" + id + "' does not exist.");
    }

    public void displayAllProjects() {
        for (int i = 0; i < projectCount; i++) {
            projects[i].displayProject();
            System.out.println("───────────────────────────────────────────────────────────────────────");
        }
    }

    public void displayProjectsByType(String type) {
        boolean found = false;
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getProjectDetails().equalsIgnoreCase(type)) {
                projects[i].displayProject();
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
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getBudget() >= min && projects[i].getBudget() <= max) {
                projects[i].displayProject();
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

        int count = project.getTeamSize();
        System.out.println("\n--- PROJECT TEAM MEMBERS ---");
        if (count == 0) {
            System.out.println("No members assigned to this project yet.");
            return;
        }
        Set<User> members = project.getMembers();
        for (int i = 0; i < count; i++) {
            System.out.printf("- %s [%s] (%s)%n", members[i].getUserName(), members[i].getRole(), members[i].getUserEmail());
        }
    }
}
