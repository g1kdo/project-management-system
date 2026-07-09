package models.project;

import models.task.Task;

public class HardwareProject extends Project{

    public HardwareProject(String projectName, String description, double budget, int teamSize) {
        super(projectName, description, budget, teamSize);
    }

    @Override
    public String getProjectDetails() {
        return Type.HARDWARE.name();
    }
}
