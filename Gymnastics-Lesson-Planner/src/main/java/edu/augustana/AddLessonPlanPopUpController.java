package edu.augustana;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AddLessonPlanPopUpController {

   @FXML
    private Button createNewLessonPlan;

   @FXML
    private void initialize(){

   }
    @FXML
    private void lessonSelected(){
        Stage currWindow = (Stage) createNewLessonPlan.getScene().getWindow();
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
