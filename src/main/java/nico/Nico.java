package nico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Entry point for the nico.Nico chatbot application.
 */

public class Nico {
    public static final Path FILE_PATH = Paths.get("data", "tasks.txt");
    public static final String DATE_TIME_INPUT_FORMAT = "dd-MM-yyyy HHmm";
    public static final String DATE_TIME_OUTPUT_FORMAT = "MMM dd yyyy HH:mm";

    /**
     * Loads tasks from the save file into the task list.
     *
     * @param tasks list that receives the saved tasks
     * @throws NicoException if the save file cannot be read or contains an invalid task
     */
    private static void readSavedTasks(List<Task> tasks) throws NicoException {
        try {
            TaskStorage.createTasksFile(FILE_PATH);
            List<String> taskStrings = Files.readAllLines(FILE_PATH);
            for (String taskString : taskStrings) {
                if (!taskString.trim().isEmpty()) {
                    Task task = createTaskFromTaskString(taskString);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new NicoException("\tSorry, I could not load tasks.txt.");
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            throw new NicoException("\tSorry, tasks.txt contains a task I could not understand.");
        }
    }

    /**
     * Reconstructs one task object from its saved display-format text.
     *
     * @param taskString saved representation of a task
     * @return the reconstructed task, with its saved completion status
     * @throws IllegalArgumentException if the saved task type or date-time values are invalid
     */
    private static Task createTaskFromTaskString(String taskString) {
        String taskType = taskString.substring(1, 2);
        boolean isDone = taskString.charAt(4) == 'X';
        String taskDetails = taskString.substring(7);

        Task task;
        switch (taskType) {
            case "T": {
                task = new Todo(taskDetails);
                break;
            }
            case "D": {
                int byStartIndex = taskDetails.lastIndexOf(" (by: ");
                String description = taskDetails.substring(0, byStartIndex);
                LocalDateTime dueTime;
                try {
                    dueTime = Parser.parseDateTime(taskDetails.substring(byStartIndex + 6, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
                } catch (NicoException e) {
                    throw new IllegalArgumentException("Invalid Saved Tasks File Formatting");
                }
                task = new Deadline(description, dueTime);
                break;
            }
            case "E": {
                int fromStartIndex = taskDetails.lastIndexOf(" (from: ");
                int toStartIndex = taskDetails.lastIndexOf(" to: ");
                String description = taskDetails.substring(0, fromStartIndex);
                LocalDateTime startTime;
                LocalDateTime endTime;

                try {
                    startTime = Parser.parseDateTime(taskDetails.substring(fromStartIndex + 8, toStartIndex), DATE_TIME_OUTPUT_FORMAT);
                    endTime = Parser.parseDateTime(taskDetails.substring(toStartIndex + 5, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
                } catch (NicoException e) {
                    throw new IllegalArgumentException("Invalid Saved Tasks File Formatting");
                }
                task = new Event(description, startTime, endTime);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown task type: " + taskType);
            }
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Starts the chatbot, loads saved tasks, and processes commands until exit.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        List<Task> tasks = new ArrayList<Task>();
        try {
            readSavedTasks(tasks);
        } catch (NicoException e) {
            ui.showMessage(e.getMessage());
        }

        ui.showWelcome();
        while(true) {
            ui.showLine();
            String ans = ui.readCommand();

            try {
                Command command = Parser.parseCommand(ans);
                command.execute(tasks, ui);
                if (command.isExit()) {
                    return;
                }
            } catch(NicoException e) {
                ui.showMessage(e.getMessage());
            }
        }
    }
}
