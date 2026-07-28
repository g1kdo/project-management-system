package utils;

import utils.exceptions.InvalidInputException;

import java.util.regex.Pattern;

public class RegexValidator {
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern TASK_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern USER_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static void validateProjectId(String id) {
        if (id == null || !PROJECT_ID_PATTERN.matcher(id).matches())
            throw new InvalidInputException("Invalid Project ID format. Use pattern P### (e.g., P001)");
    }

    public static void validateTaskId(String id) {
        if (id == null || !TASK_ID_PATTERN.matcher(id).matches())
            throw new InvalidInputException("Invalid Task ID format. Use pattern T### (e.g., T001)");
    }

    public static void validateUserEmail(String email) {
        if (email == null || !USER_EMAIL_PATTERN.matcher(email).matches())
            throw new InvalidInputException("Invalid User email format (e.g., example@gmail.com)");
    }
}
