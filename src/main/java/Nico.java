import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Nico chatbot application.
 */

public class Nico {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<Task>();
        String LINE = "____________________________________________________________";
        String banner =
                "███╗   ██╗██╗ ██████╗ ██████╗ \n" +
                "████╗  ██║██║██╔════╝██╔═══██╗\n" +
                "██╔██╗ ██║██║██║     ██║   ██║\n" +
                "██║╚██╗██║██║██║     ██║   ██║\n" +
                "██║ ╚████║██║╚██████╗╚██████╔╝\n" +
                "╚═╝  ╚═══╝╚═╝ ╚═════╝ ╚═════╝ \n";
        System.out.println(LINE);
        System.out.println(banner);
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
                            System.out.println("\t" + LINE);
                            System.out.println("\tI've marked this task as done:");
                            System.out.println("\t  " + task);
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
                            System.out.println("\t" + LINE);
                            System.out.println("\tI've marked this task as not done:");
                            System.out.println("\t  " + task);
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
                        System.out.println("\t" + LINE);
                        System.out.println("\tNice! I've added this task: ");
                        System.out.println("\t" + newTodo);
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
                            System.out.println("\t" + LINE);
                            System.out.println("\tNice! I've added this task: ");
                            System.out.println("\t" + newDeadline);
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
                            System.out.println("\tNice! I've added this task: ");
                            System.out.println("\t" + newEvent);
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
