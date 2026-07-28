package test;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.task.Status;
import models.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.concurrency.StreamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamOperationsTest {

    private StreamService streamService;
    private Project project;

    @BeforeEach
    void setup() {
        streamService = new StreamService();
        project = new SoftwareProject(
                "P001",
                "Test Project",
                "Testing",
                1000
        );
    }

    @Test
    void shouldCountCompletedTasksAcrossProjects() {

        Project p1 = new SoftwareProject("P001", "Project 1", "", 1000);
        Project p2 = new HardwareProject("P002", "Project 2", "", 2000);

        p1.addTask(new Task("T001", "Task 1", Status.COMPLETED));
        p1.addTask(new Task("T002", "Task 2", Status.PENDING));

        p2.addTask(new Task("T003", "Task 3", Status.COMPLETED));
        p2.addTask(new Task("T004", "Task 4", Status.IN_PROGRESS));

        long result = streamService.countAllCompletedTasks(List.of(p1, p2));

        assertEquals(2, result);
    }

    @Test
    void testExtractTaskNamesUsingStreams() {
        StreamService streamService = new StreamService();
        Project project = new SoftwareProject("P001", "Alpha", "Desc", 10000.0);
        project.addTask(new Task("T001", "Task One", Status.PENDING));
        project.addTask(new Task("T002", "Task Two", Status.COMPLETED));

        List<String> names = streamService.extractTaskNames(project);
        assertEquals(2, names.size());
        assertTrue(names.contains("Task One"));
        assertTrue(names.contains("Task Two"));
    }

    @Test
    void shouldFilterCompletedTasks() {

        Task completed = new Task("T001", "Task 1", Status.COMPLETED);
        Task pending = new Task("T002", "Task 2", Status.PENDING);

        project.addTask(completed);
        project.addTask(pending);

        List<Task> result =
                streamService.filterTasksByStatus(project, "COMPLETED");

        assertEquals(1, result.size());
        assertEquals(completed, result.get(0));
    }
}
