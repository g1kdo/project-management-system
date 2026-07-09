package models.project;

import models.task.Task;

public class SoftwareProject extends Project{

    public SoftwareProject(String projectName, String description, double budget, int teamSize) {
        super(projectName, description, budget, teamSize);
    }

    @Override
    public String getProjectDetails() {
        return Type.SOFTWARE.name();
    }
}
