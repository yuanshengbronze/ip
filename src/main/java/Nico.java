import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/**
 * Entry point for the Nico chatbot application.
 */
public class Nico {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<Task>();
        Pattern markPattern = Pattern.compile("^mark\\s+(-?\\d+)$");
        Pattern unmarkPattern = Pattern.compile("^unmark\\s+(-?\\d+)$");
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
            Matcher markMatcher = markPattern.matcher(ans);
            Matcher unmarkMatcher = unmarkPattern.matcher(ans);

            if(ans.equals("bye")) {
                System.out.println("\t" + LINE);
                System.out.println("\tNice seeing you. Until next time!");
                System.out.println("\t" + LINE);
                break;
            } else if(ans.equals("list")) {
                System.out.println("\t" + LINE);
                for(int i = 0; i < tasks.size(); i++) {
                    String text = String.format("\t %d. %s", i + 1, tasks.get(i));
                    System.out.println(text);
                }
            } else if(markMatcher.matches()) {
                int taskNumber = Integer.parseInt(ans.split("\\s+")[1]);
                if(taskNumber < 1 || taskNumber > tasks.size()) {
                    System.out.println("\t" + LINE);
                    System.out.println("\tSorry, that task number is not in the list.");
                } else {
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    System.out.println("\t" + LINE);
                    System.out.println("\tNice! I've marked this task as done:");
                    System.out.println("\t  " + task);
                }
            } else if(unmarkMatcher.matches()) {
                int taskNumber = Integer.parseInt(ans.split("\\s+")[1]);
                if(taskNumber < 1 || taskNumber > tasks.size()) {
                    System.out.println("\t" + LINE);
                    System.out.println("\tSorry, that task number is not in the list.");
                } else {
                    Task task = tasks.get(taskNumber - 1);
                    task.unmarkAsDone();
                    System.out.println("\t" + LINE);
                    System.out.println("\tNice! I've marked this task as not done:");
                    System.out.println("\t  " + task);
                }
            } else {
                Task newTask = new Task(ans);
                tasks.add(newTask);
                System.out.println("\t" + LINE);
                System.out.println("\tadded: " + ans);
            }
        }
    }
}
