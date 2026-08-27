package nico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Saves the current task list to nico.Nico's data file.
 */
public class TaskStorage {
    /**
     * Appends one newly created task to the data file.
     *
     * @param filePath path of the task data file
     * @param task task to append to the file
     * @throws NicoException if the task cannot be saved
     */
    public static void writeTask(Path filePath, Task task) throws NicoException {
        try {
            createTasksFile(filePath);
            Files.writeString(filePath, task + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new NicoException("\tSorry, I could not save this task to tasks.txt.");
        }
    }

    /**
     * Rewrites the data file so it matches the current task list.
     *
     * @param filePath path of the task data file
     * @param tasks tasks to save
     * @throws NicoException if the task list cannot be saved
     */
    public static void writeAllTasks(Path filePath, List<Task> tasks) throws NicoException {
        try {
            createTasksFile(filePath);
            StringBuilder savedTasks = new StringBuilder();
            for (Task task : tasks) {
                savedTasks.append(task).append(System.lineSeparator());
            }
            Files.writeString(filePath, savedTasks.toString(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new NicoException("\tSorry, I could not update tasks.txt.");
        }
    }

    /**
     * Creates the data folder and task file when they do not yet exist.
     *
     * @param filePath path of the task data file to create
     * @throws IOException if the folder or file cannot be created
     */
    public static void createTasksFile(Path filePath) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }
}
