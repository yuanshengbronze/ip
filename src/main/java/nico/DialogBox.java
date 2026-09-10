package nico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Represents one message displayed in the conversation. */
public class DialogBox extends HBox {
    private static final int MAX_TEXT_WIDTH = 270;

    /** Creates a dialog box containing the supplied message. */
    public DialogBox(String message) {
        Label text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(MAX_TEXT_WIDTH);
        text.getStyleClass().add("message-bubble");
        getStyleClass().add("dialog");
        this.getChildren().add(text);
    }

    /**
     * Aligns the dialog box on the left for a Nico response.
     */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> dialogNodes = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(dialogNodes);
        getChildren().setAll(dialogNodes);
    }

    /** Creates a right-aligned dialog box for a user message. */
    public static DialogBox getUserDialog(String message) {
        DialogBox dialogBox = new DialogBox(message);
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /** Creates a left-aligned dialog box for a Nico message. */
    public static DialogBox getNicoDialog(String message) {
        DialogBox dialogBox = new DialogBox(message);
        dialogBox.flip();
        dialogBox.getStyleClass().add("nico-dialog");
        return dialogBox;
    }
}
