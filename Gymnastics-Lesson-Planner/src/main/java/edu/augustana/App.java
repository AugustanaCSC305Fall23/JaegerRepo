package edu.augustana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * JavaFX App
 */
public class App extends Application {
    public static final String imagesFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\edu\\augustana\\images";
    // relative path didn't work so this finds the absolute path to the images folder regardless of the device
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

    /**
     * @throws IOException
     *
     * Reads the csv file and creates card objects and stores it into a hashmap
     */
    public void readDataFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(imagesFilePath + "/Demo1/DEMO1.csv"));
        String line = br.readLine();
        while ((line = br.readLine()) != null)
        {
            String[] cardData = line.split(",");    // "spliting using comma cause it's a spece"
            Card newCard = new Card(cardData);
            cardHashMap.put(cardData[0], newCard);
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
