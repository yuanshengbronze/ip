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
            String[] commandArray = ans.split("\\s+", 2);
            String commandWord = commandArray[0];

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
                    int taskNumber = Integer.parseInt(commandArray[1]);
                    if(taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println("\t" + LINE);
                        System.out.println("\tSorry, that task number is not in the list.");
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
                    int taskNumber = Integer.parseInt(commandArray[1]);
                    if(taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println("\t" + LINE);
                        System.out.println("\tSorry, that task number is not in the list.");
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
                    String taskDescription = commandArray[1];
                    Task newTodo = new Todo(taskDescription);
                    tasks.add(newTodo);
                    System.out.println("\t" + LINE);
                    System.out.println("\tNice! I've added this task: ");
                    System.out.println("\t" + newTodo);
                    break;
                }
                case "deadline": {
                    String[] parts = commandArray[1].split("\\s+/by\\s+", 2);
                    if (parts.length < 2) {
                        System.out.println("\tPlease use: deadline DESCRIPTION /by WHEN");
                    } else {
                        String taskDescription = parts[0];
                        String dueTime = parts[1];
                        Deadline newDeadline = new Deadline(taskDescription, dueTime);
                        tasks.add(newDeadline);
                        System.out.println("\t" + LINE);
                        System.out.println("\tNice! I've added this task: ");
                        System.out.println("\t" + newDeadline);
                    }
                    break;
                }
                case "event": {
                    String[] parts = commandArray[1].split("\\s+/from\\s+|\\s+/to\\s+", 3);
                    if (parts.length < 3) {
                        System.out.println("\tPlease use: event DESCRIPTION /from WHEN /to WHEN");
                    } else {
                        String taskDescription = parts[0];
                        String startTime = parts[1];
                        String endTime = parts[2];
                        Event newEvent = new Event(taskDescription, startTime, endTime);
                        tasks.add(newEvent);
                        System.out.println("\tNice! I've added this task: ");
                        System.out.println("\t" + newEvent);
                    }
                    break;
                }
                default: {
                    Task newTask = new Task(ans);
                    tasks.add(newTask);
                    System.out.println("\t" + LINE);
                    System.out.println("\tNice! I've added this task: ");
                    System.out.println("\t" + newTask);
                }
            }
        }
    }
}
