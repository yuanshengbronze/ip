import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entry point for the Nico chatbot application.
 */

public class Nico {
    public static final Path FILE_PATH = Paths.get("data", "tasks.txt");
    public static final String DATE_TIME_INPUT_FORMAT = "dd-MM-yyyy HHmm";
    public static final String DATE_TIME_OUTPUT_FORMAT = "MMM dd yyyy HH:mm";
    public static final String LINE = "____________________________________________________________";
    public static final String BANNER =
        "███╗   ██╗██╗ ██████╗ ██████╗ \n" +
        "████╗  ██║██║██╔════╝██╔═══██╗\n" +
        "██╔██╗ ██║██║██║     ██║   ██║\n" +
        "██║╚██╗██║██║██║     ██║   ██║\n" +
        "██║ ╚████║██║╚██████╗╚██████╔╝\n" +
        "╚═╝  ╚═══╝╚═╝ ╚═════╝ ╚═════╝ \n";

    /**
     * Loads tasks from the save file into the task list.
     */
    private static void readSavedTasks(List<Task> tasks) throws NicoException {
        try {
            createTasksFile();
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
     * Appends one new task to the save file.
     */
    private static void writeSavedTask(Task task) throws NicoException {
        try {
            createTasksFile();
            String taskString = task.toString();
            Files.writeString(FILE_PATH, taskString + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new NicoException("\tSorry, I could not save this task to tasks.txt.");
        }
    }

    /**
     * Rewrites the save file so it matches the current task list.
     */
    private static void writeAllSavedTasks(List<Task> tasks) throws NicoException {
        try {
            createTasksFile();
            StringBuilder savedTasks = new StringBuilder();
            for (Task task : tasks) {
                savedTasks.append(task).append(System.lineSeparator());
            }
            Files.writeString(FILE_PATH, savedTasks.toString(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new NicoException("\tSorry, I could not update tasks.txt.");
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
                    dueTime = convertToLocalDateTime(taskDetails.substring(byStartIndex + 6, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
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
                    startTime = convertToLocalDateTime(taskDetails.substring(fromStartIndex + 8, toStartIndex), DATE_TIME_OUTPUT_FORMAT);
                    endTime = convertToLocalDateTime(taskDetails.substring(toStartIndex + 5, taskDetails.length() - 1), DATE_TIME_OUTPUT_FORMAT);
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
     * Creates the data folder and save file if they do not exist.
     */
    private static void createTasksFile() throws IOException {
        Path parentDirectory = FILE_PATH.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
        if (!Files.exists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
        }
    }

    private static LocalDateTime convertToLocalDateTime(String input, String format) throws NicoException {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(input, inputFormatter);
        } catch (DateTimeParseException e) {
            throw new NicoException(String.format("\tPlease use %s format for date and time!", format));
        }
        return dateTime;
    }

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
            String[] commandArray = ans.trim().split("\\s+", 2);
            String commandWord = commandArray[0];

            try {
                switch(commandWord) {
                    case "bye": {
                        ui.showGoodbye();
                        return;
                    }
                    case "list": {
                        ui.showTaskList(tasks);
                        break;
                    }
                    case "urgent": {
                        if (hasArgument(commandArray)) {
                            throw new NicoException("\tPlease use: urgent");
                        }
                        ui.showLine();
                        printUrgentTasks(tasks, ui);
                        break;
                    }
                    case "mark": {
                        if (!hasArgument(commandArray)) {
                            throw new NicoException("\tNo task number. Please use: mark TASK_NUMBER");
                        }
                        if (!isInteger(commandArray[1])) {
                            throw new NicoException("\tTask number must be an integer.");
                        }

                        int taskNumber = Integer.parseInt(commandArray[1].trim());
                        if(taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showLine();
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            task.markAsDone();
                            writeAllSavedTasks(tasks);
                            ui.showTaskMarkedDone(task);
                        }
                        break;
                    }
                    case "unmark": {
                        if (!hasArgument(commandArray)) {
                            throw new NicoException("\tNo task number. Please use: unmark TASK_NUMBER");
                        }
                        if (!isInteger(commandArray[1])) {
                            throw new NicoException("\tTask number must be an integer.");
                        }
                        int taskNumber = Integer.parseInt(commandArray[1].trim());
                        if(taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showLine();
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            task.unmarkAsDone();
                            writeAllSavedTasks(tasks);
                            ui.showTaskMarkedNotDone(task);
                        }
                        break;
                    }
                    case "todo": {
                        if (!hasArgument(commandArray) || isEmpty(commandArray[1])) {
                            throw new NicoException("\tDescription can't be empty. Please use: todo DESCRIPTION");
                        }
                        String taskDescription = commandArray[1].trim();
                        Task newTodo = new Todo(taskDescription);
                        tasks.add(newTodo);
                        writeSavedTask(newTodo);
                        ui.showLine();
                        ui.showTaskAdded(newTodo, tasks.size());
                        break;
                    }
                    case "deadline": {
                        if (!hasArgument(commandArray)) {
                            throw new NicoException("\tDescription can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
                        }
                        String[] parts = commandArray[1].split("\\s*/by\\s+", 2);
                        if (parts.length < 2) {
                            throw new NicoException("\tPlease use: deadline DESCRIPTION /by DUE TIME");
                        } else if (isEmpty(parts[0])) {
                            throw new NicoException("\tDescription can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
                        } else if (isEmpty(parts[1])) {
                            throw new NicoException("\tDue time can't be empty. Please use: deadline DESCRIPTION /by DUE TIME");
                        } else {
                            String taskDescription = parts[0].trim();
                            String dueTimeUserString = parts[1].trim();
                            Deadline newDeadline = new Deadline(taskDescription, convertToLocalDateTime(dueTimeUserString, DATE_TIME_INPUT_FORMAT));
                            tasks.add(newDeadline);
                            writeSavedTask(newDeadline);
                            ui.showLine();
                            ui.showTaskAdded(newDeadline, tasks.size());
                        }
                        break;
                    }
                    case "event": {
                        if (!hasArgument(commandArray)) {
                            throw new NicoException("\tDescription can't be empty. Please use: event DESCRIPTION /from START TIME /to END TIME");
                        }
                        String[] parts = commandArray[1].split("\\s*/from\\s*|\\s*/to\\s*", 3);
                        if (parts.length < 3) {
                            throw new NicoException("\tPlease use: event DESCRIPTION /from START TIME /to END TIME");
                        } else if (isEmpty(parts[0])) {
                            throw new NicoException("\tThe description cannot be empty.");
                        } else if (isEmpty(parts[1]) || isEmpty(parts[2])) {
                            throw new NicoException("\tPlease use: event DESCRIPTION /from START TIME /to END TIME");
                        } else {
                            String taskDescription = parts[0].trim();
                            String startTimeUserString = parts[1].trim();
                            String endTimeUserString = parts[2].trim();

                            Event newEvent = new Event(taskDescription,
                                    convertToLocalDateTime(startTimeUserString, DATE_TIME_INPUT_FORMAT),
                                    convertToLocalDateTime(endTimeUserString, DATE_TIME_INPUT_FORMAT));
                            tasks.add(newEvent);
                            writeSavedTask(newEvent);
                            ui.showTaskAdded(newEvent, tasks.size());
                        }
                        break;
                    }
                    case "delete": {
                        if (!hasArgument(commandArray)) {
                            throw new NicoException("\tNo task number. Please use: delete TASK_NUMBER");
                        }
                        if (!isInteger(commandArray[1])) {
                            throw new NicoException("\tTask number must be an integer.");
                        }
                        int taskNumber = Integer.parseInt(commandArray[1].trim());
                        if(taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showLine();
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            tasks.remove(taskNumber - 1);
                            writeAllSavedTasks(tasks);
                            ui.showTaskRemoved(task, tasks.size());
                        }
                        break;
                    }
                    default: {
                        throw new NicoException("\tSorry, I don't understand what that command means :(");
                    }
                }
            } catch(NicoException e) {
                ui.showMessage(e.getMessage());
            }
        }
    }

    /**
     * Prints the incomplete event with the earliest start time and incomplete deadline with the earliest due time.
     */
    private static void printUrgentTasks(List<Task> tasks, Ui ui) {
        List<Event> closestEvents = findClosestEvents(tasks);
        List<Deadline> closestDeadlines = findClosestDeadlines(tasks);
        ui.showUrgentTaskGroup("Most urgent event", closestEvents);
        ui.showUrgentTaskGroup("Most urgent deadline", closestDeadlines);
    }

    /**
     * Finds all incomplete events that share the earliest start time among all saved tasks.
     */
    private static List<Event> findClosestEvents(List<Task> tasks) {
        List<Event> closestEvents = new ArrayList<Event>();
        LocalDateTime closestStartTime = null;
        for (Task task : tasks) {
            if (!(task instanceof Event event) || task.isDone()) {
                continue;
            }

            if (closestStartTime == null || event.getStartTime().isBefore(closestStartTime)) {
                closestEvents.clear();
                closestEvents.add(event);
                closestStartTime = event.getStartTime();
            } else if (event.getStartTime().isEqual(closestStartTime)) {
                closestEvents.add(event);
            }
        }
        return closestEvents;
    }

    /**
     * Finds all incomplete deadlines that share the earliest due time among all saved tasks.
     */
    private static List<Deadline> findClosestDeadlines(List<Task> tasks) {
        List<Deadline> closestDeadlines = new ArrayList<Deadline>();
        LocalDateTime closestDueTime = null;
        for (Task task : tasks) {
            if (!(task instanceof Deadline deadline) || task.isDone()) {
                continue;
            }

            if (closestDueTime == null || deadline.getDueTime().isBefore(closestDueTime)) {
                closestDeadlines.clear();
                closestDeadlines.add(deadline);
                closestDueTime = deadline.getDueTime();
            } else if (deadline.getDueTime().isEqual(closestDueTime)) {
                closestDeadlines.add(deadline);
            }
        }
        return closestDeadlines;
    }

    /**
     * Checks whether a parsed command has text after the command word.
     */
    private static boolean hasArgument(String[] commandArray) {
        return commandArray.length > 1;
    }

    /**
     * Checks whether the provided text is empty after trimming whitespace.
     */
    private static boolean isEmpty(String text) {
        return text.trim().isEmpty();
    }

    /**
     * Checks whether the provided text can be parsed as an integer.
     */
    private static boolean isInteger(String text) {
        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
