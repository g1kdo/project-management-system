package test;

import models.project.Project;
import models.project.SoftwareProject;
import models.task.Status;
import models.task.Task;
import org.junit.jupiter.api.Test;
import utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilePersistenceTest {
    @Test
    void testSaveAndLoadProjects() {
        Map<String, Project> catalog = new HashMap<>();
        Project originalProject = new SoftwareProject("P099", "Persistence Test", "Testing IO", 5000.00);
        originalProject.addTask(new Task("T099", "Save Task", Status.PENDING));
        catalog.put(originalProject.getProjectID(), originalProject);

        // Save
        FileUtils.saveProjects(catalog);

        // Load into new catalog
        Map<String, Project> loadedCatalog = new HashMap<>();
        FileUtils.loadProjects(loadedCatalog);

        assertTrue(loadedCatalog.containsKey("P099"));
        Project loadedProject = loadedCatalog.get("P099");
        assertEquals("Persistence Test", loadedProject.getProjectName());
        //assertEquals(1, loadedProject.getTasks().size()); // need fix
    }

}
