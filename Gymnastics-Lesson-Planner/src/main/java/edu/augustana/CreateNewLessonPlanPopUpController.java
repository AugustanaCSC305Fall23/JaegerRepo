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
    private void createNewLesson(){
        if (!lessonName.getText().isEmpty()) {
            Stage currWindow = (Stage) createNewLessonButton.getScene().getWindow();
            Lesson lesson = new Lesson (lessonName.getText());
            App.getCurrentSelectedCourse().addData(lesson);
            App.setCurrentSelectedLesson(lesson);
            currWindow.close();
        }
    }

}