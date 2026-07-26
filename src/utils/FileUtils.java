package utils;

import models.project.HardwareProject;
import models.project.Project;
import models.project.SoftwareProject;
import models.project.Type;
import models.task.Status;
import models.task.Task;
import utils.exceptions.InvalidInputException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * File I/O persistence utility using Java NIO and Streams.
 * Reads and writes project/task data to disk in JSON-like text format.
 */
public class FileUtils {
    private static final String FILE_PATH = "data/projects_data.json";

    /**
     * Saves all projects and their associated tasks from the catalog map to a file.
     *
     * @param projectCatalog Map containing project catalog data
     */
    public static void saveProjects(Map<String, Project> projectCatalog) {
        Path path = Paths.get(FILE_PATH);

        try {
            //ensure data directory exists
            if (path.getParent() != null && !Files.exists(path.getParent()))
                Files.createDirectories(path.getParent());

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[\n");

            List<Project> projectList = new ArrayList<>(projectCatalog.values());
            for (int i = 0; i < projectList.size(); i++) {
                Project project = projectList.get(i);
                jsonBuilder.append("  {\n");
                jsonBuilder.append(String.format("    \"projectId\": \"%s\",\n", project.getProjectID()));
                jsonBuilder.append(String.format("    \"name\": \"%s\",\n", escapeJson(project.getProjectName())));
                jsonBuilder.append(String.format("    \"type\": \"%s\",\n", project.getProjectDetails()));
                jsonBuilder.append(String.format("    \"description\": \"%s\",\n", escapeJson(project.getDescription())));
                jsonBuilder.append(String.format("    \"budget\": %.2f,\n", project.getBudget()));
                jsonBuilder.append("    \"tasks\": [\n");

                List<Task> tasks = project.getTasks();
                for (int j = 0; j < tasks.size(); j++) {
                    Task task = tasks.get(j);
                    jsonBuilder.append("      {\n");
                    jsonBuilder.append(String.format("        \"id\": \"%s\",\n", task.getTaskID()));
                    jsonBuilder.append(String.format("        \"name\": \"%s\",\n", escapeJson(task.getTaskName())));
                    jsonBuilder.append(String.format("        \"status\": \"%s\"\n", task.getStatus()));
                    jsonBuilder.append("      }");
                    if (j < tasks.size() - 1) jsonBuilder.append(",");
                    jsonBuilder.append("\n");
                }

                jsonBuilder.append("    ]\n");
                jsonBuilder.append("  }");
                if (i < projectList.size() - 1) jsonBuilder.append(",");
                jsonBuilder.append("\n");
            }
            Files.writeString(path, jsonBuilder.toString());
            System.out.println("Saving project data...");
            System.out.println("✓ Data written to " + FILE_PATH + " successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error: Failed to save data to file: " + e.getMessage());
        }
    }

    /**
     * Loads projects and tasks from disk persistence into a target map catalog.
     * Uses try-with-resources and NIO file streams.
     *
     * @param projectCatalog Target map to populate with loaded objects
     */
    public static void loadProjects(Map<String, Project> projectCatalog) {
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            System.out.println("✗ Error: Unable to load " + FILE_PATH + " (File not found)");
            System.out.println("→ Starting with default project catalog.");
            return;
        }

        System.out.println("Loading projects from file...");
        try {
            String content = Files.readString(path);
            if (content.trim().isEmpty())
                return;

            // simple parsing strategy for flat structure
            String[] projectBlocks = content.split("\\{\\s*\"projectId\"");
            int loadedCount = 0;

            for (String block : projectBlocks) {
                if (!block.contains("name")) continue;

                String id = extractJsonField(block, "projectId");
                if (id == null || id.isEmpty()) {
                    id = extractJsonField(block, "id");
                }
                String name = extractJsonField(block, "name");
                String type = extractJsonField(block, "type");
                String description = extractJsonField(block, "description");
                double budget = extractJsonDoubleField(block, "budget");

                if (id != null && name != null) {
                    Project project;
                    if (Type.HARDWARE.name().equalsIgnoreCase(type)) {
                        project = new HardwareProject(id, name, description != null ? description : "", budget);
                    } else {
                        project = new SoftwareProject(id, name, description != null ? description : "", budget);
                    }

                    // Parse tasks inside this project block
                    int tasksStartIndex = block.indexOf("\"tasks\":");
                    if (tasksStartIndex != -1) {
                        String tasksBlock = block.substring(tasksStartIndex);
                        String[] taskEntries = tasksBlock.split("\\{\\s*\"id\"");

                        for (String taskEntry : taskEntries) {
                            String taskId = extractJsonField(taskEntry, "id");
                            String taskName = extractJsonField(taskEntry, "name");
                            String taskStatus = extractJsonField(taskEntry, "status");

                            if (taskId != null && taskName != null && taskStatus != null) {
                                try {
                                    Task task = new Task(taskId, taskName, Status.valueOf(taskStatus));
                                    project.addTask(task);
                                } catch (IllegalArgumentException | InvalidInputException e) {
                                    // Skip malformed individual tasks safely
                                }
                            }
                        }
                    }

                    projectCatalog.put(project.getProjectID().toUpperCase(), project);
                    loadedCount++;
                }
            }

            System.out.printf("✓ %d projects loaded successfully from %s%n", loadedCount, FILE_PATH);
        } catch (IOException e) {
            System.out.println("✗ Error: File reading failed (" + e.getMessage() + ")");
            System.out.println("→ Starting with empty catalog.");
        }
    }

    private static String extractJsonField(String jsonBlock, String fieldName) {
        String key = "\"" + fieldName + "\":";
        int keyIndex = jsonBlock.indexOf(key);
        if (keyIndex == -1) return null;

        int startQuote = jsonBlock.indexOf("\"", keyIndex, key.length());
        if (startQuote == -1) return null;

        int endQuote = jsonBlock.indexOf("\"", startQuote + 1);
        if (endQuote == -1) return null;

        return jsonBlock.substring(startQuote + 1, endQuote);
    }

    private static double extractJsonDoubleField(String jsonBlock, String fieldName) {
        String key = "\"" + fieldName + "\":";
        int keyIndex = jsonBlock.indexOf(key);
        if (keyIndex == -1) return 0.0;

        int start = keyIndex + key.length();
        int end = jsonBlock.indexOf(",", start);
        if (end == -1) end = jsonBlock.indexOf("\n", start);
        if (end == -1) return 0.0;

        try {
            return Double.parseDouble(jsonBlock.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
