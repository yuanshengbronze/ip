import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Nico chatbot application.
 */
public class Nico {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> answers = new ArrayList<String>();
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
            if(ans.equals("bye")) {
                System.out.println("\t" + LINE);
                System.out.println("\tNice seeing you. Until next time!");
                System.out.println("\t" + LINE);
                break;
            } else if(ans.equals("list")) {
                System.out.println("\t" + LINE);
                for(int i = 0; i < answers.size(); i++) {
                    String text = String.format("\t %d. %s", i + 1, answers.get(i));
                    System.out.println(text);
                }
            } else {
                answers.add(ans);
                System.out.println("\t" + LINE);
                System.out.println("\tadded: " + ans);
            }
        }
    }
}
