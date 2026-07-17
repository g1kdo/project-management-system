package utils;

import models.task.Task;

import java.util.Scanner;

/**
 * Utility toolkit providing defensive input filtering methods.
 * Captures scanning and conversion anomalies to insulate console loops from runtime crashes.
 *
 * @author Katy Great Adonai
 * @version 2.0
 */
public class ValidationUtils {
    public static int getValidInt(Scanner scan, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String userInput = scan.nextLine().trim();
            try {
                int value = Integer.parseInt(userInput);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Invalid choice: Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid numeric input:(");
            }

        }
    }

    public static double getValidDouble(Scanner scan, String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String userInput = scan.nextLine().trim();
            try {
                double value = Integer.parseInt(userInput);
                if (value >= min) {
                    return value;
                }
                System.out.printf("Error: Value must be greater than or equal to %.2f.%n", min);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid numeric input:(");
            }

        }
    }

}
