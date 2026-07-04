package models.project;

import models.task.Task;

public abstract class Project {

    private static int idCounter = 1;
    private final String projectID;
    private String projectName;
    private String description;
    private double budget;
    private int teamSize;

    private Task[] tasks;
    private int taskCount;

    public Project(String projectName, String description, double budget, int teamSize) {
        this.projectID = String.format("PRJ%03d", idCounter++);
        this.projectName = projectName;
        this.description = description;
        this.budget = budget;
        this.teamSize = teamSize;
        this.tasks = new Task[50]; // max 50 tasks per project
        this.taskCount = 0;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }

    public double getBudget() {
        return budget;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void addTask(Task task) {
        if (taskCount < tasks.length) {
            tasks[taskCount++] = task;
        } else {
            System.out.println("Error: Task limit reached for this project:(");
        }
    }

    public abstract String getProjectDetails();
    public void displayProject() {
        System.out.printf("%-4s | %-20s | %-12s | %-9d | $%,.2f%n",
                projectID, projectName, getProjectDetails(), teamSize, budget);
        System.out.println("    | Description: " + description);
    }
}
