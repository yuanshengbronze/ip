package nico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Represents one message displayed in the conversation. */
public class DialogBox extends HBox {

    private final Label text;

    /** Creates a dialog box containing the supplied message. */
    public DialogBox(String s) {
        text = new Label(s);
        text.setWrapText(true);
        text.setMaxWidth(270);
        text.getStyleClass().add("message-bubble");
        getStyleClass().add("dialog");
        this.getChildren().add(text);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /** Creates a right-aligned dialog box for a user message. */
    public static DialogBox getUserDialog(String s) {
        DialogBox dialogBox = new DialogBox(s);
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /** Creates a left-aligned dialog box for a Nico message. */
    public static DialogBox getDukeDialog(String s) {
        DialogBox dialogBox = new DialogBox(s);
        dialogBox.flip();
        dialogBox.getStyleClass().add("nico-dialog");
        return dialogBox;
    }
}
