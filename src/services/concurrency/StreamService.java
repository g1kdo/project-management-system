package services.concurrency;

import models.project.Project;
import models.task.Task;
import services.report.ReportService;
import utils.FunctionalUtils;
import utils.exceptions.EmptyProjectException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility service dedicated to stream operations, mapping, filtering, and reduction.
 */
public class StreamService {

    /**
     * Filters projects that have reached or exceeded a specific completion percentage threshold.
     * Uses Java Streams filter() and collect().
     *
     * @param projects collection of projects to filter
     * @param threshold minimum completion percentage (0.0 to 100.0)
     * @return list of projects meeting the completion criteria
     */
    public List<Project> getProjectsAboveCompletionThreshold(Collection<Project> projects, ReportService reportService, double threshold) {
        return projects.stream()
                .filter(p -> {
                    try {
                        return reportService.calculateProjectCompletionRate(p) >= threshold;
                    } catch (EmptyProjectException e) {
                        return false; // skip empty projects safely during stream evaluation
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Extracts and returns all task names from a project using stream mapping.
     *
     * @param project target project containing tasks
     * @return list of task name strings
     */
    public List<String> extractTaskNames(Project project) {
        return project.getTasks().stream()
                .map(Task::getTaskName)
                .collect(Collectors.toList());
    }

    /**
     * Counts completed tasks across a collection of projects using flatMap and filter.
     *
     * @param projects collection of projects
     * @return total count of completed tasks across all projects
     */
    public long countAllCompletedTasks(Collection<Project> projects) {
        return projects.stream()
                .flatMap(p -> p.getTasks().stream())
                .filter(Task::isCompleted)
                .count();
    }

    /**
     * Filters tasks within a project by status using streams.
     *
     * @param project target project
     * @param status status criteria (e.g., "Completed", "Pending")
     * @return list of tasks matching the status
     */
    public List<Task> filterTasksByStatus(Project project, String status) {
        return FunctionalUtils.filterTasks(project.getTasks(), task -> task.getStatus().name().equalsIgnoreCase(status));
    }
}
