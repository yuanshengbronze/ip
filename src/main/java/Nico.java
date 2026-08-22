import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Nico chatbot application.
 */

public class Nico {
    public static final String FILE_PATH = "data/tasks.txt";
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
            File tasksFile = new File(FILE_PATH);
            try (Scanner scanner = new Scanner(tasksFile)) {
                while (scanner.hasNextLine()) {
                    String taskString = scanner.nextLine();
                    if (!taskString.trim().isEmpty()) {
                        Task task = createTaskFromTaskString(taskString);
                        tasks.add(task);
                    }
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
            try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
                fw.write(taskString + System.lineSeparator());
            }
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
            try (FileWriter fw = new FileWriter(FILE_PATH)) {
                for (Task task : tasks) {
                    fw.write(task.toString() + System.lineSeparator());
                }
            }
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
                String dueTime = taskDetails.substring(byStartIndex + 6, taskDetails.length() - 1);
                task = new Deadline(description, dueTime);
                break;
            }
            case "E": {
                int fromStartIndex = taskDetails.lastIndexOf(" (from: ");
                int toStartIndex = taskDetails.lastIndexOf(" to: ");
                String description = taskDetails.substring(0, fromStartIndex);
                String startTime = taskDetails.substring(fromStartIndex + 8, toStartIndex);
                String endTime = taskDetails.substring(toStartIndex + 5, taskDetails.length() - 1);
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
        File newFile = new File(FILE_PATH);
        File parentDirectory = newFile.getParentFile();
        if (parentDirectory != null) {
            parentDirectory.mkdirs(); //creates the data folder if it didn't exist.
        }
        newFile.createNewFile();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<Task>();
        try {
            readSavedTasks(tasks);
        } catch (NicoException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("\tHey man! It's Nico, what can I do for you?");
        while(true) {
            System.out.println("\t" + LINE);
            String ans = scanner.nextLine();
            String[] commandArray = ans.trim().split("\\s+", 2);
            String commandWord = commandArray[0];

            try {
                switch(commandWord) {
                    case "bye": {
                        System.out.println("\t" + LINE);
                        System.out.println("\tNice seeing you. Until next time!");
                        System.out.println("\t" + LINE);
                        return;
                    }
                    case "list": {
                        System.out.println("\t" + LINE);
                        for(int i = 0; i < tasks.size(); i++) {
                            String text = String.format("\t %d. %s", i + 1, tasks.get(i));
                            System.out.println(text);
                        }
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
                            System.out.println("\t" + LINE);
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            task.markAsDone();
                            writeAllSavedTasks(tasks);
                            System.out.println("\t" + LINE);
                            System.out.println("\tI've marked this task as done:");
                            System.out.println("\t\t" + task);
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
                            System.out.println("\t" + LINE);
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            task.unmarkAsDone();
                            writeAllSavedTasks(tasks);
                            System.out.println("\t" + LINE);
                            System.out.println("\tI've marked this task as not done:");
                            System.out.println("\t\t" + task);
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
                        System.out.println("\t" + LINE);
                        System.out.println("\tNice! I've added this task: ");
                        System.out.println("\t\t" + newTodo);
                        System.out.println("\tNow you have " + tasks.size() + " tasks.");
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
                            String dueTime = parts[1].trim();
                            Deadline newDeadline = new Deadline(taskDescription, dueTime);
                            tasks.add(newDeadline);
                            writeSavedTask(newDeadline);
                            System.out.println("\t" + LINE);
                            System.out.println("\tNice! I've added this task: ");
                            System.out.println("\t\t" + newDeadline);
                            System.out.println("\tNow you have " + tasks.size() + " tasks.");
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
                            String startTime = parts[1].trim();
                            String endTime = parts[2].trim();
                            Event newEvent = new Event(taskDescription, startTime, endTime);
                            tasks.add(newEvent);
                            writeSavedTask(newEvent);
                            System.out.println("\tNice! I've added this task: ");
                            System.out.println("\t\t" + newEvent);
                            System.out.println("\tNow you have " + tasks.size() + " tasks.");
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
                            System.out.println("\t" + LINE);
                            throw new NicoException("\tSorry, that task number is not in the list.");
                        } else {
                            Task task = tasks.get(taskNumber - 1);
                            tasks.remove(taskNumber - 1);
                            writeAllSavedTasks(tasks);
                            System.out.println("\t" + LINE);
                            System.out.println("\tI've removed this task");
                            System.out.println("\t\t" + task);
                            System.out.println("\tNow you have " + tasks.size() + " tasks.");
                        }
                        break;
                    }
                    default: {
                        throw new NicoException("\tSorry, I don't understand what that command means :(");
                    }
                }
            } catch(NicoException e) {
                System.out.println(e.getMessage());
            }
        }
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
