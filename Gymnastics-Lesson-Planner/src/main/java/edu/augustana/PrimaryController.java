package edu.augustana;

import java.io.IOException;
import java.nio.channels.Pipe;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("This is a test");
        App.setRoot("secondary");
    }
}
