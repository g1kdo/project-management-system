package models.project;

import models.task.Task;
import models.user.User;
import utils.exceptions.InvalidInputException;

/**
 * Represents an abstract Project within the system.
 * Enforces validation rules on core project details and maintains
 * an in-memory roster of tasks and team members.
 *
 * <p>The project's team size is computed dynamically based on the
 * active count of assigned team members.</p>
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public abstract class Project {

    private static int idCounter = 1;
    private final String projectID;
    private String projectName;
    private String description;
    private double budget;

    private User[] members;
    private int teamSize;

    private Task[] tasks;
    private int taskCount;

    public Project(String projectName, String description, double budget) {
        if (projectName == null || projectName.strip().isEmpty())
            throw  new InvalidInputException("Project name cannot be empty.");
        this.projectID = String.format("PRJ%03d", idCounter++);
        this.projectName = projectName;
        this.description = description;
        this.budget = budget;
        this.members = new User[100]; // max 100 members per project
        this.teamSize = 0;
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

    public User[] getMembers() {
        return members;
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

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void addMember(User user) {
        if (user == null)
            throw new InvalidInputException("No users found to add to project:(");

        // prevent adding duplicate users
        for (int i = 0; i < teamSize; i++) {
            if (members[i].getUserID().equals(user.getUserID()))
                throw new InvalidInputException("User '" + user.getUserName() + "' is already a member of this project.");
        }
        if (teamSize >= members.length)
            throw new InvalidInputException("Member capacity reached for this project.");

        members[teamSize++] = user;
    }

    public void addTask(Task task) {
        if (task == null)
            throw new InvalidInputException("Cannot add a null task.");
        if (taskCount >= tasks.length)
            throw new InvalidInputException("Task limit of " + tasks.length + " reached for this project");

        tasks[taskCount++] = task;
    }

    public abstract String getProjectDetails();
    public void displayProject() {
        System.out.printf("%-4s | %-20s | %-12s | %-9d | Rwf%,.2f%n",
                projectID, projectName, getProjectDetails(), teamSize, budget);
        System.out.println("    | Description: " + description);
    }
}
