package test;

import models.project.Project;
import models.project.SoftwareProject;
import models.task.Status;
import models.task.Task;
import models.user.AdminUser;
import models.user.RegularUser;
import models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.TaskService;
import utils.exceptions.InvalidInputException;
import utils.exceptions.TaskNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTests {
    private Project project;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        project = new SoftwareProject("Beta Portal", "Dynamic team validation sandbox", 45000000.00);
        taskService = new TaskService();
    }

    @Test
    void testDynamicTeamSizeIncrementOnJoin() {
        assertEquals(0, project.getTeamSize(), "Initial project team size should be zero.");

        User employee1 = new RegularUser("Katy Great Adonai", "katygreatado@gmail.com");
        project.addMember(employee1);
        assertEquals(1, project.getTeamSize(), "Team size did not dynamically update to 1 member.");

        User employee2 = new AdminUser("Aline NZIKWINKUNDA", "nzikaline@gmail.com");
        project.addMember(employee2);
        assertEquals(2, project.getTeamSize(), "Team size did not dynamically update to 2 members.");
    }

    @Test
    void testAddDuplicateMemberThrowsInvalidInputException() {
        User duplicateUser = new RegularUser("Katy Great Adonai", "katygreatado@gmail.com");
        project.addMember(duplicateUser);

        assertThrows(InvalidInputException.class, () -> {
            project.addMember(duplicateUser);
        }, "System failed to block duplicate team additions.");
    }

    @Test
    void testNonExistingTaskThrowsTaskNotFoundException() {
        Task task = new Task("Setup Database", Status.IN_PROGRESS);
        project.addTask(task);
        User user = new AdminUser("Aline NZIKWINKUNDA", "nzikaline@gmail.com");
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTaskStatus(project, "NKLN", Status.COMPLETED, user);
        });

        assertTrue(exception.getMessage().contains("NKLN"), "The exception message should mention the missing Task ID.");
    }
}
