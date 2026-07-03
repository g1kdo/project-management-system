package models.project;

import models.task.Task;

public class HardwareProject extends Project{

    public HardwareProject(String projectName, String description, double budget, int teamSize, Task[] tasks, int taskCount) {
        super(projectName, description, budget, teamSize, tasks, taskCount);
    }

    @Override
    public String getProjectDetails() {
        return "Hardware";
    }
}
