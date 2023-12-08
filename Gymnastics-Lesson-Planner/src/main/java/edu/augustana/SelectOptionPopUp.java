package edu.augustana;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class SelectOptionPopUp {

    private Stage popUpWindow;
    private final String optionType;
    private VBox optionsVBox;
    //    private Label selectedCourseLabel;
//    private Label selectedLessonLabel;
    private ObservableList<Node> windowContent = App.primaryStage.getScene().getRoot().getChildrenUnmodifiable();
    private ListView<String> cardBox = ((ListView<String>) ((VBox) windowContent.get(2)).getChildren().get(2));
    private ListView<String> equipmentBox = ((ListView<String>) ((VBox) windowContent.get(2)).getChildren().get(4));
    private SelectOptionPopUp selectLessonPopUp;

    public SelectOptionPopUp(String optionType) {
        System.out.println();
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
        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        // Create a Background with the BackgroundImage
        Background background = new Background(backgroundImg);
        contentVBox.setBackground(background);


        // intialize data into the contentVBox
        Label label = new Label("Select a " + optionType);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Segoe UI Variable Display Semil", 35));

        optionsVBox = new VBox(5);
        optionsVBox.setAlignment(Pos.CENTER);

        contentVBox.getChildren().add(label);
        contentVBox.getChildren().add(optionsVBox);

        Button loadOptionButton = new Button("Load a " + optionType);
        setLoadButtonStyle(loadOptionButton);
        loadOptionButton.setOnMouseEntered(e -> loadOptionButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: #34c6a4;" +
                        "-fx-font-size: 12;" +
                        "-fx-pref-height: 20;"));
        loadOptionButton.setOnMouseExited(e-> setLoadButtonStyle(loadOptionButton));
        loadOptionButton.setOnMouseClicked(event -> {
            try {
                loadButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contentVBox.getChildren().add(loadOptionButton);
        if (optionType.equalsIgnoreCase("course")) {
            Button clearHist = new Button("Clear History");
            setClearStyle(clearHist);
            clearHist.setOnMouseEntered(e -> clearHist.setStyle(
                            "-fx-background-color: white;" +
                            "-fx-background-radius: 20;" +
                            "-fx-text-fill: #cf5d66;" +
                            "-fx-font-size: 12;" +
                            "-fx-pref-height: 20;"));
            clearHist.setOnMouseExited(e -> setClearStyle(clearHist));
            clearHist.setOnMouseClicked(e -> App.clearHistory());
            contentVBox.getChildren().add(clearHist);
        }

        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);
    }

    private void setLoadButtonStyle(Button button){
        button.setStyle("-fx-background-color: #34c6a4;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12;" +
                "-fx-pref-height: 20;"
        );
    }
    private void setClearStyle(Button button) {
        button.setStyle("-fx-background-color: #cf5d66;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12;" +
                "-fx-pref-height: 20;"
        );
    }

    private void loadButtonClick() throws IOException {
        getPopUpWindow().close();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + optionType);
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(optionType + " (*. " + optionType.toLowerCase() + ")", "*." + optionType);
        fileChooser.getExtensionFilters().add(filter);
        Window mainWindow = App.primaryStage.getScene().getWindow();
        File chosenFile = fileChooser.showOpenDialog(mainWindow);

        App.currentLoadedCourseFile = chosenFile;

        if (chosenFile != null) {
            Course loadedCourse = Course.loadFromFile(chosenFile);
            App.setCurrentSelectedCourse(loadedCourse);
            selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
            selectLessonPopUp.getPopUpWindow().show();
        }

    }

    public Stage getPopUpWindow() {
        return popUpWindow;
    }

    public SelectOptionPopUp initializeCourseInWindow(HashMap<String, Course> data) {
        selectLessonPopUp = new SelectOptionPopUp("Lesson");
        for (Course course : data.values()) {
            addOptionToContentVBox(course.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedCourse(course);
                getPopUpWindow().close();
                selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
                selectLessonPopUp.getPopUpWindow().show();
            });
        }
        return selectLessonPopUp;
    }

    public void initializeLessonInWindow(ArrayList<Lesson> data) {
        while (!optionsVBox.getChildren().isEmpty()) {
            optionsVBox.getChildren().remove(0);
        }

        for (Lesson lesson : data) {
            addOptionToContentVBox(lesson.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedLesson(lesson);
                resetCardBoxAndEquipmentBox();
                getPopUpWindow().close();
            });
        }
    }

    private void resetCardBoxAndEquipmentBox() {
        resetCardBox();
        resetEquipmentBox();
        System.out.println();
    }

    private void resetCardBox() {
        while (!cardBox.getItems().isEmpty()) {
            cardBox.getItems().remove(0);
        }
        for (CardView cardView : App.getCurrentSelectedLesson().getSelectedCardViews()) {
            cardBox.getItems().add(cardView.getCardTitle());
        }
    }

    private void resetEquipmentBox() {
        while (!equipmentBox.getItems().isEmpty()) {
            equipmentBox.getItems().remove(0);
        }
        for (CardView cardView : App.getCurrentSelectedLesson().getSelectedCardViews()) {
            for (String e : cardView.getEquipments()) {
                equipmentBox.getItems().add(e);
            }
        }
    }

    private Button addOptionToContentVBox(String buttonName) {
        Button option = new Button(buttonName);
        setOptionButtonStyle(option);
        option.setOnMouseEntered(e -> option.setStyle(
                "-fx-background-color: #4654a4;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12;" +
                        "-fx-min-width: 120;" +
                        "-fx-pref-height: 20;"));
        option.setOnMouseExited(e -> setOptionButtonStyle(option));

        optionsVBox.getChildren().add(option);
        return option;
    }
    private void setOptionButtonStyle(Button button){
        button.setStyle("-fx-background-color: #447ca4;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12;" +
                "-fx-min-width: 120;" +
                "-fx-pref-height: 20;"
        );
    }
    public VBox getoptionsVBox() {
        return optionsVBox;
    }





}
