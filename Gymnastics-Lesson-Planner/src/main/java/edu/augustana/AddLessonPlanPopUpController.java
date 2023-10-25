package edu.augustana;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

public class AddLessonPlanPopUpController {

   @FXML
    private Button createNewLessonPlan;

    @FXML
    private VBox pickLessonVBox;


   @FXML
    private void initialize(){
    HashMap<String, Lesson> lessons = App.getLessons();

    Lesson Lesson1 = new Lesson("Lesson 1");
    Button lessonButton = new Button(Lesson1.getLessonName());
    lessonButton.setMinWidth(169);


       Lesson Lesson2 = new Lesson("Lesson 2");
       Button lessonButton2 = new Button(Lesson2.getLessonName());
       lessonButton2.setMinWidth(169);




    ScrollPane pickLessonScroll = new ScrollPane();
    pickLessonScroll.setFitToWidth(true);
    pickLessonScroll.setStyle("-fx-background-color:#E4CCFF");

    VBox lessonOption = new VBox(10);
    lessonOption.setStyle("-fx-margin: 10 0 0 0;");
    lessonOption.setAlignment(Pos.CENTER);
    lessonOption.setStyle("-fx-background-color: #E4CCFF");

    pickLessonScroll.setContent(lessonOption);
    pickLessonVBox.getChildren().add(1,pickLessonScroll);

    lessonButton.setOnMouseClicked(event -> {lessonSelected();});
    lessonOption.getChildren().add(lessonButton);
       lessonOption.getChildren().add(lessonButton2);


   }

    @FXML
    private void lessonSelected(){
        Stage currWindow = (Stage) createNewLessonPlan.getScene().getWindow();
        App.lessonSelected(true);
        currWindow.close();
    }

    @FXML
    private void newLessonPlanPopUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createNewLessonPlanPopUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Create new lesson plan");
        stage.setScene(scene);
        stage.show();
        Stage currWindow = (Stage) createNewLessonPlan.getScene().getWindow();
        currWindow.close();
    }


}
