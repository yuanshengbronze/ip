package nico;

/**
 * Represents an error caused by invalid chatbot input or task storage operations.
 */
public class NicoException extends Exception{
    /**
     * Creates an exception without a detail message.
     */
    public NicoException() {
        super();
    }

    /**
     * Creates an exception with a message suitable for displaying to the user.
     *
     * @param message explanation of the error
     */
    public NicoException(String message) {
        super(message);
    }
}
