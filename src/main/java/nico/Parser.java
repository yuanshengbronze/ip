package nico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Interprets user input and creates command objects when possible.
 */
public class Parser {
    /**
     * Splits user input into a command word and its optional argument.
     *
     * @param fullCommand Complete command entered by the user.
     * @return Command corresponding to the user input.
     * @throws NicoException If the command is not recognized or has invalid arguments.
     */
    public static Command parseCommand(String fullCommand) throws NicoException {
        String[] commandArray = fullCommand.trim().split("\\s+", 2);
        String argument = commandArray.length > 1 ? commandArray[1] : null;
        switch (commandArray[0]) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "urgent":
                if (argument != null) {
                    throw new NicoException("Please use: urgent");
                }
                return new UrgentCommand();
            case "mark":
                return new MarkCommand(argument);
            case "unmark":
                return new UnmarkCommand(argument);
            case "todo":
                return new TodoCommand(argument);
            case "deadline":
                return new DeadlineCommand(argument);
            case "event":
                return new EventCommand(argument);
            case "delete":
                return new DeleteCommand(argument);
            case "find":
                return new FindCommand(argument);
            default:
                throw new NicoException("Sorry, I don't understand what that command means :(");
            }
    }

    /**
     * Converts user-entered date-time text to a LocalDateTime value.
     *
     * @param input Date-time text to parse.
     * @param format Expected date-time format.
     * @return Parsed date and time.
     * @throws NicoException If the input does not match the expected format.
     */
    public static LocalDateTime parseDateTime(String input, String format) throws NicoException {
        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            throw new NicoException(String.format("Please use %s format for date and time!", format));
        }
    }
}
