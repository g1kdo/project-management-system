package models.project;

import models.task.Task;

public class SoftwareProject extends Project{

    public SoftwareProject(String projectID, String projectName, String description, double budget) {
        super(projectID, projectName, description, budget);
    }

    @Override
    public String getProjectDetails() {
        return Type.SOFTWARE.name();
    }
}
