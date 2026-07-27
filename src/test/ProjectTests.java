package test;

import models.project.Project;
import models.project.SoftwareProject;
import models.task.Status;
import models.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.report.ReportService;
import utils.exceptions.EmptyProjectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectTests {
    private Project project;
    private ReportService reportService;

    @BeforeEach
    void setup() {
        project = new SoftwareProject("P001","Alpha Tracker", "Core development tracking space", 15000000.00);
        reportService = new ReportService();
    }

    @Test
    void testCalculateCompletionPercentage_PartialCompletion() {
        project.addTask(new Task("T001", "Setup Database", Status.COMPLETED));
        project.addTask(new Task("T002","Implement API Layer", Status.IN_PROGRESS));
        project.addTask(new Task("T003","Write Frontend", Status.PENDING));
        project.addTask(new Task("T004","Execute Integration Tests", Status.PENDING));

        double expectedRate = 25.0;
        double actualRate = reportService.calculateProjectCompletionRate(project);

        assertEquals(expectedRate, actualRate, 0.001, "Partial completion calculation is inaccurate.");
    }

    @Test
    void testCalculateCompletionPercentage_NoTasks_ThrowsEmptyProjectException() {
        assertThrows(EmptyProjectException.class, () -> {
            reportService.calculateProjectCompletionRate(project);
        });
    }

    @Test
    void testCalculateCompletionPercentage_AllCompleted() {
        project.addTask(new Task("T001","Setup Database", Status.COMPLETED));
        project.addTask(new Task("T002","Implement API Layer", Status.COMPLETED));

        double expectedRate = 100.0;
        double actualRate = reportService.calculateProjectCompletionRate(project);

        assertEquals(expectedRate, actualRate, 0.001, "Full completion calculation failed.");
    }
}
