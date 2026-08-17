import java.util.Scanner;

/**
 * Entry point for the Nico chatbot application.
 */
public class Nico {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
        System.out.println("     Hey man! It's Nico, what can I do for you?");
        while(true) {
            System.out.println("     " + LINE);
            String ans = scanner.nextLine();
            if(ans.equals("bye")) {
                System.out.println("     " + LINE);
                System.out.println("     Nice seeing you. Until next time!");
                System.out.println("     " + LINE);
                break;
            } else {
                System.out.println("     " + LINE);
                System.out.println("     " + ans);
            }
        }
    }
}
