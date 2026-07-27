package models.project;

import models.task.Task;
import models.user.User;
import utils.RegexValidator;
import utils.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an abstract Project within the system.
 * Enforces validation rules on core project details and maintains
 * an in-memory roster of tasks and team members.
 *
 * <p>The project's team size is computed dynamically based on the
 * active count of assigned team members.</p>
 *
 * @author Katy Great Adonai
 * @version 3.0
 */
public abstract class Project {

    private final String projectID;
    private String projectName;
    private String description;
    private double budget;

    private Set<User> members;
    private List<Task> tasks;

    public Project(String projectID, String projectName, String description, double budget) {
        RegexValidator.validateProjectId(projectID);
        if (projectName == null || projectName.strip().isEmpty())
            throw  new InvalidInputException("Project name cannot be empty.");
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.budget = budget;
        this.members = new HashSet<>();
        this.tasks = new ArrayList<>();
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

    public Set<User> getMembers() {
        return members;
    }

    public int getTeamSize() {
        return members.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addMember(User user) {
        if (user == null)
            throw new InvalidInputException("No users found to add to project:(");

        boolean isAdded = members.add(user);

        if (!isAdded)
            throw new InvalidInputException("User '" + user.getUserName() + "' is already a member of this project.");
    }

    public void addTask(Task task) {
        if (task == null)
            throw new InvalidInputException("Cannot add a null task.");
        if (tasks.stream().anyMatch(t -> t.getTaskID().equalsIgnoreCase(task.getTaskID())))
            throw new InvalidInputException("Task ID '" + task.getTaskID() + "' already exists in this project.");

        tasks.add(task);
    }

    public abstract String getProjectDetails();
    public void displayProject() {
        System.out.printf("%-4s | %-20s | %-12s | %-9d | Rwf%,.2f%n",
                projectID, projectName, getProjectDetails(), getTeamSize(), budget);
        System.out.println("    | Description: " + description);
    }
}
