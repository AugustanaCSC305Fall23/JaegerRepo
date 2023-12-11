package edu.augustana;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CreateNewOptionPopUp {
    private Stage popUpWindow;
    private final String optionType;

    public CreateNewOptionPopUp(String optionType){
        this.optionType = optionType;

        popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(App.primaryStage);
        VBox contentVBox = new VBox(15);
        contentVBox.setAlignment(Pos.CENTER);

        Image backgroundImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/background.jpg")).toExternalForm());

        if (optionType.equalsIgnoreCase("lesson")) {
            backgroundImage = new Image(Objects.requireNonNull(App.class.getResource("staticFiles/images/lessonbackground.jpg")).toExternalForm());
        }
        // Create a BackgroundImage
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        // Create a Background with the BackgroundImage
        Background background = new Background(backgroundImg);
        contentVBox.setBackground(background);


        // intialize data into the contentVBox
        Label label = new Label("Create a " + optionType);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Segoe UI Variable Display Semil", 35));

        TextField optionTypeName = new TextField();
        optionTypeName.setStyle("-fx-background-radius: 20;");
        optionTypeName.setMinWidth(200);

        HBox hboc = new HBox();
        hboc.getChildren().add(optionTypeName);
        hboc.setAlignment(Pos.CENTER);

        contentVBox.getChildren().addAll(label, hboc);

        Button loadOptionButton = new Button("Create New");
        loadOptionButton.setStyle("-fx-background-color: #34c6a4;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12;" +
                "-fx-pref-height: 20;"
        );
        loadOptionButton.setOnMouseEntered(e -> loadOptionButton.setStyle("-fx-background-color: white;-fx-background-radius: 20;-fx-text-fill: #34c6a4;-fx-font-size: 12;-fx-pref-height: 20;"));
        loadOptionButton.setOnMouseClicked(event -> createClicked(optionTypeName.getText()));
        loadOptionButton.setOnMouseExited(e->loadOptionButton.setStyle("-fx-background-color: #34c6a4;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12;" +
                "-fx-pref-height: 20;"
        ));
        contentVBox.getChildren().add(loadOptionButton);

        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);
    }

    public Stage getPopUpWindow() {
        return popUpWindow;
    }

    private void createClicked(String name){
        if (name.isBlank()){
            showAlert("Name is required");
        }else if (optionType.equalsIgnoreCase("course")){
            if (!App.addCourseToCourses(new Course(name))){
                showAlert("A course of this name already exists");
            }else {
                popUpWindow.close();
                SelectOptionPopUp lessonSelection = new SelectOptionPopUp("lesson");
                lessonSelection.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
                lessonSelection.getPopUpWindow().show();
            }
        }else{
            App.addLessonToLessons(new Lesson(name));
            popUpWindow.close();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Name");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
}
