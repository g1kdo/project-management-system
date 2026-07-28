package test;

import models.project.HardwareProject;
import models.project.SoftwareProject;
import org.junit.jupiter.api.Test;
import utils.ValidationUtils;
import utils.exceptions.InvalidInputException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTests {

    @Test
    void testProjectCreationWithEmptyNameThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            new HardwareProject("P001", " ", "Implementation of the testing method", 32000000.00);
        }, "System allowed generation of blank project names.");
    }

    @Test
    void testProjectCreationWithNegativeBudgetThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            new SoftwareProject("P001","Invalid Project", "Should crash safely", -500.00);
        }, "System allowed generation of negative project values.");
    }
}
