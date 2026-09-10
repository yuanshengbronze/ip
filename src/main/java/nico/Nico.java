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

    private static final int TASK_TYPE_START_INDEX = 1;
    private static final int TASK_TYPE_END_INDEX = 2;
    private static final int TASK_STATUS_INDEX = 4;
    private static final int TASK_DETAILS_START_INDEX = 7;
    private static final int CLOSING_PARENTHESIS_LENGTH = 1;

    private static final char DONE_STATUS = 'X';
    private static final String TODO_TASK_TYPE = "T";
    private static final String DEADLINE_TASK_TYPE = "D";
    private static final String EVENT_TASK_TYPE = "E";
    private static final String DEADLINE_TIME_PREFIX = " (by: ";
    private static final String EVENT_START_TIME_PREFIX = " (from: ";
    private static final String EVENT_END_TIME_PREFIX = " to: ";

    private final List<Task> tasks = new ArrayList<>();

    /**
     * Loads saved tasks into this chatbot instance.
     *
     * @throws NicoException if the saved task file cannot be read or parsed
     */
    public void loadTasks() throws NicoException {
        List<Task> loadedTasks = new ArrayList<>();
        readSavedTasks(loadedTasks);
        tasks.clear();
        tasks.addAll(loadedTasks);
    }

    /**
     * Executes one user command against this chatbot's persistent task list.
     *
     * @param inputText command text entered by the user
     * @param ui user interface used to display command output
     * @return whether the command requests that the application exit
     * @throws NicoException if the command cannot be parsed or executed
     */
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
        } catch (IOException exception) {
            throw new NicoException("Sorry, I could not load tasks.txt.");
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException exception) {
            throw new NicoException("Sorry, tasks.txt contains a task I could not understand.");
        }
    }

    private static Task createTaskFromTaskString(String taskString) {
        String taskType = taskString.substring(TASK_TYPE_START_INDEX, TASK_TYPE_END_INDEX);
        boolean isDone = taskString.charAt(TASK_STATUS_INDEX) == DONE_STATUS;
        String taskDetails = taskString.substring(TASK_DETAILS_START_INDEX);
        Task task = createTask(taskType, taskDetails);

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private static Task createTask(String taskType, String taskDetails) {
        switch (taskType) {
            case TODO_TASK_TYPE: {
                return new Todo(taskDetails);
            }
            case DEADLINE_TASK_TYPE: {
                return createDeadlineFromTaskDetails(taskDetails);
            }
            case EVENT_TASK_TYPE: {
                return createEventFromTaskDetails(taskDetails);
            }
            default: {
                throw new IllegalArgumentException("Unknown task type: " + taskType);
            }
        }
    }

    private static Deadline createDeadlineFromTaskDetails(String taskDetails) {
        int dueTimeStartIndex = taskDetails.lastIndexOf(DEADLINE_TIME_PREFIX);
        String description = taskDetails.substring(0, dueTimeStartIndex);
        String dueTimeString = taskDetails.substring(
                dueTimeStartIndex + DEADLINE_TIME_PREFIX.length(),
                taskDetails.length() - CLOSING_PARENTHESIS_LENGTH);
        LocalDateTime dueTime = parseSavedDateTime(dueTimeString);
        return new Deadline(description, dueTime);
    }

    private static Event createEventFromTaskDetails(String taskDetails) {
        int startTimeStartIndex = taskDetails.lastIndexOf(EVENT_START_TIME_PREFIX);
        int endTimeStartIndex = taskDetails.lastIndexOf(EVENT_END_TIME_PREFIX);
        String description = taskDetails.substring(0, startTimeStartIndex);
        String startTimeString = taskDetails.substring(
                startTimeStartIndex + EVENT_START_TIME_PREFIX.length(), endTimeStartIndex);
        String endTimeString = taskDetails.substring(
                endTimeStartIndex + EVENT_END_TIME_PREFIX.length(),
                taskDetails.length() - CLOSING_PARENTHESIS_LENGTH);
        LocalDateTime startTime = parseSavedDateTime(startTimeString);
        LocalDateTime endTime = parseSavedDateTime(endTimeString);
        return new Event(description, startTime, endTime);
    }

    private static LocalDateTime parseSavedDateTime(String dateTimeString) {
        try {
            return Parser.parseDateTime(dateTimeString, DATE_TIME_OUTPUT_FORMAT);
        } catch (NicoException exception) {
            throw new IllegalArgumentException("Invalid saved task formatting", exception);
        }
    }
}
