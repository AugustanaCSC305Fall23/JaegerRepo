package edu.augustana;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateNewLessonPlanPopUpController {
    @FXML
    private Button createNewLessonButton;

    @FXML
    private TextField lessonName;
    @FXML
    private void createnewLesson(){
        if (!lessonName.getText().isEmpty()) {
            Stage currWindow = (Stage) createNewLessonButton.getScene().getWindow();
            currWindow.close();
            App.lessonSelected(true);
        }
    }
}