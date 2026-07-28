package models.project;

import models.task.Task;

public class HardwareProject extends Project{

    public HardwareProject(String projectID, String projectName, String description, double budget) {
        super(projectID, projectName, description, budget);
    }

    @Override
    public String getProjectDetails() {
        return Type.HARDWARE.name();
    }
}
