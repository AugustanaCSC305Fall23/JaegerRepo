package edu.augustana;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectOptionPopUp {

    private Stage popUpWindow;
    private final String optionType;
    private VBox optionsVBox;
//    private Label selectedCourseLabel;
//    private Label selectedLessonLabel;
    private ObservableList<Node> windowContent = App.primaryStage.getScene().getRoot().getChildrenUnmodifiable();
    private ListView<String> cardBox = ((ListView<String>)((VBox) windowContent.get(2)).getChildren().get(2));
    private ListView<String> equipmentBox = ((ListView<String>)((VBox) windowContent.get(2)).getChildren().get(4));
    private SelectOptionPopUp selectLessonPopUp;

    public SelectOptionPopUp(String optionType){
        System.out.println();
        this.optionType = optionType;

        popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(App.primaryStage);
        VBox contentVBox = new VBox(20);
        contentVBox.setAlignment(Pos.CENTER);
        contentVBox.setStyle("-fx-background-color: #E4CCFF");
        // intialize data into the contentVBox
        Label label = new Label("Select a " + optionType);
        label.setFont(new Font("Segoe Script", 29));

        optionsVBox = new VBox(5);
        optionsVBox.setAlignment(Pos.CENTER);

        contentVBox.getChildren().add(label);
        contentVBox.getChildren().add(optionsVBox);

        Button loadOptionButton = new Button("Load a "+ optionType);
        loadOptionButton.setOnMouseClicked(event -> {
            try {
                loadButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contentVBox.getChildren().add(loadOptionButton);

        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);
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

    public SelectOptionPopUp initializeCourseInWindow(HashMap<String, Course> data){
        selectLessonPopUp = new SelectOptionPopUp("Lesson");
        for (Course course: data.values()){
            addOptionToContentVBox(course.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedCourse(course);
                getPopUpWindow().close();
                selectLessonPopUp.initializeLessonInWindow(App.getCurrentSelectedCourse().getLessons());
                selectLessonPopUp.getPopUpWindow().show();
            });
        }
        return selectLessonPopUp;
    }

    public void initializeLessonInWindow(ArrayList<Lesson> data){
        while (!optionsVBox.getChildren().isEmpty()){
            optionsVBox.getChildren().remove(0);
        }

        for (Lesson lesson: data){
            addOptionToContentVBox(lesson.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedLesson(lesson);
                resetCardBoxAndEquipmentBox();
                getPopUpWindow().close();
            });
        }
    }

    private void resetCardBoxAndEquipmentBox(){
        resetCardBox();
        resetEquipmentBox();
        System.out.println();
    }

    private void resetCardBox(){
        while (!cardBox.getItems().isEmpty()){
            cardBox.getItems().remove(0);
        }
        for (Integer id: App.getCurrentSelectedLesson().getCardIndexes()){
            cardBox.getItems().add(App.getCardDatabase().get(id).getTitle());
        }
    }

    private void resetEquipmentBox(){
        while (!equipmentBox.getItems().isEmpty()){
            equipmentBox.getItems().remove(0);
        }
        for (Integer id: App.getCurrentSelectedLesson().getCardIndexes()){
            Card equipmentToAdd = App.getCardDatabase().get(id);
            for (String e: equipmentToAdd.getEquipment()){
                equipmentBox.getItems().add(e);
            }
        }
    }

    private Button addOptionToContentVBox(String buttonName){
        Button option = new Button(buttonName);
        optionsVBox.getChildren().add(option);
        return option;
    }

    public VBox getoptionsVBox() {
        return optionsVBox;
    }
}
