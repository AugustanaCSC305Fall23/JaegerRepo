package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 1000, 700);
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

   private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void popUpWindow(String fxmlFileName, Stage stage) throws IOException {
//        try{
//            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlFileName));
//            scene.setRoot(fxmlLoader.load());
//
//            Parent root = fxmlLoader.load();
//
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.WINDOW_MODAL);
//            popupStage.initOwner(stage); // Set the owner stage
//            popupStage.setScene(new Scene(root));
//
//        } catch (IOException ex) {
//            System.err.println("Can't find FXML file " + fxmlFileName);
//            ex.printStackTrace();
//        }
        Stage popup = new Stage();
//        popup.setAnchorX(stage.getWidth()/2);
//        popup.setAnchorY(stage.getHeight()/2);
//        popup.i
//        popup.getContent().addAll(new Circle(25, 25, 50));
        popup.initModality(Modality.WINDOW_MODAL);
        Scene selectLesson = new Scene(loadFXML(fxmlFileName), 400, 400);
        popup.initOwner(stage);
        popup.setScene(selectLesson);
//        popup.show(stage);

    }

    public static void main(String[] args) {
        launch();
    }

}