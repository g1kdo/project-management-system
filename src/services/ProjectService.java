package services;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;

public class ProjectService {

    private Project[] projects;
    private int projectCount;

    public ProjectService() {
        this.projectCount = 0;
        this.projects = new Project[100]; // System capacity of 100 projects
        initializeSampleData();
    }

    private void initializeSampleData() {
        addProject(new SoftwareProject("Alpha Tracker", "Task tracking app for startups", 15000.00, 5));
        addProject(new HardwareProject("IoT Sensor Kit", "Sensor prototype for smart devices", 10000.00, 3));
        addProject(new SoftwareProject("Beta Portal", "Customer onboarding dashboard", 45000.00, 12));
        addProject(new HardwareProject("Smart Thermostat", "Home automation temperature grid", 22000.00, 4));
        addProject(new SoftwareProject("Data Pipeline", "Real-time analytics syncing tool", 60000.00, 8));
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
        return null;
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
