package test;

import models.project.HardwareProject;
import models.project.SoftwareProject;
import org.junit.jupiter.api.Test;
import utils.exceptions.InvalidInputException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTests {

    @Test
    void testProjectCreationWithEmptyNameThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            new HardwareProject("", "Implementation of the testing method", 32000000.00);
        }, "System allowed generation of blank project names.");
    }

    @Test
    void testProjectCreationWithNegativeBudgetThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            new SoftwareProject("Invalid Project", "Should crash safely", -500.00);
        }, "System allowed generation of negative project values.");
    }
}
