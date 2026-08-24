import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output for the Nico chatbot.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the startup banner and greeting.
     */
    public void showWelcome() {
        System.out.println(Nico.LINE);
        System.out.println(Nico.BANNER);
        System.out.println("\tHey man! It's Nico, what can I do for you?");
    }

    /**
     * Shows the separator printed before command output.
     */
    public void showLine() {
        System.out.println("\t" + Nico.LINE);
    }

    /**
     * Shows the farewell message before the program exits.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("\tNice seeing you. Until next time!");
        showLine();
    }

    /**
     * Shows a message that has already been formatted by the caller.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Shows all tasks with their one-based list numbers.
     */
    public void showTaskList(List<Task> tasks) {
        showLine();
        for (int i = 0; i < tasks.size(); i++) {
            String text = String.format("\t %d. %s", i + 1, tasks.get(i));
            System.out.println(text);
        }
    }

    /**
     * Shows the confirmation after a task is added.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("\tNice! I've added this task: ");
        System.out.println("\t\t" + task);
        System.out.println("\tNow you have " + taskCount + " tasks.");
    }

    /**
     * Shows the confirmation after a task is marked as done.
     */
    public void showTaskMarkedDone(Task task) {
        showLine();
        System.out.println("\tI've marked this task as done:");
        System.out.println("\t\t" + task);
    }

    /**
     * Shows the confirmation after a task is marked as not done.
     */
    public void showTaskMarkedNotDone(Task task) {
        showLine();
        System.out.println("\tI've marked this task as not done:");
        System.out.println("\t\t" + task);
    }

    /**
     * Shows the confirmation after a task is removed.
     */
    public void showTaskRemoved(Task task, int taskCount) {
        showLine();
        System.out.println("\tI've removed this task");
        System.out.println("\t\t" + task);
        System.out.println("\tNow you have " + taskCount + " tasks.");
    }

    /**
     * Shows one urgent task on the same line, or tied urgent tasks as bullet points.
     */
    public void showUrgentTaskGroup(String label, List<? extends Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("\t" + label + ": None");
        } else if (tasks.size() == 1) {
            System.out.println("\t" + label + ": " + tasks.getFirst());
        } else {
            System.out.println("\t" + label + ":");
            for (Task task : tasks) {
                System.out.println("\t- " + task);
            }
        }
    }
}
