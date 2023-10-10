package edu.augustana;

import java.io.IOException;
import java.nio.channels.Pipe;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private BorderPane window;

    @FXML
    private GridPane cardGrid;

    @FXML
    private VBox filters;
    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("This is a test");
        App.setRoot("secondary");
    }

    @FXML
    private void initialize(){

    }


}
