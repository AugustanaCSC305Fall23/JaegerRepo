package edu.augustana;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectOptionPopUp {

    private Stage popUpWindow;
    private final String optionType;
    private VBox optionsVBox;
    private Label selectedCourseLabel;
    private Label selectedLessonLabel;
    private ObservableList<Node> windowContent = App.primaryStage.getScene().getRoot().getChildrenUnmodifiable();
    private VBox cardBox = (VBox) ((ScrollPane)((VBox)windowContent.get(2)).getChildren().get(2)).getContent();
    private VBox equipmentBox = (VBox) ((ScrollPane)((VBox)windowContent.get(2)).getChildren().get(4)).getContent();
    public SelectOptionPopUp(String optionType){
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
        Scene scene = new Scene(contentVBox, 600, 400);
        popUpWindow.setScene(scene);

        VBox labels = ((VBox)((VBox)(windowContent.get(1))).getChildren().get(0));
        selectedCourseLabel = (Label) ((HBox) labels.getChildren().get(0)).getChildren().get(0);
        selectedLessonLabel = (Label) ((HBox) labels.getChildren().get(1)).getChildren().get(0);
    }

    public Stage getPopUpWindow() {
        return popUpWindow;
    }

    public SelectOptionPopUp initializeCourseInWindow(HashMap<String, Course> data){
        SelectOptionPopUp selectLessonPopUp = new SelectOptionPopUp("Lesson");
        for (Course course: data.values()){
            addOptionToContentVBox(course.getName()).setOnMouseClicked(event -> {
                App.setCurrentSelectedCourse(course);
                getPopUpWindow().close();

                selectedCourseLabel.setText(course.getName());
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
                selectedLessonLabel.setText(lesson.getName());
                resetCardBoxAndEquipmentBox();

                getPopUpWindow().close();
            });
        }
    }

    private Label cardTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-border-color: grey; -fx-padding: 5px;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPadding(new Insets(10, 0, 0, 0));

        return titleLabel;
    }

    private void resetCardBoxAndEquipmentBox(){
        resetCardBox();
        resetEquipmentBox();
        System.out.println();
    }
    private void resetCardBox(){
        while (!cardBox.getChildren().isEmpty()){
            cardBox.getChildren().remove(0);
        }
        for (Integer id: App.getCurrentSelectedLesson().getCardIndexes()){
            cardBox.getChildren().add(cardTitleBox(App.getCardDatabase().get(id).getTitle()));
        }
    }

    private void resetEquipmentBox(){
        while (!equipmentBox.getChildren().isEmpty()){
            equipmentBox.getChildren().remove(0);
        }
        for (Integer id: App.getCurrentSelectedLesson().getCardIndexes()){
            Card equipmentToAdd = App.getCardDatabase().get(id);
            for (String e: equipmentToAdd.getEquipment()){
                equipmentBox.getChildren().add(cardTitleBox(e));
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
