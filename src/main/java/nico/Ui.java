package nico;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output for the nico.Nico chatbot.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________";
    public static final String BANNER =
            " _   _ _           \n" +
            "| \\ | (_) ___ ___  \n" +
            "|  \\| | |/ __/ _ \\ \n" +
            "| |\\  | | (_| (_) |\n" +
            "|_| \\_|_|\\___\\___/ \n";
    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
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
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("\tHey man! It's Nico, what can I do for you?");
    }

    /**
     * Shows the separator printed before command output.
     */
    public void showLine() {
        System.out.println("\t" + LINE);
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
        System.out.println("\t" + message);
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
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
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
     *
     * @param task Task that was removed.
     * @param taskCount number of tasks after the removal.
     */
    public void showTaskRemoved(Task task, int taskCount) {
        showLine();
        System.out.println("\tI've removed this task");
        System.out.println("\t\t" + task);
        System.out.println("\tNow you have " + taskCount + " tasks.");
    }

    /**
     * Shows one task on the same line, or multiple tasks as bullet points.
     *
     * @param label Label describing the task group.
     * @param tasks Tasks to display.
     */
    public void showTaskGroup(String label, List<? extends Task> tasks) {
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
