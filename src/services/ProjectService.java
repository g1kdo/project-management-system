package services;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import utils.exceptions.InvalidInputException;
import utils.exceptions.ProjectNotFoundException;

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
        try {
            if (projectCount < projects.length) {
                projects[projectCount++] = project;
            } else {
                System.out.println("❌ Error: Maximum project capacity reached.");
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
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
}
