package nico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Coordinates Nico's task list, storage, and command execution. */
public class Nico {
    public static final Path FILE_PATH = Paths.get("data", "tasks.txt");
    public static final String DATE_TIME_INPUT_FORMAT = "dd-MM-yyyy HHmm";
    public static final String DATE_TIME_OUTPUT_FORMAT = "MMM dd yyyy HH:mm";

    private final List<Task> tasks = new ArrayList<>();

    /** Loads saved tasks into this chatbot instance. */
    public void loadTasks() throws NicoException {
        List<Task> loadedTasks = new ArrayList<>();
        readSavedTasks(loadedTasks);
        tasks.clear();
        tasks.addAll(loadedTasks);
    }

    /** Executes one user command against this chatbot's persistent task list. */
    public boolean processCommand(String inputText, Ui ui) throws NicoException {
        Command command = Parser.parseCommand(inputText);
        command.execute(tasks, ui);
        return command.isExit();
    }

    private static void readSavedTasks(List<Task> tasks) throws NicoException {
        try {
            TaskStorage.createTasksFile(FILE_PATH);
            List<String> taskStrings = Files.readAllLines(FILE_PATH);
            for (String taskString : taskStrings) {
                if (!taskString.trim().isEmpty()) {
                    tasks.add(createTaskFromTaskString(taskString));
                }
            }
        } catch (IOException e) {
            throw new NicoException("Sorry, I could not load tasks.txt.");
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            throw new NicoException("Sorry, tasks.txt contains a task I could not understand.");
        }
    }

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
                    dueTime = Parser.parseDateTime(
                            taskDetails.substring(byStartIndex + 6, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
                } catch (NicoException e) {
                    throw new IllegalArgumentException("Invalid saved task formatting", e);
                }
                task = new Deadline(description, dueTime);
                break;
            }
            case "E": {
                int fromStartIndex = taskDetails.lastIndexOf(" (from: ");
                int toStartIndex = taskDetails.lastIndexOf(" to: ");
                String eventDescription = taskDetails.substring(0, fromStartIndex);
                try {
                    LocalDateTime startTime = Parser.parseDateTime(
                            taskDetails.substring(fromStartIndex + 8, toStartIndex), DATE_TIME_OUTPUT_FORMAT);
                    LocalDateTime endTime = Parser.parseDateTime(
                            taskDetails.substring(toStartIndex + 5, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
                    task = new Event(eventDescription, startTime, endTime);
                } catch (NicoException e) {
                    throw new IllegalArgumentException("Invalid saved task formatting", e);
                }
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
}
