package edu.augustana;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class PrimaryController {

    @FXML
    private BorderPane window;

    @FXML
    private GridPane cardGrid;

    @FXML
    private VBox filters;

    @FXML
    private ComboBox<String> pickLessonComboBox;

    @FXML
    private void initialize() {
        pickLessonComboBox.getItems().addAll("+ Create New Lesson");
    }


    @FXML
    private void plusClicked() throws IOException {
        if (App.isLessonSelected()){
            System.out.println("lesson clicked");
        }else{
            showAddLessonPlanPopUpWindow();
        }
    }


    private void showAddLessonPlanPopUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addLessonPlanPopUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Add to lesson");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

}
