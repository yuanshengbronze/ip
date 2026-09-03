package nico;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    /** Launches the JavaFX application through a classpath-safe entry point. */
    public static void main(String[] args) {
        Application.launch(Ui.class, args);
    }
}
