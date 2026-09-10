package nico;

/** Represents an error caused by invalid user input or task storage. */
public class NicoException extends Exception {
    /** Creates an exception without a detail message. */
    public NicoException() {
        super();
    }

    /**
     * Creates an exception with a detail message.
     *
     * @param message Detail message for the user.
     */
    public NicoException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a user-facing message and its underlying cause.
     *
     * @param message detail message for the user
     * @param cause underlying failure that caused this exception
     */
    public NicoException(String message, Throwable cause) {
        super(message, cause);
    }
}
