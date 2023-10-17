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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String imagesFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\edu\\augustana\\images";
    private static Scene scene;
    private static boolean selected;
    private static HashMap<String, Card> cardHashMap;

    @Override
    public void start(Stage stage) throws IOException {
        selected = false;
        cardHashMap = new HashMap<>();
        readDataFromFile();
        scene = new Scene(loadFXML("primary"), 1400, 760);
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public void readDataFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(imagesFilePath + "/Demo1/DEMO1.csv"));
        String line = br.readLine();
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] cardData = line.split(",");    // use comma as separator
            Card newCard = new Card(cardData);
            cardHashMap.put(cardData[0], newCard);
            System.out.println(cardData[0]);
        }
    }

    public static HashMap<String, Card> getCardHashMap() {
        return cardHashMap;
    }

    public static void lessonSelected(boolean selected){
        App.selected = selected;
    }

    public static boolean isLessonSelected(){
        return selected;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
