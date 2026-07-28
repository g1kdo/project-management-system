package services.concurrency;

import models.task.Task;

import java.util.List;

/**
 * Service facilitating concurrent task update simulations using Thread and Runnable.
 * Ensures thread safety using synchronized synchronization blocks.
 */
public class ConcurrencyService {

    /**
     * Simulates concurrent updates across a list of tasks using worker threads.
     *
     * @param tasks List of tasks to update in parallel
     * @param targetStatus The new status to apply to tasks concurrently
     */
    public void simulateConcurrentTaskUpdates(List<Task> tasks, String targetStatus) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks available for simulation");
            return;
        }

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    PARALLEL TASK UPDATE SIMULATION     ║");
        System.out.println("╚════════════════════════════════════════╝");

        int threadCount = Math.min(tasks.size(), 3);
        System.out.printf("Starting %d threads...%n", threadCount);

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            final Task taskToUpdate = tasks.get(i);

            Runnable taskRunner = () -> {
                // Thread-safe update block
                synchronized (taskToUpdate) {
                    System.out.printf("Thread-%d updating %s -> %s%n",
                            threadIndex + 1, taskToUpdate.getTaskID(), targetStatus);

                    try {
                        Thread.sleep(200); // simulate processing work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    taskToUpdate.setStatus(targetStatus);
                }
            };

            threads[i] = new Thread(taskRunner, "Thread-" + (i + 1));
            threads[i].start();
        }

        // wait for all worker threads to complete execution
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Concurrent execution interrupted.");
            }
        }

        System.out.println("All threads finished successfully.");
        System.out.println("✓ Task updates applied concurrently and safely.");
    }
}
