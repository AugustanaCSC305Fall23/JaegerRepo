package edu.augustana;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SelectOptionPopUp {
    private final String optionType;
    private Stage popUpWindow;
    private VBox optionsVBox;
    private ObservableList<Node> windowContent = App.primaryStage.getScene().getRoot().getChildrenUnmodifiable();
    private ListView<String> cardBox = ((ListView<String>) ((VBox) windowContent.get(2)).getChildren().get(2));
    private ListView<String> equipmentBox = ((ListView<String>) ((VBox) windowContent.get(2)).getChildren().get(4));
    private SelectOptionPopUp selectLessonPopUp;

    public SelectOptionPopUp(String optionType) {
        this.optionType = optionType;
        popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(App.primaryStage);
        if (optionType.equalsIgnoreCase("lesson")) {
            popUpWindow.initStyle(StageStyle.UNDECORATED);
        }
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
        Label label = new Label("Select a " + optionType);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Segoe UI Variable Display Semil", 35));

        optionsVBox = new VBox(5);
        optionsVBox.setAlignment(Pos.CENTER);

        contentVBox.getChildren().add(label);
        contentVBox.getChildren().add(optionsVBox);

        Button createOptionButton = new Button("Create a " + optionType);
        setLoadButtonStyle(createOptionButton);
        createOptionButton.setOnMouseEntered(e -> createOptionButton.setStyle("-fx-background-color: white;-fx-background-radius: 20;-fx-text-fill: #34c6a4;-fx-font-size: 12;-fx-pref-height: 20;"));
        createOptionButton.setOnMouseExited(e -> setLoadButtonStyle(createOptionButton));
        createOptionButton.setOnMouseClicked(event -> {
            createButtonClick();
        });

        Button loadOptionButton = new Button("Load a " + optionType);
        setLoadButtonStyle(loadOptionButton);
        loadOptionButton.setOnMouseEntered(e -> loadOptionButton.setStyle("-fx-background-color: white;-fx-background-radius: 20;-fx-text-fill: #34c6a4;-fx-font-size: 12;-fx-pref-height: 20;"));
        loadOptionButton.setOnMouseExited(e -> setLoadButtonStyle(loadOptionButton));
        loadOptionButton.setOnMouseClicked(event -> {
            try {
                loadButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox createLoadWrapper = new HBox(5);
        createLoadWrapper.setAlignment(Pos.CENTER);
        createLoadWrapper.getChildren().addAll(createOptionButton, loadOptionButton);
        contentVBox.getChildren().add(createLoadWrapper);
        if (optionType.equalsIgnoreCase("course")) {
            Button clearHist = new Button("Clear History");
            setClearStyle(clearHist);
            clearHist.setOnMouseEntered(e -> clearHist.setStyle("-fx-background-color: white;-fx-background-radius: 20;-fx-text-fill: #cf5d66;-fx-font-size: 12;-fx-pref-height: 20;"));
            clearHist.setOnMouseExited(e -> setClearStyle(clearHist));
            clearHist.setOnMouseClicked(e -> App.clearHistory());
            createLoadWrapper.getChildren().add(clearHist);
        }

        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);
    }

    private void createButtonClick() {
        popUpWindow.close();
        CreateNewOptionPopUp createNewOptionPopUp = new CreateNewOptionPopUp(optionType);
        createNewOptionPopUp.getPopUpWindow().show();
    }

    private void setLoadButtonStyle(Button button) {
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
            loadedCourse.setCourseName(App.removeFileExtension(chosenFile.getName()));
            App.addToHistory(App.removeFileExtension(chosenFile.getName()), chosenFile);
            App.setCurrentSelectedCourse(loadedCourse);
            selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
            selectLessonPopUp.getPopUpWindow().show();
        }
    }

    public Stage getPopUpWindow() {
        return popUpWindow;
    }

    public SelectOptionPopUp initializeCourseInWindow(){
        equipmentBox.getItems().removeAll();
        cardBox.getItems().removeAll();
        while (!optionsVBox.getChildren().isEmpty()){
            optionsVBox.getChildren().remove(0);
        }
        selectLessonPopUp = new SelectOptionPopUp("Lesson");
        for (String fileName: App.historyPaths.keySet()){
            try {
                System.out.println(App.historyPaths.get(fileName));
                Course loadedCourse = Course.loadFromFile(new File(App.historyPaths.get(fileName)));
                addOptionToContentVBox(fileName).setOnMouseClicked(event -> {
                    App.setCurrentSelectedCourse(loadedCourse);
                    getPopUpWindow().close();
                    selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
                    selectLessonPopUp.getPopUpWindow().show();
                });
            }catch (IOException e){
                App.historyPaths.remove(fileName);
                System.out.println("hereeeeeeee");
            }
        }
        return selectLessonPopUp;
    }

    public void initializeLessonInWindow(ArrayList<Lesson> data) {
        equipmentBox.getItems().removeAll();
        cardBox.getItems().removeAll();
        while (!optionsVBox.getChildren().isEmpty()) {
            optionsVBox.getChildren().remove(0);
        }
        for (Lesson lesson : data) {
            addOptionToContentVBox(lesson.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedLesson(lesson);
                getPopUpWindow().close();
            });
        }
    }

    private Button addOptionToContentVBox(String buttonName) {
        System.out.println(buttonName);
        Button option = new Button(buttonName);
        ImageView deleteIcon = getDeleteIcon(buttonName, optionsVBox.getChildren().size());
        setOptionButtonStyle(option);
        option.setOnMouseEntered(e -> option.setStyle(
                "-fx-background-color: #4654a4;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12;" +
                        "-fx-min-width: 120;" +
                        "-fx-pref-height: 20;"));
        option.setOnMouseExited(e -> setOptionButtonStyle(option));
        HBox optionAndDeleteIconWrapper = new HBox(5);
        optionAndDeleteIconWrapper.setAlignment(Pos.CENTER);
        optionAndDeleteIconWrapper.getChildren().addAll(option, deleteIcon);
        optionsVBox.getChildren().add(optionAndDeleteIconWrapper);
        return option;
    }

    private ImageView getDeleteIcon(String buttonName, int index) {
        ImageView deleteIcon = App.getDeleteIcon();
        deleteIcon.setOnMouseClicked(event -> {
            if (optionType.equalsIgnoreCase("course")){
                new File(App.historyPaths.get(buttonName)).delete();
                App.historyPaths.remove(buttonName);
                try {
                    App.saveCourseHistory();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                initializeCourseInWindow();
                App.setCurrentSelectedCourse(null);
            }else {
                App.getCurrentSelectedCourse().removeLesson(index);
                initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
            }
        });
        return deleteIcon;
    }

    private void setOptionButtonStyle(Button button) {
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
